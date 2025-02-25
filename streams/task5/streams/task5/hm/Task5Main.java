package streams.task5.hm;

import java.io.FileInputStream;
import oop.streams.InputStream;
import oop.streams.OutputStream;
import oop.streams.wjs.WrappedJavaInputStream;
import oop.streams.wjs.WrappedJavaOutputStream;

public class Task5Main {
	static OutputStream out;
	static InputStream in;
	static String[] words;
	static boolean scripting;

	public static void main(String[] args) throws Exception {
		out = new WrappedJavaOutputStream(System.out);
		in = new WrappedJavaInputStream(System.in);
		parse(args);

		if (scripting) {
			Script script = new Script(words, in, out);
			script.play();
		} else {
			Game game = new Game(words, in, out);
			game.play();
		}
	}

	public static void parse(String[] args) throws Exception {
		String wordsFile = null;
		String scriptFile = null;

		// Parse command line arguments
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-words") && i + 1 < args.length) {
				wordsFile = args[++i];
			} else if (args[i].equals("-script") && i + 1 < args.length) {
				scriptFile = args[++i];
				scripting = true;
			}
		}

		if (wordsFile == null) {
			throw new IllegalArgumentException("Words file must be specified with -words");
		}

		// Read words file
		FileInputStream wordsFileStream = new FileInputStream(wordsFile);
		InputStream wrappedWordsStream = new WrappedJavaInputStream(wordsFileStream);
		WordReader wordReader = new WordReader(wrappedWordsStream);
		words = wordReader.parse();

		// If script file specified, set up input stream from it
		if (scriptFile != null) {
			FileInputStream scriptFileStream = new FileInputStream(scriptFile);
			in = new WrappedJavaInputStream(scriptFileStream);
		}
	}
}
