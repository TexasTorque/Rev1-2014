package org.TexasTorque.TexasTorque2014.autonomous.drive;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.io.SensorInput;
import org.TexasTorque.TorqueLib.component.CheesyVisionServer;
import org.TexasTorque.TorqueLib.controlLoop.TorquePID;

public class AutonomousTurnCheesy extends AutonomousCommand {

    private double target;
    private TorquePID leftDrive;
    private TorquePID rightDrive;
    private boolean isDone;
    private boolean firstCycle;
    private double startTime;
    private double timeout;

    public AutonomousTurnCheesy(double distance, double maxSpeed, double timeout) {
        target = distance * Constants.CLICKS_PER_METER;
        leftDrive = new TorquePID();
        rightDrive = new TorquePID();

        double p = params.getAsDouble("A_TurnP", 0.0);
        double i = params.getAsDouble("A_TurnI", 0.0);
        double d = params.getAsDouble("A_TurnD", 0.0);
        double e = params.getAsDouble("A_TurnE", 0.0);
        leftDrive.setPIDGains(p, i, d);
        rightDrive.setPIDGains(p, i, d);
        leftDrive.setEpsilon(e);
        rightDrive.setEpsilon(e);
        double doneRange = params.getAsDouble("A_TurnDoneRange", 8.0);
        leftDrive.setDoneRange(doneRange);
        rightDrive.setDoneRange(doneRange);
        leftDrive.setMinDoneCycles((int) params.getAsDouble("A_TurnDoneCycles", 50));
        rightDrive.setMinDoneCycles((int) params.getAsDouble("A_TurnDoneCycles", 50));
        isDone = false;
        this.timeout = timeout;
        SensorInput.getInstance().resetDriveEncoders();
        reset();
    }

    public void reset() {
        firstCycle = true;
        isDone = false;
        SensorInput.getInstance().resetDriveEncoders();
    }

    public boolean run() {
        if (firstCycle) {
            System.err.println("Turning");

            if (CheesyVisionServer.getInstance().getLeftCount() > CheesyVisionServer.getInstance().getRightCount()) {
                System.err.println("LeftGoalHot");
                leftDrive.setSetpoint(target);
                rightDrive.setSetpoint(-target);
            } else {
                System.err.println("RightGoalHot");
                leftDrive.setSetpoint(-target);
                rightDrive.setSetpoint(target);
            }

            startTime = Timer.getFPGATimestamp();
            firstCycle = false;
        }
        Hashtable autonOutput = new Hashtable();
        SmartDashboard.putNumber("Left Drive", sensorInput.getLeftDrivePosition());
        SmartDashboard.putNumber("Right Drive", sensorInput.getRightDrivePosition());
        double left = leftDrive.calculate(sensorInput.getLeftDrivePosition());
        double right = rightDrive.calculate(sensorInput.getRightDrivePosition());

        isDone = leftDrive.isDone() && rightDrive.isDone();

        autonOutput.put("leftSpeed", new Double(-left));
        autonOutput.put("rightSpeed", new Double(-right));
        autonOutput.put("frontIntakeDown", Boolean.TRUE);

        driverInput.updateAutonData(autonOutput);

        if (Timer.getFPGATimestamp() - startTime > timeout) {
            return true;
        }
        return isDone;
    }
}
