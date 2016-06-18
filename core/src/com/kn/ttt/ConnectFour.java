package com.kn.ttt;

class ConnectFour extends Game {
	public ConnectFour() {
		super();

		board = new Board(6, 6);
		mg = new ConnectFourMoveGenerator(board);
		goc = new GameOverCheck(board, mg, 4, new boolean[] {true, true, true, true});
	}
}