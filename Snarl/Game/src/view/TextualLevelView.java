package view;

import java.util.ArrayList;
import java.util.List;

import modelView.EntityType;
import modelView.LevelModelView;

public class TextualLevelView implements LevelView {
	
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
