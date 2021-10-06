package frc.robot.subsystem.shooting;

import com.revrobotics.*;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpiutil.math.MathUtil;

public class Shooter {
    boolean readyToAim = false;

    private Hood hood;
    // Motor Controllers
    private CANSparkMax flywheelMotor;

    // PID Controllers
    private final CANPIDController flywheelMotorPID;

    // Encoders
    private final CANEncoder flywheelMotorEncoder;

    // Vision Light
    private final Solenoid visionLEDRing = new Solenoid(7);

    public Shooter(CANSparkMax flywheelMotor, CANSparkMax hoodMotor, DigitalInput upperSwitch, DigitalInput lowerSwitch) {
        this.flywheelMotor = flywheelMotor;
        hood = new Hood(hoodMotor, upperSwitch, lowerSwitch);

        flywheelMotor.restoreFactoryDefaults();

        // Get the PID Controller Objects
        flywheelMotorPID = flywheelMotor.getPIDController();

        // Get the Encoder Objects
        flywheelMotorEncoder = flywheelMotor.getEncoder();

        // Tune PID values
        flywheelMotorPID.setP(9e-3);
        flywheelMotorPID.setI(1e-6);
        flywheelMotorPID.setD(0);
        flywheelMotorPID.setIZone(0);
        flywheelMotorPID.setFF(0.000015);
        flywheelMotorPID.setOutputRange(-1.0, 1.0);
    }

    public void startCalibraation() {
        hood.startCalibration();
    }

    public boolean calibrate() {
        return hood.calibrate();
    }

    public void shoot(double rpm) {
        if (!hood.isReady()) {
            System.out.println("Hood not calibrated yet, cannot shoot");
            return;
        }
        flywheelMotorPID.setReference(rpm, ControlType.kVelocity);
    }

    public void shoot(double rpm, double hoodPosition) {
        hood.setHoodPosition(hoodPosition);
        if (!hood.isReady()) {
            System.out.println("Hood not calibrated yet, cannot shoot");
            return;
        }

        flywheelMotorPID.setReference(rpm, ControlType.kVelocity);
    }

    public void setShootingPercentOutput(double percentOutput) {
        flywheelMotor.set(percentOutput);
    }

    public int getShootingSpeed() {
        // Cast to int for ease of comparison
        return (int) flywheelMotorEncoder.getVelocity();
    }

    public boolean isReadyToAim() {
        return readyToAim;
    }

    public void setReadyToAim(boolean readyToAim) {
        this.readyToAim = readyToAim;
    }

    public void setPID(double p, double i, double d) {
        flywheelMotorPID.setP(p);
        flywheelMotorPID.setI(i);
        flywheelMotorPID.setD(d);
    }

    public void setVisionLight(boolean visionLightEnabled) {
        visionLEDRing.set(visionLightEnabled);
    }
}