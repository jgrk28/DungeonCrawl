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

#### Running the Client
Before starting the client, ensure you are in the same directory as the snarlClient executable and JAR file. Run ./snarlClient to start the client. You can provide any of the following command line flags:

* `--address IP`, where `IP` is an IP address the client should connect to. The default is `127.0.0.1`.
* `--port NUM`, where `NUM` is the port number the client should connect to. The default is `45678`.

#### Playing the Game
The user will start by entering their name to register with the GameManager. Once registered, the player's starting state will be displayed in the terminal, along with their current position. The player should pick a move and enter the x and y coordinates when prompted. Their view will update every time any player or adversary moves. If the player enters an invalid move, they will be notified and re-prompted. A user is given 3 retries before their turn is skipped.

If a player finds the key, is expelled, or exits the level, the corresponding message will be displayed in the terminal. If they successfully exit the level, they will automatically move on to the next level. Once the game is over, the rankings for all players will be displayed with the number of keys found, times they exited levels, and times they were expelled.

#### Viewing the Game
Each component in the game is represented in the following way:

* Wall or Empty `X`
* Space `.`
* Door `|`
* Key `!`
* Exit `@`
* Player `P`
* Adversary - Ghost `G`
* Adversary - Zombie `Z`
