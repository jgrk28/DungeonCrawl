package Manager;

import java.awt.Point;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import Game.controller.GameManager;
import Game.model.GameState;
import Game.model.InteractionResult;
import Game.model.Level;
import Game.model.Player;
import Game.modelView.PlayerModelView;
import Utils.ParseUtils;

/**
 * Represents the TraceManager. Extends the GameManager
 * and serves the purpose of testing game play.
 */
public class TraceManager extends GameManager {
	
	private JSONArray output;
	
	public TraceManager(JSONArray output) {
		super();
		this.output = output;
	}
	
	/**
	 * Plays the level and updates the TestManager with the results of player 
	 * moves
	 * @param level - the level currently being player
	 * @param maxNumTurns - the maximum number of turns for the level
	 */
	public void playLevelTrace(Level level, int maxNumTurns) {
		//While the level has not been won or lost, execute turns for each player
		//and adversary
		int turnCount = 0;
		while (this.ruleChecker.isLevelOver().equals(GameState.ACTIVE)) {
		  //End the loop if the max number of turns has been reached
		  if (turnCount == maxNumTurns) {
			  throw new IllegalStateException("Reached the max number of turns");
		  }
	      for (Map.Entry<Player, Common.Player> currPlayer : playerClients.entrySet()) {
	    	String result;
	    	
	    	PlayerModelView playerModelView = new PlayerModelView(currPlayer.getKey(), this.dungeon);
	    	List<Point> validMoves = playerModelView.getValidMoves();
	    	Point playerSource = playerModelView.getPosition();
	    	Point playerDestination = currPlayer.getValue().takeTurn(validMoves);

					JSONArray traceEntry = new JSONArray();
					traceEntry.put(playerModelView.getName());
					traceEntry.put(generateActorMove(playerDestination, playerSource));

	        if (this.ruleChecker.checkValidMove(currPlayer.getKey(), playerDestination)) {
	          //Execute the move and corresponding interaction
	          InteractionResult interactionResult = level.playerAction(currPlayer.getKey(), playerDestination);
	          result = interactionResultToString(interactionResult);

						traceEntry.put(result);
						this.output.put(traceEntry);

	          //Notify all observers of the current game state for each turn
	          notifyAllObservers();
	        } else {
	          //If user entered invalid move notify them and skip their turn
	          currPlayer.getValue().displayMessage("Invalid move, turn skipped");
	          result = "Invalid";
						traceEntry.put(result);
						this.output.put(traceEntry);
	        }
	      }
	      
	      turnCount++;

	      //Add adversary turns once we implement the AdversaryClient or at least a stub
	    }
	    //Display to observers when the level ends to provide result
	    notifyAllObservers();	
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
	 * Generates the JSON for a player's move based on their source and destination
	 * @param playerDestination - the location the player is moving to
	 * @param playerSource - the location the player is moving from
	 * @return a JSONObject representing the move of the player
	 */
	private JSONObject generateActorMove(Point playerDestination, Point playerSource) {
		JSONObject actorMove = new JSONObject();
		actorMove.put("type", "move");
		
		//If the player has chosen to skip their turn, the "to" is null
		if (playerSource.equals(playerDestination)) {
			actorMove.put("to", JSONObject.NULL);
		} else {
			actorMove.put("to", JSONUtils.Generator.generateJSONPoint(playerDestination));
		}
		return actorMove;
	}

}
