package model;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

import modelView.EntityType;

public class ActorTest {
  
  //Testing interaction results for players
  @Test
  public void testPlayerInteractionResult() {
	  Player player = new Player();
	  assertEquals(InteractionResult.NONE, player.getInteractionResult(EntityType.SPACE));
	  assertEquals(InteractionResult.NONE, player.getInteractionResult(EntityType.HALL_SPACE));
	  assertEquals(InteractionResult.FOUND_KEY, player.getInteractionResult(EntityType.KEY));
	  assertEquals(InteractionResult.EXIT, player.getInteractionResult(EntityType.EXIT));
	  assertEquals(InteractionResult.REMOVE_PLAYER, player.getInteractionResult(EntityType.GHOST));
	  assertEquals(InteractionResult.REMOVE_PLAYER, player.getInteractionResult(EntityType.ZOMBIE));	  
  }
  
  //Testing invalid interactions for players
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidPlayerInteractionResult() {
	  Player player = new Player();
	  player.getInteractionResult(EntityType.PLAYER);	  
  }
  
  //Testing interaction results for ghosts
  @Test
  public void testGhostInteractionResult() {
	  Adversary ghost = new Ghost();
	  assertEquals(InteractionResult.NONE, ghost.getInteractionResult(EntityType.SPACE));
	  assertEquals(InteractionResult.NONE, ghost.getInteractionResult(EntityType.HALL_SPACE));
	  assertEquals(InteractionResult.REMOVE_PLAYER, ghost.getInteractionResult(EntityType.PLAYER));	  
  }
  
  //Testing invalid interactions for ghosts
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidGhostInteractionResultKey() {
	  Adversary ghost = new Ghost();
	  ghost.getInteractionResult(EntityType.KEY);
  }
  
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidGhostInteractionResultExit() {
	  Adversary ghost = new Ghost();
	  ghost.getInteractionResult(EntityType.EXIT);
  }
  
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidGhostInteractionResultGhost() {
	  Adversary ghost = new Ghost();
	  ghost.getInteractionResult(EntityType.GHOST);
  }
  
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidGhostInteractionResultZombie() {
	  Adversary ghost = new Ghost();
	  ghost.getInteractionResult(EntityType.ZOMBIE);
  }
  
  //Testing interaction results for zombies
  @Test
  public void testZombieInteractionResult() {
	  Adversary zombie = new Zombie();
	  assertEquals(InteractionResult.NONE, zombie.getInteractionResult(EntityType.SPACE));
	  assertEquals(InteractionResult.NONE, zombie.getInteractionResult(EntityType.HALL_SPACE));
	  assertEquals(InteractionResult.REMOVE_PLAYER, zombie.getInteractionResult(EntityType.PLAYER));	  
  }
  
  //Testing invalid interaction for zombies
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidZombieInteractionResultKey() {
	  Adversary zombie = new Zombie();
	  zombie.getInteractionResult(EntityType.KEY);
  }
  
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidZombieInteractionResultExit() {
	  Adversary zombie = new Zombie();
	  zombie.getInteractionResult(EntityType.EXIT);
  }
  
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidZombieInteractionResultGhost() {
	  Adversary zombie = new Zombie();
	  zombie.getInteractionResult(EntityType.GHOST);
  }
  
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidZombieInteractionResultZombie() {
	  Adversary zombie = new Zombie();
	  zombie.getInteractionResult(EntityType.ZOMBIE);
  }

  //Tests for checkValidMoveDistance
  @Test
  public void testCheckValidMoveDistanceAdversaryGood() {
    Actor zombie = new Zombie();
    Actor ghost = new Ghost();
    assertTrue(zombie.checkValidMoveDistance(new Point(0,0), new Point(0, 1)));
    assertTrue(zombie.checkValidMoveDistance(new Point(5,10), new Point(5, 9)));
    assertTrue(ghost.checkValidMoveDistance(new Point(2,2), new Point(3, 2)));
    assertTrue(ghost.checkValidMoveDistance(new Point(5,0), new Point(5, -1)));
  }

  @Test
  public void testCheckValidMoveDistanceAdversaryBad() {
    Actor zombie = new Zombie();
    Actor ghost = new Ghost();
    assertFalse(zombie.checkValidMoveDistance(new Point(0,0), new Point(1, 1)));
    assertFalse(zombie.checkValidMoveDistance(new Point(5,10), new Point(5, 8)));
    assertFalse(ghost.checkValidMoveDistance(new Point(2,2), new Point(4, 3)));
    assertFalse(ghost.checkValidMoveDistance(new Point(5,0), new Point(5, -5)));
  }

