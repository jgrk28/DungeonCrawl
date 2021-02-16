package model;

import java.awt.Point;
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
	 * @return
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
	//Top right left includes the door that the hall is connected to 
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
		
	/**
	 * Walk through each location in the hall to determine if it matches the given 
	 * point. If so, return the Entity at that location
	 */
	@Override
	public Entity getDestinationEntity(Point point) {	
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
				return this.componentMap.get(steps);
			}
			
			//If you have reached the next waypoint, check to see what the nextDestination is
			if (nextDestination.equals(currentPosition)) {
				waypointCounter++;
				
				//If we have reached the end of the waypoints, the nextDestination is the EndRoom
				if (this.waypoints.size() == waypointCounter) {
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
}
