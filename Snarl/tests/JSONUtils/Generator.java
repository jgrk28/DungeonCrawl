package JSONUtils;

import Common.Player;
import Game.model.Actor;
import Game.model.Exit;
import Game.model.Ghost;
import Game.model.Hall;
import Game.model.Item;
import Game.model.Key;
import Game.model.Level;
import Game.model.LevelComponent;
import Game.model.Room;
import Game.model.Zombie;
import Game.modelView.EntityType;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class for generating JSONs related to a game of Snarl.
 * This includes the state, the level, rooms, halls, and
 * items.
 */
public class Generator {
	
  /**
   * Generates a JSONArray containing the specified location
   * @param location - the point representation of the location
   * @return the JSONArray [row, col] for the point
   */
  static public JSONArray generateJSONPoint(Point location) {
    JSONArray JSONPoint = new JSONArray();
    JSONPoint.put(location.y);
    JSONPoint.put(location.x);
    return JSONPoint;
  }

  /**
   * Generates a list of JSONArrays that represent points
   * @param points - the list of points to convert
   * @return the JSONArray containing all given points
   */
  static public JSONArray generateJSONPointList(List<Point> points) {
    JSONArray JSONPointList = new JSONArray();
    for (Point point : points) {
      JSONArray JSONPoint = generateJSONPoint(point);
      JSONPointList.put(JSONPoint);
    }
    return JSONPointList;
  }

  /**
   * Generates the actor list, including each actor's type, name, and
   * location
   * @param actors - a map of all actors and their locations
   * @return a JSONActor list of JSONObjects 
   */
  static public JSONArray generateJSONActorList(Map<Actor, Point> actors) {
    JSONArray actorPositionList = new JSONArray();
    for (Map.Entry<Actor, Point> actorEntry : actors.entrySet()) {
      JSONObject actorJSON = new JSONObject();
      Actor actor = actorEntry.getKey();
      Point position = actorEntry.getValue();

      if (actor instanceof Player) {
        actorJSON.put("type", "player");
      } else if (actor instanceof Zombie) {
        actorJSON.put("type", "zombie");
      } else if (actor instanceof Ghost) {
        actorJSON.put("type", "ghost");
      }

      actorJSON.put("name", actor.getName());
      actorJSON.put("position", generateJSONPoint(position));
      actorPositionList.put(actorJSON);
    }
    return actorPositionList;
  }

  /**
   * Generates a JSONArray for all given items
   * @param items - the provided items (keys and exits)
   * @return a JSONArray of JSONObjects representing each item
   */
  static public JSONArray generateJSONObjects(List<Item> items) {
    JSONArray objectList = new JSONArray();
    for (Item item : items) {
      JSONObject JSONItem = new JSONObject();
      if (item instanceof Key) {
        JSONItem.put("type", "key");
      }
      else if (item instanceof Exit) {
        JSONItem.put("type", "exit");
      }
      JSONArray position = generateJSONPoint(item.getLocation());
      JSONItem.put("position", position);
      objectList.put(JSONItem);
    }
    return objectList;
  }

  /**
   * Generates a JSONArray of halls. 
   * @param halls - the list of halls to convert
   * @return a JSONArray of JSONObjects representing each hall. This
   * includes the from, to, and waypoints
   */
  static public JSONArray generateJSONHalls(List<Hall> halls) {
    JSONArray JSONHalls = new JSONArray();
    for (Hall hall : halls) {
      JSONObject JSONHall = new JSONObject();

      Point startPos = hall.getStartRoomPosition();
      Point endPos = hall.getEndRoomPosition();
      List<Point> waypoints = hall.getWaypoints();

      JSONArray JSONStart = generateJSONPoint(startPos);
      JSONArray JSONEnd = generateJSONPoint(endPos);

      JSONArray JSONWaypoints = generateJSONPointList(waypoints);

      JSONHall.put("type", "hallway");
      JSONHall.put("from", JSONStart);
      JSONHall.put("to", JSONEnd);
      JSONHall.put("waypoints", JSONWaypoints);

      JSONHalls.put(JSONHall);
    }
    return JSONHalls;
  }

