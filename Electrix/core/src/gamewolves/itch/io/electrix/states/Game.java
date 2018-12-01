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
import gamewolves.itch.io.electrix.objects.Enemy;
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
    private Array<Enemy> enemies;

    private Body worldCollider;

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
        enemies = new Array<>();

        enemies.add(new Enemy());

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

        createWorld();
    }

    private void createWorld()
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        worldCollider = Physics.getWorld().createBody(bodyDef);

        addBox(2190, 2430, 540, 60);
        addBox(0, 0, 4020, 30);
        addBox(3990, 30, 30, 3990);
        addBox(0, 3990, 3990, 30);
        addBox(0, 30, 30, 3960);
        addBox(30, 750, 240, 60);
        addBox(510, 450, 120, 60);
        addBox(510, 150, 60, 300);
        addBox(570, 270, 240, 60);
        addBox(2010, 30, 60, 360);
        addBox(3630, 1110, 360, 60);
        addBox(870, 570, 360, 60);
        addBox(1230, 390, 60, 240);
        addBox(1230, 630, 300, 60);
        addBox(1470, 510, 60, 120);
        addBox(1530, 510, 180, 60);
        addBox(1650, 330, 60, 180);
        addBox(2370, 390, 240, 60);
        addBox(2370, 450, 60, 240);
        addBox(2310, 690, 120, 60);
        addBox(2850, 30, 60, 660);
        addBox(2670, 690, 240, 60);
        addBox(2670, 210, 180, 60);
        addBox(2910, 390, 780, 60);
        addBox(2010, 630, 60, 540);
        addBox(2070, 870, 60, 60);
        addBox(510, 930, 60, 300);
        addBox(390, 1230, 240, 60);
        addBox(390, 1290, 60, 600);
        addBox(210, 1890, 480, 60);
        addBox(930, 930, 60, 660);
        addBox(990, 1530, 420, 60);
        addBox(870, 1230, 60, 60);
        addBox(1230, 1110, 60, 120);
        addBox(1290, 1170, 420, 60);
        addBox(1650, 870, 60, 300);
        addBox(1530, 870, 120, 60);
        addBox(1650, 1350, 60, 60);
        addBox(1650, 1410, 240, 60);
        addBox(1350, 2070, 180, 60);
        addBox(1470, 2130, 60, 60);
        addBox(930, 1950, 60, 960);
        addBox(630, 2190, 300, 60);
        addBox(30, 2370, 360, 60);
        addBox(30, 2430, 60, 840);
        addBox(30, 3270, 360, 60);
        addBox(330, 3330, 60, 360);
        addBox(390, 3630, 900, 60);
        addBox(1230, 3690, 60, 300);
        addBox(1290, 3870, 240, 120);
        addBox(450, 2610, 180, 240);
        addBox(1230, 2310, 60, 960);
        addBox(990, 3210, 240, 60);
        addBox(1290, 2850, 180, 60);
        addBox(1590, 2490, 180, 60);
        addBox(1770, 2430, 60, 180);
        addBox(1710, 2550, 60, 60);
        addBox(1770, 2610, 420, 60);
        addBox(1830, 2670, 60, 60);
        addBox(2130, 2670, 60, 540);
        addBox(2070, 2790, 60, 60);
        addBox(1710, 3150, 420, 60);
        addBox(1890, 3510, 60, 360);
        addBox(1950, 3510, 300, 60);
        addBox(2190, 3570, 60, 240);
        addBox(2250, 3570, 60, 60);
        addBox(3030, 3150, 180, 180);
        addBox(3570, 3030, 60, 360);
        addBox(2550, 3630, 900, 60);
        addBox(3390, 3690, 60, 180);
        addBox(3450, 3810, 360, 60);
        addBox(3930, 2550, 60, 240);
        addBox(3750, 2730, 180, 60);
        addBox(2490, 3270, 120, 60);
        addBox(2490, 2730, 60, 540);
        addBox(2550, 2730, 300, 60);
        addBox(2790, 2790, 60, 120);
        addBox(2850, 2850, 180, 60);
        addBox(2970, 2730, 60, 120);
        addBox(3030, 2730, 420, 60);
        addBox(2190, 2430, 540, 60);
        addBox(2250, 2250, 60, 180);
        addBox(2310, 2250, 60, 60);
        addBox(2670, 2250, 60, 180);
        addBox(2730, 2250, 240, 60);
        addBox(2910, 2310, 60, 180);
        addBox(2970, 2430, 480, 60);
        addBox(3390, 2190, 60, 240);
        addBox(3450, 2190, 60, 60);
        addBox(2850, 1830, 60, 180);
        addBox(2910, 1830, 780, 60);
        addBox(3330, 811, 361, 60);
        addBox(3270, 750, 60, 780);
        addBox(3090, 990, 180, 120);
        addBox(3330, 1470, 360, 60);
        addBox(2370, 1470, 120, 60);
        addBox(2430, 990, 60, 480);
        addBox(2490, 990, 180, 60);
        addBox(2490, 1350, 420, 60);
        addBox(2850, 1410, 60, 120);
    }

    private void addBox(float x, float y, float width, float height)
    {
        y = world.getHeight() - y;

        x -= world.getWidth() / 2;
        y -= world.getHeight() / 2;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width * 0.5f * Main.MPP, height * 0.5f * Main.MPP, (new Vector2(x + width / 2, y - height / 2)).scl(Main.MPP), 0);

        worldCollider.createFixture(shape, 1);

        shape.dispose();
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

        if (Math.round(Main.ElapsedTime) % 5 == 0)
            enemies.add(new Enemy());

        player.update(deltaTime);
        generator.update(deltaTime);

        shots.forEach(shot -> shot.update(deltaTime));

        for (int i = 0; i < shots.size; i++)
            if (shots.get(i).disposeable)
                shots.removeIndex(i--).delete();

        enemies.forEach(enemy -> enemy.update(deltaTime));

        for (int i = 0; i < enemies.size; i++)
            if (enemies.get(i).disposeable)
                enemies.removeIndex(i--).delete();
    }

    @Override
    public void render(SpriteBatch batch)
    {
        batch.begin();
        batch.draw(world, -world.getWidth() / 2, -world.getHeight() / 2);

        enemies.forEach(enemy -> enemy.render(batch));
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

    public Shot getShotByBody(Body body)
    {
        for (Shot shot : shots)
            if (shot.getBody() == body)
                return shot;
        return null;
    }

    public Enemy getEnemyByBody(Body body)
    {
        for (Enemy shot : enemies)
            if (shot.getBody() == body)
                return shot;
        return null;
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
