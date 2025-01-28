package streams.task5.hm;

public class HangedMan {

	private final String[] words;
	public char[] currentWord;
	private boolean[] guessedLetters;
	private int remainingTries;
	private static final int DEFAULT_TRIES = 6;

	public HangedMan(String[] words) {
		this.words = words;
	}

	public void newGame(int wordIndex) {
		newGame(wordIndex, DEFAULT_TRIES);
	}

	public void newGame(int wordIndex, int tries) {
		if (wordIndex < 0 || wordIndex >= words.length) {
			throw new IllegalArgumentException("Invalid word index");
		}
		currentWord = words[wordIndex].toCharArray();
		guessedLetters = new boolean[currentWord.length];
		remainingTries = tries;
	}

	public void play(char c) {
		boolean found = false;
		for (int i = 0; i < currentWord.length; i++) {
			if (currentWord[i] == c && !guessedLetters[i]) {
				guessedLetters[i] = true;
				found = true;
			}
		}
		if (!found) {
			remainingTries--;
		}
	}

	public String guessed() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < currentWord.length; i++) {
			sb.append(guessedLetters[i] ? currentWord[i] : '-');
		}
		return sb.toString();
	}

	public boolean won() {
		for (boolean guessed : guessedLetters) {
			if (!guessed)
				return false;
		}
		return true;
	}

	public boolean lost() {
		return remainingTries <= 0;
	}

	public int getRemainingTries() {
		return remainingTries;
	}

}
