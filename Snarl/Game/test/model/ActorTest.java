package model;

import static org.junit.Assert.*;
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
    
}

