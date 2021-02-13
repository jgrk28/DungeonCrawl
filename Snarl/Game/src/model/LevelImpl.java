package model;

import java.util.List;

import modelView.EntityType;

public class LevelImpl implements Level {
	
	private List<LevelComponent> levelMap;
	
	LevelImpl(List<LevelComponent> levelMap) {
		this.levelMap = levelMap;
	}

	@Override
	public List<List<EntityType>> getMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
