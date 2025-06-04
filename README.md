# TermXIDE

A powerful file manager and code editor. It uses Termux as its backend and is inspired by MT Manager for the file manager aspect. It leverages sharedUserId for seamless integration.

## TermXIDE Application Architecture

The architecture emphasizes modularity, ensuring a clear separation of concerns among the user interface, application logic, and Termux backend integration. The use of `sharedUserId` is fundamental for seamless interaction with the Termux environment.

### High-Level Layers & Components:

```
+-----------------------------------------------------+
|           Presentation Layer (Android UI)           |
|  (Activities, Fragments, Views - Java/Kotlin)       |
|                                                     |
|   +-----------------+  +----------------------+     |
|   | File Manager UI |  | Code Editor UI       |     |
|   | (MT Manager     |  | (Syntax highlight,   |     |
|   |  inspired, dual |  |  editing tools)      |     |
|   |  pane, file ops)|  +----------------------+     |
|   +-----------------+                               |
|                          +----------------------+   |
|                          | Integrated Terminal  |   |
|                          | View (Termux Output/ |   |
|                          | Input)               |   |
|                          +----------------------+   |
+-----------------------------------------------------+
            ^                        | (UI Events, Data Binding)
            | (User Actions)         v
+---------------------------------------------------+
|        Application Logic Layer (Services)         |
|              (Java/Kotlin Classes)                |
|                                                   |
|   +-----------------------+ +-------------------+ |
|   | FileOperationService  | | CodeEditorService | |
|   +-----------------------+ +-------------------+ |
|                                                   |
|   +---------------------------------------------+ |
|   |         TermuxBridgeService (Crucial)       | |
|   | (Command Execution, Output Handling,        | |
|   |  Environment Access via Termux)             | |
|   +---------------------------------------------+ |
|                                                   |
|   +-----------------------+                       |
|   |   SettingsManager     |                       |
|   +-----------------------+                       |
+---------------------------------------------------+
            ^                        | (Commands, Data Requests)
            | (Termux Output/Data)   v (Leverages sharedUserId)
+---------------------------------------------------+
|           Backend Layer (Termux Environment)      |
|      (Separate App - Relied Upon by TermXIDE)     |
|                                                   |
|   +---------------------------------------------+ |
|   | Termux Shell (bash, zsh, etc.)              | |
|   | Core Linux Utilities (ls, cp, git, etc.)    | |
|   | Installed Packages (Python, Node, etc.)     | |
|   | Termux API (optional, for specific features)| |
|   +---------------------------------------------+ |
+---------------------------------------------------+
            ^                        | (File System Access)
            | (File System Data)     v
+---------------------------------------------------+
|          Android OS / Shared File System          |
+---------------------------------------------------+
```

### Component Breakdown:

#### Presentation Layer (UI):

* **File Manager UI Module:**
    * Responsible for rendering the file system, likely in a dual-pane layout inspired by MT Manager.
    * Handles user interactions for:
        * Browsing, selecting, and initiating file operations (copy, paste, delete, rename, create, properties, search, archive).
    * Displays:
        * File details
        * Icons
        * Permissions
* **Code Editor UI Module:**
    * Provides the interface for text editing.
    * Features include:
        * Syntax highlighting for various languages
        * Line numbers
        * Search/replace
        * Undo/redo
        * Potentially basic code completion
    * Manages:
        * Opening, saving, and closing files
        * Could support multiple tabs
* **Integrated Terminal View Module:**
    * A view component (e.g., a Fragment) embedded within the app to display real-time output from Termux commands.
    * Allows users to:
        * See results of scripts
        * View build processes
        * Interact with shell prompts initiated by TermXIDE

#### Application Logic Layer (Services):

* **FileOperationService:**
    * Contains the business logic for all file system manipulations.
    * Methods like:
        * `listFiles(path)`
        * `copyFile(source, destination)`
        * `deleteFile(path)`
        * `createDirectory(path)`
    * This service will intelligently decide whether to use standard Android file APIs or to delegate operations to Termux (via TermuxBridgeService) for actions within Termux's environment or requiring Termux tools. The sharedUserId makes direct access to Termux's file space (`/data/data/com.termux/files/`) feasible.
