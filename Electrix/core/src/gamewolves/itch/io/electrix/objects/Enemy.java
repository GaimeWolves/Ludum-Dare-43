package gamewolves.itch.io.electrix.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

import gamewolves.itch.io.electrix.Main;
import gamewolves.itch.io.electrix.physics.Filters;
import gamewolves.itch.io.electrix.physics.Physics;

public class Enemy
{
    private static final float Speed = 2.5f;
    private AnimatedSprite enemy;
    private static Texture enemyTexture;
    private static Animation<TextureRegion> enemyAnimation;

    private Body body;
    public boolean disposeable;

    public Enemy()
    {
        if (enemyTexture == null)
        {
            Array<TextureRegion> frames = new Array<>();
            enemyTexture = new Texture(Gdx.files.internal("enemy.png"));
            for (int i = 0; i < 1; i++)
                frames.add(new TextureRegion(enemyTexture, i * 32, 0, 32, 32));

            enemyAnimation = new Animation<>(0.25f, frames);
            enemyAnimation.setPlayMode(Animation.PlayMode.LOOP);
        }

        enemy = new AnimatedSprite(enemyAnimation);
        enemy.setOriginCenter();

        float angle = MathUtils.random() * 360;

        Vector2 position = (new Vector2(MathUtils.cosDeg(angle), MathUtils.sinDeg(angle))).scl(1000);
        enemy.setOriginBasedPosition(position.x, position.y);
        enemy.setRotation(angle - 180);

        enemy.play();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;

        body = Physics.getWorld().createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(enemy.getWidth() * 0.5f * Main.MPP);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = Filters.Enemy;
        fixtureDef.filter.groupIndex = 1;
        fixtureDef.filter.maskBits = Filters.MaskEnemy;
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);

        body.setTransform(position.scl(Main.MPP), (angle - 180) * MathUtils.degRad);

        disposeable = false;
    }

    public void update(float dt)
    {
        body.setLinearVelocity(body.getPosition().nor().scl(-Speed));
        enemy.setOriginBasedPosition(body.getPosition().scl(1 / Main.MPP).x, body.getPosition().scl(1 / Main.MPP).y);
    }

    public void render(SpriteBatch batch)
    {
        enemy.draw(batch);
    }

    public void delete()
    {
        Physics.getWorld().destroyBody(body);
    }

    public void dispose()
    {
        enemyTexture.dispose();
    }
}
