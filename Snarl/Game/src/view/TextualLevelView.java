package view;

import java.util.ArrayList;

import modelView.EntityType;
import modelView.LevelModelView;

/**
 * Represents an ACSII art view of the Level
 */
public class TextualLevelView implements LevelView {
	
	//The read-only version of the Level model
	private LevelModelView modelView;
	
	public TextualLevelView(LevelModelView modelView) {
		this.modelView = modelView;
	}

	@Override
	public void drawLevel() {
		
		ArrayList<ArrayList<EntityType>> level = modelView.getMap();
		StringBuilder output = new StringBuilder();
		
		for (int i = 0; i < level.size(); i++) {
			ArrayList<EntityType> row = level.get(i);
			for (int j = 0; j < row.size(); j++) {
				String currEntity = drawEntity(row.get(j));
				output.append(currEntity);
			}
			output.append("\n");
		}
		System.out.print(output.toString());
	}
	
	private String drawEntity(EntityType entity) {
		switch (entity) {
			case SPACE: return ".";
			case WALL: return "X";
			case HALL_SPACE: return "*";
			case EMPTY: return " ";
			default: throw new IllegalArgumentException("Entity type is not valid");
		}
	}

}
