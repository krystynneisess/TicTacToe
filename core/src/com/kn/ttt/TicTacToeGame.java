package com.kn.ttt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
// import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;
import java.lang.Math;
import java.util.ArrayList;

public class TicTacToeGame extends ApplicationAdapter implements InputProcessor {
	public static final int FRAME_COLS = 5;
	public static final int FRAME_ROWS = 3;

	public static final String TAG_DEBUG = "TAG_DEBUG";
	public static final String TAG_RENDER = "TAG_RENDER";

	// Batch
	SpriteBatch batch;
	Texture img;

	// Explosion animation
	Texture explosionSheet;
	TextureRegion[] explosionFrames;
	TextureRegion currentFrame;

	Animation explosionAnimation;
	float stateTime;
	boolean explosionHappening;

	// Camera
	OrthographicCamera camera;

	// Shape rendering
	ShapeRenderer sr;

	// Useful size params
	float w;
	float h;
	float s;
	float lw;

	float boardLeftEdge;  // in world coordinates
	float boardRightEdge;
	float boardTopEdge;
	float boardBottomEdge;
	float boardTileLen;

	// Input handling
	Vector3 touchPoint;
	float touchCoordinateX;
	float touchCoordinateY;

	// Game state
	TicTacToe gameState;


	@Override
	public void create () {
//		Gdx.app.log(TAG_DEBUG, "calling create()");
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		touchPoint = new Vector3();
		explosionSheet = new Texture(Gdx.files.internal("explosion.png"));
		sr = new ShapeRenderer();
		gameState = new TicTacToe();

        prepareAnimation();
        calculateUsefulNumbers();
        Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		// Render background and badlogic logo
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

//		batch.draw(img, 0, 0);
		batch.end();

		// Render tic-tac-toe board

		drawBoard();

//		ArrayList<Vector2> EXs = new ArrayList<Vector2>();
//		EXs.add(new Vector2(0f, 0f));
//		EXs.add(new Vector2(1f, 1f));
//		EXs.add(new Vector2(2f, 2f));
//
//		ArrayList<Vector2> OHs = new ArrayList<Vector2>();
//		OHs.add(new Vector2(0f, 1f));
//		OHs.add(new Vector2(0f, 2f));
//		OHs.add(new Vector2(2f, 1f));
//
//		for (Vector2 ex:EXs) {
//			drawXPiece(w, h, s, lineWidth, ex);
//		}
//
//		for (Vector2 oh:OHs) {
//			drawOPiece(w, h, s, lineWidth, oh);
//		}

		// Render explosion animation
		stateTime += Gdx.graphics.getDeltaTime();

		if (!explosionAnimation.isAnimationFinished(stateTime) && explosionHappening) {
			currentFrame = explosionAnimation.getKeyFrame(stateTime, false);
			// Converts touch input coordinates from screen to world coordinates.
			// Stores said world coordinates in Vector3 touchPoint.
			batch.begin();
			batch.draw(currentFrame, touchPoint.x, touchPoint.y);
			batch.end();
		}
	}


	public void calculateUsefulNumbers() {
		w = camera.viewportWidth;
		h = camera.viewportHeight;
		s = 900;
		lw = s/30;

		boardLeftEdge = w/2 - s/2;
		boardRightEdge = w/2 + s/2;
		boardTopEdge = h/2 + s/2;
		boardBottomEdge = h/2 - s/2;
		boardTileLen = s/3;
	}

	public void prepareAnimation() {
		// Acquire animation frames
		TextureRegion[][] textureRegions = TextureRegion.split(explosionSheet, explosionSheet.getWidth() / FRAME_COLS, explosionSheet.getHeight() / FRAME_ROWS);
		explosionFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];

		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				explosionFrames[index++] = textureRegions[i][j];
			}
		}

		// Create animation
		explosionAnimation = new Animation(0.025f, explosionFrames);
		stateTime = 0;

		// Create camera
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(camera.viewportWidth * 0.5f, camera.viewportHeight * 0.5f, 0f);
		camera.update();

