package Game.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import Game.modelView.EntityType;

/**
 * A LevelComponent that represents a Hall within a Level
 * A Hall connects two rooms in the Level
 * 
 * An ASCII representation of the hall may like a series of 
 * asterisk characters, where each endpoint connects to a Room: 
 *  ******
 *       *
 *       *
 *       *******
 * For more examples, see test/model/HallTest.java
 */
public class Hall implements LevelComponent {
	
	//A list of all entities within this LevelComponent
	private List<Entity> componentMap;
	
	//A list of all points that represent corners in a Hall
	private List<Point> waypoints;
	
	//The Room at the beginning of the Hall
	private Room startRoom;
	
	//The Room at the end of the Hall
	private Room endRoom;
	
	//The position in the StartRoom that connects through the door to the Hall
	private Point startRoomPosition;
	
	//The position in the EndRoom that connects through the door to the Hall
	private Point endRoomPosition;

	/**
	 * Initializes a new Hall with the componentMap and waypoints
	 * Both the start and end room are null during initialization
	 * This constructor is mostly used for testing
	 * @param componentMap - the map of all entities in the hall
	 * @param waypoints - a list of points that represent corners in the hall. 
	 * If there are no corners in the hall, this list will be empty
	 */
	public Hall(List<Entity> componentMap, List<Point> waypoints) {
		this.componentMap = componentMap;
		this.waypoints = waypoints;
		this.startRoom = null;
		this.endRoom = null;
	}

	/**
	 * Initializes a new Hall using a starting and ending room/door position and a list of waypoints.
	 * The Hall will start with only spaces as objects and actors do not start in a hall. This will
	 * also initialize the connections from the hall to the rooms and vice versa.
	 * This constructor is mostly used when constructing the actual level
	 * @param positionStart - the door position in the start room
	 * @param startRoom - the room connected to the start of the hallway
	 * @param positionEnd - the door position in the end room
	 * @param endRoom - the room connected to the end of the hallway
	 * @param waypoints - a list of points that represent corners in the hall.
	 * If there are no corners in the hall, this list will be empty
	 */
	public Hall(Point positionStart, Room startRoom, Point positionEnd, Room endRoom, List<Point> waypoints) {
		this.waypoints = waypoints;
		this.startRoom = null;
		this.endRoom = null;
		connectRooms(positionStart, startRoom, positionEnd, endRoom);
		this.componentMap = createEmptyComponentMap();
		startRoom.connectHall(positionStart, this);
		endRoom.connectHall(positionEnd, this);
	}
	
	/**
	 * Creates an empty component map for this hallway. By finding the length of the hallway and
	 * filling it with spaces.
	 * @return List of Entities that represents the tiles in the hallway
	 */
	private List<Entity> createEmptyComponentMap() {
		List<Entity> hallMap = new ArrayList<>();
		int hallLength = getLengthByWaypoint();
		for (int i = 0; i < hallLength; i++) {
			hallMap.add(new Space());
		}
		
		return hallMap;
	}

	/**
	 * Determines the length of the hall using the start room position, the end room
	 * position, and the waypoints
	 * @return the length of the hallway
	 */
	private int getLengthByWaypoint() {
		int length = 0;
		Point currPos = this.startRoomPosition;
		
		//Calculate the distance for each waypoint
		for (Point waypoint : this.waypoints) {
			//Add the distance in the segment counting one endpoint (the new waypoint)
			length += currPos.distance(waypoint);
			currPos = waypoint;
		}

		//Add the distance in the segment not counting either end
		length += (currPos.distance(this.endRoomPosition) - 1);
		return length;
	}

