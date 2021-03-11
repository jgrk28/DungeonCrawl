package Game.controller;

import Game.clients.AdversaryClient;
import Game.clients.PlayerClient;
import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import Game.model.Adversary;
import Game.model.Dungeon;
import Game.model.GameState;
import Game.model.Level;
import Game.model.Player;
import Game.model.RuleChecker;
import Game.model.Zombie;
import Game.modelView.PlayerModelView;
import Game.view.TextualPlayerView;

/**
 * The GameManager acts as the controller for our MVC design. It communicates 
 * between the model and the views. It has an instance of the model, and calls
 * into it to change the game state.
 */
public class GameManager {
  //The dungeon that represents the game and the corresponding ruleChecker
  private Dungeon dungeon;
  private RuleChecker ruleChecker;
  
  //Player and adversary Game.clients in the game
  private Map<Player, PlayerClient> playerClients;
  private Map<Adversary, AdversaryClient> adversaryClients;

  public GameManager() {
    this.playerClients = new LinkedHashMap<>();
    this.adversaryClients = new LinkedHashMap<>();
  }

  /**
   * Accept registrations for new players and adversaries and fills in Client maps
   * @param numPlayers - the number of players in the game
   * @param numAdversaries - the number of adversaries in the game
   */
  //This method will need to change once we implement the PlayerClient over TCP
  //This will open sockets and listen for registrations
  public void registerActors(int numPlayers, int numAdversaries) {
	//A game must have between 1 and 4 players
    if (numPlayers > 4 || numPlayers < 1) {
      throw new IllegalArgumentException("Illegal number of players");
    }
    
    //Add each player and playerClient to the map
    for (int i = 0; i < numPlayers; i++) {
      Player player = new Player();
      PlayerClient playerClient = new PlayerClient();
      this.playerClients.put(player, playerClient);
    }

    //Add each adversary and adversaryClient to the map
    for (int i = 0; i < numAdversaries; i++) {
      Adversary adversary = new Zombie();
      AdversaryClient adversaryClient = new AdversaryClient();
      this.adversaryClients.put(adversary, adversaryClient);
    }
  }

  /**
   * Initializes a Dungeon with the provided levels with registered players 
   * and adversaries
   * @param levels - the list of levels that compose a game
   */
  public void startGame(List<Level> levels) {
    List<Player> players = new ArrayList<>(this.playerClients.keySet());
    List<Adversary> adversaries = new ArrayList<>(this.adversaryClients.keySet());
    this.dungeon = new Dungeon(players, adversaries, 1, levels);
    this.ruleChecker = this.dungeon;
  }

  /**
   * Manages levels within the dungeon. Starts each level and manages the result
   * once the level ends. If the level was won, proceed to the next level. Otherwise, 
   * end the game
   */
  public void playGame() {
    Level currLevel = this.dungeon.startCurrentLevel();
    playLevel(currLevel);
    
    //If the first level was won, play all remaining levels in the game
    while (this.dungeon.isGameOver().equals(GameState.ACTIVE)) {
      currLevel = this.dungeon.getNextLevel();
      playLevel(currLevel);
    }
    endGame(this.dungeon.isGameOver());
  }

  /**
   * Manage turns for players and adversaries until the level is won or lost
   * @param level - the level being played
   */
  public void playLevel(Level level) {
	//While the level has not been won or lost, execute turns for each player
	//and adversary
    while (level.isLevelOver().equals(GameState.ACTIVE)) {
      for (Map.Entry<Player, PlayerClient> currPlayer : playerClients.entrySet()) {
        Point playerDestination = currPlayer.getValue().takeTurn();
        
        //Keep prompting the user to take a turn if the given turn is not valid
        while (!this.ruleChecker.checkValidMove(currPlayer.getKey(), playerDestination)) {
          //TODO Need to notify the playerClient that their move is invalid
          playerDestination = currPlayer.getValue().takeTurn();
        }
        //Execute the move and corresponding interaction
        level.playerAction(currPlayer.getKey(), playerDestination);

        //Notify all players of the current game state for each turn
        notifyAllPlayers();
      }

      //Add adversary turns once we implement the AdversaryClient or at least a stub
    }
    //We may want to add display to user when the level ends to provide result
  }
  
  /**
   * Notifies each player of the new game state
   */
  private void notifyAllPlayers() {
    for (Map.Entry<Player, PlayerClient> currPlayer : playerClients.entrySet()) {

      ByteArrayOutputStream gameState = new ByteArrayOutputStream();
      PrintStream printStream = new PrintStream(gameState);
      PlayerModelView playerModelView = new PlayerModelView(currPlayer.getKey(), this.dungeon);
      TextualPlayerView playerView = new TextualPlayerView(playerModelView, printStream);
      playerView.draw();
      currPlayer.getValue().displayGameState(gameState.toString());
    }
  }

  /**
   * Send results to players after a game has finished
   * @param gameOver - the result of the game
   */
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
