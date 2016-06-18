package com.kn.ttt;

class Tile {
	int x;
	int y;
	boolean isOccupied;
	String player; 

	public Tile() {
		x = -1;
		y = -1;
		isOccupied = false;
		player = null;
	}
}