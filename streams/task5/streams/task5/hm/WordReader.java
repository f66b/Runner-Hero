package streams.task5.hm;

import oop.streams.InputStream;

public class WordReader {
	private final CharReader reader;

	public WordReader(InputStream is) {
		this.reader = new CharReader(is);
	}

	public String[] parse() {
		try {
			// Read number of words
			String countLine = reader.readLine();
			int wordCount = Integer.parseInt(countLine.trim());

			// Read words
			String[] words = new String[wordCount];
			for (int i = 0; i < wordCount; i++) {
				words[i] = reader.readLine().trim();
			}

			return words;
		} catch (Exception e) {
			throw new RuntimeException("Error parsing words file", e);
		}
	}

}
