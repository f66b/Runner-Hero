package hello.greetings;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class HalGreetings {
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
		names = new String[2];
		if (index == -1) {
			// No space found, only first name was entered
			names[0] = line; // First name
			names[1] = ""; // Empty last name
		} else {
			// Split the first name and last name
			names[0] = line.substring(0, index); // First name
			names[1] = line.substring(index + 1); // Last name (could be empty)
		}

		return names;
	}

	private static void echoGreetings(PrintStream ps, String[] names) {
		ps.print("Greetings " + names[0]);
		if (!names[1].isEmpty()) {
			ps.print(" " + names[1]);
		}
		ps.println("!");
	}
}