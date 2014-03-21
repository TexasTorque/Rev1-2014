package org.TexasTorque.TorqueLib.component;

import edu.wpi.first.wpilibj.AnalogChannel;

public class TorquePotentiometer
{
    private AnalogChannel pot;
    
    private boolean firstCycle;
    private double prevVoltage;
    private double maxVoltage;
    private double minVoltage;
    
    public TorquePotentiometer(int port)
    {
        pot = new AnalogChannel(port);
        firstCycle = true;
    }
    
    public TorquePotentiometer(int sidecar, int port)
    {
        pot = new AnalogChannel(sidecar, port);
        firstCycle = true;
    }
    
    public void setRange(double max, double min)
    {
        maxVoltage = max;
        minVoltage = min;
    }
    
    public double get()
    {
        return 1 - limitValue((getRaw() - minVoltage) / (maxVoltage - minVoltage));
    }
    
    public double getRaw()
    {
        double temp = pot.getVoltage();
        if(!firstCycle && Math.abs(temp - prevVoltage) > 4)
        {
            if(prevVoltage > 4.8) {
                temp = 5 + temp;
            } else if (prevVoltage < 0.2)
            {
                temp = temp - 5;
            }
        } else {
            firstCycle = false;
        }
        prevVoltage = temp;
        return temp;
    }
    
    private double limitValue(double value)
    {
        if(value > 1.0)
        {
            return 1.0;
        }
        else
        {
            return value;
        }
    }
    
}
