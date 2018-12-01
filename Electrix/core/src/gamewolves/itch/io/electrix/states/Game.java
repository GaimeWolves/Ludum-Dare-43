package gamewolves.itch.io.electrix.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

import gamewolves.itch.io.electrix.Main;
import gamewolves.itch.io.electrix.objects.Generator;
import gamewolves.itch.io.electrix.objects.Shot;
import gamewolves.itch.io.electrix.physics.Physics;
import gamewolves.itch.io.electrix.objects.Player;

public class Game extends State implements ControllerListener
{
    public static Game Instance;

    private Player player;
    private Generator generator;

    public Array<Shot> shots;

    private Texture world;

    private Texture energyFrame, energyBarTexture;
    private Animation<TextureRegion> energyBarAnimation;

    private Vector2 controllerAxis;

    public Game()
    {
        super();
    }

    public Game(Main instance)
    {
        super(instance);
    }

    @Override
    public void init()
    {
        Instance = this;
        shots = new Array<>();

        world = new Texture(Gdx.files.internal("bg.png"));
        player = new Player();
        generator = new Generator();

        Controllers.addListener(this);
        controllerAxis = Vector2.Zero.cpy();

        energyFrame = new Texture(Gdx.files.internal("power_bar.png"));
        Array<TextureRegion> frames = new Array<>();
        energyBarTexture = new Texture(Gdx.files.internal("power_content.png"));
        for (int i = 0; i < 4; i++)
            frames.add(new TextureRegion(energyBarTexture, i * 32, 0, 32, 128));

        energyBarAnimation = new Animation<>(0.25f, frames);
        energyBarAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    @Override
    public void TouchEvent(Vector2 position)
    {

    }

    @Override
    public void HoldEvent(Vector2 position)
    {

    }

    @Override
    public void DragEvent(Vector2 start, Vector2 current, Vector2 delta)
    {

    }

    @Override
    public void DragEventEnd(Vector2 end)
    {

    }

    private void handleInput(float dt)
    {
        player.handleInput(dt, Controllers.getControllers().size > 0, controllerAxis);
    }

    @Override
    public void update(float deltaTime)
    {
        Physics.update(deltaTime);
        handleInput(deltaTime);

        player.update(deltaTime);
        generator.update(deltaTime);

        shots.forEach(shot -> shot.update(deltaTime));

        for (int i = 0; i < shots.size; i++)
            if (shots.get(i).disposeable)
                shots.removeIndex(i--);

        System.out.println(shots.size);
    }

    @Override
    public void render(SpriteBatch batch)
    {
        batch.begin();
        batch.draw(world, -world.getWidth() / 2, -world.getHeight() / 2);
        batch.end();

        Physics.render();

        generator.render(batch);

        batch.begin();
        shots.forEach(shot -> shot.render(batch));
        batch.end();

        player.render(batch);
    }

    @Override
    public void renderUI(SpriteBatch batch)
    {
        batch.begin();
        int srcY = (int)((1 - player.getEnergy()) * energyBarTexture.getHeight());
        int srcHeight = (int) (energyBarTexture.getHeight() - (1 - player.getEnergy()) * energyBarTexture.getHeight());
        TextureRegion currentFrame = energyBarAnimation.getKeyFrame(Main.ElapsedTime);

        batch.draw(energyBarTexture, Main.Camera.viewportWidth - 10 - energyFrame.getWidth(), 10, currentFrame.getRegionX(), srcY, currentFrame.getRegionWidth(), srcHeight);

        batch.draw(energyFrame, Main.Camera.viewportWidth - 10 - energyFrame.getWidth(), 10);
        batch.end();
    }

    @Override
    public void dispose()
    {

    }

    @Override
    public void connected(Controller controller) {

    }

    @Override
    public void disconnected(Controller controller) {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        if (axisCode == 2)
        {
            if (Math.abs(value) > 0.1f)
                controllerAxis.y = -value;
            else
                controllerAxis.y = 0;
        }
        else if (axisCode == 3)
        {
            if (Math.abs(value) > 0.1f)
                controllerAxis.x = value;
            else
                controllerAxis.x = 0;
        }


        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }
}
