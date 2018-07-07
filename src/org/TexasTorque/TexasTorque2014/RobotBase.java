package org.TexasTorque.TexasTorque2014;

import org.TexasTorque.TexasTorque2014.autonomous.AutonomousManager;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.io.DriverInput;
import org.TexasTorque.TexasTorque2014.io.RobotOutput;
import org.TexasTorque.TexasTorque2014.io.SensorInput;
import org.TexasTorque.TexasTorque2014.subsystem.drivebase.Drivebase;
import org.TexasTorque.TexasTorque2014.subsystem.manipulator.Manipulator;
import org.TexasTorque.torquelib.component.CheesyVisionServer;
import org.TexasTorque.torquelib.util.DashboardManager;
import org.TexasTorque.torquelib.util.Parameters;
import org.TexasTorque.torquelib.util.TorqueLogging;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotBase extends IterativeRobot implements Runnable {

    
    Thread continuousThread;
    Parameters params;
    TorqueLogging logging;
    DashboardManager dashboardManager;
    DriverInput driverInput;
    SensorInput sensorInput;
    RobotOutput robotOutput;
    Drivebase drivebase;
    Manipulator manipulator;
    AutonomousManager autonManager;
    CheesyVisionServer server;
    public final int listenPort = 1180;

    Timer robotTime;

    boolean logData;
    int logCycles;
    double numCycles;
    double previousTime;

    public void robotInit() {

        params = Parameters.getTeleopInstance();

        dashboardManager = DashboardManager.getInstance();
        robotOutput = RobotOutput.getInstance();
        driverInput = DriverInput.getInstance();
        sensorInput = SensorInput.getInstance();
        drivebase = Drivebase.getInstance();
        manipulator = Manipulator.getInstance();
        server = CheesyVisionServer.getInstance();
        server.setPort(listenPort);
        server.start();
        autonManager = new AutonomousManager();

        driverInput.pullJoystickTypes();

        robotTime = new Timer();

        numCycles = 0.0;
        SmartDashboard.putNumber("AutonomousMode", Constants.DO_NOTHING_AUTO);

        continuousThread = new Thread(this);
        continuousThread.start();
    }

    public void autonomousInit() {
        server.reset();
        server.startSamplingCounts();
        int autonMode = (int) SmartDashboard.getNumber("AutonomousMode", Constants.DO_NOTHING_AUTO);
        autonManager.setAutoMode(autonMode);
        autonManager.loadAutonomous();

        sensorInput.resetEncoders();
        loadParameters();
    }

    public void disabledInit() {
        robotOutput.setLightsState(Constants.LIGHTS_DISABLED);
        robotOutput.runLights();
        server.stopSamplingCounts();
        loadParameters();
    }

    public void run() {

        previousTime = Timer.getFPGATimestamp();

        while (true) {
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

        robotOutput.updateState();
        driverInput.updateState();
        sensorInput.updateState();

        autonManager.runAutonomous();

        robotOutput.pullFromState();

        robotOutput.runLights();
        
        pushToDashboard();
    }

    public void autonomousContinuous() {
        sensorInput.calcEncoders();
    }

    public void teleopInit() {
        server.stopSamplingCounts();
        loadParameters();
        driverInput.pullJoystickTypes();
        sensorInput.resetEncoders();
        robotTime.reset();
        robotTime.start();
    }

    public void teleopPeriodic() {

        robotOutput.updateState();
        driverInput.updateState();
        sensorInput.updateState();

        drivebase.run();
        manipulator.run();

        robotOutput.pullFromState();

        robotOutput.runLights();
        
        pushToDashboard();
    }

    public void teleopContinuous() {
        sensorInput.calcEncoders();
    }

    public void disabledPeriodic() {
        driverInput.updateState();
        sensorInput.updateState();
        robotOutput.runLights();
        pushToDashboard();
    }

    public void disabledContinuous() {
        sensorInput.calcEncoders();
    }

    public void loadParameters() {
        params.load();
        drivebase.loadParameters();
        manipulator.loadParameters();
        sensorInput.loadParameters();
        SensorInput.getState().loadParamaters();
    }
    
    public void pushToDashboard()
    {
        SmartDashboard.putBoolean("LeftHot", server.getLeftStatus());
        SmartDashboard.putBoolean("RightHot", server.getRightStatus());
        drivebase.pushToDashboard();
        manipulator.pushToDashboard();
        SensorInput.getState().pushToDashboard();
    }
}
