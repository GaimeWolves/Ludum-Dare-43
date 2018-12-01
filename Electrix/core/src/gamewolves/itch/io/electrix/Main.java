package gamewolves.itch.io.electrix;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import gamewolves.itch.io.electrix.input.InputHandler;
import gamewolves.itch.io.electrix.physics.Physics;
import gamewolves.itch.io.electrix.states.Game;
import gamewolves.itch.io.electrix.states.State;

public class Main extends ApplicationAdapter
{
    public static final float MPP = 0.01f;

    public static float ElapsedTime;
	public static OrthographicCamera Camera;
    public static int Width;
    public static int Heigth;

    private Box2DDebugRenderer debugRenderer;
    private boolean isFullscreen;
	private SpriteBatch batch;
	private SpriteBatch uiBatch;

	private boolean debug;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		uiBatch = new SpriteBatch();

		isFullscreen = false;
		debug = false;

        Gdx.graphics.setWindowedMode(Width, Heigth);

        Camera = new OrthographicCamera(1280, 720);
        Camera.translate(1280 / 2, 720 / 2);
        Camera.update();
        uiBatch.setProjectionMatrix(Camera.combined);

        Physics.init();
        Physics.setContactListener();
        State.setCurrent(new Game());

        debugRenderer = new Box2DDebugRenderer();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update();

		if (Gdx.input.isKeyJustPressed(Input.Keys.F2))
		    debug = !debug;

        batch.setProjectionMatrix(Camera.combined);
        State.getCurrentState().render(batch);
        State.getCurrentState().renderUI(uiBatch);
        if (debug)
            debugRenderer.render(Physics.getWorld(), Camera.combined.cpy().scl(1 / Main.MPP, 1 / Main.MPP, 0));
	}

	private void update()
    {
        ElapsedTime += Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1))
        {
            if (isFullscreen)
            {
                Gdx.graphics.setWindowedMode(Width, Heigth);
            }
            else
            {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
            isFullscreen = !isFullscreen;
        }
        Camera.update();
        InputHandler.update();

        if(!State.getCurrentState().disposeable)
            State.getCurrentState().update(Gdx.graphics.getDeltaTime());
    }

    public static void calculateWindowSize()
    {
        int screenWidth = Gdx.graphics.getDisplayMode().width;
        int screenHeight = Gdx.graphics.getDisplayMode().height;

        if (screenWidth > 4096 && screenHeight > 2160) { Width = 4096; Heigth = 2160; return; }
        if (screenWidth > 2560 && screenHeight > 1440) { Width = 2560; Heigth = 1440; return; }
        if (screenWidth > 1920 && screenHeight > 1080) { Width = 1920; Heigth = 1080; return; }
        if (screenWidth > 1600 && screenHeight > 900 ) { Width = 1600; Heigth = 900 ; return; }
        if (screenWidth > 1280 && screenHeight > 720 ) { Width = 1280; Heigth = 720 ; return; }
        if (screenWidth > 1024 && screenHeight > 576 ) { Width = 1024; Heigth = 576 ;         }
    }
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
