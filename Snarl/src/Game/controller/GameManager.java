package Game.controller;

import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Adversary.LocalAdversary;
import Common.Observer;
import Game.model.Adversary;
import Game.model.Dungeon;
import Game.model.GameState;
import Game.model.Level;
import Game.model.Player;
import Game.model.RuleChecker;
import Game.model.Zombie;
import Game.modelView.PlayerModelView;
import Game.view.TextualPlayerView;
import Player.LocalPlayer;

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
  private Map<Player, LocalPlayer> playerClients;
  private Map<Adversary, LocalAdversary> adversaryClients;
  
  //All observers in the game to notify when the game state changes
  private List<Observer> observers;

  public GameManager() {
    this.playerClients = new LinkedHashMap<>();
    this.adversaryClients = new LinkedHashMap<>();
    this.observers = new ArrayList<>();
  }
  
  /**
   * TODO Add comments
   * @param name
   */
  public void registerPlayer(String name) {
	  if (playerClients.size() >= 4) {
		  throw new IllegalArgumentException("No more players can be added to the game");
	  }
	  if (!checkUniqueName(name)) {
		  throw new IllegalArgumentException("A unique name must be provided");
	  }
      Player player = new Player(name);
      LocalPlayer playerClient = new LocalPlayer();
      this.playerClients.put(player, playerClient);	  
  }
  
  /**
   * TODO Add comments
   * @param name
   */
  public void registerAdversary(String name) {
	  if (!checkUniqueName(name)) {
		  throw new IllegalArgumentException("A unique name must be provided");
	  }
      Adversary adversary = new Zombie(name);
      LocalAdversary adversaryClient = new LocalAdversary();
      this.adversaryClients.put(adversary, adversaryClient);
  }
  
  private Boolean checkUniqueName(String name) {
	  for (Player player : playerClients.keySet()) {
		  if (player.hasName(name)) {
			  return false;
		  }
	  }	  
	  for (Adversary adversary : adversaryClients.keySet()) {
		  if (adversary.hasName(name)) {
			  return false;
		  }
	  }
	  return true;
  }

  /**
   * Initializes a Dungeon with the provided levels with registered players 
   * and adversaries
   * @param levels - the list of levels that compose a game
   * @throws IllegalArgumentException if no players are registered for the game
   */
  public void startGame(List<Level> levels) {
	if (playerClients.size() < 1) {
		throw new IllegalArgumentException("Cannot start game with no players");
	}
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
      this.dungeon.startCurrentLevel();
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
      for (Map.Entry<Player, LocalPlayer> currPlayer : playerClients.entrySet()) {
        PlayerModelView playerModelView = new PlayerModelView(currPlayer.getKey(), this.dungeon);
    	List<Point> validMoves = playerModelView.getValidMoves();
        Point playerDestination = currPlayer.getValue().takeTurn(validMoves);
        
        //If user entered invalid move notify them and skip their turn
        if (!this.ruleChecker.checkValidMove(currPlayer.getKey(), playerDestination)) {
          //TODO Need to notify the playerClient that their move is invalid
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
    for (Map.Entry<Player, LocalPlayer> currPlayer : playerClients.entrySet()) {

      ByteArrayOutputStream gameState = new ByteArrayOutputStream();
      PrintStream printStream = new PrintStream(gameState);
      PlayerModelView playerModelView = new PlayerModelView(currPlayer.getKey(), this.dungeon);
      TextualPlayerView playerView = new TextualPlayerView(playerModelView, printStream);
      playerView.draw();
      currPlayer.getValue().displayMessage(gameState.toString());
    }
  }

  /**
   * Send results to players after a game has finished
   * @param gameOver - the result of the game
   */
  public void endGame(GameState gameOver) {
    for (Map.Entry<Player, LocalPlayer> currPlayer : playerClients.entrySet()) {
      String gameResult;
      if (gameOver.equals(GameState.WON)) {
        gameResult = "Game is over. You Won!";
      } else if (gameOver.equals(GameState.LOST)) {
        gameResult = "Game is over. You Lost :(";
      } else {
        throw new IllegalArgumentException("Game is not over");
      }
      currPlayer.getValue().displayMessage(gameResult);
    }
  }
  
  /**
   * TODO Add comments
   * @param observer
   */
  //Adds the given Observer to the list of observers
  public void attachObserver(Observer observer) {
	  
  }
 
  /**
   * TODO Add comments
   * @param observer
   */
  //Removes the given Observer from the list of observers
  public void detachObserver(Observer observer) {
	  
  }
  
  /**
   * TODO Add comments
   */
  //Calls update() on all of the observers with the JSONObject from getState
  public void notifyObservers() {
	  
  }

}
