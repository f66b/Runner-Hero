package oop.games;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

public class HangedManSJ {

	private static String[] words;
	private static String chosenWord;
	private static char[] guessedWord;
	private static int remainingTries;
	private static final int MAX_TRIES = 6;
	private static Scanner scanner;
	private static PrintStream out;

	/*
	 * The entry point, reduced to a minimum like it should be, most of the time.
	 * The list of possible words to guess is given as the arguments.
	 */
	public static void main(String[] args) {
		init(args, System.in, System.out); // Initialize the game with given words and streams
		play(); // Start the game loop
	}

	/*
	 * The initialization of the global variables (static fields).
	 */
	public static void init(String[] args, InputStream is, PrintStream ps) {

		if (args == null || args.length == 0) {
			words = new String[] { "apple", "banana", "palix", "gruber", "polytech" }; // Default words
		} else {
			words = args;
		}

		scanner = new Scanner(is);
		out = ps;
	}

	/*
	 * A function that plays one game and offers the players to keep playing or not.
	 * The function that chooses the word to guess amongst the list of possible
	 * words, using a randomly generated number.
	 */
	public static void play() {
		boolean keepPlaying = true;

		while (keepPlaying) {

			Random random = new Random();
			int randomIndex = random.nextInt(words.length);
			initWord(randomIndex);

			playGame();

			out.println("Do you want to play again? (yes/no): ");
			String response = scanner.nextLine().trim().toLowerCase();
			keepPlaying = response.equals("yes");
		}

		out.println("Thanks for playing!");
		scanner.close();
	}

	/*
	 * The function initializes the game for the chosen word, given by its index in
	 * the array of words.
	 */
	public static void initWord(int n) {
		chosenWord = words[n];
		guessedWord = new char[chosenWord.length()];
		remainingTries = MAX_TRIES;

		for (int i = 0; i < guessedWord.length; i++) {
			guessedWord[i] = '_';
		}

		out.println("A new word has been chosen. Start guessing!");
		out.println("Word: " + new String(guessedWord));
	}

	/*
	 * A function to know if the current word has been guessed yet or not. Returns
	 * true if the current word has been guessed.
	 */
	public static boolean guessed() {
		for (char c : guessedWord) {
			if (c == '_') {
				return false;
			}
		}
		return true;
	}

	/*
	 * A function to play one game, after a word has been chosen. This function will
	 * allow only a limited number of tries.
	 */
	public static void playGame() {
		while (remainingTries > 0 && !guessed()) {
			out.println("Remaining tries: " + remainingTries);
			out.println("Guess a letter: ");
			String guess = scanner.nextLine().trim().toLowerCase();

			if (guess.length() != 1) {
				out.println("Please enter a single letter.");
				continue;
			}

			char guessedLetter = guess.charAt(0);
			boolean correctGuess = false;

			for (int i = 0; i < chosenWord.length(); i++) {
				if (chosenWord.charAt(i) == guessedLetter && guessedWord[i] == '_') {
					guessedWord[i] = guessedLetter;
					correctGuess = true;
				}
			}

			if (correctGuess) {
				out.println("Good guess!");
			} else {
				out.println("Incorrect guess.");
				remainingTries--;
			}

			out.println("Word: " + new String(guessedWord));
		}

		if (guessed()) {
			out.println("Congratulations! You guessed the word: " + chosenWord);
		} else {
			out.println("You ran out of tries! The word was: " + chosenWord);
		}
	}
}
