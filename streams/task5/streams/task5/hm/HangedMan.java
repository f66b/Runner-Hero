package streams.task5.hm;

public class HangedMan {
    private final String[] words;
    public char[] currentWord;
    private boolean[] guessedLetters;
    private int remainingTries;
    private static final int DEFAULT_TRIES = 6;
    private boolean[] usedLetters;
    
    public HangedMan(String[] words) {
        this.words = words;
        this.usedLetters = new boolean[26];
    }
    
    public void newGame(int wordIndex) {
        newGame(wordIndex, DEFAULT_TRIES);
    }
    
    public void newGame(int wordIndex, int tries) {
        if (wordIndex < 0 || wordIndex >= words.length) {
            throw new IllegalArgumentException("Invalid word index: " + wordIndex);
        }
        currentWord = words[wordIndex].toLowerCase().toCharArray();
        guessedLetters = new boolean[currentWord.length];
        remainingTries = tries;
        usedLetters = new boolean[26];
        
        // Mark non-letter characters as already guessed
        for (int i = 0; i < currentWord.length; i++) {
            if (!Character.isLetter(currentWord[i])) {
                guessedLetters[i] = true;
            }
        }
    }
    
    public void play(char c) {
        if (lost() || won()) {
            return; // Game is already over
        }
        
        c = Character.toLowerCase(c);
        
        // Only process letters
        if (!Character.isLetter(c)) {
            remainingTries--;
            return;
        }
        
        int letterIndex = c - 'a';
        if (letterIndex < 0 || letterIndex >= 26) {
            remainingTries--;
            return;
        }
        
        boolean duplicate = usedLetters[letterIndex];
        
        usedLetters[letterIndex] = true;
        
        boolean found = false;
        for (int i = 0; i < currentWord.length; i++) {
            if (Character.toLowerCase(currentWord[i]) == c) {
                guessedLetters[i] = true;
                found = true;
            }
        }
        
        if (!found || duplicate) {
            remainingTries--;
        }
    }
    
    public String guessed() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < currentWord.length; i++) {
            if (guessedLetters[i]) {
                sb.append(currentWord[i]);
            } else {
                sb.append('-');
            }
        }
        return sb.toString();
    }
    
    public boolean won() {
        for (boolean guessed : guessedLetters) {
            if (!guessed) {
                return false;
            }
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