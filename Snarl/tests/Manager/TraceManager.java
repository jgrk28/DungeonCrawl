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
 * TODO Add comments
 */
public class TraceManager extends GameManager {
	
	private JSONArray output;
	
	public TraceManager(JSONArray output) {
		super();
		this.output = output;
	}
	
	/**
	 * TODO Add comments
	 * @param level
	 * @param output
	 */
	public void playLevelTrace(Level level, int maxNumTurns) {
		//While the level has not been won or lost, execute turns for each player
		//and adversary
		int turnCount = 0;
		while (this.ruleChecker.isLevelOver().equals(GameState.ACTIVE)) {
		  if (turnCount == maxNumTurns) {
			  throw new IllegalStateException("Reached the max number of turns");
		  }
	      for (Map.Entry<Player, Common.Player> currPlayer : playerClients.entrySet()) {
	    	
	        PlayerModelView playerModelView = new PlayerModelView(currPlayer.getKey(), this.dungeon);
	    	List<Point> validMoves = playerModelView.getValidMoves();
	    	Point playerSource = playerModelView.getPosition();
	        Point playerDestination = currPlayer.getValue().takeTurn(validMoves);
	        String result;
	        
	        if (this.ruleChecker.checkValidMove(currPlayer.getKey(), playerDestination)) {
	          //Execute the move and corresponding interaction
	          InteractionResult interactionResult = level.playerAction(currPlayer.getKey(), playerDestination);
	          result = interactionResultToString(interactionResult);

	          //Notify all observers of the current game state for each turn
	          notifyAllObservers();
	        } else {
	          //If user entered invalid move notify them and skip their turn
	          currPlayer.getValue().displayMessage("Invalid move, turn skipped");
	          result = "Invalid";
	        }
	       
	        JSONArray traceEntry = new JSONArray();
	        traceEntry.put(playerModelView.getName());
	        traceEntry.put(generateActorMove(playerDestination, playerSource));
	        traceEntry.put(result);
	        this.output.put(traceEntry);	        
	      }
	      
	      turnCount++;

	      //Add adversary turns once we implement the AdversaryClient or at least a stub
	    }
	    //Display to observers when the level ends to provide result
	    notifyAllObservers();	
	}
	
	/**
	 * TODO add comment
	 * @param interactionResult
	 * @return
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
	
	private JSONObject generateActorMove(Point playerDestination, Point playerSource) {
		JSONObject actorMove = new JSONObject();
		actorMove.put("type", "move");
		if (playerSource.equals(playerDestination)) {
			actorMove.put("to", JSONObject.NULL);
		} else {
			actorMove.put("to", ParseUtils.generateJSONPoint(playerDestination));
		}
		return actorMove;
	}

}
