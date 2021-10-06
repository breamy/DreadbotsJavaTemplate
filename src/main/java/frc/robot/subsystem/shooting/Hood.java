package frc.robot.subsystem.shooting;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpiutil.math.MathUtil;

public class Hood {
    public static final double DEFAULT_SPEED = 0.75;
    public static final double FUDGE = 10.0;
    private final CANSparkMax hoodMotor;
    private final CANPIDController hoodMotorPID;
    private final CANEncoder hoodMotorEncoder;

    // Limit Switches
    private DigitalInput upperLimitSwitch;
    private DigitalInput lowerLimitSwitch;

    private boolean calibrated = false;

    // Limit Switch Variables
    private int minHoodPosition;
    private int maxHoodPosition;
    boolean lowerLimitHit = false;
    boolean upperLimitHit = false;
    private double aimPosition;
    private double range;
    protected States state;

    public Hood(CANSparkMax motor, DigitalInput upperSwitch, DigitalInput lowerSwitch) {
        hoodMotor = motor;
        hoodMotorPID = hoodMotor.getPIDController();
        hoodMotorEncoder = hoodMotor.getEncoder();

        upperLimitSwitch = upperSwitch;
        lowerLimitSwitch = lowerSwitch;

        hoodMotorPID.setP(0.1);
        hoodMotorPID.setI(0);
        hoodMotorPID.setD(0);
        hoodMotorPID.setIZone(0);
        hoodMotorPID.setFF(0.000015);
        hoodMotorPID.setOutputRange(-1.0, 1.0);

        state = States.CALIBRATING;
    }

    public void startCalibration() {
        state = States.CALIBRATING;
        setHoodPercentOutput(DEFAULT_SPEED);
    }

    public boolean calibrate() {
        if (calibrated) return true;

        if (lowerLimitHit && upperLimitHit) {
            System.out.println("both limits hit, hood calibration complete");
            // ready to aim.
            calibrated = true;
            range = maxHoodPosition - minHoodPosition;
            SmartDashboard.putNumber("range", range);
            state = States.IDLE;
            return true;
        }
        // Approach the lower limit of the hood position.
        if (isLowerLimitSwitchActive() && !lowerLimitHit) {
            minHoodPosition = getHoodPosition();
            SmartDashboard.putNumber("lowerlimit", minHoodPosition);
            // Go back the other way
            setHoodPercentOutput(hoodMotor.get() * -1);
            lowerLimitHit = true;
            return false;
        }
        // Approach the upper limit of the hood position.
        if (isUpperLimitSwitchActive() && !upperLimitHit) {
            maxHoodPosition  = getHoodPosition();
            SmartDashboard.putNumber("upperlimit", maxHoodPosition);
            // Go back the other way
            setHoodPercentOutput(hoodMotor.get() * -1);
            upperLimitHit = true;
            return false;
        }
        return false;
    }

    public boolean isReady() {
        return state.isReady();
    }

    public void setHoodPercentOutput(double percentOutput) {
        if (isLowerLimitSwitchActive() && percentOutput > 0) {
            percentOutput = 0.0;
        } else if (isUpperLimitSwitchActive() && percentOutput < 0) {
            percentOutput = 0.0;
        }
        hoodMotor.set(percentOutput);
    }

    public int getHoodPosition() {
        return (int) hoodMotorEncoder.getPosition();
    }

    public void setHoodPosition(double position) {
        if (state == States.CALIBRATING) {
            System.out.println("Hood not calibrated, cannot set hood position");
            return;
        }
        state = States.ADJUSTING;
        // ensure the target is within range
        // we allow for going slightly past the lower limit switch
        position = MathUtil.clamp(position, -0.12d, 1.0d);

        position = minHoodPosition + (position * range);
        double currentPosition = hoodMotorEncoder.getPosition();
        if (isClose(position, currentPosition)) {
            state = States.READY;
        }
        hoodMotorPID.setReference(position, ControlType.kPosition);
        SmartDashboard.putNumber("shooterAimPos", currentPosition);
        SmartDashboard.putNumber("shooterAimPosExpected", position);
    }

    private boolean isClose(double target, double actual) {
        return target - FUDGE < actual && actual < target + FUDGE;
    }

    public boolean isUpperLimitSwitchActive() {
        return !upperLimitSwitch.get();
    }

    public boolean isLowerLimitSwitchActive() {
        return !lowerLimitSwitch.get();
    }

    protected enum States {
        IDLE(true),
        CALIBRATING(false),
        ADJUSTING(false),
        READY(true);

        private boolean ready;

        States(boolean ready) {
            this.ready = ready;
        }

        public boolean isReady() {
            return ready;
        }
    }
}
