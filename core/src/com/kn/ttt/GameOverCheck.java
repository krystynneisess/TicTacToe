package com.kn.ttt;

import java.util.ArrayList;

class GameOverCheck {
	Board board;
	MoveGenerator mg;
	int connLength;
	boolean[] directions;
	// [0]:vert; [1]:horiz; [2]:diagUpRight; [3]:diagUpLeft

	public GameOverCheck(Board b, MoveGenerator mgen, int l, boolean[] d) {
		board = b;
		mg = mgen;
		connLength = l;
		directions = d;
	}

	public boolean isOver() {
		for (Integer c : getConnectionLengths()) {
			if (c >= connLength) {
				return true;
			} else if (!mg.movesRemain()) {
				return true;
			}
		}
		return false;
	}

	private ArrayList<Integer> getConnectionLengths() {
		ArrayList<Integer> conns = new ArrayList<Integer>();

		for (int i = 0; i < directions.length; i++) {
			if (directions[i]) {
				switch (i) {
					case 0: // vertical
						vertConns(conns, "A");
						vertConns(conns, "B");
						break;
					case 1: // horizontal
						horizConns(conns, "A");
						horizConns(conns, "B");
						break;
					case 2: // diagonal to upper right
						diagRightConns(conns, "A");
						diagRightConns(conns, "B");
						break;
					case 3: // diagonal to upper left
						diagLeftConns(conns, "A");
						diagLeftConns(conns, "B");
						break;
					default:
						break;
				}

			}
		}

		return conns;
	}

	private ArrayList<Integer> vertConns(ArrayList<Integer> conns, String player) {
		int currLen = 0;
		for (int j = 0; j < board.width; j++) {
			for (int i = 0; i < board.height; i++) {
				if (board.tiles[i][j].isOccupied && board.tiles[i][j].player.equals(player)) {
					currLen++;
				} else if (currLen != 0) {
					conns.add(currLen);
					currLen	= 0;
				}
			}
			if (currLen != 0) {
				conns.add(currLen);
				currLen = 0;				
			}
		}
		return conns;
	}

	private ArrayList<Integer> horizConns(ArrayList<Integer> conns, String player) {
		int currLen = 0;
		for (int i = 0; i < board.height; i++) {
			for (int j = 0; j < board.width; j++) {
				if (board.tiles[i][j].isOccupied && board.tiles[i][j].player.equals(player)) {
					currLen++;
				} else if (currLen != 0) {
					conns.add(currLen);
					currLen	= 0;
				}
			}
			if (currLen != 0) {
				conns.add(currLen);
				currLen = 0;				
			}
		}
		return conns;
	}

	private ArrayList<Integer> diagRightConns(ArrayList<Integer> conns, String player) {
		int i = board.width - 1;
		int j = board.width - 1;
		int currLen = 0;

		while (!(i == 0 && j == 0)) {
			if (board.tiles[i][j].isOccupied && board.tiles[i][j].player.equals(player)) {
				currLen++; 
			}

			if (i == 0) { // reset2 point
				if (currLen != 0) {conns.add(currLen); currLen = 0;}
				// reset 
				int tmp = i;
				i = j - 1;
				j = tmp;

			} else if (j == board.width - 1) { // reset1 point
				if (currLen != 0) {conns.add(currLen); currLen = 0;}
				// reset
				int tmp = i;
				i = j;
				j = tmp - 1;

			} else {
				i--; j++;
			}
		}

		if (board.tiles[i][j].isOccupied && board.tiles[i][j].player.equals(player)) {
			currLen++;
			conns.add(currLen);
		}

		return conns;
	}

	private ArrayList<Integer> diagLeftConns(ArrayList<Integer> conns, String player) {
		int i = board.width - 1;
		int j = 0;
		int currLen = 0;

		while (!(i == 0 && j == board.width - 1)) {
			if (board.tiles[i][j].isOccupied && board.tiles[i][j].player.equals(player)) {
				currLen++; 
			}

			if (i == 0) { // reset2 point
				if (currLen != 0) {conns.add(currLen); currLen = 0;}
				// reset
				i = board.width - (2 + j);
				j = board.width - 1;

			} else if (j == 0) { // reset1 point
				if (currLen != 0) {conns.add(currLen); currLen = 0;}
				// reset
				j = board.width - i;
				i = board.height - 1;

			} else {
				i--; j--;
			}
		}

		if (board.tiles[i][j].isOccupied && board.tiles[i][j].player.equals(player)) {
			currLen++;
			conns.add(currLen);
		}

		return conns;
	}

	// Because I was too lazy to write actual unit tests...
	public static void main(String[] args) {
		Board b = new Board(4, 4);
		b.tiles[0][0].isOccupied = true;
		b.tiles[0][0].player = "A";
		b.tiles[2][0].isOccupied = true;
		b.tiles[2][0].player = "A";
		b.tiles[3][0].isOccupied = true;
		b.tiles[3][0].player = "A";
		b.tiles[1][1].isOccupied = true;
		b.tiles[1][1].player = "A";
		b.tiles[3][1].isOccupied = true;
		b.tiles[3][1].player = "A";
		b.tiles[1][2].isOccupied = true;
		b.tiles[1][2].player = "A";
		b.tiles[2][2].isOccupied = true;
		b.tiles[2][2].player = "A";
		b.tiles[3][2].isOccupied = true;
		b.tiles[3][2].player = "A";
		b.tiles[1][3].isOccupied = true;
		b.tiles[1][3].player = "A";

		MoveGenerator mg = new MoveGenerator(b);

		GameOverCheck goc = new GameOverCheck(b, mg, 3, new boolean[]{false, false, false, true});

		ArrayList<Integer> connLens = goc.getConnectionLengths();

		for (Integer c : connLens) {
			System.out.println(c);
		}
	}
}