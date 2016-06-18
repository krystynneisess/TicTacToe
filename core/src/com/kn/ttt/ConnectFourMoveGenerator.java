package com.kn.ttt;

import java.util.ArrayList;

class ConnectFourMoveGenerator extends MoveGenerator {
	public ConnectFourMoveGenerator(Board board) {
		super(board);
	}

	protected ArrayList<Tile> getValidTiles() {
		ArrayList<Tile> validTiles = new ArrayList<Tile>();

		for (int j = 0; j < board.width; j++) {
			for (int i = board.height -1; i >= 0; i--) {
				// if unoccupied, add then break;
				Tile t = board.tiles[i][j];
				if (!t.isOccupied) {
					validTiles.add(t);
					break;
				}
			}
		}

		return validTiles;
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

		MoveGenerator cfmg = new ConnectFourMoveGenerator(b);

		int[] moveCoords = cfmg.getMoveCoords();

		System.out.println(Integer.toString(moveCoords[0]) + ", " + Integer.toString(moveCoords[1]));
		
	}
}