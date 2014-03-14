package org.TexasTorque.TorqueLib.component;

import edu.wpi.first.wpilibj.AnalogChannel;

public class TorquePotentiometer
{
    private AnalogChannel pot;
    
    private double prevVoltage;
    private double maxVoltage;
    private double minVoltage;
    
    public TorquePotentiometer(int port)
    {
        pot = new AnalogChannel(port);
    }
    
    public TorquePotentiometer(int sidecar, int port)
    {
        pot = new AnalogChannel(sidecar, port);
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
        if(Math.abs(temp - prevVoltage) > 4)
        {
            if(prevVoltage > 4) {
                temp = prevVoltage + temp;
            } else if (prevVoltage < 1)
            {
                temp = temp - 5;
            }
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
