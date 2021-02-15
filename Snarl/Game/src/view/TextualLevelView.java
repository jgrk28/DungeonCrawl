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
	public String drawLevel() {
		ArrayList<ArrayList<EntityType>> level = modelView.getMap();
		return "";
	}

}
