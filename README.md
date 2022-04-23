# Dungeon Crawl (Snarl)
This is the SwDev Spring 2021 project repository for amacphail and jkaplan
and so has most of the projects from that class.\
\
The dungeon crawl code, tests, and documentation is all in the `Snarl` folder.
Info about the design is below, more info can be found in the `Snarl/Planning`
folder.

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

# Playing a Distributed Game of Snarl #

In order to play a distributed game of Snarl, first start the SnarlServer. Then, start any clients you want to connect. Additional information on starting the server and clients can be found below. All game interactions occur in the terminal.

## SnarlServer ## 

### Running the Server ###
Before starting the server, ensure you are in the same directory as the snarlServer excutable and JAR file. Run ./snarlServer to start the server. You can provide any of the following command line flags:

* `--levels FILE`, where `FILE` is the path and name of a file containing a JSON level specifications. The default is `snarl.levels` (in the current directory)
* `--clients N`, where 1 ≤ `N` ≤ 4 is the maximum number of clients the server should wait for before starting the game. The default is `4`.
* `--wait N`, where `N` is the number of seconds to wait for the next client to connect. The default is `60`.
* `--observe` – when this option is given, the server should start a local observer to display the progress of the game.
* `--address IP`, where `IP` is an IP address on which the server should listen for connections. The default is `127.0.0.1`.
* `--port NUM`, where `NUM` is the port number the server will listen on. The default is `45678`.

The server will automatically terminate when the game is over.  

## SnarlClient

### Running the Client
Before starting the client, ensure you are in the same directory as the snarlClient executable and JAR file. Run ./snarlClient to start the client. You can provide any of the following command line flags:

* `--address IP`, where `IP` is an IP address the client should connect to. The default is `127.0.0.1`.
* `--port NUM`, where `NUM` is the port number the client should connect to. The default is `45678`.

### Playing the Game
The user will start by entering their name to register with the GameManager. Once registered, the player's starting state will be displayed in the terminal, along with their current position and health points. The player should pick a move and enter the x and y coordinates when prompted. Their view will update every time any player or adversary moves. If the player enters an invalid move, they will be notified and re-prompted. A user is given 3 retries before their turn is skipped.

The player starts each level with 20 health points. If they are attacked by an adversary their health points will decrease. If their health points ever reach zero, they are expelled from the level. A zombie does 7 damage points while a ghost does 5 damage points. A player does not have the ability to attack adversaries or other players.

If a player finds the key, is expelled, is damaged, or exits the level, the corresponding message will be displayed in the terminal. If they successfully exit the level, they will automatically move on to the next level. Once the game is over, the rankings for all players will be displayed with the number of keys found, times they exited levels, and times they were expelled.

### Viewing the Game
Each component in the game is represented in the following way:

* Wall or Empty `X`
* Space `.`
* Door `|`
* Key `!`
* Exit `@`
* Player `P`
* Adversary - Ghost `G`
* Adversary - Zombie `Z`

## Block Diagram
<pre><code>
    +-----------------------------------------------------------------------------------------------------------------------+
    |                                                                                                                       |
    |    +------------------------+                                                                                         |
    |    |   Dungeon              |                 +---------------------------------------------+                         |
    |    +------------------------+       +--------->   Level                                     |                         |
    +----+   Set players          |       |         +---------------------------------------------+                         |
         |   int numLevels        |       |         |   Map[Players, Room] playerLocations        +-------------------+     |
         |   int currLevel        |       |         |   Map[Adversary, Room] adversaryLocations   +-------+           |     |
         |   Level activeLevel    +-------+      +--+   List[List[LevelComponent]] levelMap       |       |           |     |
         +------------------------+              |  |   List[Player] playerTurnOrder              |       |           |     |
         |   initializePlayers()  |              |  |   List[Adversary] adversaryTurnOrder        |       |           |     |
         |   initializeLevel()    |              |  |   Boolean exitUnlocked                      |       |           |     |
         |   playLevel()          |              |  +---------------------------------------------+       |           |     |
         |                        |              |  |   initializeLevel()                         |       |           |     |
         +------------------------+              |  |   startLevel()                              |       |           |     |
                                                 |  |   playerTurn()                              |       |           |     |
         +------------------------------------+  |  |   adversaryTurn()                           |       |           |     |
         |   LevelComponent                   <--+  |   endLevel()                                |       |           |     |
         +------------------------------------+     |                                             |       |           |     |
         |   List[List[Entity]] componentMap  +---+ +---------------------------------------------+       |           |     |
         +------------------------------------+   |                                                       |           |     |
         |   movePlayer()                     |   |                                                       v           |     |
         |   moveAdversary()                  |   |     +----------------------------------+      +-------+-------+   |     |
         |   playerInteraction()              |   +----->   Entity                         +<-----+   Adversary   |   |     |
         |   adversaryInteraction()           |         +----------------------------------+      +---------------+   |     |
         |                                    |         |   Boolean playerInteractable     |      |               |   |     |
         +-----+-------------------------+----+         |   Boolean adversaryInteractable  |      +---------------+   |     |
               ^                         ^              |                                  |                          |     |
               |                         |              +--+---------+--------+----------+-+<---------+               |     |
               |                         |                 ^         ^        ^          ^            |               v     |
         +-----+----+              +-----+----+            |         |        |          |        +---+---------------+--+  |
         |   Hall   |              |   Room   |       +----+----+    |   +----+-----+    |        |   Player             +<-+
         +----------+              +----------+       |   Key   |    |   |   Exit   |    |        +----------------------+
         |          |              |          |       +---------+    |   +----------+    +--+     |   PlayerState state  +--+
         +----------+              +----------+       |         |    |   |          |       |     +----------------------+  |
                                                      +---------+    |   +----------+       |                               |
                                                                     |                      |                               v
                                                               +-----+----+            +----+------+        +---------------+-+
                                                               |   Wall   |            |   Space   |        |   PlayerState   |
                                                               +----------+            +-----------+        +-----------------+
                                                               |          |            |           |        |   Active        |
                                                               +----------+            +-----------+        |   Removed       |
                                                                                                            |   Completed     |
                                                                                                            +-----------------+
</pre></code>
