# Hit Point & Combat System
Our hit point system gives adversaries the ability to attack players. Each player starts with a certain number of health points that protect them from adversary attacks. When a player's health points reach 0, they are expelled from the level. When an adversary attacks a player they reduce that player's health points by the amount of "damage" they do. Since ghosts are more mobile, they do less damage than zombies. Players do not have the ability to attack other players or adversaries.

## Players
* Players are unable to damage to adversaries or players
* They store a default starting health of 20 health points
* Player's health points will reset each level
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

## Snarl Protocol
Two additional fields have been added to the player update message for the Snarl protocol. These fields indicate the player's current and maximum health points. 
```
A (player-update-message) is a JSON object containing a player update and an optional message from the server.

{ "type": "player-update",
  "current-health": (natural),
  "max-health": (natural),
  "layout": (tile-layout),
  "position": (point),
  "objects": (object-list),
  "actors": (actor-position-list),
  "message": (maybe-string)
}
```