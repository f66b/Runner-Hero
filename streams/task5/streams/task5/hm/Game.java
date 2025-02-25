package streams.task5.hm;

import java.util.Random;
import oop.streams.InputStream;
import oop.streams.OutputStream;

public class Game {
	private final PrintStream out;
    private final Keyboard keyboard;
    private final HangedMan hangedMan;
    private final Random random;
    private final String[] words;
    
    public Game(String[] words, InputStream in, OutputStream out) {
        this.out = new PrintStream(out);
        this.keyboard = new Keyboard(in);
        this.hangedMan = new HangedMan(words);
        this.random = new Random();
        this.words = words;
    }
    
    public void play() {
        boolean playAgain = true;
        
        while (playAgain) {
            // Start new game with random word
            hangedMan.newGame(random.nextInt(words.length));
            playOneGame();
            
            // Ask to play again
            out.print("Play again? (y/n): ");
            playAgain = keyboard.readChar() == 'y';
        }
        
        out.println("Thanks for playing!");
    }
    
    private void playOneGame() {
        while (!hangedMan.won() && !hangedMan.lost()) {
            // Show current state
            out.println("Word: " + hangedMan.guessed());
            out.println("Tries left: " + hangedMan.getRemainingTries());
            
            // Get next guess
            out.print("Enter a letter: ");
            char guess = keyboard.readChar();
            
            // Process guess
            hangedMan.play(guess);
        }
        
        // Show final result
        if (hangedMan.won()) {
            out.println("Congratulations! You won!");
        } else {
            out.println("Game Over! The word was: " + new String(hangedMan.currentWord));
        }
    }

}
