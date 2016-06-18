package com.kn.ttt;

import java.util.HashMap;

class GameLauncher {
	public static void main(String[] args) {
		Game g = new Game();
		HashMap<String, Integer> argMap = new HashMap<String, Integer>();
		argMap.put("tic", 0);
		argMap.put("connect", 1);
		argMap.put("mega", 2);

		switch (argMap.get(args[0])) {
			case 0:
				g = new TicTacToe();
				break;
			case 1:
				g = new ConnectFour();
				break;
			case 2:
				g = new MegaTicTacToe();
				break;
			default:
				break;
		}

		while (!g.isGameOver()) {
			g.performMove();
			g.printBoard();

			if (g.isGameOver()) {
				System.out.println("GAMEOVER? True\n");
			} else {
				System.out.println("GAMEOVER? False\n");
			}
		}
	}
}