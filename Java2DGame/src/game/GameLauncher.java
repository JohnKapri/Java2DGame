package game;

import java.applet.Applet;
import java.awt.BorderLayout;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameLauncher extends Applet{

	public static Game game = new Game();
	
	@Override
	public void start() {
		game.start();
	}
	
	@Override
	public void init() {
		setLayout(new BorderLayout());
		add(game, BorderLayout.CENTER);
		this.setMinimumSize(Game.DIMENSIONS);
		this.setMaximumSize(Game.DIMENSIONS);
		this.setPreferredSize(Game.DIMENSIONS);		
	}
	
	@Override
	public void stop() {
		game.stop();
	}
	
	public static void main(String[] args) {
		game.setMinimumSize(Game.DIMENSIONS);
		game.setMaximumSize(Game.DIMENSIONS);
		game.setPreferredSize(Game.DIMENSIONS);

		game.frame = new JFrame(Game.NAME);

		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLayout(new BorderLayout());

		game.frame.add(game, BorderLayout.CENTER);
		game.frame.pack();

		game.frame.setResizable(false);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		game.frame.requestFocus();

		game.start();
		
		game.debug(Game.DebugLevel.INFO, "Game startet");
	}
}
