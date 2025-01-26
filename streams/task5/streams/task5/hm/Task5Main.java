package streams.task5.hm;
import oop.streams.InputStream;
import oop.streams.OutputStream;
import oop.streams.wjs.WrappedJavaInputStream;
import oop.streams.wjs.WrappedJavaOutputStream;

public class Task5Main {
	public static void main(String[] args) {
        String[] words = {"hangman", "computer", "programming", "java", "stream"};
        
        OutputStream out = new WrappedJavaOutputStream(System.out);
        InputStream in = new WrappedJavaInputStream(System.in);
        
        Game game = new Game(words, in, out);
        game.play();
    }
}
