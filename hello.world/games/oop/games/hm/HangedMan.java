package oop.games.hm;

public class HangedMan {

	/*
	 * Imposed field to hold the secret words.
	 */
	private char[][] words; // Array of words, where each word is a char array
	public char[] currentWord; // The word chosen for the current game
	private boolean[] guessedLetters; // Track which letters have been guessed
	public int remainingTries; // Track remaining tries before the player loses
	public final int MAX_TRIES = 6; // Maximum allowed wrong guesses

	/*
	 * The two constructors.
	 */
	public HangedMan(char[][] words) {
		this.words = words;
	}

	public HangedMan(String[] words) {
		// Convert the array of strings into an array of char arrays
		this.words = new char[words.length][];
		for (int i = 0; i < words.length; i++) {
			this.words[i] = words[i].toCharArray();
		}
	}

	/*
	 * Initializes a new game with the nth word.
	 */
	public void newGame(int n) {
		if (n < 0 || n >= words.length) {
			throw new IllegalArgumentException("Invalid word index");
		}
		currentWord = words[n];
		guessedLetters = new boolean[currentWord.length]; // Initially no letters guessed
		remainingTries = MAX_TRIES;
	}

	/*
	 * Once a game has been initialized, this method is used to propose a character.
	 */
	public void play(char c) {
		boolean found = false;

		// Check if the character exists in the current word
		for (int i = 0; i < currentWord.length; i++) {
			if (currentWord[i] == c) {
				guessedLetters[i] = true; // Mark the letter as guessed
				found = true;
			}
		}

		// If the character was not found, decrease remaining tries
		if (!found) {
			remainingTries--;
		}
	}

	/*
	 * Returns a string that corresponds to the current guessed letters. For
	 * example: -a-a-a for the secret word "banana" and the letter 'a' that has been
	 * guessed correctly.
	 */
	public String guessed() {
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < currentWord.length; i++) {
			if (guessedLetters[i]) {
				result.append(currentWord[i]);
			} else {
				result.append('-'); // Hidden letters are shown as dashes
			}
		}

		return result.toString();
	}

	/*
	 * Returns true if the player has won. false otherwise.
	 */
	public boolean won() {
		// Player wins if all letters have been guessed
		for (boolean guessed : guessedLetters) {
			if (!guessed) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Returns true if the player has lost. false otherwise.
	 */
	public boolean lost() {
		return remainingTries <= 0;
	}

	/*
	 * Getter method for remaining tries.
	 */
	public int getRemainingTries() {
		return remainingTries;
	}
}
