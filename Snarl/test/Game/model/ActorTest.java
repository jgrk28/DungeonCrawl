package Game.model;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

import Game.modelView.EntityType;

//Tests for the Actor class
public class ActorTest {
	
  //Entity types
  private EntityType w = EntityType.WALL;
  private EntityType s = EntityType.SPACE;
  private EntityType h = EntityType.HALL_SPACE;
  private EntityType e = EntityType.EMPTY;
  private EntityType k = EntityType.KEY;
  private EntityType x = EntityType.EXIT;
  private EntityType g = EntityType.GHOST;
  private EntityType z = EntityType.ZOMBIE;
  private EntityType p = EntityType.PLAYER;
  
  //Tests for Equals
  @Test
  public void testZombieEqual() {
	Adversary zombie = new Zombie();
    assertEquals(true, zombie.equals(zombie));
    assertEquals(false, zombie.equals(new Zombie()));
  }

  @Test
  public void testGhostEqual() {
	Adversary ghost = new Ghost(); 
    assertEquals(true, ghost.equals(ghost));
    assertEquals(false, ghost.equals(new Ghost()));
  }

  @Test
  public void testPlayerEqual() {
	Player player = new Player();
    assertEquals(true, player.equals(player));
    assertEquals(false, player.equals(new Player()));
  }
  
  //Tests for getEntityType
  public void testGetEntityType() {
	  Player player = new Player();
	  Zombie zombie = new Zombie();
	  Ghost ghost = new Ghost();
	  
	  assertEquals(EntityType.PLAYER, player.getEntityType());
	  assertEquals(EntityType.GHOST, ghost.getEntityType());
      assertEquals(EntityType.ZOMBIE, zombie.getEntityType());
  }
   
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
    assertTrue(player.checkValidMovePath(new Point(2,0), new Point(3, -1), goodMap3));
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
  
  //Tests for cropViewableMap

  @Test 
  public void cropViewableMapPlayer1() {
    ModelCreator creator = new ModelCreator();
	  List<List<EntityType>> fullLevelMap = creator.initializeLevel1ViewableMap();
	  
	  List<List<EntityType>> expectedMap = Arrays.asList(
	          Arrays.asList(w, w, e, e, e),
	          Arrays.asList(s, w, e, e, e),
	          Arrays.asList(s, s, p, h, h),
	          Arrays.asList(w, w, e, e, h),
	          Arrays.asList(e, e, e, e, h));
	  
	  Player player = creator.getPlayer1();

	  assertEquals(expectedMap, player.cropViewableMap(fullLevelMap, new Point(4,2)));	    	    
  }
  
  @Test 
  public void cropViewableMapPlayer2() {
    ModelCreator creator = new ModelCreator();
    List<List<EntityType>> fullLevelMap = creator.initializeLevel1ViewableMap();
	  
	  List<List<EntityType>> expectedMap = Arrays.asList(
	          Arrays.asList(w, s, g, w, e),
	          Arrays.asList(w, s, s, w, e),
	          Arrays.asList(w, s, p, w, e),
	          Arrays.asList(s, s, x, s, h),
	          Arrays.asList(w, w, w, w, e));
	  
	  Player player = creator.getPlayer2();

	  assertEquals(expectedMap, player.cropViewableMap(fullLevelMap, new Point(7,10)));	    	    
  }
  
  @Test 
  public void cropViewableMapPlayer3() {
    ModelCreator creator = new ModelCreator();
    List<List<EntityType>> fullLevelMap = creator.initializeLevel1ViewableMap();
	  
	  List<List<EntityType>> expectedMap = Arrays.asList(
	          Arrays.asList(s, s, s, s, w),
	          Arrays.asList(s, s, s, s, w),
	          Arrays.asList(s, z, p, k, w),
	          Arrays.asList(w, w, w, w, w),
	          Arrays.asList());
	  
	  Player player = creator.getPlayer3();

	  assertEquals(expectedMap, player.cropViewableMap(fullLevelMap, new Point(3,17)));	    	    
  }
}