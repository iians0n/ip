# Duke User Guide

// Update the title above to match the actual product name

// Product screenshot goes here

// Product intro goes here

## Adding deadlines

// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```

## Duplicate detection

GOAT refuses to store the same task twice. When you add a task that matches one
already in your list, it is not added and GOAT points you at the entry you
already have.

Two tasks match when they are the same kind of task, describe the same thing,
and fall on the same dates. Spacing and capitalisation are ignored, so retyping
a task in a hurry is still recognised. Whether a task is already done makes no
difference: finishing something does not entitle the list to a second copy.

Example: `todo borrow book`

```
____________________________________________________________
 You already have this task:
   1.[T][ ] borrow book
____________________________________________________________
```

Tasks that differ in their dates are kept apart, because the same words on a
different day are a different commitment:

```
deadline return book /by 2019-12-02    added
deadline return book /by 2020-01-01    added, a different due date
deadline return book /by 2019-12-02    refused, already in the list
```

The same holds across task types: `todo return book` and
`deadline return book /by 2019-12-02` can both be in your list, since a task
with a deadline is not the same commitment as one without.


## Feature XYZ

// Feature details