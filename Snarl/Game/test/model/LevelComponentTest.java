package model;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import modelView.EntityType;
import org.junit.Before;
import org.junit.Test;

public class LevelComponentTest {
  Room room1;
  Room room2;
  Hall hall1;
  Hall hall2;
  Wall wall = new Wall();
  Space space = new Space();

  private void initRoom1() {
    List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
    componentMap.add(Arrays.asList(wall, wall, wall, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall));
    componentMap.add(Arrays.asList(wall, space, space, wall));
    componentMap.add(Arrays.asList(wall, wall, wall, wall));

    this.room1 = new Room(new Point(0,0), componentMap);
  }

  private void initRoom2() {
    List<List<Entity>> componentMap = new ArrayList<List<Entity>>();
    componentMap.add(Arrays.asList(space, wall));
    componentMap.add(Arrays.asList(wall, space));

    this.room2 = new Room(new Point(15,7), componentMap);
  }

  private void initHall1() {
    //Hall goes (2,11) -> (5,11) -> (5,8)
    List<Entity> componentMap = Arrays.asList(space, space, space, space, space, space, space);
    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(5,11));

    this.hall1 = new Hall(componentMap, waypoints);

    //Doors for hall1
    hall1.connectRooms(new Point(1,11), room1, new Point(5,7), room2);
  }

  private void initHall2() {
    //Hall goes (3,3) -> (5,3) -> (5,6) -> (2,6) -> (2,10)
    List<Entity> componentMap = Arrays.asList(space, space, space, space, space, space, space,
        space, space, space, space, space, space);
    List<Point> waypoints = new ArrayList<Point>();
    waypoints.add(new Point(5,3));
    waypoints.add(new Point(5,6));
    waypoints.add(new Point(2,6));

    this.hall2 = new Hall(componentMap, waypoints);

    //Doors for hall1
    hall2.connectRooms(new Point(2,3), room1, new Point(2,11), room2);
  }

  @Before
  public void initEntities() {
    initRoom1();
    initRoom2();
    initHall1();
    initHall2();
  }

  //TODO Are we allowing negative positions?
  @Test
  public void testGetTopLeftBound() {
    assertEquals(new Point(0,0), this.room1.getTopLeftBound());
    assertEquals(new Point(15,7), this.room2.getTopLeftBound());
    assertEquals(new Point(2,8), this.hall1.getTopLeftBound());
    assertEquals(new Point(2,3), this.hall2.getTopLeftBound());
  }

  @Test
  public void testGetBottomRightBound() {
    assertEquals(new Point(3,3), this.room1.getBottomRightBound());
    assertEquals(new Point(16,8), this.room2.getBottomRightBound());
    assertEquals(new Point(5,11), this.hall1.getBottomRightBound());
    assertEquals(new Point(5,10), this.hall2.getBottomRightBound());
  }

  @Test
  public void testGetDestinationEntity() {
    assertEquals(new Wall(), this.room1.getDestinationEntity(new Point(0,0)));
    assertEquals(new Space(), this.room1.getDestinationEntity(new Point(1,1)));
    assertEquals(new Space(), this.hall1.getDestinationEntity(new Point(5,11)));
    assertEquals(new Space(), this.hall1.getDestinationEntity(new Point(5,9)));
  }

  @Test
  public void testGetEntityType() {
    assertEquals(EntityType.WALL, this.room1.getEntityType(new Wall()));
    assertEquals(EntityType.SPACE, this.room1.getEntityType(new Space()));
    assertEquals(EntityType.WALL, this.hall1.getEntityType(new Wall()));
    assertEquals(EntityType.HALL_SPACE, this.hall1.getEntityType(new Space()));
  }
}