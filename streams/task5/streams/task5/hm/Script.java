package streams.task5.hm;

import oop.streams.InputStream;
import oop.streams.OutputStream;

public class Script {
    private final String[] words;          
    private final InputStream is;   
    private final OutputStream os; 
    private final HangedMan hangedMan;     

    public Script(String[] words, InputStream is, OutputStream os) {
        this.words = words;
        this.is = is;
        this.os = os;
        this.hangedMan = new HangedMan(words);
    }

    public void play() {
        CharReader charReader = new CharReader(is);
        String line;
        while (!(line = charReader.readLine()).equals("done")) {
            if (line.startsWith("play:")) {
                String[] parts = line.substring(5).split(",");
                int wordIndex = Integer.parseInt(parts[0].trim());
                int nTries = Integer.parseInt(parts[1].trim());
                hangedMan.newGame(wordIndex, nTries);
                writeString(os,"play: word=" + words[wordIndex] + " ntries=" + nTries + "\n");
            } else if (line.startsWith("guess:")) {
                char guess = line.charAt(7);
                hangedMan.play(guess);
                writeString(os,hangedMan.guessed() + "\n");
            } else if (line.equals("win")) {
                writeString(os,hangedMan.won() ? "Win.\n" : "KO: FAILED\n");
            } else if (line.equals("loss")) {
                writeString(os,hangedMan.lost() ? "Loss.\n" : "KO: FAILED\n");
            }
        }
        writeString(os,"OK: PASSED\n");

    }
    
    
    
    public HangedMan game() {
        return hangedMan;
    }
    private void writeString(OutputStream os, String text) {
        byte[] bytes = text.getBytes(); // Convert string to bytes
        for (byte b : bytes) {
            os.write(b); // Write each byte
        }
    }
}