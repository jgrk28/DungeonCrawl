package Game.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Game.modelView.EntityType;

/** 
 * A LevelComponent that represents a Room within a Level
 * A room has an upper-left Cartesian position, and one or more doors
 * Items, such as the key and the level exit, can be placed inside
 * of a Room
 * 
 * An ASCII representation of the room may like this:
 * 
 * XX.XXX       
 * X....X       
 * X....X      
 * X....X        
 * XXXXXX
 *
 * Where a Wall corresponds to "X", a Space corresponds to ".",
 * a Key corresponds to "!", an Exit corresponds to "@", and an Empty Space
 * is simply " "
 * For more examples, see test/model/RoomTest.java
 */
public class Room implements LevelComponent {
	
	//Upper-left Cartesian coordinates of the Room
	private Point position;
	
	//A list of all tiles within this LevelComponent
	private List<List<Tile>> componentMap;
	
	//A Map of the location and Hall that doors in the Room connect to
	private Map<Point, Hall> doors;
	
	/**
	 * Initializes a new Room with the position and componentMap
	 * @param position - upper-left Cartesian coordinates of the Room
	 * @param componentMap - the map of all entities in the Room
	 */
	public Room(Point position, List<List<Tile>> componentMap) {
		//Ensure component map is not empty
		if (componentMap.isEmpty()) {
			throw new IllegalArgumentException("The component map is empty");
		}
		this.position = position;
		this.componentMap = componentMap;
		this.doors = new HashMap<Point, Hall>();
	}

	/**
	 * Connects the Room to the given Hall
	 * @param doorPosition - the position in the Room that connects through the door to the Hall
	 * @param adjHall - the Hall the Room is being connected to
	 * @throws IllegalArgumentException if the Door is not on the boundary of the room
	 */
	public void connectHall(Point doorPosition, Hall adjHall) {
		Point bottomRightPosition = getBottomRightBound();
		if (validDoor(doorPosition, bottomRightPosition)) {
			doors.put(doorPosition, adjHall);
		} else {
			throw new IllegalArgumentException("Door is not on the boundary of the room");
		}
	}

	/**
	 * Checks if the placement of a door in a Room is valid
	 * @param doorPosition - the position in the Room that connects through the door to the Hall
	 * @param bottomRightPosition - the lower right boundary of the Room
	 * @return True if the door placement is valid, false otherwise
	 */
	private boolean validDoor(Point doorPosition, Point bottomRightPosition) {
		return doorPosition.x == this.position.x ||
				doorPosition.x == bottomRightPosition.x ||
				doorPosition.y == this.position.y ||
				doorPosition.y == bottomRightPosition.y;
	}

	@Override
	public boolean inComponent(Point point) {
		Point bottomRightPosition = getBottomRightBound();
		return point.x >= this.position.x &&
				point.x <= bottomRightPosition.x &&
				point.y >= this.position.y &&
				point.y <= bottomRightPosition.y;
	}

	@Override
	public Point getTopLeftBound() {
		return this.position;
	}

	@Override
	public Point getBottomRightBound() {
		//Get the size of the Room
		int roomY = this.componentMap.size() - 1;
		List<Tile> roomTopRow = this.componentMap.get(0);
		int roomX = roomTopRow.size() - 1;

		//Find the bottom right bound based on the upper left bound and the size of the Room
		Point bottomRightPosition = new Point(this.position.x + roomX, this.position.y + roomY);
		return bottomRightPosition;
	}

	@Override
	public EntityType getEntityType(Tile tile) {
		return tile.getEntityType();
	}

	@Override
	public Tile getDestinationTile(Point point) {
		//Check to see if the destination point exists in the Room
		if (!inComponent(point)) {
			throw new IllegalArgumentException("Point not in component");
		}

		//If the destination exists, return the tile at that location 
		int relativeX = point.x - this.position.x;
		int relativeY = point.y - this.position.y;
		List<Tile> row = this.componentMap.get(relativeY);
		return row.get(relativeX);
	}

