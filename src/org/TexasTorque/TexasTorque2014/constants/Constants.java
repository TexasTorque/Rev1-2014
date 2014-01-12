package org.TexasTorque.TexasTorque2014.constants;

public class Constants
{
    //----- Controller -----
    public final static double X_AXIS_DEADBAND = 0.07;
    public final static double Y_AXIS_DEADBAND = 0.07;
    public final static double ROTATION_DEADBAND = 0.07;
    
    public final static boolean DEFAULT_FIRST_CONTROLLER_TYPE = true;
    public final static boolean DEFAULT_SECOND_CONTROLLER_TYPE = false;
    
    //----- Light States -----
    public final static int WHITE_SOLID = 0;
    public final static int BLUE_SOLID = 1;
    public final static int RED_SOLID = 2;
    public final static int YELLOW_RED_ALLIANCE = 4;
    public final static int YELLOW_BLUE_ALLIANCE = 3;
    public final static int PARTY_MODE = 5;
    public final static int TRACKING_RED_ALLIANCE = 6;
    public final static int TRACKING_BLUE_ALLIANCE = 7;
    public final static int LOCKED_RED_ALLIANCE = 8;
    public final static int LOCKED_BLUE_ALLIANCE = 9;
    
    //----- Autonomous -----
    public final static int DO_NOTHING_AUTO = 0;
    
    //----- Drivebase -----
    public final static double DEFAULT_HIGH_SENSITIVITY = 0.7;
    public final static double STRAFE_COEFFICIENT = 0;
    public final static double FORWARD_REVERSE_COEFFICIENT = 1;
    public final static double ROTATION_COEFFICIENT = 0;
    
    //----- Gyro -----
    public static double GYRO_SENSITIVITY = 0.014;
    
    //----- Misc -----
    public final static int CYCLES_PER_LOG = 10;
    public final static double MOTOR_STOPPED = 0.0;
    public final static double PRESSURE_THRESHOLD = 2.0;
    public final static int RED_ALLIANCE = 0;
    public final static int BLUE_ALLIANCE = 1;
    
}
