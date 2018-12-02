package gamewolves.itch.io.electrix.physics;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.RayHandler;
import gamewolves.itch.io.electrix.Main;
import gamewolves.itch.io.electrix.states.Game;

public class Physics
{
    private static World world;
    private static RayHandler rayHandler;

    public static void init()
    {
        world = new World(Vector2.Zero.cpy(), false);

        RayHandler.setGammaCorrection(true);
        RayHandler.useDiffuseLight(false);
        rayHandler = new RayHandler(world);
        rayHandler.setCombinedMatrix(Main.Camera);
        rayHandler.setAmbientLight(0f, 0f, 0f, 0f);
        rayHandler.setBlurNum(3);
        rayHandler.setShadows(true);
    }

    public static void update(float deltaTime)
    {
        world.step(deltaTime, 6, 2);
    }

    public static void render()
    {
        rayHandler.setCombinedMatrix(Main.Camera.combined.cpy().scl(1 / Main.MPP, 1 / Main.MPP, 0), 0, 0, Main.Camera.viewportWidth, Main.Camera.viewportHeight);
        rayHandler.updateAndRender();
    }

    public static void dispose()
    {
        rayHandler.dispose();
        world.dispose();
    }

    public static void setContactListener()
    {
        world.setContactListener(new ContactListener()
        {
            @Override
            public void beginContact(Contact contact)
            {
                Fixture a = contact.getFixtureA();
                Fixture b = contact.getFixtureB();

                if (a.getFilterData().categoryBits == Filters.Shot)
                {
                    Game.Instance.getShotByBody(a.getBody()).disposeable = true;

                    if (b.getFilterData().categoryBits == Filters.Enemy)
                        Game.Instance.getEnemyByBody(b.getBody()).lives--;
                }
                else if (b.getFilterData().categoryBits == Filters.Shot)
                {
                    Game.Instance.getShotByBody(b.getBody()).disposeable = true;

                    if (a.getFilterData().categoryBits == Filters.Enemy)
                        Game.Instance.getEnemyByBody(a.getBody()).lives--;
                }
            }

            @Override
            public void endContact(Contact contact)
            {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold)
            {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse)
            {

            }
        });
    }

    public static World getWorld()
    {
        return world;
    }

    public static RayHandler getRayHandler() {
        return rayHandler;
    }
}
