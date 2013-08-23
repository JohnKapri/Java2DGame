package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class InputHandler implements KeyListener {

	public Key[] keys = new Key[6];
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key action = new Key();
	public Key esc = new Key();

	private List<GameActionListener> listeners = new ArrayList<GameActionListener>();
	private List<GameActionListener> newListeners = new ArrayList<GameActionListener>();

	private boolean actionPerformed;

	public InputHandler(Game game) {
		game.addKeyListener(this);
	}

	public void tick() {
		for (int i = 0; i < keys.length; i++) {
			if (keys[i] != null) {
				keys[i].update();
			}
		}
		if (actionPerformed) {
			notifyListeners();
			actionPerformed = false;
		}
	}

	private void notifyListeners() {
		for (int j = 0; j < keys.length; j++) {
			if (keys[j].gotPressed()) {
				for (int i = 0; i < listeners.size(); i++) {
					if(!newListeners.contains(listeners.get(i))) {
					listeners.get(i).actionPerformed(
							new InputEvent(keys[j], InputEventType.PRESSED));
					} else {
						newListeners.remove(listeners.get(i));
					}
				}
			}
		}
	}

	public class Key {

		public int id;
		boolean isPressed;
		private boolean lastState;
		private boolean gotPressed;

		public Key() {
			for (int i = 0; i < keys.length; i++) {
				if (keys[i] == null) {
					this.id = i;
					keys[i] = this;
					break;
				}
			}
		}

		public void toggel(boolean pressed) {
			isPressed = pressed;
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

	public enum InputEventType {
		PRESSED, RELEASED;
	}

	public class InputEvent {
		public InputEventType type;
		public Key key;

		public InputEvent(Key key, InputEventType type) {
			this.key = key;
			this.type = type;
		}
	}

	public interface GameActionListener {
		public abstract void actionPerformed(InputEvent event);
	}

	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
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
	}

	public void keyTyped(KeyEvent arg0) {
	}

	public void addListener(GameActionListener listener) {
		newListeners.add(listener);
		listeners.add(listener);
		// listeners.add(0, listener);
	}

	public void removeListener(GameActionListener listener) {
		listeners.remove(listener);
	}

	public boolean isKeyPressed(Key key) {
		return key.isPressed();
	}
}
