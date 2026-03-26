package seedu.duke;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles reading and writing expense data to a text file
 * so that data persists between sessions.
 * Each line in the file stores one expense in the format: AMOUNT | DATE | CATEGORY | DESCRIPTION.
 */
public class Storage {
    private static final String SEPARATOR = " | ";
    private static final String SPLIT_REGEX = "\\s*\\|\\s*";

    /** Number of fields expected on each saved line. */
    private static final int FIELD_COUNT = 4;
    private static final int IDX_AMOUNT      = 0;
    private static final int IDX_DATE        = 1;
    private static final int IDX_CATEGORY    = 2;
    private static final int IDX_DESCRIPTION = 3;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    private static final Logger logger = Logger.getLogger(Storage.class.getName());

    private final String filePath;
    private final Ui ui;

    /**
     * Constructs a Storage object with the given file path and Ui instance.
     *
     * @param filePath The path to the data file.
     * @param ui       The Ui object used to display messages and warnings.
     */
    public Storage(String filePath, Ui ui) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path must not be empty");
        }
        if (ui == null) {
            throw new IllegalArgumentException("Ui must not be null");
        }
        this.filePath = filePath;
        this.ui = ui;
    }

    /**
     * Loads expenses from the data file into the given ExpenseList.
     * Malformed lines are skipped with a warning; a missing file is silently ignored.
     *
     * @param expenseList The ExpenseList to populate with saved data.
     */
    public void load(ExpenseList expenseList) {
        if (expenseList == null) {
            throw new IllegalArgumentException("ExpenseList must not be null");
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                Expense expense = parseLine(line);
                if (expense != null) {
                    expenseList.addExpense(expense);
                }
            }
        } catch (IOException e) {
            ui.showLoadWarning();
            logger.log(Level.WARNING, "Could not load expense data from file: " + filePath, e);
        }
    }

    /**
     * Saves all expenses from the given ExpenseList to the data file.
     * Creates the parent directory if it does not exist.
     *
     * @param expenseList The ExpenseList whose data should be saved.
     */
    public void save(ExpenseList expenseList) {
        if (expenseList == null) {
            throw new IllegalArgumentException("ExpenseList must not be null");
        }
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                ui.showSaveWarning();
                logger.warning("Could not create data directory: " + parentDir.getAbsolutePath());
                return;
            }
        }
        try (FileWriter writer = new FileWriter(file)) {
            for (int i = 0; i < expenseList.getSize(); i++) {
                Expense expense = expenseList.getExpense(i);
                writer.write(
                        expense.getAmount()
                                + SEPARATOR + expense.getDate()
                                + SEPARATOR + expense.getCategory()
                                + SEPARATOR + expense.getDescription()
                                + System.lineSeparator()
                );
            }
        } catch (IOException e) {
            ui.showSaveWarning();
            logger.log(Level.WARNING, "Could not save expense data to file: " + filePath, e);
        }
    }

    /**
     * Parses a single line from the data file into an Expense object.
     * Returns null and displays a warning if the line is malformed or contains invalid values.
     *
     * @param line The line to parse.
     * @return The parsed Expense, or null if the line is malformed.
     */
    private Expense parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(SPLIT_REGEX, FIELD_COUNT);
        if (parts.length < FIELD_COUNT) {
            ui.showMalformedLineWarning(line);
            logger.warning("Malformed storage line (expected 4 fields): " + line);
            return null;
        }

        String amountStr   = parts[IDX_AMOUNT].trim();
        String dateStr     = parts[IDX_DATE].trim();
        String categoryStr = parts[IDX_CATEGORY].trim();
        String description = parts[IDX_DESCRIPTION].trim();

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            ui.showInvalidAmountLineWarning(line);
            logger.log(Level.WARNING, "Storage line with invalid amount: " + line, e);
            return null;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            ui.showMalformedLineWarning(line);
            logger.log(Level.WARNING, "Storage line with invalid date: " + line, e);
            return null;
        }

        if (description.isEmpty()) {
            ui.showMalformedLineWarning(line);
            logger.warning("Storage line with empty description: " + line);
            return null;
        }

        try {
            return new Expense(description, amount, categoryStr, date);
        } catch (IllegalArgumentException e) {
            ui.showInvalidAmountLineWarning(line);
            logger.log(Level.WARNING, "Storage line rejected by Expense constructor: " + line, e);
            return null;
        }
    }
}
