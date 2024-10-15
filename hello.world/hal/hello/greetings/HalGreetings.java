package hello.greetings;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class HalGreetings {

	public static void greetings(PrintStream ps, String[] args) {
		ps.print("Greetings");

		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				ps.printf(" %s", args[i]);
			}
		}

		ps.println("!");
	}

	public static void main(String[] args) throws java.io.IOException {
		String[] fullname;
		if (args == null || args.length == 0) {
			System.out.println("Hello, I am Hal, and you are?");
			fullname = readFullName(System.in);
		} else {
			System.out.println("Hello, I am Hal.");
			fullname = args;
		}
		echoGreetings(System.out, fullname);
	}

	public static String[] readFullName(InputStream in) throws java.io.IOException {
		InputStreamReader r = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(r);
		String line = br.readLine();
		int index = line.indexOf(' ');
		String[] names;
		names = line.split("\\s+");
		return names;
	}

	private static void echoGreetings(PrintStream ps, String[] names) {
		ps.print("Greetings");
		for (String name : names) {
			ps.print(" " + name);
		}
		ps.println("!");
	}
}