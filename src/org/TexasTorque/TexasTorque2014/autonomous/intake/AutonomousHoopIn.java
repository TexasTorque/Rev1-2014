/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.TexasTorque.TexasTorque2014.autonomous.intake;

import java.util.Hashtable;
import org.TexasTorque.TexasTorque2014.autonomous.AutonomousCommand;

/**
 *
 * @author Gijs
 */
public class AutonomousHoopIn extends AutonomousCommand {

    public void reset() {
    }

    public boolean run() {
        Hashtable autonOutputs = new Hashtable();
        autonOutputs.put("hoopIn", Boolean.TRUE);
        
        driverInput.updateAutonData(autonOutputs);
        
        return true;
    }

}
