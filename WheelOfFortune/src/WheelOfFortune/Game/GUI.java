package WheelOfFortune.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Handles displaying the GUI for the game.
 * Serves as the main "game loop" as it instantiates the objects used in the game.
 *
 * @author Harry Bridgen
 * @version 1.0
 * @see Game
 * @see Wheel
 * @see Quadratic
 * @see GameButton
 */
public class GUI {
	JFrame gui = new JFrame("Wheel of Fortune");

	JPanel headerPanel = new JPanel();
	JPanel contentPanel = new JPanel();
	JPanel guessPanel = new JPanel();
	JPanel buttonPanel = new JPanel();

	JLabel playerLabel = new JLabel("", SwingConstants.CENTER);
	JLabel playerMoneyLabel = new JLabel("", SwingConstants.CENTER);
	JLabel infoLabel = new JLabel("", SwingConstants.CENTER);
	JLabel contentLabel = new JLabel("", SwingConstants.CENTER);

	Wheel wheel;
	Game game;

	int guiWidth;

	/**
	 * Constructs the GUI and sets the window icon image.
	 * Sets the dimensions of the GUI window using the parameters.
	 *
	 * @param width  width of the GUI window (Integer)
	 * @param height height of the GUI window (Integer)
	 */
	public GUI(int width, int height) {
		this.guiWidth = width;

		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setSize(width, height);
		gui.setResizable(false);

		gui.setLayout(new BorderLayout());
		headerPanel.setLayout(new BorderLayout());
		contentPanel.setLayout(new BorderLayout());

		gui.add(headerPanel, BorderLayout.PAGE_START);
		gui.add(contentPanel, BorderLayout.CENTER);
		gui.add(buttonPanel, BorderLayout.PAGE_END);

		playerLabel.setFont(new Font("", Font.BOLD, 50));
		playerMoneyLabel.setFont(new Font("", Font.PLAIN, 40));
		infoLabel.setFont(new Font("", Font.PLAIN, 40));
		contentLabel.setFont(new Font("", Font.BOLD, 40));

		headerPanel.add(playerLabel, BorderLayout.PAGE_START);
		headerPanel.add(playerMoneyLabel, BorderLayout.CENTER);
		headerPanel.add(infoLabel, BorderLayout.PAGE_END);

		contentPanel.add(contentLabel, BorderLayout.PAGE_START);
		contentPanel.add(guessPanel, BorderLayout.PAGE_END);

		buttonPanel.setPreferredSize(new Dimension(gui.getWidth(), 100));

		try {
			BufferedImage wofIcon = ImageIO.read(Objects.requireNonNull(GUI.class.getResource("assets/wofIcon.png")));
			gui.setIconImage(wofIcon);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Instantiates new game and wheel objects. Displays new game screen.
	 * Adds a button to go to number of players GUI and an exit application button.
	 */
	public void newGameGUI() {
		game = new Game();
		wheel = new Wheel();

		playerLabel.setText("");
		playerMoneyLabel.setText("");
		infoLabel.setText("");
		contentLabel.setText("");

		try {
			BufferedImage loadWofImage = ImageIO.read(Objects.requireNonNull(GUI.class.getResource("assets/wof.png")));
			JLabel wofImage = new JLabel(new ImageIcon(loadWofImage));
			GameButton buttonNewGame = new GameButton("New game");
			GameButton buttonExit = new GameButton("Exit");

			contentPanel.add(wofImage);
			buttonPanel.add(buttonNewGame);
			buttonPanel.add(buttonExit);

			gui.setVisible(true);

			buttonNewGame.addActionListener(e -> {
				contentPanel.remove(wofImage);
				buttonPanel.remove(buttonNewGame);
				buttonPanel.remove(buttonExit);
				numberOfPlayersGUI();
			});
			buttonExit.addActionListener(e -> gui.dispose());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Displays a GUI that allows the user to click a button to decide the number of players for a game.
	 * When the user clicks a button, calls {@link Game#setNumberOfPlayers(int)} with appropriate parameter and
	 * calls {@link Game#randomPlayer()} to randomise the starting player.
	 *
	 * @see Game#setNumberOfPlayers(int)
	 * @see Game#randomPlayer()
	 */
	public void numberOfPlayersGUI() {
		GameButton button2Player = new GameButton("2 players");
		GameButton button3Player = new GameButton("3 players");
		GameButton button4Player = new GameButton("4 players");

		playerLabel.setText("New game");
		playerMoneyLabel.setText("<HTML><CENTER><BR><BR><BR><BR>Number of players?");
		infoLabel.setText("");
		contentLabel.setText("");

		buttonPanel.add(button2Player);
		buttonPanel.add(button3Player);
		buttonPanel.add(button4Player);

		button2Player.addActionListener(e -> {
			game.setNumberOfPlayers(2);
			game.randomPlayer();
			wheelClickGUI();
		});
		button3Player.addActionListener(e -> {
			game.setNumberOfPlayers(3);
			game.randomPlayer();
			wheelClickGUI();
		});
		button4Player.addActionListener(e -> {
			game.setNumberOfPlayers(4);
			game.randomPlayer();
			wheelClickGUI();
		});
	}

	/**
	 * Displays a GUI that shows the current player and draws the wheel to the screen.
	 * The position of the wheel is set by calling {@link Wheel#setPosition(int, int)}.
	 * Checks if the wheel has been pressed by adding an action listener to the wheel.
	 * If the action listener is clicked calls {@link Wheel#detectClicked(int, int)}.
	 *
	 * @see Wheel#setPosition(int, int)
	 * @see Wheel#detectClicked(int, int)
	 */
	public void wheelClickGUI() {
		buttonPanel.removeAll();
		buttonPanel.repaint();

		playerLabel.setText("Player " + game.displayCurrentPlayer());
		playerMoneyLabel.setText("");
		infoLabel.setText("Spin the Wheel");
		contentLabel.setText("▼");

		wheel.setPosition(guiWidth, contentPanel.getWidth());
		contentPanel.add(wheel, BorderLayout.CENTER);

		wheel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent click) {

				if (wheel.detectClicked(click.getX(), click.getY())) {
					wheel.removeMouseListener(this);
					try {
						wheelSpinAnimationGUI();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		});
	}

 	/**
	 * Instantiates a new quadratic then uses the y values of the quadratic to rotate the wheel image by calling
	 * {@link Wheel#rotate(double)} with the parameter as {@link Quadratic#calculate()}.
	 * After the wheel has stopped spinning, checks which sector landed upright then calls appropriate GUI function.
	 *
	 * @throws InterruptedException call to 'Thread.sleep()' in a loop, probably busy-waiting
	 * @see Wheel#rotate(double)
	 * @see Quadratic#calculate()
	 */
	public void wheelSpinAnimationGUI() throws InterruptedException {
		Quadratic quadratic = new Quadratic();

		while (quadratic.calculate() != 0) {
			wheel.rotate(quadratic.calculate());
			wheel.paint(wheel.getGraphics());
			Thread.sleep(2);
		}

		Thread.sleep(100);

		if (wheel.sectorLanded == 2 || wheel.sectorLanded == 6) wheelMoneyGUI(50);
		if (wheel.sectorLanded == 3 || wheel.sectorLanded == 7) wheelMoneyGUI(100);
		if (wheel.sectorLanded == 0 || wheel.sectorLanded == 4) wheelMoneyGUI(200);
		if (wheel.sectorLanded == 1) wheelBankruptGUI();
		if (wheel.sectorLanded == 5) wheelNextPlayerGUI();
	}

	/**
	 * Displays wheel prize money above the wheel and adds prize money to the
	 * current player by calling {@link Game#currentPlayerAddMoney(int)}.
	 * Adds a button to go to guess letter screen.
	 *
	 * @param wheelPrizeMoney amount of money to display and add
	 * @see Game#currentPlayerAddMoney(int)
	 */
	public void wheelMoneyGUI(int wheelPrizeMoney) {
		GameButton buttonGuessLetter = new GameButton("Guess letter");
		infoLabel.setText("+£" + wheelPrizeMoney);
		buttonPanel.add(buttonGuessLetter);

		game.currentPlayerAddMoney(wheelPrizeMoney);

		buttonGuessLetter.addActionListener(e -> {
			buttonPanel.removeAll();
			contentPanel.remove(wheel);
			contentPanel.repaint();
			guessLetterGUI();
		});
	}

	/**
	 * Displays the current player is now bankrupt and calls {@link Game#currentPlayerBankrupt()}.
	 * Then calls {@link Game#nextPlayer()} and adds a button to spin the wheel.
	 *
	 * @see Game#currentPlayerBankrupt()
	 * @see Game#nextPlayer()
	 */
	public void wheelBankruptGUI() {
		GameButton wheelButton = new GameButton("Next Player");
		infoLabel.setText("Player " + game.displayCurrentPlayer() + " bankrupt");
		buttonPanel.add(wheelButton);

		game.currentPlayerBankrupt();
		game.nextPlayer();

		wheelButton.addActionListener(e -> {
			buttonPanel.remove(wheelButton);
			wheelClickGUI();
		});
	}

	/**
	 * Displays the next player is to spin the wheel.
	 * Then calls {@link Game#nextPlayer()} and adds a button to spin the wheel.
	 *
	 * @see Game#nextPlayer()
	 */
	public void wheelNextPlayerGUI() {
		GameButton wheelButton = new GameButton("Next Player");
		infoLabel.setText("Next Player");
		buttonPanel.add(wheelButton);

		game.nextPlayer();

		wheelButton.addActionListener(e -> {
			buttonPanel.remove(wheelButton);
			wheelClickGUI();
		});
	}

	/**
	 * Displays a GUI that shows the current player, their money, a guess box and a button.
	 * The user can guess a letter of the phrase by typing in the guess box, then clicking the button.
	 */
	public void guessLetterGUI() {
		JFormattedTextField guessBox = new JFormattedTextField();
		GameButton guessButton = new GameButton("Guess");
		guessBox.setPreferredSize(new Dimension(100, 100));
		guessBox.setHorizontalAlignment(JFormattedTextField.CENTER);
		guessBox.setFont(new Font("", Font.BOLD, 80));

		playerMoneyLabel.setText("Money: £" + game.currentPlayerMoney());
		infoLabel.setText("<HTML><CENTER><BR>Guess a letter (vowels cost £200)<BR><BR><BR><BR>");
		contentLabel.setText(game.guessPhrase);
		guessPanel.add(guessBox);
		guessPanel.repaint();
		buttonPanel.add(guessButton);

		guessButton.addActionListener(e -> {
			game.playerLetterGuess = guessBox.getText().charAt(0);
			buttonPanel.remove(guessButton);
			guessPanel.remove(guessBox);
			guessPanel.repaint();
			guessErrorsGUI();
		});
	}

	/**
	 * Verifies the letter guess by calling {@link Game#checkGuessErrors(char, String)}
	 * and uses return values to display errors.
	 *
	 * @see Game#checkGuessErrors(char, String)
	 */
	public void guessErrorsGUI() {
		GameButton continueButton = new GameButton("Continue");
		buttonPanel.add(continueButton);

		switch (game.checkGuessErrors(game.playerLetterGuess, game.guessPhrase)) {
			case "notLetter":
				infoLabel.setText("<HTML><CENTER><BR>Guess must be a letter<BR><BR><BR><BR>");
				continueButton.addActionListener(e -> {
					buttonPanel.remove(continueButton);
					guessLetterGUI();
				});
				break;
			case "alreadyFoundLetter":
				infoLabel.setText("<HTML><CENTER><BR>'" + game.playerLetterGuess + "' has already been found<BR><BR><BR><BR>");
				continueButton.addActionListener(e -> {
					buttonPanel.remove(continueButton);
					guessLetterGUI();
				});
				break;
			case "guessVowel":
				playerMoneyLabel.setText("Money: £" + game.currentPlayerMoney());
				infoLabel.setText("<HTML><CENTER><BR>-£200 for vowel guess<BR><BR><BR><BR>");
				continueButton.addActionListener(e -> {
					buttonPanel.remove(continueButton);
					if (game.checkGuessLetterFound(game.playerLetterGuess, game.gamePhrase)) guessLetterCorrectGUI();
					else guessLetterIncorrectGUI();
				});
				break;
			case "notEnoughMoney":
				infoLabel.setText("<HTML><CENTER><BR>Not enough money!<BR><BR><BR><BR>");
				continueButton.addActionListener(e -> {
					buttonPanel.remove(continueButton);
					guessLetterGUI();
				});
				break;
			case "verifyLetter":
				buttonPanel.remove(continueButton);
				if (game.checkGuessLetterFound(game.playerLetterGuess, game.gamePhrase)) guessLetterCorrectGUI();
				else guessLetterIncorrectGUI();
				break;
		}
	}

	/**
	 * Displays that the user's letter guess was incorrect.
	 * Adds a button that calls next player function and lets them spin the wheel.
	 */
	public void guessLetterIncorrectGUI() {
		GameButton wheelButton = new GameButton("Next Player");

		infoLabel.setText("<HTML><CENTER><BR>Guess '" + game.playerLetterGuess + "' was incorrect<BR><BR><BR><BR>");
		buttonPanel.add(wheelButton);

		wheelButton.addActionListener(e -> {
			game.nextPlayer();
			buttonPanel.remove(wheelButton);
			wheelClickGUI();
		});
	}

	/**
	 * Displays that the user's guess was correct and adds buttons to spin the wheel or guess the full phrase.
	 * Checks if the guess completes the guess phrase by calling {@link Game#comparePhraseToPhrase(String, String)}
	 * and if true, adds a button that calls game winner GUI function.
	 *
	 * @see Game#comparePhraseToPhrase(String, String)
	 */
	public void guessLetterCorrectGUI() {
		GameButton wheelButton = new GameButton("Spin");
		GameButton guessWordButton = new GameButton("Guess phrase");
		GameButton winnerButton = new GameButton("Winner");

		playerMoneyLabel.setText("Money: £" + game.currentPlayerMoney());
		infoLabel.setText("<HTML><CENTER><BR>Guess '" + game.playerLetterGuess + "'" + " was correct<BR><BR>+£" + (100 * game.lettersFound) + "<BR><BR>");
		contentLabel.setText(game.guessPhrase);

		if (game.comparePhraseToPhrase(game.guessPhrase, game.gamePhrase)) {
			buttonPanel.add(winnerButton);

			winnerButton.addActionListener(e -> {
				buttonPanel.removeAll();
				gameWinnerGUI();
			});
		} else {
			buttonPanel.add(wheelButton);
			buttonPanel.add(guessWordButton);

			wheelButton.addActionListener(e -> {
				buttonPanel.removeAll();
				wheelClickGUI();
			});
			guessWordButton.addActionListener(e -> {
				buttonPanel.removeAll();
				guessFullPhraseGUI();
			});
		}
	}

	/**
	 * Displays a GUI with a guess box and a button.
	 * Allows the user to guess the full phrase.
	 * When the user clicks the guess button, checks if the guess is correct by
	 * calling {@link Game#comparePhraseToPhrase(String, String)} then calling appropriate GUI.
	 * @see Game#comparePhraseToPhrase(String, String)
	 */
	public void guessFullPhraseGUI() {
		JFormattedTextField guessBox = new JFormattedTextField();
		GameButton backButton = new GameButton("Back");
		GameButton guessButton = new GameButton("Guess");

		guessBox.setPreferredSize(new Dimension(450, 50));
		guessBox.setFont(new Font("", Font.PLAIN, 30));
		guessBox.setHorizontalAlignment(JFormattedTextField.CENTER);

		infoLabel.setText("<HTML><CENTER><BR>Guess the phrase<BR><BR><BR><BR");
		guessPanel.add(guessBox);
		buttonPanel.add(backButton);
		buttonPanel.add(guessButton);

		backButton.addActionListener(e -> {
			guessPanel.remove(guessBox);
			buttonPanel.remove(backButton);
			buttonPanel.remove(guessButton);
			guessLetterCorrectGUI();
		});
		guessButton.addActionListener(e -> {
			game.playerFullGuess = guessBox.getText();
			guessPanel.remove(guessBox);
			buttonPanel.remove(backButton);
			buttonPanel.remove(guessButton);
			if (game.comparePhraseToPhrase(game.playerFullGuess, game.gamePhrase)) gameWinnerGUI();
			else guessFullPhraseIncorrectGUI();
		});
	}

	/**
	 * Displays the users full phrase guess was incorrect.
	 * Then calls {@link Game#nextPlayer()} and adds a button to spin the wheel.
	 */
	public void guessFullPhraseIncorrectGUI() {
		buttonPanel.repaint();
		GameButton wheelButton = new GameButton("Next Player");
		infoLabel.setText("<HTML><CENTER><BR>Guess '" + game.playerFullGuess + "' was incorrect<BR><BR><BR><BR>");
		buttonPanel.add(wheelButton);

		game.nextPlayer();

		wheelButton.addActionListener(e -> {
			buttonPanel.remove(wheelButton);
			wheelClickGUI();
		});
	}

	/**
	 * Displays that the current player is the winner and displays {@link Game#bonusMoney()} and {@link Game#prizeMoneyTotal()}.
	 * Adds a button to make a new game and adds a button to exit the application.
	 * @see Game#bonusMoney()
	 * @see	Game#prizeMoneyTotal()
	 */
	public void gameWinnerGUI() {
		GameButton newGameButton = new GameButton("New game");
		GameButton exitButton = new GameButton("Exit");

		playerLabel.setText("Congratulations!");
		playerMoneyLabel.setText("");
		infoLabel.setText("<HTML><CENTER>Player " + game.displayCurrentPlayer() + " wins!<BR><BR>Money: £" + game.currentPlayerMoney() + "<BR>Bonus: £" + game.bonusMoney() + "<BR>Total prize: £" + game.prizeMoneyTotal() + "<BR><BR>The phrase was:<BR>");
		contentLabel.setText(game.gamePhrase);

		buttonPanel.add(newGameButton);
		buttonPanel.add(exitButton);
		buttonPanel.repaint();

		newGameButton.addActionListener(e -> {
			buttonPanel.removeAll();
			newGameGUI();
		});

		exitButton.addActionListener(e -> gui.dispose());
	}
}