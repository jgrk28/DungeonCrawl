package Manager;

import java.awt.Point;
import java.util.List;

import org.json.JSONArray;

import Common.Player;
import Game.modelView.PlayerModelView;

/**
 * TODO Add comments
 */
public class TestPlayer implements Player {
	
	private List<Point> moves;
	private JSONArray output;
	
	public TestPlayer(List<Point> moves, JSONArray output) {
		this.moves = moves;
		this.output = output;
	}

	@Override
	public Point takeTurn(List<Point> validMove) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(PlayerModelView gameState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayMessage(String message) {
		// TODO Auto-generated method stub
		
	}

}
