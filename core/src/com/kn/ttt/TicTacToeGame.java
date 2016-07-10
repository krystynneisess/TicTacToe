package com.kn.ttt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.sun.org.apache.bcel.internal.generic.FRETURN;
// import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;
import java.lang.Math;
import java.util.ArrayList;


public class TicTacToeGame extends ApplicationAdapter implements InputProcessor {
	public static final int FRAME_COLS = 5;

	public static final int FRAME_ROWS = 3;
	public static final String TAG_DEBUG = "TAG_DEBUG";

	public static final String TAG_RENDER = "TAG_RENDER";

	// Callbacks
	public interface MyGameCallback {
		public void onStartMainMenuActivity();
	}

	private static MyGameCallback myGameCallback;

	public void setMyGameCallback(MyGameCallback callback) {
		myGameCallback = callback;
	}

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
	float w; // screen width
	float h; // screen height
	float s; // board side length
	float lw; // board line width

	float boardLeftEdge;  // in world coordinates
	float boardRightEdge;
	float boardTopEdge;
	float boardBottomEdge;
	float boardTileLen;

	// Input handling
	Vector3 touchPoint;
	float touchCoordinateX;
	float touchCoordinateY;

	Stage stage;
//	Table table;
	Skin skin;
	TextButton resetButton;
	TextButton menuButton;
	TextButton.TextButtonStyle textButtonStyle;

	// Fonts
	BitmapFont font;


	// Game state
	TicTacToe gameState;

	@Override
	public void create () {
//		Gdx.app.log(TAG_DEBUG, "calling create()");
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		explosionSheet = new Texture(Gdx.files.internal("explosion.png"));
		touchPoint = new Vector3();
		sr = new ShapeRenderer();
		gameState = new TicTacToe();

        prepareAnimation();
		prepareButtons();
        calculateUsefulNumbers();

//        Gdx.input.setInputProcessor(this);
//        Gdx.input.setInputProcessor(stage);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
//        multiplexer.addProcessor(new InputAdapter() {
//            @Override
//            public boolean touchDown(int x, int y, int pointer, int button) {
//                Gdx.app.log(TAG_DEBUG, "BUTTON INT P1:  " + Integer.toString(button));
//                return true;
//            }
//        });
//        multiplexer.addProcessor(new InputAdapter() {
//            @Override
//            public boolean touchDown(int x, int y, int pointer, int button) {
//                Gdx.app.log(TAG_DEBUG, "BUTTON INT P2:  " + Integer.toString(button));
//
////                touchCoordinateX = x;
////                touchCoordinateY = y;
////                camera.unproject(touchPoint.set(touchCoordinateX, touchCoordinateY, 0f));
////
////                stateTime = 0;
////                explosionHappening = true;
////
////                processMove();
//
//                return false;
//            }
//        });
        Gdx.input.setInputProcessor(multiplexer);
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

		// Render stage
		stage.draw();

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

	@Override
	public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
//		camera.update();
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}



////////////////////////////////////////////////////////////
// INITIALIZATION HELPERS //////////////////////////////////
////////////////////////////////////////////////////////////
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

	public void prepareButtons() {
		stage = new Stage();

//		table = new Table();
//		table.setBounds(0f, 0f, w, h);

        // Skin #1
		skin = new Skin();
		Pixmap pixmap = new Pixmap(500, 150, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

		font = generateBitmapFont(60);
		skin.add("white", new Texture(pixmap));
		skin.add("defaultFont", font);

		textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.BLACK);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.font = skin.getFont("defaultFont");

        skin.add("default", textButtonStyle);

        // Skin #2
//        Skin skin2 = skin;
//
//        Pixmap pixmapLa = new Pixmap(1000, 150, Pixmap.Format.RGBA8888);
//        pixmapLa.setColor(Color.WHITE);
//        pixmapLa.fill();
//
//        TextButton.TextButtonStyle textButtonStyle2 = new TextButton.TextButtonStyle();
//        textButtonStyle2.up = skin.newDrawable("white", Color.BLACK);
//        textButtonStyle2.down = skin.newDrawable("white", Color.DARK_GRAY);
//        textButtonStyle2.font = skin.getFont("defaultFont");
//
//        skin2.add("default", textButtonStyle2);


		// RESET BUTTON
		resetButton = new TextButton("Reset Game", skin);
		int scW = Gdx.graphics.getWidth();
		int scH = Gdx.graphics.getHeight();
		float rbW = resetButton.getWidth();
		float rbH = resetButton.getHeight();
		float rbX = scW/2-rbW/2;
		float rbY = scH/4-rbH/2;
		resetButton.setPosition(rbX, rbY);
//		Gdx.app.log(TAG_DEBUG, Float.toString(w));
//        Gdx.app.log(TAG_DEBUG, Float.toString(h));
//		resetButton.setPosition(200, 200);

		stage.addActor(resetButton);

		resetButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameState.resetGame();
                Gdx.app.log(TAG_DEBUG, "Game reset!");
            }
		});

		// MENU BUTTON
		menuButton = new TextButton("Return to Main Menu", skin);
		float mbW = menuButton.getWidth();
		float mbH = menuButton.getWidth();
		float mbX = scW/2-mbW/2;
		float mbY = scH/5-mbH/2;
//		menuButton.setPosition(mbX, mbY);
		menuButton.setPosition(mbX, mbY);
//        menuButton.pad(10f);

		stage.addActor(menuButton);

		menuButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				myGameCallback.onStartMainMenuActivity();
                Gdx.app.log(TAG_DEBUG, "Switched to Main Menu!");
			}
		});


	}



////////////////////////////////////////////////////////////
// DRAWING HELPERS /////////////////////////////////////////
////////////////////////////////////////////////////////////

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

	public BitmapFont generateBitmapFont(int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal
				("fonts/hetilica.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator
				.FreeTypeFontParameter();
		parameter.size = size;

		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();

		return font;
	}




////////////////////////////////////////////////////////////
// GAME LOGIC HELPERS //////////////////////////////////////
////////////////////////////////////////////////////////////

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






////////////////////////////////////////////////////////////
// INPUT LISTENERS /////////////////////////////////////////
////////////////////////////////////////////////////////////
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
