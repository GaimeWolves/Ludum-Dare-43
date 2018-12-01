package gamewolves.itch.io.electrix.physics;

public class Filters
{
    public static final short AnyNoMask = 1;
    public static final short Sensor = 1 << 1;
    public static final short Enemy = 1 << 2;
    public static final short Player = 1 << 3;

    public static final short CategoryNone = 0;

    public static final short MaskLight = AnyNoMask;
    public static final short MaskSensor = Enemy;
    public static final short MaskAny = Short.MAX_VALUE;

}