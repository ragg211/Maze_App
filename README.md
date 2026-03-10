# Maze App

An interactive Android maze-solver application where you draw walls on a grid and watch a BFS algorithm find the shortest path in real time.

## Features

- **Draw walls** by touching or dragging anywhere on the 20-column × 15-row grid
- **Solve** the maze with a single button tap using Breadth-First Search (BFS)
- **Animated visualisation** shows every step of the search before revealing the final path
- Guaranteed shortest path from the top-left corner (**Start**) to the bottom-right corner (**End**)

### Colour Key

| Colour | Meaning |
|--------|---------|
| 🟩 Green | Start cell (top-left) |
| 🟥 Red | End cell (bottom-right) |
| ⬛ Black | Wall drawn by user |
| 🟨 Yellow | Cells explored during search |
| 🟦 Blue | Final shortest path |

## Tech Stack

| | |
|---|---|
| **Language** | Kotlin |
| **Platform** | Android (min SDK 24 / API 24+) |
| **Build system** | Gradle (Kotlin DSL) |
| **Core libraries** | AndroidX Core-KTX, AppCompat, ConstraintLayout, Material Design |
| **Async** | Kotlin Coroutines (`lifecycleScope`) |
| **Algorithm** | Breadth-First Search (BFS) |

## Project Structure

```
Maze_App/
├── app/src/main/java/com/example/algorithm/
│   ├── MainActivity.kt      # Entry point; wires up the Solve button
│   └── MazeView.kt          # Custom View – drawing, touch input & BFS solver
├── app/src/main/res/
│   └── layout/activity_main.xml
├── gradle/libs.versions.toml
└── build.gradle.kts
```

## Getting Started

### Prerequisites

- **Android Studio** Hedgehog (2023.1.1) or newer
- **JDK 11** or newer
- An Android emulator or a physical device running **Android 7.0 (API 24)** or higher

### Running the app

1. **Clone the repository**
   ```bash
   git clone https://github.com/ragg211/Maze_App.git
   cd Maze_App
   ```

2. **Open in Android Studio**
   - *File → Open* and select the project folder
   - Wait for Gradle sync to finish

3. **Run**
   - Select a device or emulator from the toolbar
   - Click **Run ▶** (or press `Shift + F10`)

### Building from the command line

```bash
# Debug APK
./gradlew assembleDebug

# Install on a connected device
./gradlew installDebug

# Release APK
./gradlew assembleRelease
```

### Running tests

```bash
# Unit tests
./gradlew test

# Instrumentation tests (requires a connected device/emulator)
./gradlew connectedAndroidTest
```

## How It Works

1. **Draw your maze** – tap or drag on any cell to toggle a wall.
2. **Press "Solve (BFS)"** – the app runs BFS starting from cell `(col 0, row 0)` (top-left) and targeting cell `(col 19, row 14)` (bottom-right).
3. **Watch the search** – explored cells turn yellow with a 15 ms delay between steps.
4. **See the path** – once the target is reached, the shortest path is traced back using parent pointers and drawn in blue with a 30 ms delay per step.

The grid is represented as a `20-column × 15-row` integer matrix. Coordinates are expressed as `(column, row)` with `(0, 0)` at the top-left and `(19, 14)` at the bottom-right:

| Value | Meaning |
|-------|---------|
| `0` | Empty |
| `1` | Wall |
| `2` | Start |
| `3` | End |
| `4` | Explored (yellow) |
| `5` | Path (blue) |

## License

This project is open source. See the repository for details.
