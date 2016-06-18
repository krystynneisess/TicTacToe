package com.kn.ttt;

/**
 * BRAINSTORMING
 * 
 * Game:
 * - Has a Board
 * - Has a GameOverCheck
 * - Has a MoveGenerator
 * - Tracks whose turn it is
 * 
 * Board:
 * - Has an nxm array of Tiles
 * 
 * Tile:
 * - x and y coords
 * - isOccupied:  True/False
 * - Player:  A/B/Null
 * 
 *
 * GameOverCheck:
 * - Examines Board for existence of connection of length l 
 *   in valid direction.
 * 
 * MoveGenerator:
 * - Examines Board to identify valid tiles (additional constraints for Connect4).
 * - Randomly chooses move among valid tiles.
 * 
 * 
 */
class Game {
	Board board;
	MoveGenerator mg;
	GameOverCheck goc;

	String[] players;
	String playerTurn;


	public Game() {
		board = new Board(3, 3);
		mg = new MoveGenerator(board);
		goc = new GameOverCheck(board, mg, 3, new boolean[] {true, true, true, true});

		players = new String[] {"A", "B"};
		playerTurn = players[0];
	}

	public boolean performMove() {
		int[] moveCoords = mg.getMoveCoords();
		int x = moveCoords[0];
		int y = moveCoords[1];

		if (moveCoords != null) {
			board.tiles[x][y].isOccupied = true;
			board.tiles[x][y].player = playerTurn;
			playerTurn = nextPlayer();

			return true;
		}
		return false;
	}

	public boolean performMove(int x, int y) {
		if (board.tiles[x][y].isOccupied == true) {
			return false;
		} 
		
		board.tiles[x][y].isOccupied = true;
		board.tiles[x][y].player = playerTurn;
		playerTurn = nextPlayer();

		return true;
	}

	public boolean isGameOver() {
		if (goc.isOver()) {
			return true;
		}
		return false;
	}

	public void resetGame() {
		board = new Board(3, 3);
		mg = new MoveGenerator(board);
		goc = new GameOverCheck(board, mg, 3, new boolean[] {true, true, true, true});

		players = new String[] {"A", "B"};
		playerTurn = players[0];
	}

	private String nextPlayer() {
		if (playerTurn.equals(players[0])) {
			return players[1];
		} else {
			return players[0];
		}
	}

	public void printBoard() {
		for (int i = 0; i < board.height; i++) {
			for (int j = 0; j < board.width; j++) {
				String p = board.tiles[i][j].player;

				if (p == null) {
					System.out.print("-");
				} else {
					System.out.print(p);
				}
			}
			System.out.println();
		}
	}
	
	// Because I was too lazy to write actual unit tests...
	public static void main(String[] args) {
		Game g = new Game();
		g.board = new Board(4, 4);
		g.board.tiles[0][0].isOccupied = true;
		g.board.tiles[0][0].player = "A";
		g.board.tiles[2][0].isOccupied = true;
		g.board.tiles[2][0].player = "B";
		g.board.tiles[3][0].isOccupied = true;
		g.board.tiles[3][0].player = "A";
		g.board.tiles[1][1].isOccupied = true;
		g.board.tiles[1][1].player = "B";
		g.board.tiles[3][1].isOccupied = true;
		g.board.tiles[3][1].player = "B";
		g.board.tiles[1][2].isOccupied = true;
		g.board.tiles[1][2].player = "B";
		g.board.tiles[2][2].isOccupied = true;
		g.board.tiles[2][2].player = "A";
		g.board.tiles[3][2].isOccupied = true;
		g.board.tiles[3][2].player = "A";
		g.board.tiles[1][3].isOccupied = true;
		g.board.tiles[1][3].player = "A";

		g.printBoard();
	}

}