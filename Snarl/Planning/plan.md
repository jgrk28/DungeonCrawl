TO: Manager  
FROM: Jacob Kaplan and ArDelia MacPhail  
DATE: February 8th, 2021  
SUBJECT: Snarl Project Analysis  

After analyzing the requirements for Snarl, we have arrived at the following system and components. A game of Snarl consists of four high level components: Dungeon, Level, Level Component, and Entity. The Dungeon is synonymous to the game software. This includes managing the initialization of players and the Level, as well as starting the current Level. The Level consists of a levelMap, which is composed of LevelComponents. The Level is responsible for keeping track of the Player and Adversary locations, the turn order, and whether the exit is unlocked. It initializes the state of the level, starts the level, manages the turn order, and ends the level when appropriate. 

A LevelComponent has one field, the componentMap of entities. This map represents the layout of an individual component, which can be either a Hall or a Room. The LevelComponent is responsible for the action of players and adversaries, including a movement and an interaction. An Entity can be a Key, Exit, Wall, Space, Player, or Adversary. A Player has a PlayerState, which can be Active, Removed, or Completed. 

LevelComponents know their dimensions, which entities are located in the component, and where they are. They need to know what other entities they are connected to, and who is moving in and out of the component. It communicates with the Level for this information. The Level knows the LevelComponent locations of the players and adversaries, the connections of LevelComponents, the state of the exit, and the turn order. They need to know where players/adversaries are moving to, if the key has been found, or if a Player has been removed from a LevelComponent. It communicates with the LevelComponent for this information. An Adversary knows what type of Adversary it is, and a Player knows their game state. A Player needs to know when their game state changes, and will communicate with the Level for this information. The Dungeon knows all players in the game, the number of levels and the state of the game. They need to know the outcome of the current Level, and will communicate with the current level for this information. 

Our proposed development plan allows for our product to be periodically demoed along the way without slowing down development. At each milestone we will only implement the parts of the components that are needed for that specific milestone. This means that at a future milestone you will most likely need to continue extending parts that were completed previously. In addition we will develop rendering of the game in parallel to the backend. This will allow us to always be able to show what is going on with the backend in our demos.

1. Rooms/Halls - We will start by building the individual rooms/halls of the dungeons. This will allow us to quickly start rendering the game for demo.
2. Players - We will then implement some basic characters and movement. A demo will allow users to move around individual rooms.
3. Full Level - We will then connect the rooms/halls into full levels and allow players to move between rooms.
4. Key/Exit - We will add the key and exit door so players are able to beat a level.
5. Adversaries - We can place automated adversaries in the level to move around following the player.
6. Multi-level Dungeon -  We can finally connect multiple levels into a dungeon for the full game experience.