	/**
	 * Connects the Hall to two given rooms
	 * @param positionStart - the position in the StartRoom that connects through the door to the Hall
	 * @param startRoom - the Room at the beginning of the Hall
	 * @param positionEnd - the position in the EndRoom that connects through the door to the Hall
	 * @param endRoom - the Room at the end of the Hall
	 * @throws IllegalArgumentException if the Hall is already connected
	 * or if the start and end Rooms are not orthogonally aligned with the Hall
	 */
	public void connectRooms(Point positionStart, Room startRoom, Point positionEnd, Room endRoom) {
		if (this.startRoom != null || this.endRoom != null) {
			throw new IllegalArgumentException("Hallway already connected");
		}

		if (validHallConnection(positionStart, positionEnd)) {
			this.startRoom = startRoom;
			this.startRoomPosition = positionStart;
			this.endRoom = endRoom;
			this.endRoomPosition = positionEnd;
		} else {
			throw new IllegalArgumentException("Room positions are not orthogonally in line with hallway "
					+ "waypoints");
		}
	}
	
	/**
	 * Checks if the Rooms being connected to the Hall are valid connections 
	 * @param positionRoomStart - the position in the StartRoom that connects through the door to the Hall
	 * @param positionRoomEnd - the position in the EndRoom that connects through the door to the Hall
	 * @return True if both connections are valid, false otherwise
	 */
	private boolean validHallConnection(Point positionRoomStart, Point positionRoomEnd) {
		
		//Check that the room positions are orthogonal to each other 
		if (this.waypoints.isEmpty()) {
			return (positionRoomStart.x == positionRoomEnd.x) || (positionRoomStart.y == positionRoomEnd.y);
		}
		
		//Check that the startRoom is orthogonal to the first waypoint
		Point firstWaypoint = this.waypoints.get(0);
		boolean validStart = firstWaypoint.x == positionRoomStart.x ||
				firstWaypoint.y == positionRoomStart.y;
		
		//Check that the endRoom is orthogonal to the last waypoint
		Point lastWaypoint = this.waypoints.get(this.waypoints.size() - 1);
		boolean validEnd = lastWaypoint.x == positionRoomEnd.x ||
				lastWaypoint.y == positionRoomEnd.y;
		
		return validStart && validEnd;	
	}

	@Override
	//Top left includes the door that the hall is connected to
	public Point getTopLeftBound() {
		
		Integer minX = startRoomPosition.x;
		Integer minY = startRoomPosition.y;

		//Iterate through all waypoints to find the minimum x and y values
		for (Point waypoint : this.waypoints) {
			if (waypoint.x < minX) {
				minX = waypoint.x;
			}
			if (waypoint.y < minY) {
				minY = waypoint.y;
			}
		}
		
		if (endRoomPosition.x < minX) {
			minX = endRoomPosition.x;
		}	
		if (endRoomPosition.y < minY) {
			minY = endRoomPosition.y;
		}
		
		return new Point(minX, minY);
	}

	@Override
	//Bottom right bound includes the door that the hall is connected to 
	public Point getBottomRightBound() {
		
		Integer maxX = startRoomPosition.x;
		Integer maxY = startRoomPosition.y;

		//Iterate through all waypoints to find the maximum x and y values
		for (Point waypoint : this.waypoints) {
			if (waypoint.x > maxX) {
				maxX = waypoint.x;
			}
			if (waypoint.y > maxY) {
				maxY = waypoint.y;
			}
		}
		
		if (endRoomPosition.x > maxX) {
			maxX = endRoomPosition.x;
		}
		if (endRoomPosition.y > maxY) {
			maxY = endRoomPosition.y;
		}
		
		return new Point(maxX, maxY);
	}

	@Override
	public EntityType getEntityType(Entity entity) {
		
		EntityType generalType = entity.getEntityType();
		
		//If the Entity is a SPACE, return the HALL_SPACE EntityType
		switch (generalType) {
			case SPACE:
				return EntityType.HALL_SPACE;
			default:
				return generalType;
		}
	}

