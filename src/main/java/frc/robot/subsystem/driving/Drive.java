package frc.robot.subsystem.driving;

import frc.robot.subsystem.Subsystem;

public interface Drive extends Subsystem {

    void drive(double forwardAxisFactor,
               double rotationAxisFactor);

    void drive(double forwardAxisFactor,
               double rotationAxisFactor,
               final DriveMode driveMode);

    public void stop();

    /**
     * DriveMode is the enumeration of the default final value multipliers for teleop.
     */
    enum DriveMode {
        TURBO(0.9),
        NORMAL(0.5),
        TURTLE(0.2);

        public double finalValueMultiplier;

        DriveMode(double finalValueMultiplier) {
            this.finalValueMultiplier = finalValueMultiplier;
        }
    }
}