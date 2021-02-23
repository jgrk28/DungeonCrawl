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
	
	/** 
	 * Initializes a new level
	 * This constructor is used primarily for testing 
	 * @param levelMap - the map of all LevelComponents within the level
	 */
	public LevelImpl(List<LevelComponent> levelMap) {
		this.levelMap = levelMap;
	}
	
	/** 
	 * Initializes a new level and places actors in starting positions
	 * @param players - list of all players in the level
	 * @param adversaries - list of all adversaries in the level
	 * @param levelMap - the map of all LevelComponents within the level
	 */
	public LevelImpl(List<Player> players, List<Adversary> adversaries, List<LevelComponent> levelMap) {
		/**
		 * A level is comprised of a series of rooms connected by hallways.
		 * A level is valid if no two rooms overlap, no two hallways overlap,
		 * and no hallways overlap with any rooms.
		 *
		 * This validation will be implemented when levels are generated 
		 * automatically
		 */

		if (levelMap.isEmpty()) {
			throw new IllegalArgumentException("Level map does not have any components");
		}
		this.levelMap = levelMap;
		this.exitUnlocked = false;
		this.levelExited = false;

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
	 * Initializes a new level and places actors in specified positions
	 * @param players - list of all players in the level
	 * @param adversaries - list of all adversaries in the level
	 * @param levelMap - the map of all LevelComponents within the level
	 */
	public LevelImpl(
			Map<Player, Point> players,
			Map<Adversary, Point> adversaries,
			List<LevelComponent> levelMap,
			boolean exitUnlocked,
			boolean levelExited
	)
	{
		if (levelMap.isEmpty()) {
			throw new IllegalArgumentException("Level map does not have any components");
		}
		this.levelMap = levelMap;
		this.exitUnlocked = exitUnlocked;
		this.levelExited = levelExited;

		//Add players to corresponding LevelComponent
		for (Map.Entry<Player, Point> entry : players.entrySet()) {
			LevelComponent component = findComponent(entry.getValue());
			playerLocations.put(entry.getKey(), component);
			Entity destinationEntity = component.getDestinationEntity(entry.getValue());
			if (!(destinationEntity instanceof Space)) {
				throw new IllegalArgumentException("Cannot place player, destination is not a space"); 
			}
			component.placeActor(entry.getKey(), entry.getValue());
		}
		//Add adversaries to corresponding LevelComponent
		for (Map.Entry<Adversary, Point> entry : adversaries.entrySet()) {
			LevelComponent component = findComponent(entry.getValue());
			adversaryLocations.put(entry.getKey(), component);
			Entity destinationEntity = component.getDestinationEntity(entry.getValue());
			if (!(destinationEntity instanceof Space)) {
				throw new IllegalArgumentException("Cannot place adversary, destination is not a space"); 
			}
			component.placeActor(entry.getKey(), entry.getValue());
		}
	}

	private Room getBoundaryRoom(boolean closeToOriginFlag) {
		Room boundaryRoom = null;
		LevelComponent firstComponent = levelMap.get(0);
		Point firstTopLeft = firstComponent.getTopLeftBound();
		Point origin = new Point(0, 0);
		double distToOrigin = firstTopLeft.distance(origin);
		for (LevelComponent component : levelMap) {
			if (!(component instanceof Room)) {
				continue;
			}
			Point topLeft = component.getTopLeftBound();

			double newDistToOrigin = topLeft.distance(origin);
			if (closeToOriginFlag && newDistToOrigin < distToOrigin) {
				distToOrigin = newDistToOrigin;
				boundaryRoom = (Room)firstComponent;
			}
			if (!closeToOriginFlag && newDistToOrigin > distToOrigin) {
				distToOrigin = newDistToOrigin;
				boundaryRoom = (Room)firstComponent;
			}
		}
		if (boundaryRoom == null) {
			throw new IllegalArgumentException("No rooms in level");
		}

		return boundaryRoom;
	}

	private void placeActorValidly(Actor actor, Room room) {
		Point roomTopLeft = room.getTopLeftBound();
		Point roomBottomRight = room.getBottomRightBound();

		for (int row = roomTopLeft.y; row <= roomBottomRight.y; row++) {
			for (int col = roomTopLeft.x; col <= roomBottomRight.x; col++) {
				Point currPoint = new Point(col, row);
				try {
					room.placeActor(actor, currPoint);
					return;
				} catch (IllegalArgumentException e) {
					//Do nothing
				}
			}
		}
		throw new IllegalArgumentException("Room has no empty spaces to place a new actor");
	}

	private LevelComponent findComponent(Point point) {
		for (LevelComponent component : this.levelMap) {
			if (component.inComponent(point)) {
				return component;
			}
		}
		throw new IllegalArgumentException("Point is not anywhere within the level");
	}

	//This method will be implemented at a later milestone as we realized this was not asked for
	//Initialize a random level from the given seed. Level will place the given players 
	//and adversaries
	public LevelImpl(List<Player> players, List<Adversary> adversaries, long seed) {
		//Create all the rooms, then connect with halls
		//Make sure the room does not overlap with existing rooms
		//Randomize dimensions of room
		//Helper function that takes in a list of level components and the random num, gives
		//us a size and location for a new room that is valid
		//Pass this to the level component and add to the levelMap
		//Set bounds for min and max number of rooms in a level
	}

	//This method will be implemented at a later milestone as we realized this was not asked for
	//Create a new random LevelComponent 
	private LevelComponent generateRandomRoom(Random generator) {
		//Generate a random size and position
		//Check if valid
		//Loop until valid values are found
		//Create the room
		return null;
	}

	//This method will be useful when we start to build the random level generator
	private Boolean validRoomPlacement(Point topLeftPos, Point bottomRightPos) {
		//Loop through all things in the LevelMap
		for (LevelComponent component : levelMap) {
			Point topRightPos = new Point(bottomRightPos.x, topLeftPos.y);
			Point bottomLeftPos = new Point(topLeftPos.x, bottomRightPos.y);

			Point componentTopLeft = component.getTopLeftBound();
			Point componentBottomRight = component.getBottomRightBound();

			//Check if rooms overlap, return false if this is the case
			//Conditions that rooms overlap:
			// - any corner of new room is in old room
			// - old room TopLeft or old room BotRight is in new room
			if (component.inComponent(topLeftPos)
					|| component.inComponent(topRightPos)
					|| component.inComponent(bottomLeftPos)
					|| component.inComponent(bottomRightPos)
				  || isPointWithinBounds(componentTopLeft, topLeftPos, bottomRightPos)
				  || isPointWithinBounds(componentBottomRight, topLeftPos, bottomRightPos))
			{
				return false;
			}
		}
		return true;
	}

	private boolean isPointWithinBounds(Point pointToCheck, Point topLeftBound, Point bottomRightBound) {
		return pointToCheck.x <= bottomRightBound.x
				&& pointToCheck.x >= topLeftBound.x
				&& pointToCheck.y <= bottomRightBound.y
				&& pointToCheck.y >= topLeftBound.y;
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

	/**
	 * Check if the actor is leaving the room that they are currently in
	 * If so, we want to try to place them in the new room and remove them from the old room
	 * Otherwise, we want to move them within the current room 
	 */
	@Override
	public void playerAction(Player player, Point destination) {
		LevelComponent sourceComponent = this.playerLocations.get(player);
		LevelComponent destinationComponent;
		
		if (sourceComponent.inComponent(destination)) {
			destinationComponent = sourceComponent;		
		} else {
			destinationComponent = findComponent(destination);
		}
		
		Entity destinationEntity = destinationComponent.getDestinationEntity(destination);
		EntityType destinationType = destinationComponent.getEntityType(destinationEntity);
		InteractionResult interaction = player.getInteractionResult(destinationType);
		
		//If player runs into a exit or adversary, we are removing instead of moving
		boolean placePlayer = !interaction.equals(InteractionResult.EXIT) 
				|| !interaction.equals(InteractionResult.REMOVE_PLAYER);
			
		//If player is moving to a new room, we need to check that we can place them in this room
		if (!destinationComponent.equals(sourceComponent)) {
			sourceComponent.removeActor(player);
		} else {
			destinationComponent.removeActor(player);
		}
		
		if (placePlayer) {
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
		
		if (sourceComponent.inComponent(destination)) {
			destinationComponent = sourceComponent;		
		} else {
			destinationComponent = findComponent(destination);
		}
		
		Entity destinationEntity = destinationComponent.getDestinationEntity(destination);
		EntityType destinationType = destinationComponent.getEntityType(destinationEntity);
		InteractionResult interaction = adversary.getInteractionResult(destinationType);
			
		//If player is moving to a new room, we need to check that we can place them in this room
		if (!destinationComponent.equals(sourceComponent)) {
			sourceComponent.removeActor(adversary);
		} else {
			destinationComponent.removeActor(adversary);
		}
		
		destinationComponent.placeActor(adversary, destination);
		this.adversaryLocations.replace(adversary, destinationComponent);	
	
		if (interaction.equals(InteractionResult.REMOVE_PLAYER)) {
			this.playerLocations.remove(destinationEntity);
		}		
	}
	
	/**
	 * 		case SPACE: 
				return InteractionResult.NONE;
			case HALL_SPACE: 
				return InteractionResult.NONE;
			case PLAYER:
				return InteractionResult.REMOVE_PLAYER;
			default:
				throw new IllegalArgumentException("Illegal interaction entity for adversary");
	 */
	

	@Override
	public GameState isLevelOver() {
		if (!playerLocations.isEmpty()) {
			return GameState.ACTIVE;
		}
		if (levelExited) {
			return GameState.WON;
		}
		else {
			return GameState.LOST;
		}
	}

}
