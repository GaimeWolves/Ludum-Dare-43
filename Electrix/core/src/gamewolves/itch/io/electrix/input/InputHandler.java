package gamewolves.itch.io.electrix.input;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import gamewolves.itch.io.electrix.Main;

public class InputHandler
{
    public static Vector2 CurrentMousePos;

    private static Vector2 start;
    private static Vector2 current;
    private static boolean isDragging;

    private static Array<TouchCallback> touchCallbacks = new Array<>();

    public static void addTouchCallback(TouchCallback touchCallback)
    {
        InputHandler.touchCallbacks.add(touchCallback);
    }

    public static void removeTouchCallback(TouchCallback touchCallback)
    {
        InputHandler.touchCallbacks.removeValue(touchCallback, true);
    }

    public static void update()
    {
        CurrentMousePos = new Vector2(
                Main.Camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).x,
                Main.Camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).y
        );

        if (touchCallbacks.size == 0) return;
        if (Gdx.input.justTouched())
        {
            for (TouchCallback touchCallback : touchCallbacks)
            {
                touchCallback.TouchEvent(CurrentMousePos);
            }
        }

        if (Gdx.input.isTouched())
        {
            if (!isDragging) { isDragging = true; start = CurrentMousePos; current = CurrentMousePos; }

            Vector2 difference = CurrentMousePos.cpy().sub(current);

            for (TouchCallback touchCallback : touchCallbacks)
            {
                touchCallback.HoldEvent(CurrentMousePos);
                touchCallback.DragEvent(start, current, difference);
            }

            current = CurrentMousePos;
        }
        else
        {
            if (isDragging)
            {
                for (TouchCallback touchCallback : touchCallbacks)
                {
                    touchCallback.DragEventEnd(current);
                }
            }
            isDragging = false;
        }
    }
}
