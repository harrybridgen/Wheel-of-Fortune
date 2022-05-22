package WheelOfFortune.Game;
import java.util.Random;

/**
 *Handles wheel of fortune game logic and data.
 *@author Harry Bridgen
 *@version 1.0
 */
public class Game {

	int[] playerMoney;
	int currentPlayer;
	int numberOfPlayers;
	int guessCount = 20;
	int lettersFound;
	int vowelCost = 200;
	char playerLetterGuess;
	String playerFullGuess;
	String[] phrases = {"wheel of fortune", "kill two birds with one stone", "its raining cats and dogs", "knowledge is power", "early bird gets the worm", "its not rocket science"};
	String gamePhrase;
	String guessPhrase;

	/**
	 *Constructs a new game with a random phrase from the phrase string.
	 * Makes the guess phrase equal to the game phrase with all letters replaced with a dash.
	 */
	public Game() {
		Random random = new Random();
		gamePhrase = phrases[random.nextInt(phrases.length)];
		guessPhrase = gamePhrase.replaceAll("[a-z]", "\\-");
	}

	/**Used for returning the current player for display in the GUI.
	 * Adds 1 to current player because the number of players array starts at 0.
	 * @return Current player (Integer)
	 */
	public int displayCurrentPlayer() {
		return (currentPlayer + 1);
	}

	/**Changes the current player by +1. Changes current player to first player if currently on last player.
	 */
	public void nextPlayer() {
		if (displayCurrentPlayer() == numberOfPlayers) currentPlayer = 0;
		else currentPlayer += 1;
	}

	/**Sets the number of players for the game and sets the length of player money array to the number of players.
	 * @param players Number of players (Integer)
	 */
	public void setNumberOfPlayers(int players) {
		numberOfPlayers = players;
		playerMoney = new int[numberOfPlayers];
	}

	/**
	 *Randomises the current player by generating a random number within the bounds of the number of players.
	 */
	public void randomPlayer() {
		Random random = new Random();
		currentPlayer = random.nextInt(numberOfPlayers);
	}

	/**Displays the current player's money based on the current players position in the player money array.
	 * @return Integer current player's money
	 */
	public int currentPlayerMoney() {
		return playerMoney[currentPlayer];
	}


	/**Adds money to the player money array in the position of the current player.
	 * @param money Amount of money to add to the current player (Integer)
	 */
	public void currentPlayerAddMoney(int money) {
		playerMoney[currentPlayer] += money;
	}

	/**
	 *Uses the {@link Game#currentPlayerAddMoney(int)} function to set the current player's
	 * money to 0 by subtracting the player's money from the player's money.
	 * @see Game#currentPlayerAddMoney(int)
	 */
	public void currentPlayerBankrupt() {
		currentPlayerAddMoney(-currentPlayerMoney());
	}

	/**Bonus money with the default values starts at £5000 and decreases by £250 for every incorrect guess.
	 * @return Bonus money (Integer)
	 */
	public int bonusMoney() {
		return (guessCount * 250);
	}

	/**Adds {@link Game#currentPlayerMoney()} and {@link Game#bonusMoney()} to find the total prize money.
	 * @return Total prize money (Integer)
	 * @see Game#currentPlayerMoney()
	 * @see Game#bonusMoney()
	 */
	public int prizeMoneyTotal() {
		return (currentPlayerMoney() + bonusMoney());
	}

	/**Checks the user's letter guess against multiple rules using {@link Game#compareGuessToPhrase(char, String)}.
	 * Checks if the guess is a letter, if the letter has been found, if the guess is a vowel and if the player has enough money
	 * to guess a vowel.
	 * @return Description of outcome (String)
	 * @param letterGuess Player's letter guess (Character)
	 * @param phrase Phrase the check the guess against (String)
	 * @see Game#compareGuessToPhrase(char, String)
	 */
	public String checkGuessErrors(char letterGuess, String phrase) {
		int lettersFound = compareGuessToPhrase(letterGuess, phrase);
		if (lettersFound >= 1) {
			return "alreadyFoundLetter";
		} else if (!Character.isLetter(letterGuess)) {
			return "notLetter";
		} else if (letterGuess == 'a' || letterGuess == 'e' || letterGuess == 'i' || letterGuess == 'o' || letterGuess == 'u') {
			if (currentPlayerMoney() >= vowelCost) {
				currentPlayerAddMoney(-vowelCost);
				return "guessVowel";
			} else return "notEnoughMoney";
		} else return "verifyLetter";
	}

	/**Checks if the user letter guess matches at least 1 letter in the game phrase using {@link Game#compareGuessToPhrase(char, String)}.
	 * @return True/false (Boolean)
	 * @param letterGuess Player's letter guess (Character)
	 * @param phrase Phrase the check the guess against (String)
	 * @see Game#compareGuessToPhrase(char, String)
	 */
	public boolean checkGuessLetterFound(char letterGuess, String phrase) {
		int lettersFound = compareGuessToPhrase(letterGuess, phrase);
		this.lettersFound = lettersFound;

		if (lettersFound >= 1) {
			currentPlayerAddMoney(rewardLettersFound(lettersFound));
			if (guessCount > 0) guessCount--;
			return true;
		}
		return false;
	}

	/**Finds the reward for a correct letter guess by taking the amount of letters found and multiplying by 100.
	 * @param lettersFound Number of letters found (Integer)
	 * @return Reward for finding letters (Integer)
	 */
	public int rewardLettersFound(int lettersFound) {
		return 100 * lettersFound;
	}

	/**Used for comparing a letter guess and a phrase. Appends the guess
	 * phrase based on the letter found.
	 * @param letterGuess Player's letter guess (Character)
	 * @param phrase Phrase to compare guess to (String)
	 * @return Number of letters found (Integer)
	 */
	public int compareGuessToPhrase(char letterGuess, String phrase) {
		StringBuilder newGuessPhrase = new StringBuilder();

		int lettersFound = 0;
		int n = 0;

		while (n < phrase.length()) {
			if (letterGuess == phrase.charAt(n)) {
				newGuessPhrase.append(letterGuess);
				lettersFound++;
			} else newGuessPhrase.append(guessPhrase.charAt(n));
			n++;
		}
		guessPhrase = String.valueOf(newGuessPhrase);
		return lettersFound;
	}

	/**Used for comparing 2 phrases. If the phrases match, returns true.
	 * @param phraseGuess Phrase 1 (String)
	 * @param phrase Phrase 2 (String)
	 * @return True/false (Boolean)
	 */
	public boolean comparePhraseToPhrase(String phraseGuess, String phrase) {
		return phraseGuess.equals(phrase);
	}
}




