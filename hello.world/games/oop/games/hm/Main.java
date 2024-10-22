package oop.games.hm;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		Game game;
		game = new Game(args, System.in, System.out);
		game.play();
	}
}