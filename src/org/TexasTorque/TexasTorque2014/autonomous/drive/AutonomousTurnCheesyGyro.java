package org.TexasTorque.TexasTorque2014.autonomous.drive;

import edu.wpi.first.wpilibj.Timer;
import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;
import org.TexasTorque.TexasTorque2014.constants.Constants;
import org.TexasTorque.TexasTorque2014.io.SensorInput;
import org.TexasTorque.TorqueLib.component.CheesyVisionServer;
import org.TexasTorque.TorqueLib.controlLoop.TorquePID;

public class AutonomousTurnCheesyGyro extends AutonomousCommand {

    private double target;
    private TorquePID gyroPID;
    private boolean isDone;
    private boolean firstCycle;
    private double startTime;
    private double timeout;
    private double doneTime;
    private boolean firstDone;
    private double postTurnWait;

    public AutonomousTurnCheesyGyro(double degrees, double maxSpeed, double timeout) {
        target = degrees;
        gyroPID = new TorquePID();

        double p = params.getAsDouble("A_GyroTurnP", 0.0);
        double i = params.getAsDouble("A_GyroTurnI", 0.0);
        double d = params.getAsDouble("A_GyroTurnD", 0.0);
        double e = params.getAsDouble("A_GyroTurnE", 0.0);
        double doneRange = params.getAsDouble("A_GyroTurnDoneRange", 8.0);
        double maxOut = params.getAsDouble("A_CheesyTurnMaxOuput", 1.0);
        
        gyroPID.setPIDGains(p, i, d);
        gyroPID.setEpsilon(e);
        gyroPID.setDoneRange(doneRange);
        gyroPID.setMinDoneCycles(1);
        gyroPID.setMaxOutput(maxOut);
        
        postTurnWait = params.getAsDouble("A_CheesyTurnWait", 0.25);
        firstDone = true;
        
        isDone = false;
        this.timeout = timeout;
        
        this.reset();
    }

    public void reset() {
        firstCycle = true;
        isDone = false;
        SensorInput.getInstance().resetDriveEncoders();
        SensorInput.getInstance().resetGyro();
        CheesyVisionServer.getInstance().reset();
    }

    public boolean run() {
        if (firstCycle) {
            System.err.println("Turning");
            System.err.println("L: "+CheesyVisionServer.getInstance().getLeftCount()+ " R: "+CheesyVisionServer.getInstance().getRightCount());
            System.err.println("L: "+CheesyVisionServer.getInstance().getLeftStatus()+ " R: "+CheesyVisionServer.getInstance().getRightStatus());

            if (CheesyVisionServer.getInstance().getLeftCount() > CheesyVisionServer.getInstance().getRightCount()) {
                System.err.println("LeftGoalHot");
                gyroPID.setSetpoint(-target);
            } else {
                System.err.println("RightGoalHot");
                gyroPID.setSetpoint(target);
            }

            startTime = Timer.getFPGATimestamp();
            firstCycle = false;
        }
        Hashtable autonOutput = new Hashtable();
        
        double left  =  gyroPID.calculate(sensorInput.getGyroAngle());
        double right = -gyroPID.calculate(sensorInput.getGyroAngle());
        
        autonOutput.put("leftSpeed", new Double(left));
        autonOutput.put("rightSpeed", new Double(right));
        autonOutput.put("frontIntakeDown", Boolean.TRUE);
        autonOutput.put("driveMode", new Boolean(Constants.OMNI_MODE));

        driverInput.updateAutonData(autonOutput);

        isDone = gyroPID.isDone();
        if(isDone && firstDone)
        {
            System.err.println("TurnDoneFinished");
            doneTime = Timer.getFPGATimestamp();
            firstDone = false;
        }
        
        if (Timer.getFPGATimestamp() - startTime > timeout) {
            System.err.print("Turn Timeout");
            return true;
        }
        return Timer.getFPGATimestamp() - doneTime > postTurnWait;
    }
}
