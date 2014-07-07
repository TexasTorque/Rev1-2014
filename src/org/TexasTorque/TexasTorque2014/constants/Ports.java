package org.TexasTorque.TexasTorque2014.constants;

public class Ports {

    //----- Misc -----
    public final static int SIDECAR_ONE = 1;
    public final static int SIDECAR_TWO = 2;

    //----- Controllers -----
    public final static int DRIVE_CONTROLLER_PORT = 1;
    public final static int OPERATOR_CONTROLLER_PORT = 2;

    //----- Motors -----
    //----- Sidecar 1 -----
    public final static int RIGHT_FRONT_DRIVE_SIDECAR = 1;
    public final static int RIGHT_REAR_DRIVE_SIDECAR = 1;

    public final static int RIGHT_FRONT_DRIVE_MOTOR_PORT = 10;
    public final static int RIGHT_REAR_DRIVE_MOTOR_PORT = 3;

    public final static int FRONT_INTAKE_TILT_SIDECAR = 1;
    public final static int FRONT_INTAKE_TILT_MOTOR_PORT = 2;

    public final static int FRONT_INTAKE_SIDECAR = 1;
    public final static int FRONT_INTAKE_MOTOR_PORT = 4;

    public final static int CATAPULT_SIDECAR = 2;
    public final static int CATAPULT_MOTOR_PORT = 3;
    public final static int CATAPULT_B_SIDECAR = 1;
    public final static int CATAPULT_B_MOTOR_PORT = 9;

    //----- Sidecar 2 -----
    public final static int LEFT_FRONT_DRIVE_SIDECAR = 2;
    public final static int LEFT_REAR_DRIVE_SIDECAR = 2;

    public final static int LEFT_FRONT_DRIVE_MOTOR_PORT = 1;
    public final static int LEFT_REAR_DRIVE_MOTOR_PORT = 9;

    public final static int REAR_INTAKE_TILT_SIDECAR = 2;
    public final static int REAR_INTAKE_TILT_MOTOR_PORT = 8;

    public final static int REAR_INTAKE_SIDECAR = 2;
    public final static int REAR_INTAKE_MOTOR_PORT = 10;

    //----- Solenoids -----
    public final static int FRONT_DRIVEBASE_SWITCHER = 1;
    public final static int REAR_DRIVEBASE_SWITCHER = 3;
    public final static int CATAPULT_RELEASE = 4;
    public final static int HOOP = 6;
            
    //----- Digital Inputs -----
    //----- Sidecar 1 -----
    public final static int LEFT_FRONT_DRIVE_ENCODER_SIDECAR = 2;
    public final static int LEFT_FRONT_DRIVE_ENCODER_A_PORT = 4;
    public final static int LEFT_FRONT_DRIVE_ENCODER_B_PORT = 5;
    
    public final static int RIGHT_FRONT_DRIVE_ENCODER_SIDECAR = 1;
    public final static int RIGHT_FRONT_DRIVE_ENCODER_A_PORT = 4;
    public final static int RIGHT_FRONT_DRIVE_ENCODER_B_PORT = 5;
    
    public final static int LEFT_REAR_DRIVE_ENCODER_SIDECAR = 2;
    public final static int LEFT_REAR_DRIVE_ENCODER_PORT = 13;
    
    public final static int RIGHT_REAR_DRIVE_ENCODER_SIDECAR = 1;
    public final static int RIGHT_REAR_DRIVE_ENCODER_PORT = 6;
    
    public final static int CATAPULT_ENCODER_SIDECAR = 2;
    public final static int CATAPULT_ENCODER_A_PORT = 6;
    public final static int CATAPULT_ENCODER_B_PORT = 7;
    
    public final static int CATAPULT_LIMIT_SWITCH_SIDECAR = 2;
    public final static int CATAPULT_LIMIT_SWITCH_PORT = 14;
    public final static int CATAPULT_LIMIT_SWITCH_B_SIDECAR = 1;
    public final static int CATAPULT_LIMIT_SWITCH_B_PORT = 14;
    
    public final static int PRESSURE_SWITCH_PORT = 1;
    public final static int PRESSURE_SWITCH_SIDECAR = 1;

    //----- Analog Inputs -----
    public final static int GYRO_PORT = 1;
    //public final static int ANALOG_PRESSURE_PORT = 0;
    
    public final static int FRONT_INTAKE_TILT_POT_PORT = 7;
    public final static int REAR_INTAKE_TILT_POT_PORT = 2;

    //----- Relays -----
    //----- Sidecar 1 -----
    public final static int COMPRESSOR_SIDECAR = 1;
    public final static int COMPRESSOR_RELAY_PORT = 2;

    //----- Lights -----
    public final static int LIGHTS_SIDECAR = 2;
    public final static int LIGHTS_A_PORT = 7;
    public final static int LIGHTS_B_PORT = 8;
    public final static int LIGHTS_C_PORT = 10;
    public final static int LIGHTS_D_PORT = 11;
}
