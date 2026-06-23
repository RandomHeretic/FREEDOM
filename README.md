# Freedom

This project is aimed at recreating the [Freedom board game](https://boardgamegeek.com/boardgame/100480/freedom) with the added feature of playability against AIs.

## Build and Run

```bash
./gradlew run
```

## How to Use

Once you start the application, you'll be faced with the choice of starting a new game or loading a saved game. If you would like to start a new game, select the size of the board and the players, then press **"Play"**.

Keep in mind that:

* **Player** is a human player; as such, manual input will be needed.
* **Random** is an AI that will pick random moves on each of its turns.
* **AI** is a simple AI that looks a few turns ahead, trying to predict optimal moves.

By pressing **"Load Game"** you can choose a save file to load a preexisting game that you may have saved. You'll be opened at the directory where the application stores save files, but you can navigate to your own files if you have some saved in other directories.

In the game, if you have selected a human player, you have the option to place a stone where indicated by the UI. If you need to take the special action **"skip"**, a button in the side panel will appear. During your game, at any point you can save your progress with the box in the side panel. Save files will be written to the following directories, depending on the OS of your machine:

* **Windows:** `C:\Users\<username>\.freedom\saves`
* **macOS:** `/Users/<username>/.freedom/saves`
* **Linux:** `/home/<username>/.freedom/saves`

> **Note:** if you save multiple times using the same name for the save file, your old file will be overwritten.

During the game you'll also have the option to return to the main menu by pressing the **"Menu"** button. Be careful, since this will remove you from the game — be sure to save first if you wish to keep track of it.

## About Freedom

Freedom is a game structured on the board and pieces of the game Go. The first player (White) places the first stone anywhere on the board, then in alternating fashion each player must place their next stone next to the stone of the last move. This procedure goes on until a player can't put their stone on an empty slot; when this happens and there are still open slots on the board, the player gains the freedom to place their stone wherever they please.

The game concludes when all the slots of the board are occupied, with one exception: if the player who should place the last stone would lose points in doing so, they are allowed to skip the placement, ending the game.

The player's score is decided by counting the **"alive"** stones. A stone is considered alive if it belongs to a line containing exactly four stones of the same colour.
