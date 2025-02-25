package streams.task5.hm;


import oop.streams.InputStream;

public class CharReader {
	 private final InputStream is;
	    
	    public CharReader(InputStream is) {
	        this.is = is;
	    }
	    
	    public String readLine() {
	        try {
	            StringBuilder sb = new StringBuilder();
	            int b;
	            while (is.available() > 0 && (b = is.read()) != -1) {
	                char c = (char) b;
	                if (c == '\n') {
	                    break;
	                }
	                sb.append(c);
	            }
	            return sb.toString();
	        } catch (Exception e) {
	            throw new RuntimeException("Error reading line", e);
	        }
	    }
	    
	    public char readChar() {
	        try {
	            if (is.available() > 0) {
	                return (char) is.read();
	            }
	            throw new RuntimeException("No more characters available");
	        } catch (Exception e) {
	            throw new RuntimeException("Error reading character", e);
	        }
	    }

}
