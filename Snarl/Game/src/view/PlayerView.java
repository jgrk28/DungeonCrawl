package view;

import java.io.PrintStream;

import modelView.PlayerModelView;

public class PlayerView implements LevelView {
	
	private PlayerModelView playerView;
	private PrintStream output;
	
	public PlayerView(PlayerModelView playerView, PrintStream output) {
		this.playerView = playerView;
		this.output = output;
	}

	@Override
	public void drawLevel() {
		// TODO Auto-generated method stub
		
	}
	
	

}
