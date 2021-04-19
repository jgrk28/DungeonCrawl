# Hit Point & Combat System

## Players
* Players are unable to damage to adversaries or players
* They store a default starting health of 20 health points
* If they get hit, the health points are updated 
* Once the health points have been reduced to 0 or below, they will be expelled from the level
* A message is sent to the player when they are hit
* The current health points are also displayed during player updates

## Adversaries
* Adversaries are only able to damage players
* Adversaries store the amount of damage that they do, based on their type
* If an adversary damages a player, they only move on to the square the player is on if the player is expelled

### Ghosts
* The damage by a ghost is 5 health points

### Zombies
* The damage by a zombie is 7 health points

//TODO add notes about snarl protocol changes