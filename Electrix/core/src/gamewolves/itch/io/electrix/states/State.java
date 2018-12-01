package gamewolves.itch.io.electrix.states;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import gamewolves.itch.io.electrix.Main;
import gamewolves.itch.io.electrix.input.InputHandler;
import gamewolves.itch.io.electrix.input.TouchCallback;


public abstract class State implements TouchCallback
{
    private static State currentState;
    private Main instance;
    public boolean disposeable;

    public State()
    {
        instance = null;
        disposeable = false;
    }

    public State(Main instance)
    {
        this.instance = instance;
        disposeable = false;
    }

    public static void setCurrent(State state)
    {
        if (currentState != null)
        {
            currentState.dispose();
            InputHandler.removeTouchCallback(currentState);
        }
        currentState = state;
        currentState.init();
        InputHandler.addTouchCallback(state);
    }

    public static State getCurrentState()
    {
        return currentState;
    }

    public abstract void init();

    public abstract void update(float deltaTime);

    public abstract void render(SpriteBatch batch);

    public abstract void renderUI(SpriteBatch batch);

    @Override
    public void TouchEvent(Vector2 position) {}

    @Override
    public void HoldEvent(Vector2 position) {}

    @Override
    public void DragEvent(Vector2 start, Vector2 current, Vector2 delta) {}

    @Override
    public void DragEventEnd(Vector2 end) {}

    public abstract void dispose();
}
