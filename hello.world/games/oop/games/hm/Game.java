package oop.games.hm;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Random;

public class Game {
	private PrintStream ps;
	private Keyboard keyboard;
	private HangedMan hangedMan;
	private String[] words = { "apple", "banana", "grape", "orange", "peach" }; // Example word list

	public Game(String[] args, InputStream in, PrintStream out) {
		this.ps = out;
		this.keyboard = new Keyboard(in, out);
		this.hangedMan = new HangedMan(words); // Initialize HangedMan with the words list
	}

	public void play() throws IOException {
		boolean keepPlaying = true;

		while (keepPlaying) {
			// Choose a random word from the list
			int randomWordIndex = new Random().nextInt(words.length);
			hangedMan.newGame(randomWordIndex); // Initialize a new game with the chosen word

			ps.println("Welcome to Hanged Man! Try to guess the word.");
			playGame(); // Play one round

			// Ask if the player wants to play again
			ps.println("Do you want to play again? (y/n)");
			char response = keyboard.read("");
			keepPlaying = response == 'y' || response == 'Y'; // Continue if player chooses 'y'
		}

		ps.println("Thanks for playing!");
	}

	private void playGame() throws IOException {
		// Loop while the player has neither won nor lost
		while (!hangedMan.won() && !hangedMan.lost()) {
			ps.println("Current word: " + hangedMan.guessed()); // Show the current guessed state of the word
			ps.println("Guess a letter: ");
			char guess = keyboard.read(""); // Get the player's guess

			hangedMan.play(guess); // Apply the guess to the game

			// If the player won
			if (hangedMan.won()) {
				ps.println("Congratulations, you won! The word was: " + new String(hangedMan.guessed()));
			}
			// If the player lost
			else if (hangedMan.lost()) {
				ps.println("Sorry, you've lost! The word was: " + new String(hangedMan.currentWord)); // Reveal the correct word
			}
			// Otherwise, show the current state and remaining tries
			else {
				ps.println("Remaining tries: " + hangedMan.getRemainingTries());
			}
		}
	}
}
