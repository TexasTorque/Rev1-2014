package org.TexasTorque.TexasTorque2014;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.TexasTorque.TexasTorque2014.io.*;
import org.TexasTorque.TexasTorque2014.subsystem.drivebase.Drivebase;
import org.TexasTorque.TexasTorque2014.subsystem.manipulator.Manipulator;
import org.TexasTorque.TorqueLib.util.DashboardManager;
import org.TexasTorque.TorqueLib.util.Parameters;
import org.TexasTorque.TorqueLib.util.TorqueLogging;

public class RobotBase extends IterativeRobot implements Runnable {

    Thread continuousThread;

    Watchdog watchdog;
    Parameters params;
    TorqueLogging logging;
    DashboardManager dashboardManager;
    DriverInput driverInput;
    SensorInput sensorInput;
    RobotOutput robotOutput;
    Drivebase drivebase;
    Manipulator manipulator;

    Timer robotTime;

    boolean logData;
    int logCycles;
    double numCycles;
    double previousTime;

    public void robotInit() {
        watchdog = getWatchdog();
        watchdog.setEnabled(true);
        watchdog.setExpiration(0.5);

        params = Parameters.getTeleopInstance();

        dashboardManager = DashboardManager.getInstance();
        robotOutput = RobotOutput.getInstance();
        driverInput = DriverInput.getInstance();
        sensorInput = SensorInput.getInstance();
        drivebase = Drivebase.getInstance();
        manipulator = Manipulator.getInstance();

        driverInput.pullJoystickTypes();

        robotTime = new Timer();

        numCycles = 0.0;

        continuousThread = new Thread(this);
        continuousThread.start();
    }

    public void autonomousInit() {
        sensorInput.resetEncoders();
        loadParameters();
    }

    public void disabledInit() {
        loadParameters();
    }

    public void run() {

        previousTime = Timer.getFPGATimestamp();

        while (true) {
            watchdog.feed();
            if (isAutonomous() && isEnabled()) {
                autonomousContinuous();
                Timer.delay(0.004);
            } else if (isOperatorControl() && isEnabled()) {
                teleopContinuous();
                Timer.delay(0.004);
            } else if (isDisabled()) {
                disabledContinuous();
                Timer.delay(0.05);
            }

            numCycles++;
            SmartDashboard.putNumber("NumCycles", numCycles);
        }
    }

    public void autonomousPeriodic() {
        watchdog.feed();
    }

    public void autonomousContinuous() {
        sensorInput.calcEncoders();
    }

    public void teleopInit() {
        loadParameters();
        driverInput.pullJoystickTypes();
        sensorInput.resetEncoders();
        robotTime.reset();
        robotTime.start();
    }

    public void teleopPeriodic() {
        watchdog.feed();
        
        robotOutput.updateState();
        driverInput.updateState();
        sensorInput.updateState();
       
        drivebase.run();
        manipulator.run();
        
        
        drivebase.pushToDashboard();
        robotOutput.pullFromState();
        SensorInput.getState().pushToDashboard();
        
    }

    public void teleopContinuous() {
        sensorInput.calcEncoders();
        
    }

    public void disabledPeriodic() {
        driverInput.updateState();
        sensorInput.updateState();
        sensorInput.getState().pushToDashboard();
        watchdog.feed();
    }

    public void disabledContinuous() {
        sensorInput.calcEncoders();
    }
    
    public void loadParameters()
    {
        params.load();
        drivebase.loadParameters();
        manipulator.loadParameters();
        SensorInput.getState().loadParamaters();
    }
}
