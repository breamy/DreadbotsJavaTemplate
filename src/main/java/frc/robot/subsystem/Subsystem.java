package frc.robot.subsystem;

import edu.wpi.first.wpilibj.Timer;

public interface Subsystem {
    void setTestingCompleted(boolean testingCompleted);
    void setCurrentTestIndex(int currentTestIndex);
    Timer getTimer();
    // Testing Methods
    void testInit();
    void testPeriodic();
    boolean isTestingCompleted();
}
