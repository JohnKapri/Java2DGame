package game;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;

public class GameLauncher extends Applet {

	private static final long serialVersionUID = 1L;
	
	public static Game game = new Game();

	@Override
	public void start() {
		game.start();
	}

	@Override
	public void init() {
		this.setMinimumSize(Game.DIMENSIONS);
		this.setMaximumSize(Game.DIMENSIONS);
		this.setPreferredSize(Game.DIMENSIONS);
		setLayout(new BorderLayout());
		add(game, BorderLayout.CENTER);

		Game.homeDir = System.getProperty("user.home") + File.separator
				+ ".graverobber" + File.separator;
		Game.isApplet = true;
		
		Game.instance = game;
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
		
		game.requestFocus();

		Game.homeDir = System.getProperty("user.home") + File.separator
				+ ".graverobber" + File.separator;

		game.start();
		
		Game.instance = game;

		// System.out.println(GlobalBounds.doBoundsCollide(new GlobalBounds(140,
		// 100, 8), new GlobalBounds(148, 100, 8)));

		Game.debug(Game.DebugLevel.INFO, "Game startet");
	}
}
