package model;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelView.EntityType;

/** 
 * A LevelComponent that represents a Room within a Level
 * A room has an upper-left Cartesian position, and one or more doors
 * Entities, such as the key and the level exit, can be placed inside
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
	
	//A list of all entities within this LevelComponent
	private List<List<Entity>> componentMap;
	
	//A Map of the location and Hall that doors in the Room connect to
	private Map<Point, Hall> doors;
	
	/**
	 * Initializes a new Room with the position and componentMap
	 * @param position - upper-left Cartesian coordinates of the Room
	 * @param componentMap - the map of all entities in the Room
	 */
	public Room(Point position, List<List<Entity>> componentMap) {
		this.position = position;
		//TO-DO Ensure component map is not empty
		this.componentMap = componentMap;
		this.doors = new HashMap<Point, Hall>();
	}

	/**
	 * Connects the Room to the given Hall
	 * @param doorPosition - the position in the Hall that connects through the door to the Room
	 * @param adjHall - the Hall the Room is being connected to
	 * @throws IllegalArgumentException if the Door is not on the boundary of the room
	 */
	public void connectHall(Point doorPosition, Hall adjHall) {
		Point bottomRightPosition = getBottomRightBound();
		if (validDoor(doorPosition, bottomRightPosition)) {
			doors.put(position, adjHall);
		} else {
			throw new IllegalArgumentException("Door is not on the boundary of the room");
		}
	}

	/**
	 * Checks if the placement of a door in a Room is valid
	 * @param doorPosition - the position in the Hall that connects through the door to the Room
	 * @param bottomRightPosition - the lower right boundary of the Room
	 * @return True if the door placement is valid, false otherwise
	 */
	private boolean validDoor(Point doorPosition, Point bottomRightPosition) {
		return doorPosition.x == (this.position.x - 1) ||
				doorPosition.x == (bottomRightPosition.x + 1) ||
				doorPosition.y == (this.position.y - 1)||
				doorPosition.y == (bottomRightPosition.y + 1);
	}

	/**
	 * Checks if a given coordinate is located within the Room
	 * @param point - the coordinate to check
	 * @param bottomRightPosition - the lower right boundary of the Room
	 * @return True if the point is located within the LevelComponent, false otherwise
	 */
	private boolean validPoint(Point point, Point bottomRightPosition) {
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
		List<Entity> roomTopRow = this.componentMap.get(0);
		int roomX = roomTopRow.size() - 1;

		//Find the bottom right bound based on the upper left bound and the size of the Room
		Point bottomRightPosition = new Point(this.position.x + roomX, this.position.y + roomY);
		return bottomRightPosition;
	}

	@Override
	public EntityType getEntityType(Entity entity) {
		return entity.getEntityType();
	}

	@Override
	public Entity getDestinationEntity(Point point) {
		Point bottomRight = getBottomRightBound();
		
		//Check to see if the destination point exists in the Room
		if (!validPoint(point, bottomRight)) {
			throw new IllegalArgumentException("Point not in component");
		}

		//If the destination exists, return the Entity at that location 
		int relativeX = point.x - this.position.x;
		int relativeY = point.y - this.position.y;
		List<Entity> row = this.componentMap.get(relativeY);
		return row.get(relativeX);
	}

	@Override
	public InteractionResult actorAction(Actor actor, Point destination) {
		Entity destinationEntity = getDestinationEntity(destination);
		EntityType destinationType = this.getEntityType(destinationEntity);
		return actor.getInteractionResult(destinationType);
	}

	@Override
	public void placeActor(Actor actor, Point destination) {
		//We will need to check that the destination is valid and 
		//check the destination entity as well
		
	}
}

