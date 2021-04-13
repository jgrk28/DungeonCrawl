package Game.controller;

import Common.AdversaryClient;
import Game.model.InteractionResult;
import Game.modelView.DungeonModelView;
import Game.view.TextualDungeonView;
import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Adversary.LocalGhost;
import Adversary.LocalZombie;
import Common.Observer;
import Game.model.Adversary;
import Game.model.Dungeon;
import Game.model.GameState;
import Game.model.Ghost;
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
		  throw new IllegalStateException("No more players can be added to the game");
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
   * Registers a specified number of Zombies and Ghosts with the GameManager.
   * Creates a LocalZombie and LocalGhost for each corresponding Zombie and
   * Ghost
   * @param numZombies - the number of Zombies to register
   * @param numGhosts - the number of Ghosts to register
   */
  public void registerAdversaries(int numZombies, int numGhosts) {
	  for (int i = 0; i < numZombies; i++) {
		  Adversary zombie = new Zombie();
		  LocalZombie zombieClient = new LocalZombie();
		  this.adversaryClients.put(zombie, zombieClient);		  
	  }  
	  
	  for (int i = 0; i < numGhosts; i++) {
		  Adversary ghost = new Ghost();
		  LocalGhost ghostClient = new LocalGhost();
		  this.adversaryClients.put(ghost, ghostClient);	  
	  } 
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
    this.dungeon = new Dungeon(players, 1, levels);
    this.ruleChecker = this.dungeon;

    sendAdversariesLevel(this.dungeon.getCurrentLevel());
    this.dungeon.startCurrentLevel(adversaries);
    sendLevelStart();
    notifyAllObservers(null);
  }
  
  /**
   * Initializes a Dungeon with the provided levels, with the specified start
   * level as the current level. Sends the current level to all adversaries,
   * generates the ghosts and adversaries specific to the level, and starts
   * the level by randomly placing the players and adversaries
   * @param levels - the list of levels that compose a game
   * @param startLevel - the index of the level to start the game with
   * @throws IllegalArgumentException if no players are registered for the game
   */
  public void startGame(List<Level> levels, int startLevel) {
	if (playerClients.size() < 1) {
		throw new IllegalArgumentException("Cannot start game with no players");
	}
    List<Player> players = new ArrayList<>(this.playerClients.keySet());
    
    this.dungeon = new Dungeon(players, startLevel, levels);
    this.ruleChecker = this.dungeon;

    sendAdversariesLevel(this.dungeon.getCurrentLevel());
    List<Adversary> adversaries = getLevelAdversaries(startLevel);
    this.dungeon.startCurrentLevelRandom(adversaries);
    sendLevelStart();
    notifyAllObservers(null);
  }

  /**
   * Gets all adversaries that will be part of the current level.
   * The number of Ghosts and Zombies is based on the index of
   * the current level
   * @param levelNum - the index of the current level
   * @return the list of all Adversaries in the current level
   */
  private List<Adversary> getLevelAdversaries(int levelNum) {
    int numZombies = Math.floorDiv(levelNum, 2) + 1;
    int numGhosts = Math.floorDiv((levelNum - 1), 2);

    List<Adversary> adversaries = new ArrayList<>();

    for (Adversary adversary : this.adversaryClients.keySet()) {
      if (adversary instanceof Zombie && numZombies > 0) {
        adversaries.add(adversary);
        numZombies--;
      }
      if (adversary instanceof Ghost && numGhosts > 0) {
        adversaries.add(adversary);
        numGhosts--;
      }
    }
    return adversaries;
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
    this.dungeon = new Dungeon(players, 1, levels);
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
      int levelNum = this.dungeon.getCurrentLevelIndex();
      List<Adversary> adversaries = getLevelAdversaries(levelNum);
      this.dungeon.startCurrentLevelRandom(adversaries);
      notifyAllObservers(null);
      playLevel(currLevel);
    }
  }

  /**
   * Manage turns for players and adversaries until the level is won or lost
   * @param level - the level being played
   */
  public void playLevel(Level level) {
	  //While the level has not been won or lost, execute turns for each player
	  //and adversary
    while (this.ruleChecker.isLevelOver().equals(GameState.ACTIVE)) {
      //Player turns
      processPlayerTurns(level);
      //Adversary turns
      processAdversaryTurns(level);
    }
    sendLevelEnd();
  }
  
  /**
   * Processes the turns for all players in the level
   * @param level - the level being played
   * @throws IllegalArgumentException if the player's turn is invalid
   */
  private void processPlayerTurns(Level level) {
	  int maxAttempts = 3;
	  
	  for (Map.Entry<Player, Common.Player> currPlayer : playerClients.entrySet()) {
          Player player = currPlayer.getKey();
          Common.Player client = currPlayer.getValue();
          
          //Check that the player is active in the level
          if (!level.getActivePlayers().containsKey(player)) {
            continue;
          }
          PlayerModelView playerModelView = new PlayerModelView(player, this.dungeon);
      	  List<Point> validMoves = playerModelView.getValidMoves();
          Point playerDestination = client.takeTurn(validMoves);
          
          for (int i = 0; i < maxAttempts; i++ ) {
              
        	  if (playerDestination == null) {
            	  playerDestination = playerModelView.getPosition();
              }
              
        	  if (this.ruleChecker.checkValidMove(player, playerDestination)) {
        		  //Execute the move and corresponding interaction
        		  InteractionResult interactionResult = level.playerAction(player, playerDestination);
        		  String message = processResult(interactionResult, player, client);
        		  String result = interactionResultToString(interactionResult);
        		  client.displayMessage(result);

        		  //Notify all observers of the current game state for each turn
        		  notifyAllObservers(message);
        		  
        		  break;
        	  } else {
        		  //If user entered invalid move notify them and skip their turn
        		  client.displayMessage("Invalid");
        		  playerDestination = client.takeTurn(validMoves);
        	  }
          }
         if (!this.ruleChecker.isLevelOver().equals(GameState.ACTIVE)) {
        	 break;
         }
	  }
  }
  
	/**
	 * Converts an InteractionResult to the corresponding string value for the JSON output
	 * @param interactionResult - the result of a move
	 * @return a string representing the result of a move
	 */
	private String interactionResultToString(InteractionResult interactionResult) {
		switch (interactionResult) {
			case NONE:
				return "OK";
			case FOUND_KEY:
				return "Key";
			case EXIT:
				return "Exit";
			case REMOVE_PLAYER:
				return "Eject";
			default:
				throw new IllegalArgumentException("Invalid interaction result type");
		}
	}
  
  /**
   * Processes the turns for all adversaries in the level
   * @param level - the level being played
   * @throws IllegalArgumentException if the actor's turn is invalid
   */
  private void processAdversaryTurns(Level level) {
	  for (Map.Entry<Adversary, AdversaryClient> currAdversary : adversaryClients.entrySet()) {
          if (!this.ruleChecker.isLevelOver().equals(GameState.ACTIVE)) {
          	 break;
           }
          
          Map<Player, Point> players = level.getActivePlayers();
          Map<Adversary, Point> adversaries = level.getActiveAdversaries();
          AdversaryClient client = currAdversary.getValue();
          Adversary adversary = currAdversary.getKey();
          
          //Check that the adversary is active in the level
          if (!adversaries.containsKey(adversary)) {
            continue;
          }
          client.updateActorLocations(players, adversaries, adversary);

          Point adversaryDestination = client.takeTurn();

          if (this.ruleChecker.checkValidMove(adversary, adversaryDestination)) {
            //Execute the move and corresponding interaction
        	
            InteractionResult result = level.adversaryAction(adversary, adversaryDestination);
            
            String message = null;
            
            //If a player is removed, update the PlayerClient
            if (result.equals(InteractionResult.REMOVE_PLAYER)) {
            	Map<Player, Point> activePlayersAfterMove = level.getActivePlayers();
            	Set<Player> allActivePlayers = players.keySet();   	
            	allActivePlayers.removeAll(activePlayersAfterMove.keySet());
            	for (Player player : allActivePlayers) {
            		message = processResult(result, player, this.playerClients.get(player));
            	}
            }

            //Notify all observers of the current game state for each turn
            notifyAllObservers(message);
          } else {
            //If adversary is not a local implementation we will need to handle invalid moves
            throw new IllegalArgumentException("Invalid adversary moves");
          }
        }	  
  }

  /**
   * Processes the result of the player's action and displays the corresponding message
   * @param result - the result of a player's action
   * @param player - the player's avatar 
   * @param client - the client on the user's side
   */
  private String processResult(InteractionResult result, Player player, Common.Player client) {
    switch (result) {
      case FOUND_KEY:
        player.foundKey();
        return "Player " + player.getName() + " found the key.\n";
      case REMOVE_PLAYER:
        return "Player " + player.getName() + " was expelled.\n";
      case EXIT:
        player.exited();
        return "Player " + player.getName() + " exited.\n";
	default:
		return null;
    }
  }

  /**
   * Displays the number of times a player successfully exited and the number of keys they found. 
   * If there is more than one player, rank the players by the number of times they exited (most 
   * to least), followed by the number of keys (most to least)
   */
  @SuppressWarnings("unchecked")
