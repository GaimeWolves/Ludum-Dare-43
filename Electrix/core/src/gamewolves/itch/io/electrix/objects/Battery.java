package gamewolves.itch.io.electrix.objects;

import com.badlogic.gdx.Gdx;
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

public class Battery
{
    public static final float ChargeTime = 10f;

    private static Texture idleTexture;
    private static Texture chargedTexture;
    private static Animation<TextureRegion> idleAnimation;
    private static Animation<TextureRegion> chargedAnimation;

    private AnimatedSprite battery;
    private Body body;

    private boolean locked;
    private boolean charged;
    private boolean repelled;

    private float charge;

    private PointLight light;

    public Battery(Vector2 position)
    {
        if (idleTexture == null)
        {
            Array<TextureRegion> frames = new Array<>();
            idleTexture = new Texture(Gdx.files.internal("battery_idle.png"));
            for (int i = 0; i < 1; i++)
                frames.add(new TextureRegion(idleTexture, i * 32, 0, 64, 110));

            idleAnimation = new Animation<>(0.25f, frames);
            idleAnimation.setPlayMode(Animation.PlayMode.LOOP);

            frames = new Array<>();
            chargedTexture = new Texture(Gdx.files.internal("battery_charged.png"));
            for (int i = 0; i < 13; i++)
                frames.add(new TextureRegion(chargedTexture, i * 64, 0, 64, 110));

            chargedAnimation = new Animation<>(0.1f, frames);
            chargedAnimation.setPlayMode(Animation.PlayMode.LOOP);
        }

        battery = new AnimatedSprite(idleAnimation);
        battery.setOriginCenter();
        battery.setOriginBasedPosition(position.x, position.y);
        battery.play();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;

        body = Physics.getWorld().createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((battery.getWidth() / 2 - 4) * Main.MPP, (battery.getHeight() / 2 - 14) * Main.MPP, new Vector2(0, -5).scl(Main.MPP), 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = Filters.Battery;
        fixtureDef.filter.maskBits = Filters.MaskBattery;
        fixtureDef.shape = shape;
        fixtureDef.density = 15f;

        body.createFixture(fixtureDef);

        body.setTransform(position.scl(Main.MPP), 0);
    }

    public void update(float dt)
    {
        body.setLinearVelocity(body.getLinearVelocity().scl(0.8f));
        battery.setOriginBasedPosition(body.getPosition().scl(1 / Main.MPP).x, body.getPosition().scl(1 / Main.MPP).y);

        if (light != null)
            light.setPosition(body.getPosition());

        if (body.getPosition().len() < 3)
            charge += dt;

        if (charge >= ChargeTime && ! charged)
        {
            charged = true;
            battery.setAnimation(chargedAnimation);

            light = new PointLight(Physics.getRayHandler(), 250, new Color(0.2f, 0.2f, 0.8f, 0.25f), 1.5f, 0, 0);
            light.setContactFilter(Filters.AnyNoMask, Filters.CategoryNone, Filters.MaskLight);
        }

        if (repelled)
        {
            repelled = false;
            body.setTransform(body.getPosition().sub(0, 1.5f), 0);
        }

        if (locked && body.isActive())
        {
            body.setActive(false);
        }
    }

    public void render(SpriteBatch batch)
    {
        battery.draw(batch);
    }

    public void delete()
    {
        if (light != null)
            light.remove(true);
    }

    public void dispose()
    {
        chargedTexture.dispose();
        idleTexture.dispose();
    }

    public Body getBody() {
        return body;
    }

    public boolean isCharged() {
        return charged;
    }

    public void lock()
    {
        locked = true;
    }

    public void repell()
    {
        repelled = true;
    }
}
