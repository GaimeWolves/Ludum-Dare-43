package eu.masterofgames.dungeontower.transitions;

public class TransitionHandler
{
    private static ITransitionAnimate currentTransitionAnimate;
    private static ITransitionEnd currentTransitionEnd;

    /**
     * Invokes the animateMethod of the Object.
     * If this returns true the endMethod will be invoked.
     */
    public static void update(float deltaTime) {
        if (inTransition())
        {
            if (currentTransitionAnimate.animateMethod(deltaTime))
            {
                if (currentTransitionEnd != null) currentTransitionEnd.endMethod();
                currentTransitionAnimate = null;
                currentTransitionEnd = null;
            }
        }
    }

    public static void setTransition(ITransitionAnimate transitionAnimate, ITransitionEnd transitionEnd)
    {
        currentTransitionAnimate = transitionAnimate;
        currentTransitionEnd = transitionEnd;
    }

    public static boolean inTransition()
    {
        return currentTransitionAnimate != null;
    }

    public interface ITransitionAnimate
    {
        boolean animateMethod(float deltaTime);
    }

    public interface ITransitionEnd
    {
        void endMethod();
    }
}
