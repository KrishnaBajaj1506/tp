package seedu.duke;

import java.util.ArrayList;

/**
 * Represents the list of expenses tracked by the user.
 * Handles adding, deleting, retrieving, and replacing expenses.
 */
public class ExpenseList {
    private final ArrayList<Expense> expenses;

    public ExpenseList() {
        this.expenses = new ArrayList<>();
    }

    /**
     * Adds a new expense to the end of the list.
     *
     * @param expense The expense to add.
     */
    public void addExpense(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("Expense must not be null");
        }
        expenses.add(expense);
    }

    /**
     * Returns the current number of expenses in the list.
     *
     * @return The number of expenses currently in the list.
     */
    public int getSize() {
        return expenses.size();
    }

    /**
     * Returns the expense at the specified 0-based index.
     *
     * @param index The 0-based position of the expense to retrieve.
     * @return The expense at the given index.
     */
    public Expense getExpense(int index) {
        return expenses.get(index);
    }

    /**
     * Replaces the expense at the given 0-based index with a new one.
     *
     * @param index   The 0-based position of the expense to replace.
     * @param expense The new Expense object to store at that position.
     */
    public void setExpense(int index, Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("Expense must not be null");
        }
        expenses.set(index, expense);
    }

    /**
     * Removes and returns the expense at the given 0-based index.
     *
     * @param index The 0-based position of the expense to delete.
     * @return The expense that was removed.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public Expense deleteExpense(int index) throws IndexOutOfBoundsException {
        return expenses.remove(index);
    }
}
