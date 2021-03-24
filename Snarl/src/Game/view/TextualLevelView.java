package Game.view;

import java.io.PrintStream;
import java.util.List;

import Game.modelView.EntityType;
import Game.modelView.LevelModelView;

/**
 * Represents an ACSII art view of the Level
 * 
 * An textual view of the Level may like this:
 * 
 * XXXX         
 * X..X         
 * X..X         
 * X..X    XXXXX
 * X...****....X
 * XXXX    X...X
 *         X...X
 *         X...X
 *         XXXXX
 *
 * Where each Entity corresponds to the following:
 * - Wall (X)
 * - Space (.)
 * - Hall Space (*)
 * - Key (!)
 * - Exit (@)
 * - Player (P)
 * - Adversary - Ghost (G)
 * - Adversary - Zombie (Z)
 * - Empty - where no entities have been placed  (" ")
 */
public class TextualLevelView extends AbstractTextualView {
	//The read-only version of the Level model
	private LevelModelView modelView;
	
	public TextualLevelView(LevelModelView modelView, PrintStream output) {
		super(output);
		this.modelView = modelView;
	}

	@Override
	public void draw() {
		List<List<EntityType>> level = modelView.getMap();
		this.output.println(drawLevelMap(level));
	}

}
