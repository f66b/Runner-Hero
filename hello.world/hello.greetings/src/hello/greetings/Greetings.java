package hello.greetings;

import java.io.PrintStream;

public class Greetings {

	public static void greetings(PrintStream ps, String[] args) {
	    ps.print("Greetings");
	    
	    if (args != null) {
	        for (int i = 0; i < args.length; i++) {
	            ps.printf(" %s", args[i]);
	        }
	    }
	    
	    ps.println("!");
	}
}