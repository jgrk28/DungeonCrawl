package controller;

import clients.AdversaryClient;
import clients.PlayerClient;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Adversary;
import model.Dungeon;
import model.GameState;
import model.Level;
import model.Player;
import model.RuleChecker;
import model.Zombie;

public class GameManager {
  private Dungeon dungeon;
  private RuleChecker ruleChecker;
  private Map<Player, PlayerClient> playerClients;
  private Map<Adversary, AdversaryClient> adversaryClients;

  public GameManager() {
    this.playerClients = new LinkedHashMap<>();
    this.adversaryClients = new LinkedHashMap<>();
  }

  //This method will need to change once we implement the PlayerClient over TCP
  //This will open sockets and listen for registrations
  public void registerActors(int numPlayers, int numAdversaries) {
    if (numPlayers > 4 || numPlayers < 1) {
      throw new IllegalArgumentException("Illegal number of players");
    }
    for (int i = 0; i < numPlayers; i++) {
      Player player = new Player();
      PlayerClient playerClient = new PlayerClient();
      this.playerClients.put(player, playerClient);
    }

    for (int i = 0; i < numAdversaries; i++) {
      Adversary adversary = new Zombie();
      AdversaryClient adversaryClient = new AdversaryClient();
      this.adversaryClients.put(adversary, adversaryClient);
    }
  }

  public void startGame(List<Level> levels) {
    List<Player> players = new ArrayList<>(this.playerClients.keySet());
    List<Adversary> adversaries = new ArrayList<>(this.adversaryClients.keySet());
    this.dungeon = new Dungeon(players, adversaries, 1, levels);
    this.ruleChecker = this.dungeon;
  }

  public void playGame() {
    Level currLevel = this.dungeon.startCurrentLevel();
    playLevel(currLevel);
    //Figure out how to loop through the levels and end the game
  }

  public void playLevel(Level level) {
    while (level.isLevelOver().equals(GameState.ACTIVE)) {
      for (Map.Entry<Player, PlayerClient> currPlayer : playerClients.entrySet()) {

        Point playerDestination = currPlayer.getValue().takeTurn();
        while (!this.ruleChecker.checkValidMove(currPlayer.getKey(), playerDestination)) {
          //Need to notify the playerClient that their move is invalid
          playerDestination = currPlayer.getValue().takeTurn();
        }
        level.playerAction(currPlayer.getKey(), playerDestination);

        //Need to notify the player of the new gamestate
        //This will send the players location, inLevel, and viewable map
        //This may happen by just updating the player observer instead on the player as the
        //information just needs to get to the user.
        currPlayer.getValue().displayGameState();
        //Will need to add functions in level to get that information
      }

      //Add adversary turns once we implement the AdversaryClient or at least a stub
    }
  }

  public void endGame() {

  }

}