  @Test
  public void testCheckValidMoveDistancePlayerGood() {
    Actor player = new Player();
    assertTrue(player.checkValidMoveDistance(new Point(0,0), new Point(0, 1)));
    assertTrue(player.checkValidMoveDistance(new Point(5,10), new Point(5, 8)));
    assertTrue(player.checkValidMoveDistance(new Point(5,0), new Point(5, -2)));
    assertTrue(player.checkValidMoveDistance(new Point(2,4), new Point(0, 4)));
    assertTrue(player.checkValidMoveDistance(new Point(2,2), new Point(3, 3)));
    assertTrue(player.checkValidMoveDistance(new Point(0,0), new Point(-1, -1)));
    assertTrue(player.checkValidMoveDistance(new Point(4,7), new Point(3, 8)));
  }

  @Test
  public void testCheckValidMoveDistancePlayerBad() {
    Actor player = new Player();
    assertFalse(player.checkValidMoveDistance(new Point(0,0), new Point(0, 3)));
    assertFalse(player.checkValidMoveDistance(new Point(5,10), new Point(6, 8)));
    assertFalse(player.checkValidMoveDistance(new Point(5,0), new Point(5, -3)));
    assertFalse(player.checkValidMoveDistance(new Point(2,4), new Point(10, 4)));
    assertFalse(player.checkValidMoveDistance(new Point(2,2), new Point(5, 3)));
  }

  //Tests for checkValidMovePath
  @Test
  public void testCheckValidMovePathGood() {
    EntityType w = EntityType.WALL;
    EntityType s = EntityType.SPACE;
    EntityType p = EntityType.PLAYER;
    EntityType g = EntityType.GHOST;
    EntityType e = EntityType.EMPTY;
    Actor player = new Player();
    List<List<EntityType>> goodMap1 = Arrays.asList(
        Arrays.asList(p, s),
        Arrays.asList(s, s));
    List<List<EntityType>> goodMap2 = Arrays.asList(
        Arrays.asList(s, w),
        Arrays.asList(s, p));
    List<List<EntityType>> goodMap3 = Arrays.asList(
        Arrays.asList(e, s),
        Arrays.asList(p, p));
    List<List<EntityType>> goodMap4 = Arrays.asList(
        Arrays.asList(p, g));
    List<List<EntityType>> goodMap5 = Arrays.asList(
            Arrays.asList(p));
    assertTrue(player.checkValidMovePath(new Point(0,0), new Point(1, 1), goodMap1));
    assertTrue(player.checkValidMovePath(new Point(4,3), new Point(3, 2), goodMap2));
    //TODO confirm if players can jump over other players during a move
    //assertTrue(player.checkValidMovePath(new Point(2,0), new Point(3, -1), goodMap3));
    assertTrue(player.checkValidMovePath(new Point(5,5), new Point(6, 5), goodMap4));
    assertTrue(player.checkValidMovePath(new Point(5,5), new Point(5, 5), goodMap5));
  }

  @Test
  public void testCheckValidMovePathBad() {
    EntityType w = EntityType.WALL;
    EntityType s = EntityType.SPACE;
    EntityType p = EntityType.PLAYER;
    EntityType g = EntityType.GHOST;
    EntityType e = EntityType.EMPTY;
    Actor player = new Player();
    List<List<EntityType>> badMap1 = Arrays.asList(
        Arrays.asList(p, e),
        Arrays.asList(e, s));
    List<List<EntityType>> badMap2 = Arrays.asList(
        Arrays.asList(p, w),
        Arrays.asList(s, p));
    List<List<EntityType>> badMap3 = Arrays.asList(
        Arrays.asList(g, w),
        Arrays.asList(p, s));
    List<List<EntityType>> badMap4 = Arrays.asList(
        Arrays.asList(p),
        Arrays.asList(p));
    assertFalse(player.checkValidMovePath(new Point(0,0), new Point(1, 1), badMap1));
    assertFalse(player.checkValidMovePath(new Point(8,3), new Point(7, 2), badMap2));
    assertFalse(player.checkValidMovePath(new Point(7,0), new Point(8, -1), badMap3));
    assertFalse(player.checkValidMovePath(new Point(0,5), new Point(0, 6), badMap4));
  }
}

