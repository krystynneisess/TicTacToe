package com.kn.ttt;

import java.util.ArrayList;
import java.util.Random;

class MoveGenerator {
	Board board;

	public MoveGenerator(Board b) {
		board = b;
	}

	public boolean movesRemain() {
		if (getValidTiles().size() == 0) {
			return false;
		}
		return true;
	}

	public int[] getMoveCoords() {
		ArrayList<Tile> validTiles = getValidTiles();

		if (validTiles.size() == 0) {
			return null;
		}

		int rand = new Random().nextInt(validTiles.size());
		Tile move = validTiles.get(rand);
		int[] moveCoords = new int[] {move.x, move.y};

		return moveCoords;
	}

	protected ArrayList<Tile> getValidTiles() {
		ArrayList<Tile> validTiles = new ArrayList<Tile>();

		for (Tile[] tRow : board.tiles) {
			for (Tile t : tRow) {
				if (!t.isOccupied) {
					validTiles.add(t);
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

		MoveGenerator mg = new MoveGenerator(b);

		int[] moveCoords = mg.getMoveCoords();

		System.out.println(Integer.toString(moveCoords[0]) + ", " + Integer.toString(moveCoords[1]));
	}

}