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
            public final static int LEFT_FRONT_DRIVE_SIDECAR = 1;
            public final static int RIGHT_FRONT_DRIVE_SIDECAR = 1;
            public final static int LEFT_REAR_DRIVE_SIDECAR = 1;
            public final static int RIGHT_REAR_DRIVE_SIDECAR = 1;
            public final static int RIGHT_FRONT_DRIVE_MOTOR_PORT = 6;
            public final static int RIGHT_REAR_DRIVE_MOTOR_PORT = 1;
            public final static int LEFT_FRONT_DRIVE_MOTOR_PORT = 4;
            public final static int LEFT_REAR_DRIVE_MOTOR_PORT = 3;

    //----- Solenoids -----
    //----- Digital Inputs -----
        //----- Sidecar 1 -----
            public final static int LEFT_FRONT_DRIVE_ENCODER_A_PORT = 7;
            public final static int LEFT_FRONT_DRIVE_ENCODER_B_PORT = 8;
            public final static int RIGHT_FRONT_DRIVE_ENCODER_A_PORT = 1;
            public final static int RIGHT_FRONT_DRIVE_ENCODER_B_PORT = 2;
            public final static int LEFT_REAR_DRIVE_ENCODER_A_PORT = 12;
            public final static int LEFT_REAR_DRIVE_ENCODER_B_PORT = 11;
            public final static int RIGHT_REAR_DRIVE_ENCODER_A_PORT = 9;
            public final static int RIGHT_REAR_DRIVE_ENCODER_B_PORT = 10;
            public final static int PRESSURE_SWITCH_PORT = 0;

    //----- Analog Inputs -----
        public final static int GYRO_PORT = 0;
        public final static int ANALOG_PRESSURE_PORT = 0;

    //----- Relays -----
        //----- Sidecar 1 -----
            public final static int COMPRESSOR_SIDECAR = 0;
            public final static int COMPRESSOR_RELAY_PORT = 1;

}