  /**
   * Generates the layout for a room based on the entityMap, origin, and doors
   * @param entityMap - the layout of entities in the room
   * @param roomOrigin - the origin of the room
   * @param doors - the doors located in the room
   * @return a JSONArray of the layout of the room, where 0 is a wall, 1 is a
   * space, and 2 is a door
   */
  static public JSONArray generateJSONLayout(
      List<List<EntityType>> entityMap, Point roomOrigin, Set<Point> doors) {
    JSONArray layoutOutput = new JSONArray();
    Set<Point> relativeDoors = new HashSet<>();

    //Determine the relative locations of the doors
    for (Point point : doors) {
      Point relPoint = new Point(point.x - roomOrigin.x, point.y - roomOrigin.y);
      relativeDoors.add(relPoint);
    }

    for (int i = 0; i < entityMap.size(); i++) {
      List<EntityType> entityRow = entityMap.get(i);
      JSONArray JSONRow = new JSONArray();
      
      for (int j = 0; j < entityRow.size(); j++) {
    	  
        EntityType entity = entityRow.get(j);
        
        //If the entity is empty or a wall, place a 0
        if (entity.equals(EntityType.EMPTY) || entity.equals(EntityType.WALL)) {
          JSONRow.put(0);
    
        } 
        //If the entity is a door, place a 2
        else if (relativeDoors.contains(new Point(j, i))) {
          JSONRow.put(2);
        }
        //Otherwise, place a 1
        else {
          JSONRow.put(1);
        }
      }
      layoutOutput.put(JSONRow);
    }
    return layoutOutput;
  }

  /**
   * Determines the bounds of a room based on the origin and bottom right
   * coordinate 
   * @param origin - the origin of the room
   * @param bottomRight - the bottom right coordinate of the room
   * @return a JSON object containing the number of rows and columns
   */
  static public JSONObject generateJSONBounds(Point origin, Point bottomRight) {
	//Account for off-by-1 calculation 
    int rows = bottomRight.y - origin.y + 1;
    int columns = bottomRight.x - origin.x + 1;

    JSONObject JSONBounds = new JSONObject();
    JSONBounds.put("rows", rows);
    JSONBounds.put("columns", columns);
    return JSONBounds;
  }

  /**
   * Generates a JSONArray of rooms 
   * @param rooms - the list of rooms to convert to JSON format
   * @return a JSONArray representation of the list of rooms
   */
  static public JSONArray generateJSONRooms(List<Room> rooms) {
    JSONArray JSONRooms = new JSONArray();
    
    for (Room room : rooms) {
      JSONObject JSONRoom = new JSONObject();
      Point origin = room.getTopLeftBound();
      JSONArray JSONOrigin = generateJSONPoint(origin);
      Point bottomRight = room.getBottomRightBound();
      JSONObject JSONBounds = generateJSONBounds(origin, bottomRight);
      List<List<EntityType>> entityMap = room.getComponentMap();
      Map<Point, Hall> doors = room.getDoors();
      JSONArray JSONLayout = generateJSONLayout(entityMap, origin, doors.keySet());

      JSONRoom.put("type", "room");
      JSONRoom.put("origin", JSONOrigin);
      JSONRoom.put("bounds", JSONBounds);
      JSONRoom.put("layout", JSONLayout);

      JSONRooms.put(JSONRoom);
    }
    
    return JSONRooms;
  }

  /**
   * Generates the JSON representation of a level
   * @param level - the level to convert to JSON format
   * @return the JSONObject containing all relevant information
   * for the level 
   */
  static public JSONObject generateJSONLevel(Level level) {
    List<LevelComponent> levelMap = level.getLevelMap();
    List<Room> rooms = new ArrayList<>();
    List<Hall> halls = new ArrayList<>();

    //Group all LevelComponents in the level into Rooms or Halls
    for (LevelComponent component : levelMap) {
      if (component instanceof Room) {
        rooms.add((Room) component);
      } else if (component instanceof Hall) {
        halls.add((Hall) component);
      } else {
        throw new IllegalArgumentException("Invalid LevelComponent type");
      }
    }
    
    //Generate rooms, halls, and objects
    JSONArray JSONRooms = generateJSONRooms(rooms);
    JSONArray JSONHalls = generateJSONHalls(halls);
    JSONArray JSONObjects = generateJSONObjects(level.getItems());

    JSONObject JSONLevel = new JSONObject();
    JSONLevel.put("type", "level");
    JSONLevel.put("rooms", JSONRooms);
    JSONLevel.put("hallways", JSONHalls);
    JSONLevel.put("objects", JSONObjects);

    return JSONLevel;
  }

  /**
   * Generates a JSON representation of the state for the given level
   * @param level - the level to convert into the JSON state format
   * @return a JSONObject representing the state of the provided level
   */
  static public JSONObject generateJSONState(Level level) {
    JSONObject JSONState = new JSONObject();

    JSONObject JSONLevel = generateJSONLevel(level);
    JSONArray JSONPlayers = generateJSONActorList(level.getActivePlayers());
    JSONArray JSONAdversaries = generateJSONActorList(level.getActiveAdversaries());
    Boolean exitLocked = !level.getExitUnlocked();

    JSONState.put("type", "state");
    JSONState.put("level", JSONLevel);
    JSONState.put("players", JSONPlayers);
    JSONState.put("adversaries", JSONAdversaries);
    JSONState.put("exit-locked", exitLocked);

    return JSONState;
  }
}
