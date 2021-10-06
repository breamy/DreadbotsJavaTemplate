package frc.robot.subsystem.driving;

import frc.robot.subsystem.Subsystem;

public interface Drive extends Subsystem {

    void drive(double forwardAxisFactor,
               double rotationAxisFactor);

    void drive(double forwardAxisFactor,
               double rotationAxisFactor,
               final DriveMode driveMode);

    public void stop();
}