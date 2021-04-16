package Game.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import Game.modelView.EntityType;
import java.util.Map;
import java.util.Set;

/**
 * Represents a player within a game of Snarl
 */
public class Player extends AbstractActor {
	private static final int maxMoveDistance = 2;
	private static final int sightBoxWidth = 2;

	private int keysFound;
	private int numExits;
	private int numEjects;

	/**
	 * Initialize a Player with a unique name
	 * @param name - the name of the Player
	 */
	public Player(String name) {
		super(name);
		this.keysFound = 0;
		this.numExits = 0;
		this.numEjects = 0;
	}

	/**
	 * Initialize a Player with a unique ID
	 */
	public Player() {
		super();
		this.keysFound = 0;
		this.numExits = 0;
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.PLAYER;
	}

	@Override
	public InteractionResult getInteractionResult(EntityType entityType) {
		switch (entityType) {
			case HALL_SPACE:
				//Same as SPACE
			case SPACE:
				return InteractionResult.NONE;
			case KEY:
				return InteractionResult.FOUND_KEY;
			case EXIT:
				return InteractionResult.EXIT;		
			case GHOST:
				//Same as ZOMBIE
			case ZOMBIE:
				return InteractionResult.REMOVE_PLAYER;
			default:
				throw new IllegalArgumentException("Illegal interaction entity for player");
		}
	}

	@Override
	public InteractionResult getTileInteractionResult(Tile destTile) {
		EntityType destType = destTile.getEntityType();
		switch (destType) {
			case HALL_SPACE:
				//Same as SPACE
			case SPACE:
				return InteractionResult.NONE;
			case KEY:
				return InteractionResult.FOUND_KEY;
			case EXIT:
				return InteractionResult.EXIT;
			case GHOST:
				//Same as ZOMBIE
			case ZOMBIE:
				return InteractionResult.REMOVE_PLAYER;
			case PLAYER:
				if (this.equals(destTile.getActor())) {
					Item item = destTile.getItem();
					if (item == null) {
						return InteractionResult.NONE;
					} else if (item instanceof Key) {
						return InteractionResult.FOUND_KEY;
					} else if (item instanceof Exit) {
						return InteractionResult.EXIT;
					} else {
						throw new IllegalArgumentException("Unhandled item type");
					}
				} else {
					throw new IllegalArgumentException("Illegal interaction entity for player");
				}
			default:
				throw new IllegalArgumentException("Illegal interaction entity for player");
		}
	}

	@Override
	public Boolean checkValidMoveDistance(Point source, Point destination) {
		int distance = Math.abs(source.x - destination.x) 
				+ Math.abs(source.y - destination.y);
		return distance <= maxMoveDistance;
	}
	
	@Override
	public Boolean checkValidMovePath(Point source, Point destination, 
			List<List<EntityType>> intermediateTypes) {
		
		//Determine the relative source and destination 
		int minX = Math.min(source.x, destination.x);
		int minY = Math.min(source.y, destination.y);
		Point relativeSourcePoint = new Point(source.x - minX, source.y - minY);
		Point relativeDestPoint = new Point(destination.x - minX, destination.y - minY);
		
		List<Point> visited = new ArrayList<>();
		
		//If the player is located at the destination, the move path is valid
		//Otherwise, conduct depth first search to locate a valid move path
		if (relativeSourcePoint.equals(relativeDestPoint)) {
			return true;
		} else {
			visited.add(relativeSourcePoint);
			return depthFirstSearchRecursive(visited, relativeSourcePoint, 
					relativeDestPoint, intermediateTypes);
		}
		
	}
	
	/**
	 * Conduct depth first search on potential paths between the source
	 * and destination
	 * @param visited - visited neighbors 
	 * @param currPoint - the current point being searched
	 * @param destPoint - the destination that the player is trying to move to
	 * @param intermediateTypes - a map of all intermediate entities between the 
	 * source and destination
	 * @return true if there is a valid path to the destination
	 */
	private Boolean depthFirstSearchRecursive(List<Point> visited, Point currPoint, 
			Point destPoint, List<List<EntityType>> intermediateTypes) {
		List<Point> neighbors = getNeighbors(currPoint, destPoint);
		
		for (Point point : neighbors) {
			
			if (visited.contains(point)) {
				continue;
			} else {
				visited.add(point);
			}
			//Get the entity at this location
			EntityType entityType = intermediateTypes.get(point.y).get(point.x);
			
			//If the entity is at the destination, and the destination can be landed on,
			//return true.
			if (isTraversable(entityType) && point.equals(destPoint)) {
				return true;
			}
			//Do not explore this node if the entity can not be landed on and is not a player
			else if (!isTraversable(entityType) && !entityType.equals(EntityType.PLAYER)) {
				continue;
			} else {
				if (depthFirstSearchRecursive(visited, point, destPoint, intermediateTypes)) {
					return true;
				}
			}
		}	
		//if no valid move paths are found
		return false;
	}
	
	/**
	 * Gets all the neighbors around the given point
	 * @param currPoint - the current point
	 * @param bottomRightBound - the bound that the point is contained within
	 * @return the list of points directly surrounding the given point
	 */
	private List<Point> getNeighbors(Point currPoint, Point bottomRightBound) {
		List<Point> neighbors = new ArrayList<>();
		
		//If the neighbor is within the bounds, add it to the list
		if ((currPoint.x + 1) <= bottomRightBound.x) {
			neighbors.add(new Point(currPoint.x + 1, currPoint.y));		
		}
		if ((currPoint.x - 1) >= 0) {
			neighbors.add(new Point(currPoint.x - 1, currPoint.y));		
		}
		if ((currPoint.y + 1) <= bottomRightBound.y) {
			neighbors.add(new Point(currPoint.x, currPoint.y + 1));	
		}
		if ((currPoint.y - 1) >= 0) {
			neighbors.add(new Point(currPoint.x, currPoint.y - 1));	
		}
		return neighbors;		
	}

