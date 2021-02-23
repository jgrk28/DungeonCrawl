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
	//TODO split into 4
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidGhostInteractionResult() {
	  Adversary ghost = new Ghost();
	  ghost.getInteractionResult(EntityType.KEY);
	  ghost.getInteractionResult(EntityType.EXIT);
	  ghost.getInteractionResult(EntityType.GHOST);
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
	//TODO split into 4
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidZombieInteractionResult() {
	  Adversary zombie = new Zombie();
	  zombie.getInteractionResult(EntityType.KEY);
	  zombie.getInteractionResult(EntityType.EXIT);
	  zombie.getInteractionResult(EntityType.GHOST);
	  zombie.getInteractionResult(EntityType.ZOMBIE);
  }
  
  
}