* **CodeEditorService:**
    * Manages the state of open files, buffers, and editor configurations.
    * Handles:
        * File I/O for the editor
        * Syntax highlighting logic (possibly by integrating a library)
        * Other editor-specific backend tasks
    * May interact with TermuxBridgeService to:
        * Run linters
        * Run formatters
        * Build commands related to the open file
* **TermuxBridgeService (Key Component):**
    * The central point of interaction with the Termux backend.
    * Responsibilities:
        * Executing commands within the Termux environment (e.g., `git pull`, `python my_script.py`, `ls -l`)
        * Capturing stdout, stderr, and exit codes from Termux commands
        * Streaming output to the UI (e.g., the Integrated Terminal View)
        * Accessing Termux environment variables
        * Managing Termux sessions or processes if necessary
    * This service heavily relies on the sharedUserId to execute commands with the appropriate permissions and context within Termux. This could involve:
        * Using Termux's internal APIs/intents if available and designed for sharedUserId interaction
        * Directly executing binaries within the shared user context
* **SettingsManager:**
    * Manages user preferences for:
        * The file manager
        * Editor (themes, fonts, keybindings)
        * Termux integration
    * Handles persistence of these settings (e.g., using Android's SharedPreferences).

#### Backend Layer (Termux Environment):

* This is not part of TermXIDE's codebase but is a prerequisite app.
* TermXIDE leverages Termux for:
    * **Shell and Utilities:** Access to a full Linux-like environment with standard command-line tools (`ls`, `cp`, `mv`, `grep`, `find`, `tar`, `git`, etc.).
    * **Programming Runtimes & Compilers:** Ability to execute scripts and programs written in languages installed within Termux (Python, Node.js, Clang, etc.).
    * **File System:** Termux's home directory and installed packages.

#### Cross-Cutting Concern: sharedUserId

As stated in the README, "It uses sharedid." This is `android:sharedUserId` in the AndroidManifest.xml.

* **Significance:**
    * **Seamless File Access:** TermXIDE and Termux run with the same Linux user ID. This grants TermXIDE direct and native access to Termux's private files (e.g., `/data/data/com.termux/files/home`) without needing root or complex workarounds like the Storage Access Framework for this specific interaction. This is crucial for a file manager and code editor deeply integrated with Termux.
    * **Integrated Process Execution:** Simplifies running commands and scripts within the Termux environment, inheriting its PATH, environment variables, and permissions.
    * **Reduced Permission Complexity:** Avoids many Android permission hurdles when interacting between the two apps.

#### Example Data Flow: Running a Python script from the Editor

1. **User Action:** User opens `my_script.py` (located in Termux's home directory) in TermXIDE's Code Editor UI and clicks a "Run Script" button.
2. **UI to Logic:** The CodeEditorUI informs the CodeEditorService of the action.
3. **Logic to Bridge:** The CodeEditorService constructs the command (e.g., `python ~/my_script.py`) and passes it to the TermuxBridgeService.
4. **Bridge to Termux:** The TermuxBridgeService, leveraging the sharedUserId, executes the command within the Termux environment. This might involve:
    * Using Termux-specific intents (if Termux provides them for sharedUserId peers).
    * Directly forking a process with the Termux user's UID and environment.
5. **Execution & Output:** Termux executes the Python script. stdout and stderr are captured by the TermuxBridgeService.
6. **Output to UI:** The TermuxBridgeService streams the output back to the ApplicationLogic Layer, which then updates the IntegratedTerminalView in the UI.

### Key Design Principles:

* Leverage Termux: Offload complex command-line operations, script execution, and access to a rich set of tools to Termux.
* User Experience: Provide a polished UI, with the file manager inspired by MT Manager for familiarity and efficiency.
* Deep Integration: Use sharedUserId to make the boundary between TermXIDE and Termux as transparent as possible for file access and command execution.
* Asynchronous Operations: All potentially long-running tasks (file operations, command execution) must be done asynchronously to keep the UI responsive.

This architecture provides a solid foundation for building a powerful and well-integrated file manager and code editor on top of Termux. The TermuxBridgeService and the implications of sharedUserId are central to its success.

## Contributing

Contributions are welcome! Please create a pull request or open an issue for any enhancements or bug fixes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact

For any questions or inquiries, please contact [SK2006MC](https://github.com/SK2006MC).