	@Override
	public void removeActor(Actor actor) {
		Point absoluteActorLocation = findActorLocation(actor);
		//Find the relative location in the room
		Point relativeActorLocation = new Point(absoluteActorLocation.x - position.x,
				absoluteActorLocation.y - position.y);
		List<Tile> actorRow = componentMap.get(relativeActorLocation.y);
		Tile tile = actorRow.get(relativeActorLocation.x);
		tile.removeActor();
	}
	
	@Override
	public Point findActorLocation(Actor actor) {
		//Iterate through the Room and check if the current tile
		//has the given actor
		for (int i = 0; i < componentMap.size(); i++) {
			List<Tile> tileRow = componentMap.get(i);
			for (int j = 0; j < tileRow.size(); j++) {
				Tile currTile = tileRow.get(j);
				try {
					Actor tileActor = currTile.getActor();
					if (tileActor != null && tileActor.equals(actor)) {
						return new Point(j + position.x, i + position.y);
					}
				} catch (IllegalArgumentException e) {
					continue;
				}
			}
		}
		throw new IllegalArgumentException("Actor is not in this component");
	}

	@Override
	public void placeActor(Actor actor, Point destination) {
		if (!inComponent(destination)) {
			throw new IllegalArgumentException("Point not in component");
		}

		//Find the relative position of the actor in the room
		Point relativePos = new Point(destination.x - this.position.x,
				destination.y - this.position.y);
		List<Tile> row = this.componentMap.get(relativePos.y);
		Tile tile = row.get(relativePos.x);
		tile.placeActor(actor);
		row.set(relativePos.x, tile);
	}


	@Override
	public void placeItem(Item item) {
		Point destination = item.getLocation();
		
		if (!inComponent(destination)) {
			throw new IllegalArgumentException("Point not in component");
		}
		
		Point relativePos = new Point(destination.x - this.position.x,
				destination.y - this.position.y);
		List<Tile> row = this.componentMap.get(relativePos.y);
		Tile tile = row.get(relativePos.x);
		tile.placeItem(item);
		row.set(relativePos.x, tile);
	}


	@Override
	public Map<Point, Hall> getDoors() {
		return this.doors;
	}

	/**
	 * Getter for the component map of EntityType for the room
	 * @return the matrix of entityType that compose this room
	 */
	public List<List<EntityType>> getComponentMap() {
		List<List<EntityType>> entityMap = new ArrayList<>();
		for (List<Tile> row : this.componentMap) {
			List<EntityType> entityRow = new ArrayList<>();
			for (Tile tile : row) {
				entityRow.add(getEntityType(tile));
			}
			entityMap.add(entityRow);
		}
		return entityMap;
	}
	
	@Override
	public int hashCode() {
		//Can not check hashCode of Hall because that relies on the hashCode of room
		return this.position.hashCode()
				* this.componentMap.hashCode()
				* this.doors.keySet().hashCode();
	}

	@Override
	public boolean equals(Object obj) { 
        if (obj == this) { 
            return true; 
        } 
        if (!(obj instanceof Room)) { 
            return false; 
        } 
        
        Room room = (Room) obj;       
        return room.checkSameFields(this.position, this.componentMap, this.doors);
	}

	/**
	 * Helper method for equals that checks that the fields for the two
	 * rooms are the same
	 * @param position - upper-left Cartesian coordinates of the Room
	 * @param componentMap - a list of all entities within this LevelComponent
	 * @param doors - a map of the location and Hall that doors in the Room connect to
	 * @return true if the fields are equal for both rooms
	 */
	private boolean checkSameFields(
			Point position,
			List<List<Tile>> componentMap,
			Map<Point, Hall> doors
	) {
		return position.equals(this.position)
				&& componentMap.equals(this.componentMap)
				&& doors.keySet().equals(this.doors.keySet());
	}

}

