package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import modelView.EntityType;

/**
 * Represents a Level within a game of Snarl
 * An ASCII representation of the Level may like this:
 * 
 * XXXX         
 * X..X         
 * X..X         
 * X..X    XXXXX
 * X...****....X
 * XXXX    X...X
 *         X...X
 *         X...X
 *         XXXXX
 *
 * Where each Entity corresponds to the following:
 * - Wall (X)
 * - Space (.)
 * - Hall Space (*)
 * - Key (!)
 * - Exit (@)
 * - Player (P)
 * - Adversary - Ghost (G)
 * - Adversary - Zombie (Z)
 * - Empty - where no entities have been placed  (" ")
 *  
 * For more examples, see test/view/TextualLevelView.java
 */
public class LevelImpl implements Level {
	
	//The map of LevelComponents that compose the Level
	private List<LevelComponent> levelMap;

	//The map of all EntityTypes used to render the Level
	private ArrayList<ArrayList<EntityType>> viewableMap;
	
	//Ordered map of players that reflects the turn order 
	//and maps to each player to their current location
	private LinkedHashMap<Player,LevelComponent> playerLocations;
	
	//Ordered map of adversaries that reflects the turn order 
	//and maps to each adversary to their current location
	private LinkedHashMap<Adversary,LevelComponent> adversaryLocations;
	
	//True if exit has been unlocked by finding the key
	private Boolean exitUnlocked;
	
	//True if at least one player has exited the level
	private Boolean levelExited;
	
	//Key within the level, stores its own position
	private Key key;
	private LevelComponent keyRoom;
	
	//Exit within the level, stores its own position
	private Exit exit;
	private LevelComponent exitRoom;
	
	/** 
	 * Initializes a new level. This constructor is used primarily for testing 
	 * @param levelMap - the map of all LevelComponents within the level
	 * @param key - the key within the level
	 * @param exit - the exit within the level
	 */
	public LevelImpl(List<LevelComponent> levelMap, Key key, Exit exit) {
		/**
		 * A level is comprised of a series of rooms connected by hallways.
		 * A level is valid if no two rooms overlap, no two hallways overlap,
		 * and no hallways overlap with any rooms.
		 *
		 * This validation will be implemented when levels are generated 
		 * automatically
		 */
		this.levelMap = levelMap;
		this.exitUnlocked = false;
		this.levelExited = false;
		this.playerLocations = new LinkedHashMap<>();
		this.adversaryLocations = new LinkedHashMap<>();
		this.key = key;
		if (this.key == null) {
			this.keyRoom = null;
		} else {
			this.keyRoom = findComponent(this.key.location);
			placeKey();
		}
		this.exit = exit;
		if (this.exit == null) {
			this.exitRoom = null;
		} else {
			this.exitRoom = findComponent(this.exit.location);
			placeExit();
		}
	}
	
	/** 
	 * Initializes a new level and places actors in starting positions
	 * All players are placed in the top left-most room of the level
	 * All adversaries are placed in the bottom right-most room of the level
	 * @param players - list of all players in the level
	 * @param adversaries - list of all adversaries in the level
	 * @param levelMap - the map of all LevelComponents within the level
	 * @param key - the key within the level
	 * @param exit - the exit within the level
	 */
	public LevelImpl(List<Player> players, List<Adversary> adversaries, List<LevelComponent> levelMap, Key key, Exit exit) {
		if (levelMap.isEmpty()) {
			throw new IllegalArgumentException("Level map does not have any components");
		}
		
		//Initialize fields
		this.levelMap = levelMap;
		this.exitUnlocked = false;
		this.levelExited = false;
		this.playerLocations = new LinkedHashMap<>();
		this.adversaryLocations = new LinkedHashMap<>();
		this.key = key;
		this.keyRoom = findComponent(this.key.location);
		this.exit = exit;
		this.exitRoom = findComponent(this.exit.location);
		
		//Place the key and exit
		placeKey();
		placeExit();

		//Identify the top left-most and bottom right-most rooms in the level
		Room topLeftRoom = getBoundaryRoom(true);
		Room bottomRightRoom = getBoundaryRoom(false);

		//Add players to top left room
		for (Player player : players) {
			playerLocations.put(player, topLeftRoom);
			placeActorValidly(player, topLeftRoom);
		}
		
		//Add adversaries to bottom right room
		for (Adversary adversary : adversaries) {
			adversaryLocations.put(adversary, bottomRightRoom);
			placeActorValidly(adversary, bottomRightRoom);
		}
	}

