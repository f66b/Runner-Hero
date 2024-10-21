package oop.games.hm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;

public class Keyboard {
	private PrintStream out;
	private Reader reader;
	private BufferedReader br;

	public Keyboard(InputStream in, PrintStream out) {
		this.out = out;
		this.reader = new InputStreamReader(in);
		this.br = new BufferedReader(reader);
	}

	public char read(String msg) throws IOException {
		if (msg != null)
			out.print(msg);
		out.flush();
		String line = br.readLine();
		char c = line.charAt(0);
		return c;
	}
}
