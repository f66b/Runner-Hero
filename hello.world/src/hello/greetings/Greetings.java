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

	public static void main(String[] args) {
		// Call the greetings method and pass System.out as the PrintStream
		greetings(System.out, args);
	}
}