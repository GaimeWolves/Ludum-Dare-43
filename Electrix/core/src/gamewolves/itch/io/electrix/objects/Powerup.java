package gamewolves.itch.io.electrix.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import gamewolves.itch.io.electrix.Main;
import gamewolves.itch.io.electrix.physics.Filters;
import gamewolves.itch.io.electrix.physics.Physics;

public class Powerup
{
    public static final Vector2[] SpawnPosition = new Vector2[] {
            new Vector2(1380, 480),
            new Vector2(2070, 2730),
            new Vector2(2910, 2790),
            new Vector2(3300, 3780),
            new Vector2(2490, 510)
    };

    public static final float Charge = 0.25f;

    private static Texture powerupTexture;
    private static Animation<TextureRegion> powerupAnimation;

    private AnimatedSprite sprite;
    private PointLight light;

    public Powerup(Vector2 position)
    {
        if (powerupTexture == null) {
            powerupTexture = new Texture(Gdx.files.internal("powerup.png"));

            Array<TextureRegion> frames = new Array<>();
            for (int i = 0; i < 5; i++)
                frames.add(new TextureRegion(powerupTexture, i * 32, 0, 32, 32));

            powerupAnimation = new Animation<>(0.15f, frames);
            powerupAnimation.setPlayMode(Animation.PlayMode.LOOP);
        }

        sprite = new AnimatedSprite(powerupAnimation);
        sprite.setOriginCenter();
        sprite.setOriginBasedPosition(position.x, position.y);
        sprite.play();

        light = new PointLight(Physics.getRayHandler(), 500, new Color(0.2f, 0.2f, 0.8f, 0.5f), 0.5f, position.x * Main.MPP, position.y * Main.MPP);
        light.setContactFilter(Filters.AnyNoMask, Filters.CategoryNone, Filters.MaskLight);
    }

    public void render(SpriteBatch batch)
    {
        sprite.draw(batch);
    }

    public void delete()
    {
        light.remove(true);
    }

    public static void dispose()
    {
        powerupTexture.dispose();
    }

    public AnimatedSprite getSprite() {
        return sprite;
    }
}
