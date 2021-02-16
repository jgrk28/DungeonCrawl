package model;

import java.awt.Point;
import java.util.List;
import modelView.EntityType;

/**
 * A LevelComponent that represents a Hall within a Level
 * A Hall connects two rooms in the Level
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
	 * @param waypoints - a list of points that represent corners in the hall
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
		
		if (this.waypoints.isEmpty()) {
			//Check that the room positions are orthogonal
			return (positionRoomStart.x == positionRoomEnd.x) || (positionRoomStart.y == positionRoomEnd.y);
		}
		
		Point firstWaypoint = this.waypoints.get(0);
		boolean validStart = firstWaypoint.x == positionRoomStart.x ||
				firstWaypoint.y == positionRoomStart.y;
		
		Point lastWaypoint = this.waypoints.get(this.waypoints.size() - 1);
		boolean validEnd = lastWaypoint.x == positionRoomEnd.x ||
				lastWaypoint.y == positionRoomEnd.y;
		
		return validStart && validEnd;	
	}


	//Top right left includes the door that the hall is connected to 
	@Override
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

	//Bottom right bound includes the door that the hall is connected to 
	@Override
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
		
	//Next destination => next waypoint, or destination room
	//Loop through counter for length of hall
	//Each time in the loop, update internal position by 1 (x or y) based on next destination 
	//When at the destination, update destination
	//Check each time if you are at the point you are looking for. If yes, exit
	//Count is index for component map
	@Override
	public Entity getDestinationEntity(Point point) {	
		//Start at StartRoom
		Point currentPosition = this.startRoomPosition;
		
		//Keeps track of the current waypoint
		int waypointCounter = 0;
		
		Point nextDestination;
		
		if (this.waypoints.isEmpty()) {
			nextDestination = this.endRoomPosition;
		} else {
			nextDestination = this.waypoints.get(waypointCounter);
		}
		
		currentPosition = stepTowardDestination(currentPosition, nextDestination);
		
		for (int steps = 0; steps < this.componentMap.size(); steps++) {
			if (currentPosition.equals(point)) {
				return this.componentMap.get(steps);
			}
			
			if (nextDestination.equals(currentPosition)) {
				waypointCounter++;
				if (this.waypoints.size() == waypointCounter) {
					nextDestination = this.endRoomPosition;
				} else {
					nextDestination = this.waypoints.get(waypointCounter);
				}
			}
			currentPosition = stepTowardDestination(currentPosition, nextDestination);
		}

		throw new IllegalArgumentException("Point not in component");
	}
	
	private Point stepTowardDestination(Point source, Point destination) {
		
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
