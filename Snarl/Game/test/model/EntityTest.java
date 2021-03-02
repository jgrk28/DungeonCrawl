package model;

import static org.junit.Assert.*;

import java.awt.Point;
import modelView.EntityType;
import org.junit.Before;
import org.junit.Test;

public class EntityTest {
  Entity wall;
  Entity space;
  Entity key;
  Entity exit;
  Entity zombie;
  Entity ghost;
  Entity player;

  @Before
  public void initEntities() {
    this.wall = new Wall();
    this.space = new Space();
    this.key = new Key(new Point(0, 0));
    this.exit = new Exit(new Point(0, 0));
    this.zombie = new Zombie();
    this.ghost = new Ghost();
    this.player = new Player();
  }

  @Test
  public void testGetEntityType() {
    assertEquals(EntityType.WALL, wall.getEntityType());
    assertEquals(EntityType.SPACE, space.getEntityType());
    assertEquals(EntityType.KEY, key.getEntityType());
    assertEquals(EntityType.EXIT, exit.getEntityType());
    assertEquals(EntityType.ZOMBIE, zombie.getEntityType());
    assertEquals(EntityType.GHOST, ghost.getEntityType());
    assertEquals(EntityType.PLAYER, player.getEntityType());
  }

  //A Wall should be equal to any object of the Wall class
  @Test
  public void testWallEqual() {
    assertEquals(true, wall.equals(wall));
    assertEquals(true, wall.equals(new Wall()));
  }

  //A Space should be equal to any object of the Space class
  @Test
  public void testSpaceEqual() {
    assertEquals(true, space.equals(space));
    assertEquals(true, space.equals(new Space()));
  }

  //All other entities should be only equal to an object if it is the same exact object
  @Test
  public void testKeyEqual() {
    assertEquals(true, key.equals(key));
    assertEquals(false, key.equals(new Key(new Point(0, 0))));
  }

  @Test
  public void testExitEqual() {
    assertEquals(true, exit.equals(exit));
    assertEquals(false, exit.equals(new Exit(new Point(0, 0))));
  }

  @Test
  public void testZombieEqual() {
    assertEquals(true, zombie.equals(zombie));
    assertEquals(false, zombie.equals(new Zombie()));
  }

  @Test
  public void testGhostEqual() {
    assertEquals(true, ghost.equals(ghost));
    assertEquals(false, ghost.equals(new Ghost()));
  }

  @Test
  public void testPlayerEqual() {
    assertEquals(true, player.equals(player));
    assertEquals(false, player.equals(new Player()));
  }
}