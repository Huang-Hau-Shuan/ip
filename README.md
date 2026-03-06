# Julius

Julius is a personal task-management chatbot that runs in your terminal.
It tracks todos, deadlines, and events, and saves everything automatically between sessions.

## Setting Up

**Prerequisites:** JDK 17, IntelliJ IDEA (latest version recommended).

1. Open IntelliJ. If a project is already open, go to `File` > `Close Project`.
2. Click **Open**, select the project directory, and click **OK**.
3. Configure the project to use **JDK 17**:
   - Go to `File` > `Project Structure` > `Project`.
   - Set **SDK** to JDK 17 and **Language level** to `SDK default`.
4. Locate `src/main/java/julius/Julius.java`, right-click it, and choose **Run 'Julius.main()'**.
5. You should see the Julius greeting in the Run console.

## User Guide

Full usage instructions, command formats, and examples are in the
[User Guide](docs/README.md).

## Project Notes

- Task data is saved automatically to `data/julius.txt`.
- Keep `src/main/java` as the root folder for Java source files.
