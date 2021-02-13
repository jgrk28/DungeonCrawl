package view;

import java.util.List;

import modelView.EntityType;
import modelView.LevelModelView;

public class TextualLevelView implements LevelView {
	
	private LevelModelView modelView;
	
	TextualLevelView(LevelModelView modelView) {
		this.modelView = modelView;
	}

	@Override
	public void drawLevel() {
		List<List<EntityType>> level = modelView.getMap();
		//Draw the level to STDOUT
	}

}

//Create level with random things, level components, connect them together
//Create LevelView and pass in Model as modelView
//Call drawLevel and check if the output reflects the input
//X for wall, . for space, | for door