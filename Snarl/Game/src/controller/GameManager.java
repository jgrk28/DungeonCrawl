package controller;

import clients.AdversaryClient;
import clients.PlayerClient;
import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
import modelView.PlayerModelView;
import view.TextualPlayerView;

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
    while (this.dungeon.isGameOver().equals(GameState.ACTIVE)) {
      currLevel = this.dungeon.getNextLevel();
      playLevel(currLevel);
    }
    endGame(this.dungeon.isGameOver());
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

        notifyAllPlayers();
      }

      //Add adversary turns once we implement the AdversaryClient or at least a stub
    }
    //We may want to add display to user when the level ends to give info
  }

  private void notifyAllPlayers() {
    for (Map.Entry<Player, PlayerClient> currPlayer : playerClients.entrySet()) {
      //This will notify the player of the new gamestate
      //This may happen by just updating the player observer instead on the player as the
      //information just needs to get to the user.
      ByteArrayOutputStream gameState = new ByteArrayOutputStream();
      PrintStream printStream = new PrintStream(gameState);
      PlayerModelView playerModelView = new PlayerModelView(currPlayer.getKey(), this.dungeon);
      TextualPlayerView playerView = new TextualPlayerView(playerModelView, printStream);
      playerView.draw();

      currPlayer.getValue().displayGameState(gameState.toString());
    }
  }

  public void endGame(GameState gameOver) {
    for (Map.Entry<Player, PlayerClient> currPlayer : playerClients.entrySet()) {
      String gameResult;
      if (gameOver.equals(GameState.WON)) {
        gameResult = "Game is over. You Won!";
      } else if (gameOver.equals(GameState.LOST)) {
        gameResult = "Game is over. You Lost :(";
      } else {
        throw new IllegalArgumentException("Game is not over");
      }

      currPlayer.getValue().displayGameState(gameResult);
    }
  }

}
