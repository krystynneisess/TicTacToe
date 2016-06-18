package com.kn.ttt;

class MegaTicTacToe extends TicTacToe {
	public MegaTicTacToe() {
		super();

		board = new Board(8, 8);
		mg = new MoveGenerator(board);
		goc = new GameOverCheck(board, mg, 3, new boolean[] {true, true, false, false});
	}
}