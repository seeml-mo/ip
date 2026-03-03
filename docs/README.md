# Seemlmot Project User Guide

This is a project user guide for the Java task manager named *Seemlmot*. Given below are instructions on how to set up and use it.

## Setting up in Intellij

Prerequisites: **JDK 17**, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first).
2. Open the project into Intellij as follows:
1. Click `Open`.
2. Select the project directory, and click `OK`.
3. If there are any further prompts, accept the defaults.


3. Configure the project to use **JDK 17** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).




In the same dialog, set the **Project language level** field to the `SDK default` option.
4. After that, locate the `src/main/java/seemlmot/Seemlmot.java` file, right-click it, and choose `Run Seemlmot.main()`. If the setup is correct, you should see the welcome message and a notification regarding loaded data.

---

## Features

### Adding Tasks

Seemlmot can manage three types of tasks:

* `todo`: A simple task without a date.
* `deadline`: A task that needs to be done by a specific time.
* `event`: A task that starts and ends at specific times.

### Automated Storage

Your tasks are automatically saved to and loaded from a local file located at `./data/seemlmot.txt`. You can also manually trigger a save using the `save` command.

---

## Usage

### `todo` - Add a To-Do task

Adds a task without a deadline to the list.

* Format: `todo [description]`
* Example: `todo buy groceries`

### `deadline` - Add a Deadline

Adds a task with a specific deadline.

* Format: `deadline [description] /by [time]`
* Example: `deadline return book /by Sunday`

### `event` - Add an Event

Adds a task that occurs during a time range.

* Format: `event [description] /from [start] /to [end]`
* Example: `event orientation /from Mon 2pm /to 4pm`

### `list` - List all tasks

Displays all tasks currently in your list with their status (Done/Not Done).

* Format: `list`

### `mark` / `unmark` - Update task status

Marks a specific task as completed or incomplete.

* Format: `mark [index]` or `unmark [index]`
* Example: `mark 1`

### `save` - Save current state

Manually saves the current task list to the data file.

* Format: `save`

### `bye` - Exit

Exits the application.

* Format: `bye`

---

## Command Summary

| Action | Format |
| --- | --- |
| **Todo** | `todo [description]` |
| **Deadline** | `deadline [description] /by [time]` |
| **Event** | `event [description] /from [start] /to [end]` |
| **List** | `list` |
| **Mark** | `mark [index]` |
| **Save** | `save` |
| **Exit** | `bye` |

---

**Warning:** Keep the `src/main/java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

---

### Key Adjustments made:

1. **Strict Markdown Hierarchy**: Kept the `#`, `##`, and `###` exactly as they would appear in a professional `.md` documentation.
2. **Original Warning Block**: Restored the "Warning" section at the end regarding the folder structure.
3. **Inline Code Formatting**: Used backticks (```) for commands and file paths, as seen in the original "Duke" template.
4. **Package Path**: Updated the run instructions to point to `seemlmot/Seemlmot.java` (reflecting your package structure).
