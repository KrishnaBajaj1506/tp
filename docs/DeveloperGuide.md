# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Design & implementation

{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}

### Edit Expense Feature

The edit feature allows users to modify one or more fields of an existing expense using the `edit` command.

**How it works:**

The user provides a 1-based index followed by one or more optional flags:
- `/amount` to update the monetary value
- `/desc` to update the description
- `/c` to update the category
- `/d` to update the date (must follow `YYYY-MM-DD` format)

At least one flag must be supplied; omitted fields retain their existing values.

**Implementation:**

`Parser.parseEditCommand()` extracts the index and each flag from the input string sequentially.
Each flag is located by its keyword, its value is extracted up to the next `/` or end of input, and then stripped from the working string before the next flag is processed.
This allows flags to appear in any order without ambiguity.

Once all fields are parsed, an `EditCommand` is constructed with nullable fields for each of the four attributes.
In `EditCommand.execute()`, the existing `Expense` at the given index is retrieved, each non-null field replaces the corresponding existing value, and a new `Expense` object is created and written back via `ExpenseList.setExpense()`.

**Design considerations:**

`Expense` objects are immutable (all fields are `final`), so editing produces a new `Expense` rather than mutating the existing one.
An alternative considered was making `Expense` mutable, but immutability was preferred to avoid unintended side effects across the codebase.

### Category and Date Parsing (Add Command)

The `add` command supports two optional flags: `/c` for category and `/d` for date.

`Parser.parseAddCommand()` first extracts the mandatory amount, then strips `/d` and `/c` flags from the remaining input one at a time.
The date token is parsed with `ResolverStyle.STRICT` to reject impossible calendar dates such as `2026-02-30`.
Whatever text remains after both flags are removed becomes the description, which means the description does not need to appear in a fixed position relative to the flags.

If no category is supplied, `Expense` defaults to `"Others"`. If no date is supplied, it defaults to today's date via `LocalDate.now()`.

### Input Validation (Strict Commands)

The `list`, `help`, `exit`, and `total` commands do not accept any arguments.
If trailing text is detected after these keywords, the parser calls `ui.showUnknownCommand()` and returns `null`, preventing silent misinterpretation of user input such as `help something` or `exit now`.

### Budget Feature

The budget feature allows users to set a spending limit using the `budget` command.
The budget value is stored in `ExpenseList`, which is responsible for tracking total expenses and checking if the budget is exceeded.
When an expense is added through `AddCommand`, the system calls `isOverBudget()` to determine whether a warning should be displayed.
The budget is persisted in `Storage` as a special line and reloaded when the application starts.
This design keeps business logic within `ExpenseList`, ensuring separation of concerns between data handling and command execution.
An alternative approach considered was performing budget checks inside `AddCommand`, but this was avoided to maintain cleaner object-oriented design.

## Product scope

### Target user profile

{Describe the target user profile}

### Value proposition

{Describe the value proposition: what problem does it solve?}

## User Stories

|Version| As a ... | I want to ... | So that I can ...|
|--------|----------|---------------|------------------|
|v1.0|new user|see usage instructions|refer to them when I forget how to use the application|
|v1.0|user|add an expense with a description and amount|keep track of what I have spent|
|v1.0|user|delete an expense by index|remove entries I added by mistake|
|v2.0|user|assign a category and date to an expense|organise my spending history|
|v2.0|user|edit an existing expense|correct mistakes without deleting and re-adding entries|
|v2.0|user|find a to-do item by name|locate a to-do without having to go through the entire list|

## Non-Functional Requirements

{Give non-functional requirements}

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
