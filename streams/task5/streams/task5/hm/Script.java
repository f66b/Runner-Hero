package streams.task5.hm;

import oop.streams.InputStream;
import oop.streams.OutputStream;

public class Script {
	private final String[] words;
	private final CharReader reader;
	private final PrintStream out;
	private final HangedMan hangedMan;

	public Script(String[] words, InputStream is, OutputStream os) {
		this.words = words;
		this.reader = new CharReader(is);
		this.out = new PrintStream(os);
		this.hangedMan = new HangedMan(words);
	}

	public void play() {
		try {
			String line;
			while (true) {
				line = reader.readLine().trim();

				if (line.equals("done")) {
					break;
				}

				if (line.startsWith("play: ")) {
					handlePlay(line.substring(6));
				} else if (line.startsWith("guess: ")) {
					handleGuess(line.charAt(7));
				} else if (line.equals("win")) {
					verifyWin();
				} else if (line.equals("loss")) {
					verifyLoss();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error executing script", e);
		}
	}

	private void handlePlay(String params) {
		String[] parts = params.split(",");
		int wordIndex = Integer.parseInt(parts[0].trim()) - 1; // Convert 1-based to 0-based index
		int tries = Integer.parseInt(parts[1].trim());
		hangedMan.newGame(wordIndex, tries);
		out.println("Starting new game with word: " + hangedMan.guessed());
	}

	private void handleGuess(char letter) {
		hangedMan.play(letter);
		out.println("Guessed '" + letter + "': " + hangedMan.guessed());
		out.println("Remaining tries: " + hangedMan.getRemainingTries());
	}

	private void verifyWin() {
		if (!hangedMan.won()) {
			throw new RuntimeException("Expected win but game is not won");
		}
		out.println("Game won as expected");
	}

	private void verifyLoss() {
		if (!hangedMan.lost()) {
			throw new RuntimeException("Expected loss but game is not lost");
		}
		out.println("Game lost as expected");
	}

	public HangedMan game() {
		return hangedMan;
	}

}
