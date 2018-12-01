package gamewolves.itch.io.electrix.input;


import com.badlogic.gdx.math.Vector2;

public interface TouchCallback
{
    void TouchEvent(Vector2 position);

    void HoldEvent(Vector2 position);

    void DragEvent(Vector2 start, Vector2 current, Vector2 delta);

    void DragEventEnd(Vector2 end);
}
