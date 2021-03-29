package Game.controller;

import Common.AdversaryClient;
import Game.model.Actor;
import Game.modelView.DungeonModelView;
import Game.view.TextualDungeonView;
import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Adversary.LocalZombie;
import Common.Observer;
import Game.model.Adversary;
import Game.model.Dungeon;
import Game.model.GameState;
import Game.model.Level;
import Game.model.Player;
import Game.model.RuleChecker;
import Game.model.Zombie;
import Game.modelView.PlayerModelView;

/**
 * The GameManager acts as the controller for our MVC design. It communicates 
 * between the model and the views. It has an instance of the model, and calls
 * into it to change the game state.
 */
public class GameManager {
  //The dungeon that represents the game and the corresponding ruleChecker
  protected Dungeon dungeon;
  protected RuleChecker ruleChecker;
  
  //Player and adversary clients in the game
  protected Map<Player, Common.Player> playerClients;
  private Map<Adversary, AdversaryClient> adversaryClients;
  
  //All observers in the game to notify when the game state changes
  private List<Observer> observers;

  //Constructs the game manager with empty clients and observer fields
  public GameManager() {
    this.playerClients = new LinkedHashMap<>();
    this.adversaryClients = new LinkedHashMap<>();
    this.observers = new ArrayList<>();
  }
  
  /**
   * Registers a player with the GameManager
   * @param name - the name of the player
   * @param playerClient - the client associated with this player that will be
   * inputting moves
   */
  public void registerPlayer(String name, Common.Player playerClient) {
	  if (playerClients.size() >= 4) {
		  throw new IllegalArgumentException("No more players can be added to the game");
	  }
	  if (!checkUniqueName(name)) {
		  throw new IllegalArgumentException("A unique name must be provided");
	  }
      Player player = new Player(name);
      this.playerClients.put(player, playerClient);	  
  }
  
  /**
   * Registers an adversary with the GameManager
   * @param name - the name of the adversary
   */
  public void registerAdversary(String name) {
	  if (!checkUniqueName(name)) {
		  throw new IllegalArgumentException("A unique name must be provided");
	  }
      Adversary adversary = new Zombie(name);
      LocalZombie adversaryClient = new LocalZombie();
      this.adversaryClients.put(adversary, adversaryClient);
  }
  
  /**
   * Checks that the name is unique based on existing players and adversaries in
   * the game
   * @param name - the name to check
   * @return true if the name is unique
   */
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
   * and adversaries, starts the first level and notifies all observers
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

    sendAdversariesLevel(this.dungeon.getCurrentLevel());
    this.dungeon.startCurrentLevel();
    notifyAllObservers();
  }

  /**
   * Initializes a Dungeon with the provided levels with registered players
   * and adversaries
   * @param levels - the list of levels that compose a game
   * @throws IllegalArgumentException if no players are registered for the game
   */
  public void initDungeon(List<Level> levels) {
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
    Level currLevel = this.dungeon.getCurrentLevel();
    playLevel(currLevel);
    //While the game is still active, play all remaining levels in the game
    while (this.ruleChecker.isGameOver().equals(GameState.ACTIVE)) {
      currLevel = this.dungeon.getNextLevel();
      sendAdversariesLevel(currLevel);
      this.dungeon.startCurrentLevel();
      playLevel(currLevel);
    }
    //When the game is over notify all observers of the end state
    notifyAllObservers();
  }

  /**
   * Manage turns for players and adversaries until the level is won or lost
   * @param level - the level being played
   */
  public void playLevel(Level level) {
	  //While the level has not been won or lost, execute turns for each player
	  //and adversary
    while (this.ruleChecker.isLevelOver().equals(GameState.ACTIVE)) {
      for (Map.Entry<Player, Common.Player> currPlayer : playerClients.entrySet()) {
        PlayerModelView playerModelView = new PlayerModelView(currPlayer.getKey(), this.dungeon);
    	  List<Point> validMoves = playerModelView.getValidMoves();
        Point playerDestination = currPlayer.getValue().takeTurn(validMoves);

        if (this.ruleChecker.checkValidMove(currPlayer.getKey(), playerDestination)) {
          //Execute the move and corresponding interaction
          level.playerAction(currPlayer.getKey(), playerDestination);

          //Notify all observers of the current game state for each turn
          notifyAllObservers();
        } else {
          //If user entered invalid move notify them and skip their turn
          currPlayer.getValue().displayMessage("Invalid move, turn skipped");
        }
      }
      //Adversary turns
      for (Map.Entry<Adversary, AdversaryClient> currAdversary : adversaryClients.entrySet()) {
        Map<Actor, Point> players = level.getActivePlayers();
        Map<Actor, Point> adversaries = level.getActiveAdversaries();
        AdversaryClient client = currAdversary.getValue();
        Adversary adversary = currAdversary.getKey();
        client.updateActorLocations(players, adversaries, adversary);

        Point adversaryDestination = client.takeTurn();

        if (this.ruleChecker.checkValidMove(adversary, adversaryDestination)) {
          //Execute the move and corresponding interaction
          level.adversaryAction(adversary, adversaryDestination);

          //Notify all observers of the current game state for each turn
          notifyAllObservers();
        } else {
          //If user entered invalid move notify them and skip their turn
          //TODO adversary skipped
        }

      }
    }
    //Display to observers when the level ends to provide result
    notifyAllObservers();
  }
  
  /**
   * Adds the given Observer to the list of observers
   * @param observer - the observer to add
   */
  public void attachObserver(Observer observer) {
    this.observers.add(observer);
  }
 
  /**
   * Removes the given Observer from the list of observers
   * @param observer - the observer to remove
   */
  public void detachObserver(Observer observer) {
    this.observers.remove(observer);
  }
  
  /**
   * Calls update() on all of the observers. This includes observer components,
   * as well as players. The players are updated of the game state relative to
   * their view, whereas the observer can see the entire game
   */
  public void notifyAllObservers() {
	  for (Observer observer : this.observers) {
		  ByteArrayOutputStream gameState = new ByteArrayOutputStream();
		  PrintStream printStream = new PrintStream(gameState);
		  DungeonModelView dungeonModelView = this.dungeon;
		  TextualDungeonView dungeonView = new TextualDungeonView(dungeonModelView, printStream);
		  dungeonView.draw();
		  observer.update(gameState.toString());
	  }

	  for (Map.Entry<Player, Common.Player> currPlayer : playerClients.entrySet()) {
		  PlayerModelView playerModelView = new PlayerModelView(currPlayer.getKey(), this.dungeon);
		  currPlayer.getValue().update(playerModelView);
    }
  }

  /**
   * TODO comment
   * @param level
   */
  private void sendAdversariesLevel(Level level) {
    for (AdversaryClient client : adversaryClients.values()) {
      client.getLevelStart(level);
    }
  }

}
