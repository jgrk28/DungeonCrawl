package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import modelView.EntityType;

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
		//Start at -1 to account for extra space in start and end position as those
		//positions are in their respective rooms.
		int length = -1;
		Point currPos = this.startRoomPosition;
		
		//Calculate the distance for each waypoint
		for (Point waypoint : this.waypoints) {
			length += currPos.distance(waypoint);
			currPos = waypoint;
		}
		
		length += currPos.distance(this.endRoomPosition);
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
		int actorIndex = findActor(actor);
		componentMap.set(actorIndex, new Space());			
	}
	
	/**
	 * Finds the location of the actor within the componentMap
	 * @param actor - the actor to be found
	 * @return the index in the hall that the actor is located at 
	 * @throws IllegalArgumentException if the actor is not in the hall
	 */
	private int findActor(Actor actor) {
		for (int i = 0; i < componentMap.size(); i++) {
			if (componentMap.get(i).equals(actor)) {
				return i;
			}
		}
		throw new IllegalArgumentException("Actor is not in this component");
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

	/**
	 * Getter for the position of the connection (door) to the room at the start of this hallway.
	 * @return the position of the connection (door).
	 * //TODO do we need this?
	 */
	public Point getStartRoomPosition() {
		return this.startRoomPosition;
	}

	/**
	 * Getter for the position of the connection (door) to the room at the end of this hallway.
	 * @return the position of the connection (door).
	 * //TODO do we need this?
	 */
	public Point getEndRoomPosition() {
		return this.endRoomPosition;
	}
	
	/**
	 * TODO add comment
	 * //TODO do we need this?
	 */
	public List<Entity> getComponentMap() {
		return this.componentMap;
	}
	
	/**
	 * TODO add comment
	 * //TODO do we need this?
	 */
	public List<Point> getWaypoints() {
		return this.waypoints;
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

	//TODO I feel like we can just use == bc we only ever have 1 hall
	@Override
	public boolean equals(Object obj) { 
        if (obj == this) { 
            return true; 
        } 
        if (!(obj instanceof Hall)) { 
            return false; 
        } 
        
        Hall hall = (Hall) obj;       
        return  this.waypoints.equals(hall.getWaypoints())
        		&& this.componentMap.equals(hall.getComponentMap())
        		&& this.startRoom.equals(hall.getStartRoom())
        		&& this.endRoom.equals(hall.getEndRoom())
        		&& this.startRoomPosition.equals(hall.getStartRoomPosition())
        		&& this.endRoomPosition.equals(hall.getEndRoomPosition());
	}

}