	/**
	 * Returns the subsection of the level map that the player can see
	 * based on their position and range of sight
	 * @param fullLevel - the full map of EntityType for the level
	 * @param levelOffset - the top left coordinate for the full level
	 * @param playerLocation - the player's location in the level
	 * @return the cropped map that the player can see
	 * @throws IllegalArgumentException if the fullLevel contains no components
	 */
	public List<List<EntityType>> cropViewableMap(
			List<List<EntityType>> fullLevel,
			Point levelOffset,
			Point playerLocation) {
		List<List<EntityType>> croppedMap = new ArrayList<>();
		if (fullLevel.size() == 0) {
			throw new IllegalArgumentException("Full Level map contains no components");
		}

		Point relativePlayerLocation = new Point(playerLocation.x - levelOffset.x,
				playerLocation.y - levelOffset.y);

		//Determine the visible range for the player
		int fullLevelYMin = relativePlayerLocation.y - sightBoxWidth;
		int fullLevelYMax = relativePlayerLocation.y + sightBoxWidth;
		int fullLevelXMin = relativePlayerLocation.x - sightBoxWidth;
		int fullLevelXMax = relativePlayerLocation.x + sightBoxWidth;
		
		//Iterate through the visible range and get the EntityType at the corresponding
		//location in the full map
		for (int fullLevelY = fullLevelYMin; fullLevelY <= fullLevelYMax; fullLevelY++) {
			List<EntityType> croppedRow = new ArrayList<>();
			for (int fullLevelX = fullLevelXMin; fullLevelX <= fullLevelXMax; fullLevelX++) {
				if (fullLevelY >= 0
						&& fullLevelX >= 0
						&& fullLevelY < fullLevel.size()
						&& fullLevelX < fullLevel.get(0).size()) {
					EntityType entityType = fullLevel.get(fullLevelY).get(fullLevelX);
					croppedRow.add(entityType);
				} else {
					croppedRow.add(EntityType.EMPTY);
				}
			}
			croppedMap.add(croppedRow);
		}
		return croppedMap;
	}

	/**
	 * Returns the subsection of the level map that the player can see
	 * based on their position and range of sight
	 * @param fullLevel - the full map of Tile for the level
	 * @param playerLocation - the player's location in the level
	 * @return the cropped map that the player can see
	 * @throws IllegalArgumentException if the fullLevel contains no components
	 */
	public List<List<Tile>> cropTileMap(
			List<List<Tile>> fullLevel,
			Point levelOffset,
			Point playerLocation) {
		List<List<Tile>> croppedMap = new ArrayList<>();
		if (fullLevel.size() == 0) {
			throw new IllegalArgumentException("Full Level map contains no components");
		}

		Point relativePlayerLocation = new Point(playerLocation.x - levelOffset.x,
				playerLocation.y - levelOffset.y);

		//Determine the visible range for the player
		int fullLevelYMin = relativePlayerLocation.y - sightBoxWidth;
		int fullLevelYMax = relativePlayerLocation.y + sightBoxWidth;
		int fullLevelXMin = relativePlayerLocation.x - sightBoxWidth;
		int fullLevelXMax = relativePlayerLocation.x + sightBoxWidth;

		//Iterate through the visible range and get the Tile at the corresponding
		//location in the full map
		for (int fullLevelY = fullLevelYMin; fullLevelY <= fullLevelYMax; fullLevelY++) {
			List<Tile> croppedRow = new ArrayList<>();
			for (int fullLevelX = fullLevelXMin; fullLevelX <= fullLevelXMax; fullLevelX++) {
				if (fullLevelY >= 0
						&& fullLevelX >= 0
						&& fullLevelY < fullLevel.size()
						&& fullLevelX < fullLevel.get(0).size()) {
					Tile tile = fullLevel.get(fullLevelY).get(fullLevelX);
					croppedRow.add(tile);
				} else {
					croppedRow.add(new Wall());
				}
			}
			croppedMap.add(croppedRow);
		}
		return croppedMap;
	}

	/**
	 * Increments the keysFound field when the player finds a key in the level
	 */
	public void foundKey() {
		this.keysFound++;
	}

	/**
	 * Increments the numExits field when the player exits the level
	 */
	public void exited() {
		this.numExits++;
	}

	/**
	 * Increments the numEjects field when the player is ejected the level
	 */
	public void eject() {
		this.numEjects++;
	}

	/**
	 * Getter for the keysFound field
	 */
	public int getKeysFound() {
		return this.keysFound;
	}

	/**
	 * Getter for the numExits field
	 */
	public int getNumExits() {
		return this.numExits;
	}

	/**
	 * Getter for the numEjects field
	 */
	public int getNumEjects() {
		return this.numEjects;
	}

	/**
	 * Finds all doors that are within the player's view
	 * @param allDoors - all doors within the level
	 * @return the list of door locations that are visible
	 */
	public List<Point> visibleDoors(Set<Point> allDoors, Point playerLocation) {
		List<Point> visibleDoors = new ArrayList<>();
		for (Point door : allDoors) {
			int distanceX = Math.abs(door.x - playerLocation.x);
			int distanceY = Math.abs(door.y - playerLocation.y);
			if (distanceX <= sightBoxWidth && distanceY <= sightBoxWidth) {
				visibleDoors.add(door);
			}
		}
		return visibleDoors;
	}

	@Override
	public int getMaxMoveDistance() {
		return maxMoveDistance;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Player)) {
			return false;
		}

		Player player = (Player) obj;
		return player.hasName(this.name);
	}
}
