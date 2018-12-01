package gamewolves.itch.io.electrix.states;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import box2dLight.PointLight;
import gamewolves.itch.io.electrix.Main;
import gamewolves.itch.io.electrix.physics.Physics;
import gamewolves.itch.io.electrix.player.Player;

public class Game extends State
{
    private Player player;

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
        player = new Player();
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
        player.handleInput(dt);
    }

    @Override
    public void update(float deltaTime)
    {
        handleInput(deltaTime);
        player.update(deltaTime);
        Physics.update(deltaTime);
    }

    @Override
    public void render(SpriteBatch batch)
    {
        player.render(batch);

        Physics.render();

        //Render UI
    }

    @Override
    public void dispose()
    {

    }
}
