package game;

import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class InputHandler implements KeyListener {

	private Game game;
	private List<Key> keys = new ArrayList<Key>();
	private List<GameActionListener> listeners = new ArrayList<GameActionListener>();
	private boolean actionPerformed;

	public InputHandler(Game game) {
		game.addKeyListener(this);
		this.game = game;
	}

	public void tick() {
		for (Key k : keys) {
			k.update();
		}
		if (actionPerformed) {
			for(int i = 0; i < listeners.size(); i++) {
				listeners.get(i).actionPerformed(this);
			}
			actionPerformed = false;
		}
	}

	public class Key {
		public Key(InputHandler input) {
			input.addKey(this);
		}

		private boolean isPressed;
		private boolean lastState;
		private boolean gotPressed;

		public void toggel(boolean pressed) {
			this.isPressed = pressed;
		}

		public void update() {
			if (lastState != isPressed && isPressed == true) {
				gotPressed = true;
				lastState = isPressed;
			} else {
				gotPressed = false;
				lastState = isPressed;
			}
		}

		public boolean isPressed() {
			return isPressed;
		}

		public boolean gotPressed() {
			return gotPressed;
		}
	}

	public interface GameActionListener {
		public abstract void actionPerformed(InputHandler input);
	}

	public Key up = new Key(this);
	public Key down = new Key(this);
	public Key left = new Key(this);
	public Key right = new Key(this);
	public Key action = new Key(this);
	public Key esc = new Key(this);
	public Key mouseLeft = new Key(this);
	public Key mouseRight = new Key(this);

	public void keyPressed(KeyEvent arg0) {
		toggleKey(arg0.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent arg0) {
		toggleKey(arg0.getKeyCode(), false);
	}

	public void keyTyped(KeyEvent arg0) {

	}

	public void toggleKey(int keyCode, boolean pressed) {
		actionPerformed = true;

		if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
			up.toggel(pressed);
		}
		if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
			down.toggel(pressed);
		}
		if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
			left.toggel(pressed);
		}
		if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
			right.toggel(pressed);
		}
		if (keyCode == KeyEvent.VK_E) {
			action.toggel(pressed);
		}
		if (keyCode == KeyEvent.VK_ESCAPE) {
			esc.toggel(pressed);
		}
		if (keyCode == MouseEvent.BUTTON1) {
			mouseLeft.toggel(pressed);
		}
		if (keyCode == MouseEvent.BUTTON2) {
			mouseRight.toggel(pressed);
		}
	}

	public void addListener(GameActionListener listener) {
		listeners.add(listener);
	}

	public void removeListener(GameActionListener listener) {
		listeners.remove(listener);
	}

	private void addKey(Key key) {
		keys.add(key);
	}

	public int getMouseX() {
		PointerInfo pointer = MouseInfo.getPointerInfo();
		int x = pointer.getLocation().x - game.frame.getBounds().x - 3;
		x = x / Game.SCALE;
		if (x < 0 || x > game.getWidth() / Game.SCALE) {
			x = -1;
		}
		return x;
	}

	public int getMouseY() {
		PointerInfo pointer = MouseInfo.getPointerInfo();
		int y = pointer.getLocation().y - game.frame.getBounds().y - 24;
		y = y / Game.SCALE;
		if (y < 0 || y > game.getHeight() / Game.SCALE) {
			y = -1;
		}
		return y;
	}
}
