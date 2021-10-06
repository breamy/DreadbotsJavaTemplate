package frc.robot.subsystem.driving;
/**
 * DriveMode is the enumeration of the default final value multipliers for teleop.
 */
public enum DriveMode {
    TURBO(0.9),
    NORMAL(0.5),
    TURTLE(0.2);

    public double finalValueMultiplier;

    DriveMode(double finalValueMultiplier) {
        this.finalValueMultiplier = finalValueMultiplier;
    }
}
