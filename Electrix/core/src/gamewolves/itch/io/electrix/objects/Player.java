package gamewolves.itch.io.electrix.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

import box2dLight.ConeLight;
import gamewolves.itch.io.electrix.Main;
import gamewolves.itch.io.electrix.input.InputHandler;
import gamewolves.itch.io.electrix.physics.Physics;

public class Player
{
    private static final float MaxSpeed = 500;
    private static final float MinSpeed = 100;
    private static final float BaseEnergyLoss = .03f;
    private static final float BaseEnergyGain = .25f;
    private static final float MaxAngle = 45f;

    private AnimatedSprite sprite;
    private Texture playerIdleTexture;
    private Animation<TextureRegion> playerIdle;
    private ConeLight light;
    private Body body;

    private float energy;

    public Player()
    {
        Array<TextureRegion> frames = new Array<>();
        playerIdleTexture = new Texture(Gdx.files.internal("player_idle.png"));
        for (int i = 0; i < 4; i++)
            frames.add(new TextureRegion(playerIdleTexture, i * 32, 0, 32, 32));

        playerIdle = new Animation<>(0.25f, frames);
        playerIdle.setPlayMode(Animation.PlayMode.LOOP);
        sprite = new AnimatedSprite(playerIdle);
        sprite.setOriginCenter();
        sprite.setPosition(Main.Camera.viewportWidth / 2, Main.Camera.viewportHeight / 2);
        sprite.play();

        light = new ConeLight(Physics.getRayHandler(), 500, new Color(0.8f, 0.8f, 0.8f, 1), 1000, sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2, sprite.getRotation(), MaxAngle);
        light.setSoft(true);
        light.setSoftnessLength(50);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;

        body = Physics.getWorld().createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2, sprite.getHeight() / 2);

        body.createFixture(shape, 1);

        body.setTransform(0, 100, 0);



        energy = 1;
    }

    public void handleInput(float dt, boolean isControllerConnected, Vector2 controllerSpeed)
    {
        if (!isControllerConnected)
            sprite.setRotation(InputHandler.CurrentMousePos.sub(new Vector2(sprite.getX() + sprite.getOriginX(), sprite.getY() + sprite.getOriginY())).angle());

        Vector2 speed = new Vector2();

        if (Gdx.input.isKeyPressed(Input.Keys.W))
            speed.y = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            speed.x = -1;
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            speed.y = -1;
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            speed.x = 1;

        if (isControllerConnected) {
            speed = controllerSpeed.cpy();

            Vector2 value = new Vector2(Controllers.getControllers().first().getAxis(1), -Controllers.getControllers().first().getAxis(0));

            if (value.len() > 0.05f)
                sprite.setRotation(value.angle());
        }

        speed.nor();
        speed.scl(Math.max(energy * MaxSpeed, MinSpeed));

        body.setLinearVelocity(speed);
    }

    public void update(float dt)
    {
        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
        sprite.update(dt);
        light.setConeDegree(MaxAngle * energy);
        light.setDirection(sprite.getRotation());
        light.setPosition(sprite.getX() + sprite.getOriginX(), sprite.getY() + sprite.getOriginY());

        Main.Camera.position.set(sprite.getX() + sprite.getOriginX(), sprite.getY() + sprite.getOriginY(), 0);

        if (body.getPosition().len() < 300)
            energy += BaseEnergyGain * dt;

        energy -= BaseEnergyLoss * dt;
        energy = Math.min(Math.max(energy, 0), 1);
    }

    public void render(SpriteBatch batch)
    {
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    public float getEnergy()
    {
        return energy;
    }
}
