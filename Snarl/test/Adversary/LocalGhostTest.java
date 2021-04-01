package Adversary;

import static org.junit.Assert.*;

import Game.model.Actor;
import Game.model.Adversary;
import Game.model.Level;
import Game.model.ModelCreator;
import Game.model.Player;
import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class LocalGhostTest {
  LocalGhost ghostClient;
  Player player1;
  Player player2;
  Player player3;
  Adversary ghost1;
  Adversary ghost2;
  Adversary zombie1;
  Adversary zombie2;
  Map<Player, Point> playerLocations;

  //These tests also implicitly test getLevelStart and updateActorLocations
  @Before
  public void initializeLocalGhost() {
    ModelCreator creator = new ModelCreator();

    this.ghostClient = new LocalGhost();
    Level level = creator.initializeLevel1NoActor();
    this.ghostClient.getLevelStart(level);

    this.player1 = creator.getPlayer1();
    this.player2 = creator.getPlayer2();
    this.player3 = creator.getPlayer3();
    this.ghost1 = creator.getGhost1();
    this.ghost2 = creator.getGhost2();
    this.zombie1 = creator.getZombie1();
    this.zombie2 = creator.getZombie2();
    this.playerLocations = new HashMap<>();
    playerLocations.put(this.player1, new Point(4, 2));
    playerLocations.put(this.player2, new Point(6, 11));
    playerLocations.put(this.player2, new Point(2, 14));
  }

  /*
  Level for reference

  XXXX
  X..X
  X...P**
  XXXX  *
        *
        *
        *
       X.XX
       X..X
       X..X
       X..X    XXXXX
    ***.P@.****....X
    *  XXXX    X...X
    *          X...X
  XXPXXX       X...X
  X....X       XXXXX
  X....X
  X...!X
  XXXXXX

   */

  @Test
  public void testTakeTurnChase() {
    Map<Adversary, Point> adversaryLocation = new HashMap<>();
    adversaryLocation.put(this.ghost1, new Point(1,1));
    adversaryLocation.put(this.ghost2, new Point(2,11));
    adversaryLocation.put(this.zombie1, new Point(7,9));
    adversaryLocation.put(this.zombie2, new Point(6,2));

    this.ghostClient.updateActorLocations(this.playerLocations, adversaryLocation, this.ghost1);

    Set<Point> acceptableMoves = new HashSet<>();
    acceptableMoves.add(new Point(1,2));
    acceptableMoves.add(new Point(2,1));
    assertTrue(acceptableMoves.contains(this.ghostClient.takeTurn()));

    this.ghostClient.updateActorLocations(this.playerLocations, adversaryLocation, this.ghost2);
    assertEquals(new Point(2, 12), this.ghostClient.takeTurn());
  }

  @Test
  public void testTakeTurnChaseNavigate() {
    Map<Adversary, Point> adversaryLocation = new HashMap<>();
    adversaryLocation.put(this.ghost1, new Point(2,1));
    adversaryLocation.put(this.ghost2, new Point(6,9));
    adversaryLocation.put(this.zombie1, new Point(6,10));
    adversaryLocation.put(this.zombie2, new Point(4,15));

    this.ghostClient.updateActorLocations(this.playerLocations, adversaryLocation, this.ghost1);
    assertEquals(new Point(2, 2), this.ghostClient.takeTurn());

    this.ghostClient.updateActorLocations(this.playerLocations, adversaryLocation, this.ghost2);
    assertEquals(new Point(7, 9), this.ghostClient.takeTurn());
  }

  @Test
  public void testTakeTurnTeleport() {
    Map<Adversary, Point> adversaryLocation = new HashMap<>();
    adversaryLocation.put(this.ghost1, new Point(13,11));
    adversaryLocation.put(this.ghost2, new Point(16,13));
    adversaryLocation.put(this.zombie1, new Point(6,10));
    adversaryLocation.put(this.zombie2, new Point(4,15));

    this.ghostClient.updateActorLocations(this.playerLocations, adversaryLocation, this.ghost1);
    Set<Point> acceptableMoves1 = new HashSet<>();
    acceptableMoves1.add(new Point(13,12));
    acceptableMoves1.add(new Point(13,10));
    assertTrue(acceptableMoves1.contains(this.ghostClient.takeTurn()));

    this.ghostClient.updateActorLocations(this.playerLocations, adversaryLocation, this.ghost2);
    Set<Point> acceptableMoves2 = new HashSet<>();
    acceptableMoves2.add(new Point(16,12));
    acceptableMoves2.add(new Point(15,13));
    assertTrue(acceptableMoves2.contains(this.ghostClient.takeTurn()));
  }

  @Test
  public void testCheckValidMoveValid() {
    Map<Adversary, Point> adversaryLocation = new HashMap<>();
    adversaryLocation.put(this.ghost1, new Point(2,1));
    adversaryLocation.put(this.ghost2, new Point(8,11));
    adversaryLocation.put(this.zombie1, new Point(6,10));
    adversaryLocation.put(this.zombie2, new Point(4,15));

    this.ghostClient.updateActorLocations(this.playerLocations, adversaryLocation, this.ghost1);
    assertTrue(this.ghostClient.checkValidMove(new Point(1, 1)));
    assertTrue(this.ghostClient.checkValidMove(new Point(3, 1)));
    assertTrue(this.ghostClient.checkValidMove(new Point(2, 2)));
    assertTrue(this.ghostClient.checkValidMove(new Point(2, 0)));

    this.ghostClient.updateActorLocations(this.playerLocations, adversaryLocation, this.ghost1);
    assertTrue(this.ghostClient.checkValidMove(new Point(9, 11)));
    assertTrue(this.ghostClient.checkValidMove(new Point(7, 11)));
    assertTrue(this.ghostClient.checkValidMove(new Point(8, 10)));
    assertTrue(this.ghostClient.checkValidMove(new Point(8, 12)));
  }

  @Test
  public void testCheckValidMoveInvalid() {
    Map<Adversary, Point> adversaryLocation = new HashMap<>();
    adversaryLocation.put(this.ghost1, new Point(6,2));
    adversaryLocation.put(this.ghost2, new Point(6,9));
    adversaryLocation.put(this.zombie1, new Point(6,10));
    adversaryLocation.put(this.zombie2, new Point(4,15));

    this.ghostClient.updateActorLocations(this.playerLocations, adversaryLocation, this.ghost1);
    assertFalse(this.ghostClient.checkValidMove(new Point(7, 2)));
    assertFalse(this.ghostClient.checkValidMove(new Point(6, 1)));

    this.ghostClient.updateActorLocations(this.playerLocations, adversaryLocation, this.ghost1);
    assertFalse(this.ghostClient.checkValidMove(new Point(6, 10)));
  }
}