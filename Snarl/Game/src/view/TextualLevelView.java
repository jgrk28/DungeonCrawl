package view;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import modelView.EntityType;
import modelView.LevelModelView;

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
public class TextualLevelView implements LevelView {
	
	//The read-only version of the Level model
	private LevelModelView modelView;
	private PrintStream output;
	
	public TextualLevelView(LevelModelView modelView, PrintStream output) {
		this.modelView = modelView;
		this.output = output;
	}

	@Override
	public void drawLevel() {
		
		ArrayList<ArrayList<EntityType>> level = modelView.getMap();
		StringBuilder output = new StringBuilder();
		
		//Iterate through the level, identify the entity, and append to
		//the output
		for (int i = 0; i < level.size(); i++) {
			ArrayList<EntityType> row = level.get(i);
			for (int j = 0; j < row.size(); j++) {
				String currEntity = drawEntity(row.get(j));
				output.append(currEntity);
			}
			output.append("\n");
		}
		this.output.print(output.toString());
	}
	
	/**
	 * Returns the String representation for a given EntityType
	 * @param entity - the given EntityType from the Level
	 * @return the String corresponding to an EntityType
	 */
	private String drawEntity(EntityType entity) {
		switch (entity) {
			case SPACE: return ".";
			case WALL: return "X";
			case HALL_SPACE: return "*";
			case KEY: return "!";
			case EXIT: return "@";
			case PLAYER: return "P";
			case GHOST: return "G";
			case ZOMBIE: return "Z";
			case EMPTY: return " ";
			default: throw new IllegalArgumentException("Entity type is not valid");
		}
	}

}