	/**
	 * Initializes a level and places actors in specified positions
	 * @param players - list of all players in the level and their location
	 * @param adversaries - list of all adversaries in the level and their location
	 * @param levelMap - the map of all LevelComponents within the level
	 * @param exitUnlocked - true if the exit has been unlocked
	 * @param levelExited - true if a player has exited the level
	 * @param key - the key within the level
	 * @param exit - the exit within the level
	 */
	public LevelImpl(
			Map<Player, Point> players,
			Map<Adversary, Point> adversaries,
			List<LevelComponent> levelMap,
			boolean exitUnlocked,
			boolean levelExited,
			Key key, 
			Exit exit
	)
	{
		if (levelMap.isEmpty()) {
			throw new IllegalArgumentException("Level map does not have any components");
		}
		
		//Initialize fields
		this.levelMap = levelMap;
		this.exitUnlocked = exitUnlocked;
		this.levelExited = levelExited;
		this.playerLocations = new LinkedHashMap<>();
		this.adversaryLocations = new LinkedHashMap<>();
		this.key = key;
		this.keyRoom = findComponent(this.key.location);
		this.exit = exit;
		this.exitRoom = findComponent(this.exit.location);
		
		//Place the key and exit
		placeKey();
		placeExit();

		//Add players to the corresponding LevelComponent
		for (Map.Entry<Player, Point> entry : players.entrySet()) {
			//Find the component based on the position of the player
			LevelComponent component = findComponent(entry.getValue());
			
			//Track the player and the LevelComponent they are located in
			playerLocations.put(entry.getKey(), component);
			
			//Check that the destination of the player is a space and place the actor
			//If the location is not a space, throw the corresponding error
			Entity destinationEntity = component.getDestinationEntity(entry.getValue());
			if (!(destinationEntity instanceof Space)) {
				throw new IllegalArgumentException("Cannot place player, destination is not a space"); 
			}
			component.placeActor(entry.getKey(), entry.getValue());
		}
		//Add adversaries to corresponding LevelComponent
		for (Map.Entry<Adversary, Point> entry : adversaries.entrySet()) {
			//Find the component based on the position of the adversary
			LevelComponent component = findComponent(entry.getValue());
			
			//Track the adversary and the LevelComponent they are located in
			adversaryLocations.put(entry.getKey(), component);
			
			//Check that the destination of the adversary is a space and place the actor
			//If the location is not a space, throw the corresponding error
			Entity destinationEntity = component.getDestinationEntity(entry.getValue());
			if (!(destinationEntity instanceof Space)) {
				throw new IllegalArgumentException("Cannot place adversary, destination is not a space"); 
			}
			component.placeActor(entry.getKey(), entry.getValue());
		}
	}


	//TODO combine these to just placeObject
	/**
	 * Places the key for this level in its position as determined by its field. If the position is
	 * occupied it will simply not place the key. This function will be called when the level is
	 * created.
	 */
	private void placeKey() {
		LevelComponent component = findComponent(this.key.location);
		component.placeKey(this.key);
	}

	/**
	 * Places the exit for this level in its position as determined by its field. If the position is
	 * occupied it will simply not place the exit. This function will be called when the level is
	 * created and also any time an actor moves off the exit.
	 */
	private void placeExit() {
		LevelComponent component = findComponent(this.exit.location);
		component.placeExit(this.exit);	
	}

	/**
	 * Finds the top left-most or bottom right-most room in the level map
	 * @param closeToOriginFlag - true if we would like the top left-most room,
	 * false if we would like the bottom right-most room
	 * @return the boundary room
	 */
	private Room getBoundaryRoom(boolean closeToOriginFlag) {
		Room boundaryRoom = null;
		Point boundaryPos = null;
		Point origin = new Point(0, 0);

		//Iterate through all components in the level
		for (LevelComponent component : levelMap) {
			//Only process the component if it is a room
			if (!(component instanceof Room)) {
				continue;
			}
			
			Point topLeft = component.getTopLeftBound();

			//Initialize fields on first iteration
			if (boundaryPos == null) {
				boundaryRoom = (Room)component;
				boundaryPos = topLeft;
			}
			
			//Calculate the distance to the origin for boundary room
			//and the current component
			double boundaryDistToOrigin = boundaryPos.distance(origin);
			double newDistToOrigin = topLeft.distance(origin);

			//When trying to find the top left-most room, replace the boundary room
			//if the current room is closer to the origin than the boundary room,
			//or if the rooms are the same distance from the origin and the current 
			//room has a smaller row index
			boolean replaceRoomMin = closeToOriginFlag
					&& (newDistToOrigin < boundaryDistToOrigin
					|| (newDistToOrigin == boundaryDistToOrigin && topLeft.y < boundaryPos.y));

			//When trying to find the bottom right-most room, replace the boundary room
			//if the current room is further from the origin than the boundary room,
			//or if the rooms are the same distance from the origin and the current 
			//room has a bigger row index
			boolean replaceRoomMax = !closeToOriginFlag
					&& (newDistToOrigin > boundaryDistToOrigin
					|| (newDistToOrigin == boundaryDistToOrigin && topLeft.y > boundaryPos.y));

			if (replaceRoomMin || replaceRoomMax) {
				boundaryRoom = (Room)component;
				boundaryPos = topLeft;
			}
		}

		if (boundaryRoom == null) {
			throw new IllegalArgumentException("No rooms in level");
		}

		return boundaryRoom;
	}

