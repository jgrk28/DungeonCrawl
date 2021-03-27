package Game.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Game.modelView.EntityType;
import java.util.Set;

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
 * Where each EntityType corresponds to the following:
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
	//TODO do we actually need this here
	private List<List<EntityType>> viewableMap;
	
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
	
	//All items in the level
	private List<Item> items;
	
	/** 
	 * Initializes a new level. This constructor is used primarily for testing 
	 * @param levelMap - the map of all LevelComponents within the level
	 * @param items - all items in the level
	 */
	public LevelImpl(List<LevelComponent> levelMap, List<Item> items) {
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
		this.items = items;
		
		for (Item item : this.items) {	
			placeItem(item);
		}	
	}
	
	/** 
	 * Initializes a new level and places actors in starting positions
	 * All players are placed in the top left-most room of the level
	 * All adversaries are placed in the bottom right-most room of the level
	 * @param players - list of all players in the level
	 * @param adversaries - list of all adversaries in the level
	 * @param levelMap - the map of all LevelComponents within the level
	 * @param items - all items in the level
	 */
	public LevelImpl(List<Player> players, List<Adversary> adversaries, 
			List<LevelComponent> levelMap, List<Item> items) {
		if (levelMap.isEmpty()) {
			throw new IllegalArgumentException("Level map does not have any components");
		}
		
		//Initialize fields
		this.levelMap = levelMap;
		this.exitUnlocked = false;
		this.levelExited = false;
		this.playerLocations = new LinkedHashMap<>();
		this.adversaryLocations = new LinkedHashMap<>();
		this.items = items;
		
		for (Item item : this.items) {	
			placeItem(item);
		}

		placeActors(players, adversaries);
	}

	/**
	 * Initializes a level and places actors in specified positions
	 * @param players - list of all players in the level and their location
	 * @param adversaries - list of all adversaries in the level and their location
	 * @param levelMap - the map of all LevelComponents within the level
	 * @param exitUnlocked - true if the exit has been unlocked
	 * @param levelExited - true if a player has exited the level
	 * @param items - all items in the level
	 */
	public LevelImpl(
			Map<Player, Point> players,
			Map<Adversary, Point> adversaries,
			List<LevelComponent> levelMap,
			boolean exitUnlocked,
			boolean levelExited,
			List<Item> items
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
		this.items = items;
		
		for (Item item : this.items) {	
			placeItem(item);
		}

		//Add players to the corresponding LevelComponent
		for (Map.Entry<Player, Point> entry : players.entrySet()) {
			//Find the component based on the position of the player
			LevelComponent component = findComponent(entry.getValue());
			
			//Track the player and the LevelComponent they are located in
			playerLocations.put(entry.getKey(), component);
			
			//Check that the destination of the player is a space and place the actor
			//If the location is not a space, throw the corresponding error
			Tile destinationTile = component.getDestinationTile(entry.getValue());
			EntityType destinationEntity = component.getEntityType(destinationTile);
			if (!(entry.getKey().isTraversable(destinationEntity))) {
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
			Tile destinationTile = component.getDestinationTile(entry.getValue());
			EntityType destinationEntity = component.getEntityType(destinationTile);
			if (!(entry.getKey().isTraversable(destinationEntity))) {
				throw new IllegalArgumentException("Cannot place adversary, destination is not a space"); 
			}
			component.placeActor(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Places the item (key or exit) in the level
	 * @param item - the item to be placed
	 */
	private void placeItem(Item item) {
		LevelComponent component = findComponent(item.getLocation());
		component.placeItem(item);
		
	}

	@Override
	public void placeActors(List<Player> players, List<Adversary> adversaries) {
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
					Tile destinationTile = room.getDestinationTile(currPoint);
					EntityType destinationEntity = room.getEntityType(destinationTile);
					if (destinationEntity.equals(EntityType.SPACE)) {
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
		throw new IllegalArgumentException("Point is not within a LevelComponent");
	}
	
	@Override
	public List<List<EntityType>> getMap() {
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
	private List<List<EntityType>> initializeEmptyMap(Point topLeftBound,
			Point bottomRightBound) {
		//Determine the size of the map based on the bounds
		int xSize = bottomRightBound.x - topLeftBound.x + 1;
		int ySize = bottomRightBound.y - topLeftBound.y + 1;

		List<List<EntityType>> emptyMap = new ArrayList<>();

		//Iterate through the map and place an EMPTY EntityType at
		//each coordinate
		for (int i = 0; i < ySize; i++) {
			List<EntityType> emptyRow = new ArrayList<>();
			for (int j = 0; j < xSize; j++) {
				emptyRow.add(j, EntityType.EMPTY);
			}
			emptyMap.add(i, emptyRow);
		}
		return emptyMap;
	}

	/**
	 * Adds each EntityType within a LevelComponent to the viewableMap
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
					//Check if a Tile exists at the given coordinates in the LevelComponent
					//If these coordinates are not available for this LevelComponent, no changes are made
					Tile destTile = component.getDestinationTile(new Point(j, i));
					
					//Get the EntityType of the tile at these coordinates
					EntityType destEntityDrawable = component.getEntityType(destTile);
					
					//Add the EntityType to the viewableMap
					int croppedYIndex = i - topLeftBound.y;
					int croppedXIndex = j - topLeftBound.x;
					List<EntityType> editRow = this.viewableMap.get(croppedYIndex);
					editRow.set(croppedXIndex, destEntityDrawable);
				} catch (IllegalArgumentException e) {
					//Do Nothing
				}
			}
		}
	}

	@Override
	public InteractionResult playerAction(Player player, Point destination) {
		LevelComponent sourceComponent = this.playerLocations.get(player);
		LevelComponent destinationComponent = findDestinationComponent(sourceComponent, destination);
		Tile destTile = destinationComponent.getDestinationTile(destination);
		InteractionResult interaction = player.getTileInteractionResult(destTile);

		if (interaction.equals(InteractionResult.EXIT) && !exitUnlocked) {
			interaction = InteractionResult.NONE;
		}
		//If player runs into an unlocked exit or adversary, we are removing the player
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

		if (interaction.equals(InteractionResult.FOUND_KEY)) {
			Tile destinationTile = destinationComponent.getDestinationTile(destination);
			this.items.remove(destinationTile.getItem());
			destinationTile.removeItem();
			this.exitUnlocked = true;
		}

		if (interaction.equals(InteractionResult.EXIT)) {
			this.levelExited = true;
		}

		return interaction;
	}
	
	@Override
	public InteractionResult adversaryAction(Adversary adversary, Point destination) {
		LevelComponent sourceComponent = this.adversaryLocations.get(adversary);
		LevelComponent destinationComponent = findDestinationComponent(sourceComponent, destination);

		Tile destTile = destinationComponent.getDestinationTile(destination);
		InteractionResult interaction = adversary.getTileInteractionResult(destTile);
			
		//If adversary is moving to a new room, remove them from the source room
		//Otherwise, remove them from their current position
		if (!destinationComponent.equals(sourceComponent)) {
			sourceComponent.removeActor(adversary);
		} else {
			destinationComponent.removeActor(adversary);
		}	
	
		//If the adversary interacts with a player, remove the player from the level
		if (interaction.equals(InteractionResult.REMOVE_PLAYER)) {
			Tile destinationTile = destinationComponent.getDestinationTile(destination);
			this.playerLocations.remove(destinationTile.getActor());
		}	
		
		//Place the adversary and update their location
		destinationComponent.placeActor(adversary, destination);
		this.adversaryLocations.replace(adversary, destinationComponent);

		return interaction;
	}
	
	/**
	 * Finds the component that the destination point is in. It checks the source component first
	 * @param sourceComponent - the component where the move originated
	 * @param destination - the destination to move to
	 * @return the LevelComponent that the destination point is in
	 */
	private LevelComponent findDestinationComponent(LevelComponent sourceComponent, Point destination) {		
		//If the actor is not moving within the same LevelComponent, 
		//find the destination LevelComponent
		if (sourceComponent.inComponent(destination)) {
			return sourceComponent;		
		} else {
			return findComponent(destination);
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

	@Override
	public Boolean checkValidMove(Actor actor, Point destination) {
		LevelComponent sourceComponent;
		
		//Get the actor location from the corresponding map
		if (actor instanceof Player) {
			sourceComponent = this.playerLocations.get(actor);
		} else if (actor instanceof Adversary) {
			sourceComponent = this.adversaryLocations.get(actor);
		} else {
			throw new IllegalArgumentException("Invalid actor type");
		}
		
		//Find the point that the actor is located at
		Point source = sourceComponent.findActorLocation(actor);

		//Check that the actor is moving a valid distance
		if (!actor.checkValidMoveDistance(source, destination)) {
			return false;
		}
		
		//Identify all intermediate EntityTypes between the source and the destination
		//If a valid path exists, return true
		List<List<EntityType>> intermediateTypes = getIntermediateTypes(source, destination, sourceComponent);
		return actor.checkValidMovePath(source, destination, intermediateTypes);			
	}
	
	/**
	 * Returns a matrix of intermediate paths between the source and destination 
	 * @param source - the current location of the actor
	 * @param destination - the destination of the actor
	 * @param sourceComponent - the component that the actor is located in
	 * @return a list of lists for each EntityType in the intermediate paths
	 */
	private List<List<EntityType>> getIntermediateTypes(Point source, Point destination, LevelComponent sourceComponent) {
		List<List<EntityType>> intermediateTypes = new ArrayList<>();

		int minX = Math.min(source.x, destination.x);
		int maxX = Math.max(source.x, destination.x);
		int minY = Math.min(source.y, destination.y);
		int maxY = Math.max(source.y, destination.y);
		
		//Iterate from the smallest coordinate to the largest coordinate for the source
		//and destination
		for (int row = minY; row <= maxY; row++) {			
			List<EntityType> currRow = new ArrayList<>();
			for (int col = minX; col <= maxX; col++) {
				Point currPoint = new Point(col, row);
				EntityType currType;
				//If the point is not in a LevelComponent, it is the EMPTY EntityType
				try {
					LevelComponent destinationComponent = findComponent(currPoint);
					Tile currTile = destinationComponent.getDestinationTile(currPoint);
					currType = destinationComponent.getEntityType(currTile);
				} catch (IllegalArgumentException e) {
					currType = EntityType.EMPTY;
				}
				currRow.add(currType);	
			} 		
			intermediateTypes.add(currRow);	
		}  
		return intermediateTypes;
	}

	@Override
	public Boolean checkValidLevelState(List<Player> players, List<Adversary> adversaries) {
		if (this.levelExited && !this.exitUnlocked) {
			return false;
		} 
		else if (!checkValidItems()) {
			return false;		
		} else if (!checkValidActors(players, adversaries)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Checks that the items in the level are valid. The items are valid if there
	 * is exactly one exit and exactly one key. If the exit is unlocked, there 
	 * should be no keys in the level
	 * @return true if the items in the level are valid
	 */
	private Boolean checkValidItems() {
		int exitCount = 0;
		int keyCount = 0;
		List<Point> positions = new ArrayList<>();
		//Check that there is at exactly one exit
		//Check if there is exactly one key or if the exit is unlocked
		for (Item item : this.items) {
			if (positions.contains(item.getLocation())) {
				return false;
			} else {
				positions.add(item.getLocation());
			}
			
			if (item instanceof Exit) {
				exitCount++;
			} else if (item instanceof Key) {
				keyCount++;
			} else {
				//Return false if the level contains an invalid item
				return false;
			}
		}
		
		return exitCount == 1 && (keyCount == 1 || (keyCount == 0 && this.exitUnlocked));
		
	}
	
	/**
	 * Checks that every actor in the level is a member of the game
	 * @param players - all players to check
	 * @param adversaries - all adversaries to check
	 * @return true if all actors are in the game, false otherwise
	 */
	private Boolean checkValidActors(List<Player> players, List<Adversary> adversaries) {
		for (Player player : this.playerLocations.keySet()) {
			if (!players.contains(player)) {
				return false;
			}
		}
		for (Adversary adversary : this.adversaryLocations.keySet()) {
			if (!adversaries.contains(adversary)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Boolean isPlayerAlive(Player player) {
		return playerLocations.containsKey(player);
	}

	@Override
	public List<List<EntityType>> getPlayerMap(Player player) {
		LevelComponent sourceComponent = playerLocations.get(player);
		Point playerLocation = sourceComponent.findActorLocation(player);
		
		List<List<EntityType>> fullLevel = getMap();
		Point topLeft = getTopLeft();
		//Crop map based on player's location
		return player.cropViewableMap(fullLevel, topLeft, playerLocation);
	}

	@Override
	public Player getPlayer(String name) {
		for (Player player : this.playerLocations.keySet()) {
			if (player.hasName(name)) {
				return player;
			}
		}
		throw new IllegalArgumentException("Actor does not exist in this level");
	}
	

	@Override
	public List<Point> getValidMoves(Player player) {
		LevelComponent sourceComponent = playerLocations.get(player);
		Point playerLocation = sourceComponent.findActorLocation(player);	
		
		//Get all potential moves based on the player's location
		List<Point> allPotentialMoves = player.getPotentialMoves(playerLocation);	
		List<Point> validMoves = new ArrayList<>();
		
		for (Point point : allPotentialMoves) {		
			//Identify all intermediate EntityTypes between the source and the destination
			//If a valid path exists, return true
			List<List<EntityType>> intermediateTypes = getIntermediateTypes(playerLocation, point, sourceComponent);
			if (player.checkValidMovePath(playerLocation, point, intermediateTypes)) {
				validMoves.add(point);
			}
		}
		return validMoves;
	}

	@Override
	public Point getActorPosition(Actor actor) {
		LevelComponent sourceComponent;
		if (actor instanceof Player) {
			sourceComponent = playerLocations.get(actor);
		} else if (actor instanceof Adversary) {
			sourceComponent = adversaryLocations.get(actor);
		} else {
			throw new IllegalArgumentException("Invalid actor type");
		}
		return sourceComponent.findActorLocation(actor);
	}

	@Override
	public List<Point> getVisibleDoors(Player player) {
		Point playerLocation = getActorPosition(player);
		Set<Point> allDoors = new HashSet<>();
		for (LevelComponent component : this.levelMap) {
			//If the component is a room, add the door locations to the set
			if (component instanceof Room) {
				Room room = (Room)component;
				Set<Point> roomDoors = room.getDoors().keySet();
				allDoors.addAll(roomDoors);
			}
		}
		return player.visibleDoors(allDoors, playerLocation);
	}

	@Override
	public List<Item> getVisibleItems(Player player) {
		List<Item> visibleItems = new ArrayList<>();
		Point playerLocation = getActorPosition(player);
		
		//Get the cropped map of the level that the player can see
		List<List<Tile>> tileMap = getTileMap();
		Point topLeft = getTopLeft();
		List<List<Tile>> croppedTileMap = player.cropTileMap(tileMap, topLeft, playerLocation);
		
		for (List<Tile> tileRow : croppedTileMap) {
			for (Tile tile : tileRow) {
				if (tile instanceof Wall) {
					continue;
				}
				Item item = tile.getItem();
				//If an item exists on the tile, add it to the list
				if (item != null) {
					visibleItems.add(item);
				}
			}
		}
		return visibleItems;
	}

	@Override
	public Map<Actor, Point> getVisibleActors(Player player) {
		Map<Actor, Point> visibleActors = new LinkedHashMap<>();
		Point playerLocation = getActorPosition(player);
		
		//Get the cropped map of the level that the player can see
		List<List<Tile>> tileMap = getTileMap();
		Point topLeft = getTopLeft();
		List<List<Tile>> croppedTileMap = player.cropTileMap(tileMap, topLeft, playerLocation);
		
		for (List<Tile> tileRow : croppedTileMap) {
			for (Tile tile : tileRow) {
				if (tile instanceof Wall) {
					continue;
				}
				Actor actor = tile.getActor();
				//If an actor exists on the tile, add it to the list
				if (actor != null && !player.equals(actor)) {
					Point actorPosition = getActorPosition(actor);
					visibleActors.put(actor, actorPosition);
				}
			}
		}
		return visibleActors;
	}

	@Override
	public Map<Actor, Point> getActivePlayers() {
		Map<Actor, Point> activePlayers = new LinkedHashMap<>();
		for (Map.Entry<Player, LevelComponent> locationEntry : this.playerLocations.entrySet()) {
			Player player = locationEntry.getKey();
			Point playerPoint = locationEntry.getValue().findActorLocation(player);
			activePlayers.put(player, playerPoint);
		}
		return activePlayers;
	}

	@Override
	public Map<Actor, Point> getActiveAdversaries() {
		Map<Actor, Point> activeAdversaries = new LinkedHashMap<>();
		for (Map.Entry<Adversary, LevelComponent> locationEntry : this.adversaryLocations.entrySet()) {
			Adversary adversary = locationEntry.getKey();
			Point adversaryPoint = locationEntry.getValue().findActorLocation(adversary);
			activeAdversaries.put(adversary, adversaryPoint);
		}
		return activeAdversaries;
	}

	@Override
	public Boolean getExitUnlocked() {
		return this.exitUnlocked;
	}

	@Override
	public List<LevelComponent> getLevelMap() {
		return this.levelMap;
	}

	@Override
	public List<Item> getItems() {
		return this.items;
	}

	/**
	 * Gets the map for the level composed of tiles
	 * @return a map of all tiles within the level
	 */
	private List<List<Tile>> getTileMap() {
		Point topLeftBound = getTopLeft();
		Point bottomRightBound = getBottomRight();
		List<List<Tile>> tileMap = initializeEmptyTileMap(topLeftBound, bottomRightBound);

		for (LevelComponent component : this.levelMap) {
			addToTileMap(tileMap, component, topLeftBound, bottomRightBound);
		}

		return tileMap;
	}

	/**
	 * Initializes a new map of Tiles as Walls
	 * @return the map of Wall Tiles
	 * @param topLeftBound - top left bound of viewable level
	 * @param bottomRightBound - bottom right bound of viewable level
	 */
	private List<List<Tile>> initializeEmptyTileMap(Point topLeftBound,
			Point bottomRightBound) {
		//Determine the size of the map based on the bounds
		int xSize = bottomRightBound.x - topLeftBound.x + 1;
		int ySize = bottomRightBound.y - topLeftBound.y + 1;

		List<List<Tile>> emptyMap = new ArrayList<>();

		//Iterate through the map and place a Wall at
		//each coordinate
		for (int i = 0; i < ySize; i++) {
			List<Tile> emptyRow = new ArrayList<>();
			for (int j = 0; j < xSize; j++) {
				emptyRow.add(j, new Wall());
			}
			emptyMap.add(i, emptyRow);
		}
		return emptyMap;
	}

	/**
	 * Adds each Tile within a LevelComponent to the tileMap
	 * @param component - the LevelComponent to add to the tileMap
	 * @param topLeftBound - top left bound of the level
	 * @param bottomRightBound - bottom right bound of the level
	 */
	private void addToTileMap(
			List<List<Tile>> tileMap,
			LevelComponent component,
			Point topLeftBound,
			Point bottomRightBound) {
		//Iterate through the level bounds
		for (int i = topLeftBound.y; i <= bottomRightBound.y; i++) {
			for (int j = topLeftBound.x; j <= bottomRightBound.x; j++) {
				try {
					//Check if a Tile exists at the given coordinates in the LevelComponent
					//If these coordinates are not available for this LevelComponent, no changes are made
					Tile destTile = component.getDestinationTile(new Point(j, i));

					//Add the Tile to the viewableMap
					int croppedYIndex = i - topLeftBound.y;
					int croppedXIndex = j - topLeftBound.x;
					List<Tile> editRow = tileMap.get(croppedYIndex);
					editRow.set(croppedXIndex, destTile);
				} catch (IllegalArgumentException e) {
					//Do Nothing
				}
			}
		}
	}

	@Override
	public int hashCode() {
		return this.levelMap.hashCode()
				* this.playerLocations.hashCode()
				* this.adversaryLocations.hashCode()
				* this.exitUnlocked.hashCode()
				* this.levelExited.hashCode()
				* this.items.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof LevelImpl)) {
			return false;
		}

		LevelImpl otherLevel = (LevelImpl) obj;
		return this.levelMap.equals(otherLevel.levelMap)
				&& this.playerLocations.equals(otherLevel.playerLocations)
				&& this.adversaryLocations.equals(otherLevel.adversaryLocations)
				&& this.exitUnlocked.equals(otherLevel.exitUnlocked)
				&& this.levelExited.equals(otherLevel.levelExited)
				&& this.items.equals(otherLevel.items);
	}

}