//		Gdx.app.log(TAG_RENDER, "Gdx.graphics.get<Width,Height>():  (" + Float.toString(Gdx.graphics.getWidth()) + ", " + Float.toString(Gdx.graphics.getHeight()) + ")");
//		Gdx.app.log(TAG_RENDER, "camera.viewport<Width,Height>():  (" + Float.toString(camera.viewportWidth) + ", " + Float.toString(camera.viewportHeight) + ")");
	}


	// Board drawing convenience methods
	// w = camera width; h = camera height; s = board side length;
	// lineWidth = length of grid line
	public void drawBoard() {
		sr.setProjectionMatrix(camera.combined);
		sr.begin(ShapeRenderer.ShapeType.Filled);
		sr.setColor(Color.BLACK);
		sr.rect(w/2-s/2, h/2-s/2, s, s);
		sr.end();

		sr.begin(ShapeRenderer.ShapeType.Filled);
		sr.setColor(Color.WHITE);
		sr.rectLine(w/2-s/6, h/2-(s/2-lw), w/2-s/6, h/2+(s/2-lw), lw); // vert
		sr.rectLine(w/2+s/6, h/2-(s/2-lw), w/2+s/6, h/2+(s/2-lw), lw);
		sr.rectLine(w/2-(s/2-lw), h/2-s/6, w/2+(s/2-lw), h/2-s/6, lw); // horiz
		sr.rectLine(w/2-(s/2-lw), h/2+s/6, w/2+(s/2-lw), h/2+s/6, lw);
		sr.end();

        for (Tile[] tRow:gameState.board.tiles) {
            for (Tile t:tRow) {
                if (t.isOccupied == false) {
                    continue;
                }

                Vector2 pos = new Vector2();
                pos.x = t.x;
                pos.y = t.y;

                if(t.player.equals("A")) {
                    drawXPiece(pos);
                } else {
                    drawOPiece(pos);
                }
            }
        }
	}

	public void drawXPiece(Vector2 pos) {
		sr.setProjectionMatrix(camera.combined);
		sr.begin(ShapeRenderer.ShapeType.Filled);
		sr.setColor(0.5f, 0.5f, 0.5f, 1f);
		Vector2 basePos = new Vector2(w/2-s/3, h/2+s/3);
		Vector2 piecePos = new Vector2(basePos.x + pos.x * s/3, basePos.y - pos.y * s/3);
		float d = s/9; //width/height of x piece
		sr.rectLine(piecePos.x-d/2, piecePos.y+d/2, piecePos.x+d/2, piecePos.y-d/2, lw);
		sr.rectLine(piecePos.x+d/2, piecePos.y+d/2, piecePos.x-d/2, piecePos.y-d/2, lw);
//		sr.x(basePos.x + pos.x * s/3, basePos.y - pos.y * s/3, s/8);
		sr.end();
	}

	public void drawOPiece(Vector2 pos) {
		sr.setProjectionMatrix(camera.combined);
		sr.begin(ShapeRenderer.ShapeType.Filled);
		sr.setColor(0.75f, 0.75f, 0.75f, 1f);
		Vector2 basePos = new Vector2(w/2-s/3, h/2+s/3);
		Vector2 piecePos = new Vector2(basePos.x + pos.x * s/3, basePos.y - pos.y * s/3);
		float d = s/9 + (float)Math.sqrt(2) * lw; //width/height of x
		sr.circle(piecePos.x, piecePos.y, d/2);

		sr.setColor(Color.BLACK);
		sr.circle(piecePos.x, piecePos.y, d/2-lw);
		sr.end();
	}

	// Input to Game Logic translation
	public boolean processMove() {
		if (touchPoint.x > boardRightEdge || touchPoint.x < boardLeftEdge ||
				touchPoint.y > boardTopEdge || touchPoint.y <boardBottomEdge) {
			return false;
		}

		int xIndex;
		int yIndex;

		// set x
		if (touchPoint.x < boardLeftEdge + boardTileLen) {
			xIndex = 0;
		} else if(touchPoint.x < boardLeftEdge + 2*boardTileLen) {
			xIndex = 1;
		} else {
			xIndex = 2;
		}

		// set y
		if (touchPoint.y > boardTopEdge - boardTileLen) {
			yIndex = 0;
		} else if(touchPoint.y > boardTopEdge - 2*boardTileLen) {
			yIndex = 1;
		} else {
			yIndex = 2;
		}

		gameState.performMove(xIndex, yIndex);

        Gdx.app.log(TAG_RENDER, "(" + Integer.toString(xIndex) + ", " + Integer.toString(yIndex) + ")");
        return true;
	}

    public boolean resetIfOver() {
        if (gameState.isGameOver()) {
            gameState.resetGame();
            return true;
        }
        return false;
    }


	// Input Processor overridden methods
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touchCoordinateX = screenX;
		touchCoordinateY = screenY;
		camera.unproject(touchPoint.set(touchCoordinateX, touchCoordinateY, 0f));

		stateTime = 0;
		explosionHappening = true;

		processMove();
//        resetIfOver();

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int button) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
