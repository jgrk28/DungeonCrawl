# Data Definitions #    

## Class GameManager ##  

**Fields**  
* Dungeon dungeon  
    * Represents the entire game state, including levels, rooms, hallways, players, adversaries, and other entities  

**Methods**  
* void startGame()  
    * Accept registrations for new players and adversaries, and initialize a Dungeon (including all the levels) with those players and adversaries  
* void playGame()  
    * Place players and adversaries in first level, manage their turns until they beat the last level or all players are removed in a level  
* void endGame()  
    * Send results to players after a game has finished  

## Class Dungeon ##  

**Constructor**   
* Dungeon(List<Player> players, List<Adversary> adversaries)  
    * Initializes all the levels, sets the players and adversaries for the dungeon  
    * Args:  
        * players - all players in this Dungeon  
        * adversaries - all adversaries in this Dungeon  
 
**Fields**  
* Set<Players> players  
    * All players in the game regardless of status in current level  
* Set<Adversary> adversaries  
    * All adversaries in the game regardless of status in current level  
* Int currLevel  
    * Current level number of the Dungeon that the players are on  
* List<Level> levels  
    * All levels in the game. Each level keeps track of the game state that is specific to itself  

**Methods**   
* Level getNextLevel()   
    * Returns the next level of the game  
* Boolean isLastLevel()  
    * Determines if the game is on the last level  
 
## Class Level ##   

**Constructor**  
* Level(Set<Player> players, Set<Adversary> adversaries, long seed)  
    * Initialize a random level from the given seed. Level will place the given players and adversaries  
    * Args:  
        * players - all active players to place in this Level  
        * adversaries - all active adversaries to place in this Level  
        * seed - used to initialize a random number generator  

**Fields**  
* LinkedHashMap<Actor,LevelComponent> actorLocations  
    * Ordered map of players and adversaries that reflects the turn order and maps to each player to their current location  
* List<List<Entity>> levelMap  
    * Matrix of the entire level that shows the entities positions relative to each other   
* Boolean exitUnlocked  
    * True if exit has been unlocked by finding the key  

**Methods**  
* void actorAction(Actor actor, Point destination)  
    * Moves the actor and deals with any interaction that occurs during the movement  
    * Args:  
        * actor - the actor that is moving  
        * destination - the cartesian coordinate for the player’s destination  
* GameState isLevelOver()  
    * Checks if the level is over. If so, if it was won or lost  
    * GameState can be Active, Won, or Lost  

## Interface LevelComponent ##  

**Methods**  
* InteractionResult actorAction(Actor actor, Destination destination)  
    * Moves the actor to the destination and processes the interaction for this move  
    * InteractionResult can be Exit, FoundKey, RemovePlayer, or None   
* Boolean checkValidMove(Player player, Point destination)  
    * Checks that the move is valid for the given player  
* Entity getDestinationEntity(Point destination)  
    * Returns the entity at the destination   
    * Args:  
        * destination - the cartesian coordinate for the destination  
* void enterComponent(Actor actor, LevelComponent source)  
    * Places the actor in this LevelComponent  
    * Args:  
        * actor - the actor moving to this component  
        * source - the LevelComponent the actor is currently in  
 
## Class Hall implements LevelComponent ##  

**Fields**  
* List<Entity> componentMap  
    * Contains all entities within the hall, including a door at each end to connect to the corresponding room  
* List<Point> waypoints  
    * Contains the cartesian points that represent the direction of the hallway  
 
## Class Room implements LevelComponent ##  

**Fields**  
* List<List<Entity>> componentMap  
    * Contains all entities within the room  

