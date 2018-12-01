package gamewolves.itch.io.electrix.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

import box2dLight.PointLight;
import gamewolves.itch.io.electrix.Main;
import gamewolves.itch.io.electrix.physics.Physics;

public class Generator
{
    private Texture generatorTexture;
    private Animation<TextureRegion> generatorAnimation;
    private AnimatedSprite generator;

    private PointLight light;
    private Body body;

    public Generator()
    {
        generatorTexture = new Texture(Gdx.files.internal("generator.png"));

        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 14; i++)
            frames.add(new TextureRegion(generatorTexture, i * 128, 0, 128, 128));

        generatorAnimation = new Animation<>(0.05f, frames);
        generatorAnimation.setPlayMode(Animation.PlayMode.LOOP);
        generator = new AnimatedSprite(generatorAnimation);
        generator.setOriginCenter();
        generator.setOriginBasedPosition(0, 0);
        generator.play();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        body = Physics.getWorld().createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(generator.getWidth() / 2, generator.getHeight() / 2);

        body.createFixture(shape, 1);

        light = new PointLight(Physics.getRayHandler(), 500, new Color(0.5f, 0.5f, 0.5f, 1f), 600, 0, 0);
    }

    public void update(float dt)
    {

    }

    public void render(SpriteBatch batch)
    {
        batch.begin();
        generator.draw(batch);
        batch.end();
    }
}
