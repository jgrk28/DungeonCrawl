package Game.model;

import static org.junit.Assert.assertEquals;

import java.awt.Point;

import org.junit.Test;

import Game.modelView.EntityType;

public class ItemTest {
	
	//Tests for equals
	@Test
	public void testKeyEqual() {
		Item key = new Key(new Point(2,0));
	    assertEquals(true, key.equals(key));
	    assertEquals(false, key.equals(new Key(new Point(0, 0))));
	}

	@Test
	public void testExitEqual() {
		Item exit = new Exit(new Point(2,0));
	    assertEquals(true, exit.equals(exit));
	    assertEquals(false, exit.equals(new Exit(new Point(0, 0))));
	}
	
	//Tests for getEntityType
	@Test
	public void testGetEntityTypeKey() {
		Item key = new Key(new Point(2,0));
	    assertEquals(EntityType.KEY, key.getEntityType());
	}
	
	@Test
	public void testGetEntityTypeExit() {
		Item exit = new Exit(new Point(2,0));
	    assertEquals(EntityType.EXIT, exit.getEntityType());
	}
		
	//Tests for getLocation
	@Test
	public void testGetLocationKey() {
		Item key = new Key(new Point(2,0));
	    assertEquals(new Point(2,0), key.getLocation());
	}
	
	@Test
	public void testGetLocationExit() {
		Item exit = new Exit(new Point(2,0));
	    assertEquals(new Point(2,0), exit.getLocation());
	}		

}
