The purpose of this interface is to check the rules for our GameState. We initially built checking into our GameState, so this interface primarily calls those methods from a high level. We decided to keep the checking in each respective interface, Level and LevelComponent, because they have access to the data needed to check the rules. This interface could be used by the GameController to validate moves and check when the level or game is over.

## Interface RuleChecker ##

### Methods ###
* GameState isGameOver()  
  * Checks if the game is over. If so, if it was won or lost  
  * Determines if the level is over, and checks if this level is the last level in the dungeon  
  * At least one player must exit the last level in order to win the game  
  * GameState can be Active, Won, or Lost
* GameState isLevelOver()  
  * Checks if the level is over. If so, if it was won or lost  
  * In order for a level to be over, all players must be removed from the current level
  * If any player exited the level, the level was won. Otherwise, the level was lost  
  * GameState can be Active, Won, or Lost  
* Boolean checkValidMove(Actor actor, Point destination)
  * Checks that the move is valid for the given actor and if the interaction at the destination is valid
   A move is valid if it is within the movement bounds of the given actor, and if the actor can interact with the destination entity
  * An actor can be a player or an adversary


## Class Dungeon implements RuleChecker ##

## Interface Level ## 

### Methods ###
* GameState isLevelOver()  
  * Checks if the level is over. If so, if it was won or lost  
  * GameState can be Active, Won, or Lost  
* Boolean checkValidMove(Actor actor, Point destination)
  * Checks that the move is valid for the given actor and if the interaction at the destination is valid

## Interface LevelComponent ##

### Methods ###
* Boolean checkValidMove(Actor actor, Point destination)
  * Checks that the move is valid for the given actor and if the interaction at the destination is valid

