# Julius User Guide

**Julius** is a personal task-management chatbot that runs in your terminal.
You type commands; Julius tracks your todos, deadlines, and events — and saves everything automatically between sessions.

---

## Quick Start

1. Ensure you have **JDK 17** installed.
2. Download the latest `julius.jar` from the releases page.
3. Run it from a terminal in the folder that contains the jar:
   ```
   java -jar julius.jar
   ```
4. Julius will greet you and load any previously saved tasks automatically.
5. Type a command and press **Enter**. Type `bye` to exit.

---

## Command Summary

| Action | Format | Example |
|---|---|---|
| Add todo | `todo DESCRIPTION` | `todo read textbook` |
| Add deadline | `deadline DESCRIPTION /by DATETIME` | `deadline submit report /by 2026-03-15 2359` |
| Add event | `event DESCRIPTION /from START /to END` | `event team meeting /from Mon 2pm /to 4pm` |
| List all tasks | `list` | `list` |
| Find by keyword | `find KEYWORD` | `find book` |
| Deadlines on a date | `deadline on DATE` | `deadline on 2026-03-15` |
| Mark as done | `mark INDEX` | `mark 2` |
| Unmark as done | `unmark INDEX` | `unmark 2` |
| Delete a task | `delete INDEX` | `delete 3` |
| Exit | `bye` | `bye` |

> **Notes:**
> - `DATETIME` must be in `yyyy-MM-dd HHmm` format (e.g. `2026-03-15 2359`).
> - `DATE` (for `deadline on`) must be in `yyyy-MM-dd` format (e.g. `2026-03-15`).
> - `INDEX` refers to the task number shown by the `list` command, starting from 1.

---

## Features

### Adding a Todo — `todo`

Adds a task with no date or time constraint.

**Format:** `todo DESCRIPTION`

**Example:**
```
todo read textbook
```
**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read textbook
 Now you have 1 tasks in the list.
____________________________________________________________
```

---

### Adding a Deadline — `deadline`

Adds a task that must be completed by a specific date and time.
The date and time must be in `yyyy-MM-dd HHmm` format.

**Format:** `deadline DESCRIPTION /by DATETIME`

**Example:**
```
deadline submit report /by 2026-03-15 2359
```
**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [D][ ] submit report (by: Mar 15 2026, 11:59PM)
 Now you have 2 tasks in the list.
____________________________________________________________
```

---

### Adding an Event — `event`

Adds a task that spans a time window. The start and end times are free-text.

**Format:** `event DESCRIPTION /from START /to END`

**Example:**
```
event team meeting /from Mon 2pm /to 4pm
```
**Expected output:**
```
____________________________________________________________
 Got it. I've added this task:
   [E][ ] team meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
```

---

### Listing All Tasks — `list`

Displays every task currently in the list, numbered from 1.

**Format:** `list`

**Expected output:**
```
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read textbook
 2.[D][ ] submit report (by: Mar 15 2026, 11:59PM)
 3.[E][ ] team meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
```

---

### Finding Tasks by Keyword — `find`

Searches all task descriptions for a keyword (case-insensitive) and lists the matches.

**Format:** `find KEYWORD`

**Example:**
```
find report
```
**Expected output:**
```
____________________________________________________________
 Here are the matching tasks in your list:
 1.[D][ ] submit report (by: Mar 15 2026, 11:59PM)
____________________________________________________________
```

---

### Viewing Deadlines on a Specific Date — `deadline on`

Lists all deadline tasks due on a given date. The date must be in `yyyy-MM-dd` format.

**Format:** `deadline on DATE`

**Example:**
```
deadline on 2026-03-15
```
**Expected output:**
```
____________________________________________________________
 Here are the deadlines on Mar 15 2026:
 1.[D][ ] submit report (by: Mar 15 2026, 11:59PM)
____________________________________________________________
```

---

### Marking a Task as Done — `mark`

Marks the task at the specified index as completed.

**Format:** `mark INDEX`

**Example:**
```
mark 1
```
**Expected output:**
```
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read textbook
____________________________________________________________
```

---

### Unmarking a Task — `unmark`

Reverts a completed task back to not done.

**Format:** `unmark INDEX`

**Example:**
```
unmark 1
```
**Expected output:**
```
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] read textbook
____________________________________________________________
```

---

### Deleting a Task — `delete`

Permanently removes a task from the list by its index.

**Format:** `delete INDEX`

**Example:**
```
delete 2
```
**Expected output:**
```
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] submit report (by: Mar 15 2026, 11:59PM)
 Now you have 2 tasks in the list.
____________________________________________________________
```

---

### Automatic Saving

Julius automatically saves your task list to `data/julius.txt` after every change (add, delete, mark, unmark).
The file is loaded automatically the next time Julius starts, so no data is lost between sessions.
You do not need to do anything to trigger a save.

---

### Exiting — `bye`

Closes Julius gracefully.

**Format:** `bye`

**Expected output:**
```
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```
