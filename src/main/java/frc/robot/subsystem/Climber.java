package frc.robot.subsystem;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;

public class Climber {

    private static final double DEFAULT_SPEED = 0.5;
    private Solenoid telescopeSol;
    private SpeedController winchMotor;

    public Climber(SpeedController motor, Solenoid solenoid) {
        winchMotor = motor;
        telescopeSol = solenoid;
    }

    public void extendTelescope(boolean extended) {
        telescopeSol.set(extended);
    }

    private void runWinch(double winchSpeed) {
        winchMotor.set(winchSpeed);
    }

    public void runWinchUp() {
        runWinch(DEFAULT_SPEED);
    }

    public void runWinchDown() {
        runWinch(-DEFAULT_SPEED);
    }

    public void stopWinch() {
        winchMotor.stopMotor();
    }
}