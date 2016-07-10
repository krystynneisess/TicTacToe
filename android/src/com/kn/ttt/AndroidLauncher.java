package com.kn.ttt;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.kn.ttt.TicTacToeGame;

public class AndroidLauncher extends AndroidApplication implements TicTacToeGame.MyGameCallback{
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		TicTacToeGame tttG = new TicTacToeGame();
		tttG.setMyGameCallback(this);

		initialize(new TicTacToeGame(), config);
	}

	@Override
	public void onStartMainMenuActivity() {
		Intent mainMenuIntent = new Intent(this, MainMenuActivity.class);
		startActivity(mainMenuIntent);
	}
}
