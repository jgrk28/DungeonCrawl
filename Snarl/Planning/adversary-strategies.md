## Adversary Strategies ##

TODO Add intro and example situations

###Ghosts###
- Checks if there is a player within a 6 step radius of the Ghost
- If players exist within that radius, take 1 step towards the closet player
- If there are no players in that radius, enter a wall tile and move to a new room

###Zombies###
- Checks if there is a player in the Zombie's LevelComponent
- If there are players in the LevelComponent, take 1 step towards the closest player
- If there are no players in the LevelComponent, make some valid arbitrary move
