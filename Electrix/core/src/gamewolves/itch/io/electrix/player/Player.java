package gamewolves.itch.io.electrix.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

import box2dLight.ConeLight;
import gamewolves.itch.io.electrix.Main;
import gamewolves.itch.io.electrix.input.InputHandler;
import gamewolves.itch.io.electrix.physics.Physics;

public class Player
{
    private static final float Speed = 100;

    private AnimatedSprite sprite;
    private Texture playerIdleTexture;
    private Animation<TextureRegion> playerIdle;
    private ConeLight light;

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

        light = new ConeLight(Physics.getRayHandler(), 500, Color.GOLD, 500, sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2, sprite.getRotation(), 30);
    }

    public void handleInput(float dt)
    {
        sprite.setRotation(InputHandler.CurrentMousePos.sub(new Vector2(sprite.getX() + sprite.getOriginX(), sprite.getY() + sprite.getOriginY())).angle());

        if (Gdx.input.isKeyPressed(Input.Keys.W))
            sprite.translateY(Speed * dt);
        else if (Gdx.input.isKeyPressed(Input.Keys.A))
            sprite.translateX(-Speed * dt);
        else if (Gdx.input.isKeyPressed(Input.Keys.S))
            sprite.translateY(-Speed * dt);
        else if (Gdx.input.isKeyPressed(Input.Keys.D))
            sprite.translateX(Speed * dt);
    }

    public void update(float dt)
    {
        sprite.update(dt);
        light.setDirection(sprite.getRotation());
        light.setPosition(sprite.getX() + sprite.getOriginX(), sprite.getY() + sprite.getOriginY());
    }

    public void render(SpriteBatch batch)
    {
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }
}
