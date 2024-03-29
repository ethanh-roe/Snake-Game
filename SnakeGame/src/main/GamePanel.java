package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
	Random random;
	Timer timer;
	static final int SCREEN_HEIGHT = 600;
	static final int SCREEN_WIDTH = 600;
	static final int SQUARE_SIZE = 50;
	static final int BOARD_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / SQUARE_SIZE;
	static final int DELAY = 100;
	static int NUM_APPLES = 5;
	final int x[] = new int[BOARD_UNITS];
	final int y[] = new int[BOARD_UNITS];
	final int appleX[] = new int[BOARD_UNITS];
	final int appleY[] = new int[BOARD_UNITS];
	int snakeLength = 3;
	int pointsEaten;
	char direction = 'R';
	char nextDirection;
	boolean running = true;
	boolean started = false;
	boolean lost = false;
	boolean won = false;
	boolean restarted = false;

	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		x[0] = 3 * SQUARE_SIZE;
		y[0] = 3 * SQUARE_SIZE;
		x[1] = 2 * SQUARE_SIZE;
		y[1] = 3 * SQUARE_SIZE;
		x[2] = 1 * SQUARE_SIZE;
		y[2] = 3 * SQUARE_SIZE;
		timer = new Timer(DELAY, this);
		timer.start();
		startGame();
	}

	public void gameOver(Graphics g) {
		g.setColor(Color.red);
		g.setFont(new Font("Times", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("You Lose", (SCREEN_WIDTH - metrics.stringWidth("You Lose")) / 2, SCREEN_HEIGHT / 2);
		lost = true;
	}

	public void startGame() {
		spawnApple();

	}

	public boolean checkWinCondition() {
		int area = ((SCREEN_WIDTH * SCREEN_HEIGHT) / SQUARE_SIZE) / SQUARE_SIZE;
		if (snakeLength == area) {
			running = false;
			won = true;
			return true;
		} else {
			return false;
		}
	}

	public void restart() {
		snakeLength = 3;
		x[0] = 3 * SQUARE_SIZE;
		y[0] = 3 * SQUARE_SIZE;
		x[1] = 2 * SQUARE_SIZE;
		y[1] = 3 * SQUARE_SIZE;
		x[2] = 1 * SQUARE_SIZE;
		y[2] = 3 * SQUARE_SIZE;
		restarted = true;
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		colorSquares(g);
		draw(g);

	}
	
	public void gameWon(Graphics g) {
		g.setColor(Color.green);
		g.setFont(new Font("Times", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("You Win!", (SCREEN_WIDTH - metrics.stringWidth("You Win!")) / 2, SCREEN_HEIGHT / 2);
	}

	public void draw(Graphics g) {

		if (checkWinCondition()) {
			gameWon(g);
		}

		if (running) {
			drawApple(g);
			drawSnake(g);
		} else if (lost) {
			gameOver(g);

		} else if (restarted) {
			drawSnake(g);
			drawApple(g);
			restarted = false;
		}

	}

	public void drawApple(Graphics g) {
		g.setColor(Color.red);
		for (int i = 0; i < NUM_APPLES; i++) {
			g.fillOval(appleX[i], appleY[i], SQUARE_SIZE, SQUARE_SIZE);
		}
	}

	public void drawSnake(Graphics g) {
		for (int i = 0; i < snakeLength; i++) {
			if (i == 0) {
				g.setColor(Color.magenta);
			} else {
				g.setColor(Color.blue);
			}
			g.fillOval(x[i], y[i], SQUARE_SIZE, SQUARE_SIZE);
		}
	}

	public void colorSquares(Graphics g) {
		int flip = 0;
		for (int i = 0; i < SCREEN_HEIGHT / SQUARE_SIZE; i++) {
			if (i % 2 == 0) {
				flip = 1;
			} else {
				flip = 0;
			}
			for (int j = 0; j < SCREEN_WIDTH / SQUARE_SIZE; j++) {
				if (flip == 0) {
					g.setColor(Color.gray);
					flip++;
				} else {
					g.setColor(Color.darkGray);
					flip--;
				}
				g.fillRect(j * SQUARE_SIZE, i * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
			}
		}
	}

	public void spawnApple() {
		boolean exit = false;
		for (int i = 0; i < NUM_APPLES; i++) {
			appleX[i] = random.nextInt((int) (SCREEN_WIDTH / SQUARE_SIZE)) * SQUARE_SIZE;
			appleY[i] = random.nextInt((int) (SCREEN_HEIGHT / SQUARE_SIZE)) * SQUARE_SIZE;
			for (int j = 0; j < snakeLength; j++) {
				if (appleX[i] == x[j] && appleY[i] == y[j]) {
					exit = true;
				}
				if (exit) {
					i--;
					exit = false;
					break;
				}
			}
		}
	}

	public void move() {
		switch (direction) {
		case 'U':
			for (int i = snakeLength; i > 0; i--) {
				x[i] = x[i - 1];
				y[i] = y[i - 1];
			}
			y[0] -= SQUARE_SIZE;
			break;

		case 'D':
			for (int i = snakeLength; i > 0; i--) {
				x[i] = x[i - 1];
				y[i] = y[i - 1];
			}
			y[0] += SQUARE_SIZE;
			break;

		case 'L':
			for (int i = snakeLength; i > 0; i--) {
				x[i] = x[i - 1];
				y[i] = y[i - 1];
			}
			x[0] -= SQUARE_SIZE;
			break;

		case 'R':
			for (int i = snakeLength; i > 0; i--) {
				x[i] = x[i - 1];
				y[i] = y[i - 1];
			}
			x[0] += SQUARE_SIZE;
			break;

		}
	}

	public void growSnake() {
		snakeLength++;
	}

	public void checkApple() {
		for (int i = 0; i < NUM_APPLES; i++) {
			if (appleX[i] == x[0] && appleY[i] == y[0]) {
				growSnake();
				appleX[i] = random.nextInt((int) (SCREEN_WIDTH / SQUARE_SIZE)) * SQUARE_SIZE;
				appleY[i] = random.nextInt((int) (SCREEN_HEIGHT / SQUARE_SIZE)) * SQUARE_SIZE;
				break;
			}
		}
	}

	public void checkCollision() {
		for (int i = snakeLength; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
				lost = true;
			}
		}
		if (x[0] < 0) {
			running = false;
			lost = true;
		}

		if (x[0] > SCREEN_WIDTH) {
			running = false;
			lost = true;
		}

		if (y[0] < 0) {
			running = false;
			lost = true;
		}

		if (y[0] > SCREEN_HEIGHT) {
			running = false;
			lost = true;
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			direction = nextDirection;
			move();
			repaint();
			checkApple();
			checkCollision();
		}
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			int code = e.getKeyCode();
			switch (code) {
			case KeyEvent.VK_UP:
				if (direction == 'D') {
					break;
				}
				nextDirection = 'U';
				running = true;
				started = true;
				break;
			case KeyEvent.VK_W:
				if (direction == 'D') {
					break;
				}
				nextDirection = 'U';
				running = true;
				started = true;
				break;
			case KeyEvent.VK_DOWN:
				if (direction == 'U') {
					break;
				}
				nextDirection = 'D';
				running = true;
				started = true;
				break;
			case KeyEvent.VK_S:
				if (direction == 'U') {
					break;
				}
				nextDirection = 'D';
				running = true;
				started = true;
				break;
			case KeyEvent.VK_LEFT:
				if (direction == 'R') {
					break;
				}
				nextDirection = 'L';
				running = true;
				started = true;
				break;

			case KeyEvent.VK_A:
				if (direction == 'R') {
					break;
				}
				nextDirection = 'L';
				running = true;
				started = true;
				break;
			case KeyEvent.VK_RIGHT:
				if (direction == 'L') {
					break;
				}
				nextDirection = 'R';
				running = true;
				started = true;
				break;

			case KeyEvent.VK_D:
				if (direction == 'L') {
					break;
				}
				nextDirection = 'R';
				running = true;
				started = true;
				break;

			case KeyEvent.VK_R:
				if (lost) {
					lost = false;
					restart();
				}
			}

		}
	}
}