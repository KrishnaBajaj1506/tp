# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Design & implementation

{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}
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
|v2.0|user|find a to-do item by name|locate a to-do without having to go through the entire list|

## Non-Functional Requirements

{Give non-functional requirements}

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
