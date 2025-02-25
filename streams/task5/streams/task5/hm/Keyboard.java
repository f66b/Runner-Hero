package streams.task5.hm;
import oop.streams.DataInputStream;
import oop.streams.InputStream;
public class Keyboard {
private final DataInputStream dis;
    
    public Keyboard(InputStream is) {
        this.dis = new DataInputStream(is);
    }
    
    public char readChar() {
        String line = readLine();
        return line.length() > 0 ? line.charAt(0) : '\0';
    }
    
    public String readLine() {
        try {
            return dis.readUTF();
        } catch (Exception e) {
            throw new RuntimeException("Error reading from input stream", e);
        }
    }
}
