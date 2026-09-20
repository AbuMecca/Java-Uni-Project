# X&O Game (JavaFX Tic-Tac-Toe)

A two-player Tic-Tac-Toe (X & O) desktop game built in **Java** with **JavaFX**, where each player picks their **own image (even a GIF!)** as their mark, plays against a turn timer with background music and voice cues, and has every result saved to a database scoreboard.

Made by **Amro Hossameldin Zakaria** and **Youssef Omar Kamal Zaki**.

---

## About

This is a university project whose main objective was to demonstrate the **four core Object-Oriented Programming (OOP) concepts** together with a working **database** integration.

Instead of plain "X" and "O" letters, each player chooses an image file at the start (we used GIFs of football players), which then fills that player's cells on the board. The game plays background music, plays a voice cue for each symbol, gives every player a 10-second countdown per turn, and permanently stores every game's result in an Apache Derby database that can be viewed through an in-app scoreboard.

---

## Features

- **Two-player local play** on a stretchable 3×3 grid
- **Custom image symbols** — each player picks any `.png`, `.jpg`, or `.gif` as their mark
- **10-second turn timer** — run out of time and your turn is skipped
- **Background music** that loops while you play
- **Voice cues** announcing the X and O symbols
- **Persistent scoreboard** — results are saved to a database and shown in a table
- **Auto-restart** after each round, with the winner starting the next game
- **Auto-reset** of the scoreboard after every 10 games (and a manual Reset button)

---

## Main Objective — The Four OOP Concepts

This project was designed to show all four pillars of OOP in real, working code:

| OOP Concept | Where it is used |
|---|---|
| **Abstraction** | `Participant` is an `abstract class` that defines the shared shape of a participant. |
| **Inheritance** | `Player extends Participant`, and `XO_Game extends Application`. |
| **Encapsulation** | All classes keep their fields `private`/`protected` and expose them only through getters (e.g. `Board`, `Player`, `Score`). |
| **Polymorphism** | Methods are overridden with `@Override` — `toString()` in `Player` and `start()` in `XO_Game`. |

Plus a **database** requirement, met with an Apache Derby database (see below).

---

## Tech Stack

- **Java 11**
- **JavaFX** (UI, media/audio, image handling)
- **Apache Derby** (embedded/network SQL database)
- **Maven** (build & dependency management)

---

## Project Structure

```
X-O-Game/
├── src/
│   └── main/
│       ├── java/
│       │   └── xo_game/
│       │       ├── XO_Game.java          # Application entry point
│       │       ├── GameController.java   # Main game flow & UI
│       │       ├── Board.java            # 3x3 board model + win/draw logic
│       │       ├── Participant.java      # Abstract base class
│       │       ├── Player.java           # Player (extends Participant)
│       │       ├── Score.java            # One round's result
│       │       ├── DatabaseManager.java  # Save / fetch / reset scores
│       │       ├── DbUtil.java           # Derby connection helper
│       │       ├── ScoreboardView.java   # Scoreboard table window
│       │       └── TurnTimer.java        # 10-second countdown
│       └── resources/
│           └── audio/
│               ├── bg.wav                # Background music (looped)
│               ├── X.wav                 # Voice cue for X
│               └── O.wav                 # Voice cue for O
├── pom.xml
├── .gitignore
└── README.md
```

> **Note:** The `audio` folder is required for the game to run — it is loaded from the classpath (`/audio/bg.wav`, `/audio/X.wav`, `/audio/O.wav`).

---

## Prerequisites

Before running, make sure you have:

- **JDK 11** installed
- **JavaFX 11+** SDK
- **Apache Derby** available (the network driver, `org.apache.derby.jdbc.ClientDriver`)
- **Maven** (if building with Maven)

---

## Setup & How to Run

1. **Start Apache Derby** (network server) on the default port `1527`.
   The database `XOGameDB` is created automatically on first connect (`create=true`).

2. **Create the scoreboard table** in `XOGameDB`:

   ```sql
   CREATE TABLE GameHistory (
       Id       INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
       Player1  VARCHAR(100),
       Player2  VARCHAR(100),
       Winner   VARCHAR(100),
       PlayedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   ```

3. **Make sure the `audio` files exist** under `src/main/resources/audio/`
   (`bg.wav`, `X.wav`, `O.wav`).

4. **Run the game:**

   ```
   mvn clean javafx:run
   ```

   Or run `XO_Game.java` directly from your IDE (NetBeans / IntelliJ / Eclipse).

---

## How to Play

1. **Enter gamer tags** — each player types their name when prompted.
2. **Choose your symbol image** — a file picker opens for each player; select any image or GIF to use as your mark.
3. **Take turns** clicking a cell. You have **10 seconds** per turn — if the timer hits zero, your turn is skipped.
4. **Win** by lining up three of your marks in a row, column, or diagonal.
5. A popup announces the **winner** (or a **draw**), the result is saved, and a new round starts.
6. Click **View Scoreboard** at the bottom to see all past results, or **Reset** to clear them.

---

## Database

The game uses **Apache Derby** to persist results.

- **Connection:** `jdbc:derby://localhost:1527/XOGameDB;create=true`
- **Table:** `GameHistory` (columns: `Player1`, `Player2`, `Winner`, `PlayedAt`)
- **Operations** (in `DatabaseManager`):
  - `saveGame()` → inserts a new result
  - `fetchAllGames()` → loads all results, newest first
  - `resetGames()` → clears the table

---

## Authors

- **Amro Hossameldin Zakaria** — [@AbuMecca](https://github.com/AbuMecca)
- **Youssef Omar Kamal Zaki**

---

## License

This project was created for educational purposes as a university assignment.
