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
import Utils.ParseUtils;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

public class Generator {
  /**
   * TODO add comment
   * @param location
   * @return
   */
  static public JSONArray generateJSONPoint(Point location) {
    JSONArray JSONPoint = new JSONArray();
    JSONPoint.put(location.y);
    JSONPoint.put(location.x);
    return JSONPoint;
  }

  /**
   * TODO add comment
   * @param points
   * @return
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
   * TODO add comment
   * @param actors
   * @return
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
   * TODO add comment
   * @param items
   * @return
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
   * TODO add comment
   * @param halls
   * @return
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
   * TODO add comment
   * @param entityMap
   * @param roomOrigin
   * @param doors
   * @return
   */
  static public JSONArray generateJSONLayout(
      List<List<EntityType>> entityMap, Point roomOrigin, Set<Point> doors) {
    JSONArray layoutOutput = new JSONArray();
    Set<Point> relativeDoors = new HashSet<>();

    for (Point point : doors) {
      Point relPoint = new Point(point.x - roomOrigin.x, point.y - roomOrigin.y);
      relativeDoors.add(relPoint);
    }

    for (int i = 0; i < entityMap.size(); i++) {
      List<EntityType> entityRow = entityMap.get(i);
      JSONArray JSONRow = new JSONArray();
      for (int j = 0; j < entityRow.size(); j++) {
        EntityType entity = entityRow.get(j);
        if (entity.equals(EntityType.EMPTY) || entity.equals(EntityType.WALL)) {
          JSONRow.put(0);
        } else if (relativeDoors.contains(new Point(j, i))) {
          JSONRow.put(2);
        } else {
          JSONRow.put(1);
        }
      }
      layoutOutput.put(JSONRow);
    }
    return layoutOutput;
  }

  /**
   * TODO add comment
   * @param origin
   * @param bottomRight
   * @return
   */
  static public JSONObject generateJSONBounds(Point origin, Point bottomRight) {
    int rows = bottomRight.y - origin.y + 1;
    int columns = bottomRight.x - origin.x + 1;

    JSONObject JSONBounds = new JSONObject();
    JSONBounds.put("rows", rows);
    JSONBounds.put("columns", columns);
    return JSONBounds;
  }

  /**
   * TODO add comment
   * @param rooms
   * @return
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
   * TODO add comment
   * @param level
   * @return
   */
  static public JSONObject generateJSONLevel(Level level) {
    List<LevelComponent> levelMap = level.getLevelMap();
    List<Room> rooms = new ArrayList<>();
    List<Hall> halls = new ArrayList<>();

    for (LevelComponent component : levelMap) {
      if (component instanceof Room) {
        rooms.add((Room) component);
      } else if (component instanceof Hall) {
        halls.add((Hall) component);
      } else {
        throw new IllegalArgumentException("Invalid LevelComponent type");
      }
    }
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
   * TODO add comment
   * @param level
   * @return
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
