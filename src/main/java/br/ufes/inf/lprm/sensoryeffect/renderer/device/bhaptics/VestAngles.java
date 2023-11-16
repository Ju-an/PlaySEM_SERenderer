package br.ufes.inf.lprm.sensoryeffect.renderer.device.bhaptics;

public class VestAngles {
    //FRONT
    public final static int[] FRONT_LEFT = {315};
    public final static int[] FRONT_CENTERLEFT = {337};
    public final static int[] FRONT_CENTER = {0};
    public final static int[] FRONT_CENTERRIGHT = {23};
    public final static int[] FRONT_RIGHT = {45};

    //MIDWAY
    public final static int[] MIDWAY_LEFT = {270};
    public final static int[] MIDWAY_CENTERLEFT = {270};
    public final static int[] MIDWAY_CENTER = {0};
    public final static int[] MIDWAY_CENTERRIGHT = {90};
    public final static int[] MIDWAY_RIGHT = {90};

    //BACK
    public final static int[] BACK_LEFT = {225};
    public final static int[] BACK_CENTERLEFT = {203};
    public final static int[] BACK_CENTER = {180};
    public final static int[] BACK_CENTERRIGHT = {157};
    public final static int[] BACK_RIGHT = {135};

    //FRONT + MIDWAY + BACK
    public final static int[] LEFT = {225, 270, 315};
    public final static int[] CENTERLEFT = {203, 270, 337};
    public final static int[] CENTER = {337, 0, 23, 203, 180, 157};
    public final static int[] CENTERRIGHT = {23, 90, 157};
    public final static int[] RIGHT = {45, 90, 135};

    //LEFT + CENTERLEFT + CENTER + CENTERRIGHT + RIGHT
    public final static int[] FRONT = {315, 337, 0, 23, 45};
    public final static int[] MIDWAY = {225, 270, 315, 45, 90, 135};
    public final static int[] BACK = {225, 203, 180, 157, 135};

    //ALL
    public final static int[] ALL = {203, 225, 270, 315, 337, 0, 23, 45, 90, 135, 157, 180};
}
