package model;

import static org.junit.Assert.*;

import modelView.EntityType;
import org.junit.Before;
import org.junit.Test;

public class EntityTest {
  Entity wall;
  Entity space;

  @Before
  public void initEntities() {
    this.wall = new Wall();
    this.space = new Space();
  }

  @Test
  public void testGetEntityType() {
    assertEquals(EntityType.WALL, wall.getEntityType());
    assertEquals(EntityType.SPACE, space.getEntityType());
  }
}