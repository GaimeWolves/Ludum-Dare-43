package gamewolves.itch.io.electrix.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

import box2dLight.PointLight;
import gamewolves.itch.io.electrix.Main;
import gamewolves.itch.io.electrix.physics.Filters;
import gamewolves.itch.io.electrix.physics.Physics;

public class Shot
{
    private static final float Speed = 8;

    private static Texture shotTexture;
    private static Animation<TextureRegion> shotAnimation;

    private AnimatedSprite shot;
    private Vector2 direction;

    private Body body;
    private PointLight light;

    public boolean disposeable;

    public Shot(Vector2 position, Vector2 direction)
    {
        if (shotTexture == null)
        {
            shotTexture = new Texture(Gdx.files.internal("shot.png"));
            Array<TextureRegion> frames = new Array<>();
            for (int i = 0; i < 4; i++)
                frames.add(new TextureRegion(shotTexture, i * 8, 0, 8, 16));

            shotAnimation = new Animation<>(0.05f, frames);
            shotAnimation.setPlayMode(Animation.PlayMode.LOOP);
        }

        shot = new AnimatedSprite(shotAnimation);
        shot.setOriginCenter();
        shot.setOriginBasedPosition(position.x, position.y);
        shot.rotate(direction.angle() + 90);
        shot.play();

        this.direction = direction;
        disposeable = false;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;

        body = Physics.getWorld().createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((shot.getWidth() / 2) * Main.MPP, (shot.getHeight() / 2) * Main.MPP, Vector2.Zero.cpy(), direction.angleRad() + MathUtils.PI / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = Filters.Sensor;
        fixtureDef.filter.groupIndex = Filters.CategoryNone;
        fixtureDef.filter.maskBits = Filters.MaskSensor;

        body.createFixture(fixtureDef);

        body.setTransform(position.scl(Main.MPP), 0);

        light = new PointLight(Physics.getRayHandler(), 250, new Color(0.2f, 0.2f, 0.8f, 0.5f), 0.5f, 0, 0);
        light.setContactFilter(Filters.AnyNoMask, Filters.CategoryNone, Filters.MaskLight);
    }

    public void update(float dt)
    {
        body.setTransform(body.getPosition().add(direction.cpy().scl(Speed * dt)), 0);
        shot.setOriginBasedPosition(body.getPosition().scl(1 / Main.MPP).x, body.getPosition().scl(1 / Main.MPP).y);
        light.setPosition(body.getPosition());

        if (body.getPosition().len() * (1 / Main.MPP) > 5600)
            disposeable = true;
    }

    public void render(SpriteBatch batch)
    {
        shot.draw(batch);
    }

    public void delete()
    {
        light.remove(true);
        Physics.getWorld().destroyBody(body);
    }

    public void dispose()
    {
        shotTexture.dispose();
    }

    public Body getBody() {
        return body;
    }
}