public void endGame() {
    List<Player> orderedPlayers = new ArrayList<>(this.playerClients.keySet());

    //Compare all players by the number of times they exited and the number of keys they found
    //Resource: https://stackoverflow.com/questions/4805606/how-to-sort-by-two-fields-in-java
    Collections.sort(orderedPlayers, new Comparator() {
      public int compare(Object o1, Object o2) {
        Player player1 = (Player) o1;
        Player player2 = (Player) o2;

        int exitsComp = Integer.compare(player1.getNumExits(), player2.getNumExits());
        if (exitsComp != 0) {
          return exitsComp;
        }

        return Integer.compare(player1.getKeysFound(), player2.getKeysFound());
      }});

    StringBuilder output = new StringBuilder();
    output.append("Player Rankings:\n");
    for (Player player : orderedPlayers) {
      output.append(player.getName() + ": Exited " + player.getNumExits() + " times. " +
          "Found " + player.getKeysFound() + " keys.\n");
    }
    output.append("\n");

    //Output the player rankings to all observers and players
    String stringOut = output.toString();
    for (Observer observer : this.observers) {
      observer.update(stringOut);
    }

    for (Common.Player client : playerClients.values()) {
      client.displayMessage(stringOut);
    }
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
  public void notifyAllObservers(String message) {
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
		  currPlayer.getValue().update(playerModelView, message);
    }
  }

  /**
   * TODO
   */
  private void sendLevelStart() {
    for (Map.Entry<Player, Common.Player> currPlayer : playerClients.entrySet()) {
      Common.Player playerClient = currPlayer.getValue();
      int levelIndex = this.dungeon.getCurrentLevelIndex();
      Set<String> nameList = new HashSet<>();
      for (Player player : playerClients.keySet()) {
        nameList.add(player.getName());
      }
      playerClient.sendLevelStart(levelIndex, nameList);
    }
  }

  /**
   * TODO
   */
  private void sendLevelEnd() {
    for (Map.Entry<Player, Common.Player> currPlayer : playerClients.entrySet()) {
      Common.Player playerClient = currPlayer.getValue();
      playerClient.sendLevelEnd(playerClients.keySet());
    }
  }

  /**
   * Sends all AdversaryClients in the current level the corresponding level
   * information
   * @param level - the level to send
   */
  private void sendAdversariesLevel(Level level) {
    for (AdversaryClient client : adversaryClients.values()) {
      client.getLevelStart(level);
    }
  }

}
