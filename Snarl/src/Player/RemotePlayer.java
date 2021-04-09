package Player;

import java.awt.Point;
import java.util.List;

import Common.Player;
import Game.modelView.PlayerModelView;
import Remote.Server;

/**
 * TODO Add comments
 */
public class RemotePlayer implements Player {
	
	Server server;
	String name;
	
	public RemotePlayer(Server server, String name) {
		this.server = server;
		this.name = name;
	}

	@Override
	public Point takeTurn(List<Point> validMove) {
		return this.server.takeTurn(this.name, validMove);
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
