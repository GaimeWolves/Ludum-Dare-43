package gamewolves.itch.io.electrix.physics;

public class Filters
{
    public static final short AnyNoMask = 1;
    public static final short Shot = 1 << 1;
    public static final short Enemy = 1 << 2;
    public static final short Player = 1 << 3;
    public static final short Generator = 1 << 4;
    public static final short Battery = 1 << 5;
    public static final short Station = 1 << 6;

    public static final short CategoryNone = 0;

    public static final short MaskLight = AnyNoMask | Generator;
    public static final short MaskShot = Enemy | AnyNoMask | Generator | Station;
    public static final short MaskEnemy = Shot | Generator;
    public static final short MaskGenerator = AnyNoMask | Shot | Enemy | Player;
    public static final short MaskBattery = Player | AnyNoMask | Generator | Station | Battery;
    public static final short MaskChargeStationBorder = Player | Battery | Shot;
    public static final short MaskChargeStationPlayer = Player | Shot;
    public static final short MaskChargeStationSensor = Battery;
    public static final short MaskAny = Short.MAX_VALUE;

}