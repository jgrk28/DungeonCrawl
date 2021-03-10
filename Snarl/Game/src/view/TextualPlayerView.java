package view;

import com.sun.org.apache.xpath.internal.operations.Bool;
import java.io.PrintStream;

import java.util.List;
import modelView.EntityType;
import modelView.PlayerModelView;

public class TextualPlayerView extends AbstractTextualView {
	protected PlayerModelView playerModelView;
	
	public TextualPlayerView(PlayerModelView playerModelView, PrintStream output) {
		super(output);
		this.playerModelView = playerModelView;
	}

	@Override
	public void draw() {
		int currLevelIndex = playerModelView.getCurrentLevel();
		Boolean isAlive = playerModelView.isPlayerAlive();
		StringBuilder output = new StringBuilder();
		output.append("You are currently on level: " + currLevelIndex + "\n");
		if (isAlive) {
			output.append("You are currently active in the level\n");
		} else {
			output.append("You are no longer active in the level\n");
		}
		this.output.print(output.toString());

		List<List<EntityType>> level = playerModelView.getMap();
		drawLevelMap(level);
	}
}
