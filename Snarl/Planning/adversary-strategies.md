## Adversary Strategies ##

Adversaries are the enemies in the dungeon and have automated movements to chase down the player and provide an interesting game experience. The movement strategies are different for each type of adversary. Ghosts have the ability to move freely through the level, and can also teleport randomly by moving into walls. Zombies are constrained to the room that they are spawned in. If a player is located within that room, they will chase the player. Otherwise, they will move arbitrarily. 

###Ghosts###
- Checks if there is a player within a 6 step radius of the Ghost
- If players exist within that radius, take 1 step towards the closest player
- If there are no players in that radius, move towards the closest door and teleport through an adjacent Wall

Examples: 

```
XXXX   
X..X         
XP.X         
X..X    XXXXX
X...****G...X
XXXX    X...X
        X...X
        X...X
        XXXXX
```
In the above example, the Ghost will move into the adjacent wall and be randomly transported to another room, since the player is not within their chase radius. 

```
XXXX   
X..X         
X..X         
X..X    XXXXX
X..G*P**....X
XXXX    X...X
        X...X
        X...X
        XXXXX
```
In this example, the Ghost will follow the player into the hallway for as long as they remain in their chase radius.

```
XXXX   
XG.X         
X..X         
X..X    XXXXX
X...****.P..X
XXXX    X...X
        X...X
        X...X
        XXXXX
```
In this example, the Ghost will move towards the closest door since the player is not in their chase radius. Once the Ghost reaches the door, they will teleport.

###Zombies###
- Checks if there is a player in the Zombie's LevelComponent
- If there are players in the LevelComponent, take 1 step towards the closest player
- If there are no players in the LevelComponent,make some valid random move

Examples: 

```
XXXX   
X..X         
XP.X         
X..X    XXXXX
XZ..****....X
XXXX    X...X
        X...X
        X...X
        XXXXX
```
In the above example, the Zombie will move up towards the player because they are in the same room.

```
XXXX   
X..X         
X..X         
X..X    XXXXX
XZ..****....X
XXXX    XP..X
        X...X
        X...X
        XXXXX
```
In this example, the player is in a different room from the Zombie. The Zombie will then make a valid, random move. 

```
XXXX   
X..X         
X..X         
X..X    XXXXX
X.ZP****....X
XXXX    X...X
        X...X
        X...X
        XXXXX
```
In this example, the Zombie is unable to reach the player because they are located on a door tile. The Zombie will be forced to make a valid, arbitrary move. 
