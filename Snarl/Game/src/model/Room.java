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
		//Check to see if the destination point exists in the Room
		if (!inComponent(point)) {
			throw new IllegalArgumentException("Point not in component");
		}

		//If the destination exists, return the Entity at that location 
		int relativeX = point.x - this.position.x;
		int relativeY = point.y - this.position.y;
		List<Entity> row = this.componentMap.get(relativeY);
		return row.get(relativeX);
	}

	@Override
	public void moveActor(Actor actor, Point destination) {
		removeActor(actor);	
		placeActor(actor, destination);
	}
	
	@Override
	public void removeActor(Actor actor) {
		Point actorLocation = findActor(actor);
		List<Entity> actorRow = componentMap.get(actorLocation.y);
		actorRow.set(actorLocation.x, new Space());
	}
	
	/**
	 * Finds the location of the actor within the componentMap
	 * @param actor - the actor to be found
	 * @return the point that the actor is located at 
	 * @throws IllegalArgumentException if the actor is not in the room
	 */
	private Point findActor(Actor actor) {
		//Iterate through the Room and check if the current Entity
		//is the given actor
		for (int i = 0; i < componentMap.size(); i++) {
			List<Entity> entityRow = componentMap.get(i);
			for (int j = 0; j < entityRow.size(); j++) {
				Entity currEntity = entityRow.get(j);
				if (currEntity.equals(actor)) {
					return new Point(j,i);
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
		List<Entity> row = this.componentMap.get(relativePos.y);
		row.set(relativePos.x, actor);
	}

	@Override
	public void placeKey(Key key) {
		if (this.getDestinationEntity(key.location).equals(new Space())) {
			List<Entity> keyRow = this.componentMap.get(key.location.y);
			keyRow.set(key.location.x, key);			
		}
	}

	@Override
	public void placeExit(Exit exit) {
		if (this.getDestinationEntity(exit.location).equals(new Space())) {
			List<Entity> exitRow = this.componentMap.get(exit.location.y);
			exitRow.set(exit.location.x, exit);			
		}	
	}

	/**
	 * TODO add comment
	 */
	public Point getOrigin() {
		return this.position;
	}

	/**
	 * TODO add comment
	 */
	public Map<Point, Hall> getDoors() {
		return this.doors;
	}
	
	/**
	 * TODO add comment
	 */
	public List<List<Entity>> getComponentMap() {
		return this.componentMap;
	}
	
	@Override
	public int hashCode() {
	  return this.position.hashCode() * this.componentMap.hashCode();
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
        return  this.position.equals(room.getOrigin())
        		&& this.componentMap.equals(room.getComponentMap());
	}

}

