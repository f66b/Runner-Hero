package oop.games.hm;

import java.io.IOException;

public class main {
	public static void main(String[] args) throws IOException {
		Game game;
		game = new Game(args, System.in, System.out);
		game.play();
	}
}