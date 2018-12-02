package gamewolves.itch.io.electrix.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

import box2dLight.PointLight;
import gamewolves.itch.io.electrix.Main;
import gamewolves.itch.io.electrix.physics.Filters;
import gamewolves.itch.io.electrix.physics.Physics;

public class DefenceStation
{
    private static Texture idleTexture, chargedTexture;
    private static Animation<TextureRegion> idleAnimation, chargedAnimation;

    private AnimatedSprite sprite;
    private Body body;

    private PointLight light1, light2;
    public boolean charged;

    private Sound generator;
    private long id;

    public DefenceStation(Vector2 position)
    {
        generator = Gdx.audio.newSound(Gdx.files.internal("sounds/generator.wav"));

        if (idleTexture == null)
        {
            Array<TextureRegion> frames = new Array<>();
            idleTexture = new Texture(Gdx.files.internal("defence_idle.png"));
            for (int i = 0; i < 1; i++)
                frames.add(new TextureRegion(idleTexture, i * 196, 0, 196, 222));

            idleAnimation = new Animation<>(0.25f, frames);
            idleAnimation.setPlayMode(Animation.PlayMode.LOOP);

            frames = new Array<>();
            chargedTexture = new Texture(Gdx.files.internal("defence_charged.png"));
            for (int i = 0; i < 5; i++)
                frames.add(new TextureRegion(chargedTexture, i * 196, 0, 196, 222));

            chargedAnimation = new Animation<>(0.1f, frames);
            chargedAnimation.setPlayMode(Animation.PlayMode.LOOP);
        }

        sprite = new AnimatedSprite(idleAnimation);
        sprite.setOriginCenter();
        sprite.setOriginBasedPosition(position.x, position.y);
        sprite.play();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;

        body = Physics.getWorld().createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(26.5f * Main.MPP, 63 * Main.MPP, (new Vector2(-sprite.getWidth() / 2 + 26.5f, -sprite.getHeight() / 2 + 63).scl(Main.MPP)), 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = Filters.Station;
        fixtureDef.filter.maskBits = Filters.MaskChargeStationBorder;
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);

        shape.setAsBox(98 * Main.MPP, 50 * Main.MPP, (new Vector2(-sprite.getWidth() / 2 + 98, sprite.getHeight() / 2 + -50).scl(Main.MPP)), 0);

        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);

        shape.setAsBox(27 * Main.MPP, 63 * Main.MPP, (new Vector2(142 + 27 - sprite.getWidth() / 2, -sprite.getHeight() / 2 + 63).scl(Main.MPP)), 0);

        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);

        shape.setAsBox(44.5f * Main.MPP, 26.5f * Main.MPP, new Vector2(0, -20).scl(Main.MPP), 0);

        fixtureDef.shape = shape;
        fixtureDef.filter.maskBits = Filters.MaskChargeStationPlayer;

        body.createFixture(fixtureDef);

        shape.setAsBox(44.5f * Main.MPP, 2 * Main.MPP, new Vector2(0, 10).scl(Main.MPP), 0);

        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.maskBits = Filters.MaskChargeStationSensor;

        body.createFixture(fixtureDef);

        body.setTransform(position.scl(Main.MPP), 0);
    }

    public void update(float volume)
    {
        if (charged)
        {
            if (id == 0)
                id = generator.loop();

            generator.setVolume(id, volume);
        }

        if (charged && light1 == null)
        {
            sprite.setAnimation(chargedAnimation);

            light1 = new PointLight(Physics.getRayHandler(), 250, new Color(0.2f, 0.2f, 0.8f, 0.5f), 4f, 0, 0);
            light1.setContactFilter(Filters.AnyNoMask, Filters.CategoryNone, Filters.MaskLight);

            light2 = new PointLight(Physics.getRayHandler(), 250, new Color(0.2f, 0.2f, 0.8f, 0.5f), 4f, 0, 0);
            light2.setContactFilter(Filters.AnyNoMask, Filters.CategoryNone, Filters.MaskLight);
        }

        if (light1 != null)
        {
            light1.setPosition(body.getPosition().sub(-0.75f, -0.7f));
            light2.setPosition(body.getPosition().sub(0.75f, -0.7f));
        }
    }

    public void render(SpriteBatch batch)
    {
        sprite.draw(batch);
    }

    public void delete()
    {
        if (light1 != null) {
            light1.remove(true);
            light2.remove(true);
        }

        generator.dispose();
    }

    public static void dispose()
    {
        idleTexture.dispose();
        chargedTexture.dispose();
    }

    public Body getBody() {
        return body;
    }
}