	/**
	 * Places the actor at the first valid position in the given room
	 * @param actor - the actor to be placed
	 * @param room - the room to place the actor in
	 */
	private void placeActorValidly(Actor actor, Room room) {
		Point roomTopLeft = room.getTopLeftBound();
		Point roomBottomRight = room.getBottomRightBound();

		//Iterate through each position in the room
		for (int row = roomTopLeft.y; row <= roomBottomRight.y; row++) {
			for (int col = roomTopLeft.x; col <= roomBottomRight.x; col++) {
				Point currPoint = new Point(col, row);
				try {
					//If the position contains a space, place the actor here
					Entity destEntity = room.getDestinationEntity(currPoint);
					if (destEntity instanceof Space) {
						room.placeActor(actor, currPoint);
						return;
					}
				} catch (IllegalArgumentException e) {
					//Do nothing
				}
			}
		}
		throw new IllegalArgumentException("Room has no empty spaces to place a new actor");
	}

	@Override
	public LevelComponent findComponent(Point point) {
		for (LevelComponent component : this.levelMap) {
			if (component.inComponent(point)) {
				return component;
			}
		}
		throw new IllegalArgumentException("Point is not anywhere within the level");
	}
	
	@Override
	public ArrayList<ArrayList<EntityType>> getMap() {
		Point topLeftBound = getTopLeft();
		Point bottomRightBound = getBottomRight();
		this.viewableMap = initializeEmptyMap(topLeftBound, bottomRightBound);

		for (LevelComponent component : levelMap) {
			addToViewableMap(component, topLeftBound, bottomRightBound);
		}

		return this.viewableMap;
	}

	/**
	 * Returns the top left boundary for the level
	 * This is the left most coordinate at which a LevelComponent is placed
	 * @return the coordinates of the top left boundary
	 */
	private Point getTopLeft() {
		//Level will always have at least one component
		LevelComponent firstComponent = this.levelMap.get(0);
		Point initTopLeft = firstComponent.getTopLeftBound();
		Integer minX = initTopLeft.x;
		Integer minY = initTopLeft.y;

		//Iterate through all LevelComponents
		//Return the minimum X and minimum Y values that are found
		for (LevelComponent component : levelMap) {
			Point topLeft = component.getTopLeftBound();
			if (topLeft.x < minX) {
				minX = topLeft.x;
			}
			if (topLeft.y < minY) {
				minY = topLeft.y;
			}
		}
		return new Point(minX, minY);
	}

	/**
	 * Returns the bottom right boundary for the level
	 * This is the right most coordinate at which a LevelComponent is placed
	 * @return the coordinates of the bottom right boundary
	 */
	private Point getBottomRight() {
		//Level will always have at least one component
		LevelComponent firstComponent = this.levelMap.get(0);
		Point initBottomRight = firstComponent.getBottomRightBound();
		Integer maxX = initBottomRight.x;
		Integer maxY = initBottomRight.y;
		
		//Iterate through all LevelComponents
		//Return the maximum X and maximum Y values that are found
		for (LevelComponent component : levelMap) {
			Point bottomRight = component.getBottomRightBound();
			if (bottomRight.x > maxX) {
				maxX = bottomRight.x;
			}
			if (bottomRight.y > maxY) {
				maxY = bottomRight.y;
			}
		}
		return new Point(maxX, maxY);
	}

	/**
	 * Initializes a new map of EntityTypes as EMPTY
	 * @return the map of EMPTY EntityTypes
	 * @param topLeftBound - top left bound of viewable level
	 * @param bottomRightBound - bottom right bound of viewable level
	 */
	ArrayList<ArrayList<EntityType>> initializeEmptyMap(Point topLeftBound,
			Point bottomRightBound) {
		//Determine the size of the map based on the bounds
		int xSize = bottomRightBound.x - topLeftBound.x + 1;
		int ySize = bottomRightBound.y - topLeftBound.y + 1;

		ArrayList<ArrayList<EntityType>> emptyMap = new ArrayList<>();

		//Iterate through the map and place an EMPTY EntityType at
		//each coordinate
		for (int i = 0; i < ySize; i++) {
			ArrayList<EntityType> emptyRow = new ArrayList<>();
			for (int j = 0; j < xSize; j++) {
				emptyRow.add(j, EntityType.EMPTY);
			}
			emptyMap.add(i, emptyRow);
		}
		return emptyMap;
	}

