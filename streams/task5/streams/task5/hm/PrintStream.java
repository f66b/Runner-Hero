package streams.task5.hm;

import oop.streams.DataOutputStream;
import oop.streams.OutputStream;
public class PrintStream {
private final DataOutputStream dos;
    
    public PrintStream(OutputStream os) {
        this.dos = new DataOutputStream(os);
    }
    
    public void print(String s) {
        try {
            if (s != null) {
                dos.writeUTF(s);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error writing to output stream", e);
        }
    }
    
    public void println(String s) {
        print(s);
        print("\n");
    }

}
