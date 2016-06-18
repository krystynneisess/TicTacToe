package com.kn.ttt;

class Board {
	int width;
	int height;
	Tile[][] tiles;

	public Board(int n, int m) {
		width = n;
		height = m;
		tiles = new Tile[n][m];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				tiles[i][j] = new Tile();
				tiles[i][j].x = i;
				tiles[i][j].y = j;
			}
		}
	}
}