	@Override
	public boolean inComponent(Point point) {
		try {
			getHallwayIndex(point);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	@Override
	public Entity getDestinationEntity(Point point) {
		int hallIndex = getHallwayIndex(point);
		return componentMap.get(hallIndex);
	}

	/**
	 * Walk through each location in the hall to determine if it matches the given
	 * point. If so, return the index of the hallway at that point.
	 * @param point - the point in cartesian space to find the hall index for
	 * @throws IllegalArgumentException if the point is nowhere in the hall
	 */
	private int getHallwayIndex(Point point) {
		//Start at StartRoom
		Point currentPosition = this.startRoomPosition;
		
		//Keep track of the current waypoint
		int waypointCounter = 0;
		
		Point nextDestination;
		
		//If there are no waypoints, the destination is the endRoom
		//Otherwise, the destination is the next waypoint
		if (this.waypoints.isEmpty()) {
			nextDestination = this.endRoomPosition;
		} else {
			nextDestination = this.waypoints.get(waypointCounter);
		}
		
		//Take one step into the hall in the direction of the nextDestination
		currentPosition = stepTowardDestination(currentPosition, nextDestination);
		
		//Iterate through each entity in the hall
		for (int steps = 0; steps < this.componentMap.size(); steps++) {
			
			//If the current coordinate is the given point, return the Entity at this location
			if (currentPosition.equals(point)) {
				return steps;
			}
			
			//If you have reached the next waypoint, check to see what the nextDestination is
			if (nextDestination.equals(currentPosition)) {
				waypointCounter++;
				
				//If we have reached the end of the waypoints, the nextDestination is the EndRoom
				if (this.waypoints.size() <= waypointCounter) {
					nextDestination = this.endRoomPosition;
				} else {
					//Otherwise, get the next waypoint in the list
					nextDestination = this.waypoints.get(waypointCounter);
				}
			}
			//Update the current position to the next step in the Hall
			currentPosition = stepTowardDestination(currentPosition, nextDestination);
		}

		//If the Entity is not located, throw the corresponding error
		throw new IllegalArgumentException("Point not in component");
	}
	
	/**
	 * Returns the coordinate for the next step in the direction of the destination
	 * @param source - the current position in the hall
	 * @param destination - the direction to move towards in the hall (waypoint or endRoom)
	 * @return the coordinates for the next step in the hall
	 */
	private Point stepTowardDestination(Point source, Point destination) {
		
		//If the source and destination are not aligned horizontally or vertically, throw an error
		//Otherwise, identify which direction to move toward and return the corresponding coordinate
		if ((source.x != destination.x) && (source.y != destination.y)) {
			throw new IllegalArgumentException("Source and destination are not orthogonally aligned");
		} else if (source.x > destination.x) {
			return new Point(source.x - 1, source.y);
		} else if (source.x < destination.x) {
			return new Point(source.x + 1, source.y);
		} else if (source.y > destination.y) {
			return new Point(source.x, source.y - 1);
		} else if (source.y < destination.y) {
			return new Point(source.x, source.y + 1);
		} else {
			return source;		
		}				
	}

	@Override
	public void moveActor(Actor actor, Point destination) {
		removeActor(actor);
		placeActor(actor, destination);	
	}

	@Override
	public void removeActor(Actor actor) {
		int actorIndex = findEntity(actor);
		componentMap.set(actorIndex, new Space());			
	}
	
	@Override
	public Point findEntityLocation(Entity entity) {
		int entityIndex = findEntity(entity);

		int length = 0;

		Point nextDestination;
		//If there are no waypoints, the destination is the endRoom
		//Otherwise, the destination is the next waypoint
		if (this.waypoints.isEmpty()) {
			nextDestination = this.endRoomPosition;
		} else {
			nextDestination = this.waypoints.get(0);
		}
		//Step into the hall
		Point prevWaypoint  = stepTowardDestination(startRoomPosition, nextDestination);

		//Calculate the distance for each waypoint
		for (int waypointCount = 0; waypointCount <= waypoints.size(); waypointCount++) {
			//If we have reached the end of the waypoints, the nextDestination is the EndRoom
			if (this.waypoints.size() <= waypointCount) {
				//Step into the hall
				nextDestination = stepTowardDestination(this.endRoomPosition, prevWaypoint);
			} else {
				//Otherwise, get the next waypoint in the list
				nextDestination = this.waypoints.get(waypointCount);
			}
			
			//If the index is located between the current position and the next destination,
			//find the location of the entity
			if ((length + prevWaypoint.distance(nextDestination)) > entityIndex) {
				int distFromWaypoint = entityIndex - length;
				Point currPosition = prevWaypoint;
				for (int i = 0; i < distFromWaypoint; i++) {
					currPosition = stepTowardDestination(currPosition, nextDestination);
				}
				return currPosition;
			}
			length += prevWaypoint.distance(nextDestination);
			prevWaypoint = nextDestination;
		}

		throw new IllegalArgumentException("Entity is not in this component");

	}
	
	/**
	 * Finds the location of the entity within the componentMap
	 * @param entity - the entity to be found
	 * @return the index in the hall that the entity is located at 
	 * @throws IllegalArgumentException if the entity is not in the hall
	 */
	private int findEntity(Entity entity) {
		for (int i = 0; i < componentMap.size(); i++) {
			if (componentMap.get(i).equals(entity)) {
				return i;
			}
		}
		throw new IllegalArgumentException("Entity is not in this component");
	}

	@Override
	public void placeActor(Actor actor, Point destination) {
		int hallIndex = getHallwayIndex(destination);
		componentMap.set(hallIndex, actor);
	}

	@Override
	public void placeKey(Key key) {
		if (this.getDestinationEntity(key.location).equals(new Space())) {
			int hallIndex = getHallwayIndex(key.location);
			componentMap.set(hallIndex, key);		
		}	
	}

	@Override
	public void placeExit(Exit exit) {
		if (this.getDestinationEntity(exit.location).equals(new Space())) {
			int hallIndex = getHallwayIndex(exit.location);
			componentMap.set(hallIndex, exit);		
		}	
	}

	/**
	 * Getter for the room connected to the start of this hallway. This room will be set at
	 * construction but this getter will allow easy traversal of the level map.
	 * @return the room connected to the start of the hallway
	 */
	public Room getStartRoom() {
		return this.startRoom;
	}

	/**
	 * Getter for the room connected to the end of this hallway. This room will be set at
  	 * construction but this getter will allow easy traversal of the level map.
	 * @return the room connected to the end of the hallway
	 */
	public Room getEndRoom() {
		return this.endRoom;
	}
	
	@Override
	public int hashCode() {
	  return this.waypoints.hashCode() 
			  * this.componentMap.hashCode()
			  * this.startRoom.hashCode()
			  * this.endRoom.hashCode()
			  * this.startRoomPosition.hashCode()
			  * this.endRoomPosition.hashCode();
	}

	@Override
	public boolean equals(Object obj) { 
        if (obj == this) { 
            return true; 
        } 
        if (!(obj instanceof Hall)) { 
            return false; 
        } 
        
        Hall hall = (Hall) obj;
        return hall.checkSameFields(
        		this.waypoints,
						this.componentMap,
						this.startRoom,
						this.endRoom,
						this.startRoomPosition,
						this.endRoomPosition
						);
	}

	/**
	 * Helper method for equals that checks that the fields for the two
	 * halls are the same
	 * @param waypoints - the waypoints in the hall
	 * @param componentMap - the map of the hall
	 * @param startRoom - the start room of the hall
	 * @param endRoom - the end room of the hall
	 * @param startRoomPosition - the position of the start room
	 * @param endRoomPosition - the position of the end room
	 * @return true if the fields are equal for both halls
	 */
	private boolean checkSameFields(
			List<Point> waypoints,
			List<Entity> componentMap,
			Room startRoom,
			Room endRoom,
			Point startRoomPosition,
			Point endRoomPosition
	) {
		return waypoints.equals(this.waypoints)
				&& componentMap.equals(this.componentMap)
				&& startRoom.equals(this.startRoom)
				&& endRoom.equals(this.endRoom)
				&& startRoomPosition.equals(this.startRoomPosition)
				&& endRoomPosition.equals(this.endRoomPosition);
	}

}
