package Game.model;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

import Game.modelView.EntityType;

public class TileTest {
	
	//Tests for GetActor
	@Test (expected = IllegalArgumentException.class)
	public void testWallExceptionGetActor() {
		Tile wall = new Wall();
		wall.getActor();		
	}
	
	@Test
	public void testSpaceGetPlayer() {
		Player player = new Player();		
		Item key = new Key(new Point(0,0));
		Tile space = new Space(key, player);
		assertEquals(player, space.getActor());		
	}
	
	@Test 
	public void testSpaceGetGhost() {
		Adversary ghost = new Ghost();		
		Item key = new Key(new Point(0,0));
		Tile space = new Space(key, ghost);
		assertEquals(ghost, space.getActor());		
	}
	
	@Test 
	public void testSpaceGetZombie() {
		Zombie zombie = new Zombie();		
		Item key = new Key(new Point(0,0));
		Tile space = new Space(key, zombie);
		assertEquals(zombie, space.getActor());		
	}
	
	//Tests for placeActor
	@Test (expected = IllegalArgumentException.class)
	public void testWallExceptionPlaceActor() {
		Tile wall = new Wall();
		Player player = new Player();
		wall.placeActor(player);		
	}
	
	@Test 
	public void testSpacePlacePlayer() {
		Tile space = new Space();
		Player player = new Player();
		space.placeActor(player);	
		assertEquals(EntityType.PLAYER, space.getEntityType());
	}
	
	@Test 
	public void testSpacePlaceGhost() {
		Tile space = new Space();
		Adversary ghost = new Ghost();
		space.placeActor(ghost);	
		assertEquals(EntityType.GHOST, space.getEntityType());	
	}
	
	@Test 
	public void testSpacePlaceZombie() {
		Tile space = new Space();
		Adversary zombie = new Zombie();
		space.placeActor(zombie);	
		assertEquals(EntityType.ZOMBIE, space.getEntityType());		
	}
	
	@Test 
	public void testSpacePlaceMultipleActors() {
		Tile space = new Space();
		Player player = new Player();
		Adversary zombie = new Zombie();
		space.placeActor(player);
		space.placeActor(zombie);
		assertEquals(EntityType.ZOMBIE, space.getEntityType());			
	}	
	
	//Tests for placeItem
	
	@Test (expected = IllegalArgumentException.class)
	public void testWallExceptionPlaceItem() {
		Tile wall = new Wall();
		Item key = new Key(new Point(0,0));
		wall.placeItem(key);		
	}
	
	@Test 
	public void testSpacePlaceKey() {
		Tile space = new Space();
		Item key = new Key(new Point(0,0));
		space.placeItem(key);	
		assertEquals(EntityType.KEY, space.getEntityType());
	}
	
	@Test 
	public void testSpacePlaceExit() {
		Tile space = new Space();
		Item exit = new Exit(new Point(0,0));
		space.placeItem(exit);	
		assertEquals(EntityType.EXIT, space.getEntityType());
	}
	
	@Test 
	public void testSpacePlaceMultipleItems() {
		Tile space = new Space();
		Item key = new Key(new Point(0,0));
		Item exit = new Exit(new Point(0,0));
		space.placeItem(key);	
		space.placeItem(exit);	
		assertEquals(EntityType.EXIT, space.getEntityType());
	}
	
	
	//Tests for GetEntityType
	@Test
	public void testGetEntityTypeSpace() {
		Tile space = new Space();
		assertEquals(EntityType.SPACE, space.getEntityType());		
	}
	
	@Test
	public void testGetEntityTypeWall() {
		Tile wall = new Wall();
		assertEquals(EntityType.WALL, wall.getEntityType());		
	}
	
	//Tests for Equals
	@Test
	public void testWallEqual() {
		Tile wall = new Wall();
		assertTrue(wall.equals(wall));	
		assertTrue(wall.equals(new Wall()));	
	}
	
	@Test
	public void testSpaceEqual() {
		Tile space = new Space();
		assertTrue(space.equals(space));
		assertTrue(space.equals(new Space()));
	}
	
	@Test
	public void testSpaceWithFieldsNotEqual() {
		Player player = new Player();		
		Item key = new Key(new Point(0,0));
		Tile space = new Space(key, player);
		assertFalse(space.equals(new Space()));
	}
}
