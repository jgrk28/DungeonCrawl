# Local Snarl
## Running the game

This game is played entirely in the terminal and currently only supports one local player.
To run the game, run the executable using the command `./localSnarl`.
The executable also has the following optional arguments.

`--levels FILENAME` where FILENAME is the name of a file containing JSON level specifications. The file must be located in the same folder that the executable is being run from. We have provided two examples for this assignment: `snarl.levels` (default) and `simpleLevel.levels`.

`--players N` where N is the number of players. This implementation only supports a single player. If the given N is not 1, it will print an error message saying so and the game will end. Default is 1.

`--start N` where N is the level to start from. If N is greater than the number of available levels, an error will be thrown. Default is 1.

`--observe` If this option is given, an observer view (the full level) will be presented in addition to the player view. They will both be displayed in the terminal.

## Playing the game

The player will start by entering their name to register with the GameManager. Once registered, the player's starting state will be displayed in the terminal and they will see a list of all available moves as well as their location. The player should pick a move from the list of valid moves and enter the x and y coordinates when prompted. Their view will update every time any player or adversary moves. The observer view will also update on every game state change. If the player enters an invalid move their turn will be skipped and they will not move at all.

If a player finds the key, is expelled, or exits the level, the corresponding message will be displayed in the terminal. If they successfully exit the level, they will automatically move on to the next level. Once the game is over, the rankings for all players will be displayed with the number of keys found and times they exited levels.

## Viewing the game

Each component in the game is represented in the following way:
 * Wall `X`
 * Space `.`
 * Hall Space `*`
 * Key `!`
 * Exit `@`
 * Player `P`
 * Adversary - Ghost `G`
 * Adversary - Zombie `Z`
 * Empty (outside rooms and halls) ` ` (space)