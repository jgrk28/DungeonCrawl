package Player;

import java.awt.Point;
import java.util.List;

import Common.Player;
import Game.modelView.PlayerModelView;
import Remote.Server;
import java.util.Set;

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
	public void update(PlayerModelView gameState) { this.server.update(this.name, gameState); }

	@Override
	public void displayMessage(String message) {
		// TODO Might want to implement this
		
	}

	@Override
	public void sendLevelStart(int levelIndex, Set<Game.model.Player> levelPlayers) {
		this.server.sendLevelStart(this.name, levelIndex, levelPlayers);
	}

	@Override
	public void sendLevelEnd(Set<Game.model.Player> levelPlayers) {
		this.server.sendLevelEnd(this.name, levelPlayers);
	}

}