	/**
	 * Adds each Entity within a LevelComponent to the viewableMap
	 * @param component - the LevelComponent to add to the viewableMap
	 * @param topLeftBound - top left bound of viewable level
	 * @param bottomRightBound - bottom right bound of viewable level
	 */
	private void addToViewableMap(LevelComponent component, Point topLeftBound,
			Point bottomRightBound) {
		//Iterate through the viewableMap
		for (int i = topLeftBound.y; i <= bottomRightBound.y; i++) {
			for (int j = topLeftBound.x; j <= bottomRightBound.x; j++) {
				try {
					//Check if an Entity exists at the given coordinates in the LevelComponent
					//If these coordinates are not available for this LevelComponent, no changes are made
					Entity destEntity = component.getDestinationEntity(new Point(j, i));
					
					//Get the EntityType of the entity at these coordinates
					EntityType destEntityDrawable = component.getEntityType(destEntity);
					
					//Add the EntityType to the viewableMap
					int croppedYIndex = i - topLeftBound.y;
					int croppedXIndex = j - topLeftBound.x;
					ArrayList<EntityType> editRow = this.viewableMap.get(croppedYIndex);
					editRow.set(croppedXIndex, destEntityDrawable);
				} catch (IllegalArgumentException e) {
					//Do Nothing
				}
			}
		}
	}

	@Override
	public void playerAction(Player player, Point destination) {
		LevelComponent sourceComponent = this.playerLocations.get(player);
		LevelComponent destinationComponent;
		
		//If the player is not moving within the same LevelComponent, 
		//find the destination LevelComponent
		if (sourceComponent.inComponent(destination)) {
			destinationComponent = sourceComponent;		
		} else {
			destinationComponent = findComponent(destination);
		}
		
		//Determine the EntityType at the destination and the resulting interaction
		Entity destinationEntity = destinationComponent.getDestinationEntity(destination);
		EntityType destinationType = destinationComponent.getEntityType(destinationEntity);
		InteractionResult interaction = player.getInteractionResult(destinationType);
		
		//If player runs into a exit or adversary, we are removing the player
		//instead of moving them
		boolean removePlayer = interaction.equals(InteractionResult.EXIT)
				|| interaction.equals(InteractionResult.REMOVE_PLAYER);
			
		//If player is moving to a new room, remove them from the source room
		//Otherwise, remove them from their current position
		if (!destinationComponent.equals(sourceComponent)) {
			sourceComponent.removeActor(player);
		} else {
			destinationComponent.removeActor(player);
		}
		
		//If the player is not removed from the level, place them at the destination
		if (!removePlayer) {
			destinationComponent.placeActor(player, destination);
			this.playerLocations.replace(player, destinationComponent);	
		} else {
			this.playerLocations.remove(player);
		}
		
		if (interaction.equals(InteractionResult.EXIT)) {
			this.levelExited = true;
		}
		
		if (interaction.equals(InteractionResult.FOUND_KEY)) {
			this.exitUnlocked = true;
		}			
	}
	
	@Override
	public void adversaryAction(Adversary adversary, Point destination) {	
		LevelComponent sourceComponent = this.adversaryLocations.get(adversary);
		LevelComponent destinationComponent;
		
		//If the adversary is not moving within the same LevelComponent, 
		//find the destination LevelComponent
		if (sourceComponent.inComponent(destination)) {
			destinationComponent = sourceComponent;		
		} else {
			destinationComponent = findComponent(destination);
		}
		
		//Determine the EntityType at the destination and the resulting interaction
		Entity destinationEntity = destinationComponent.getDestinationEntity(destination);
		EntityType destinationType = destinationComponent.getEntityType(destinationEntity);
		InteractionResult interaction = adversary.getInteractionResult(destinationType);
			
		//If adversary is moving to a new room, remove them from the source room
		//Otherwise, remove them from their current position
		if (!destinationComponent.equals(sourceComponent)) {
			sourceComponent.removeActor(adversary);
		} else {
			destinationComponent.removeActor(adversary);
		}
		
		//Place the adversary and update their location
		destinationComponent.placeActor(adversary, destination);
		this.adversaryLocations.replace(adversary, destinationComponent);	
	
		//If the adversary interacts with a player, remove the player from the level
		if (interaction.equals(InteractionResult.REMOVE_PLAYER)) {
			this.playerLocations.remove(destinationEntity);
		}	
		
		if (sourceComponent.equals(keyRoom)) {
			sourceComponent.placeKey(this.key);
			
		} else if (sourceComponent.equals(exitRoom)) {
			sourceComponent.placeExit(this.exit);			
		}
	}
	
	@Override
	public GameState isLevelOver() {
		//If all players have not been removed from the level,
		//the level is still active
		if (!playerLocations.isEmpty()) {
			return GameState.ACTIVE;
		}
		//If any player exited the level, the level has been won
		if (levelExited) {
			return GameState.WON;
		}
		else {
			return GameState.LOST;
		}
	}

}
