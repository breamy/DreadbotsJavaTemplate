// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TimedRobot;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMax;
import frc.robot.subsystem.Climber;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.driving.Drive;
import frc.robot.subsystem.driving.DriveMode;
import frc.robot.subsystem.driving.TankDrive;
import frc.robot.subsystem.shooting.Shooter;
import frc.robot.utility.Constants;
import frc.robot.utility.DreadbotController;

import static frc.robot.utility.Constants.FLY_WHEEL_MOTOR_ID;

/**
 * The VM is configured to automatically run this class, and to call the methods corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private static final CANSparkMaxLowLevel.MotorType K_MOTORTYPE = CANSparkMaxLowLevel.MotorType.kBrushless;

    private DreadbotController primaryJoystick;
    private DreadbotController secondaryJoystick;
    private Drive drive;
    private Intake intake;
    private Climber climber;
    private Shooter shooter;
    /**
     * This method is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        primaryJoystick = new DreadbotController(0);
        secondaryJoystick = new DreadbotController(1);

        // Drivetrain
        SpeedController leftFront = new CANSparkMax(1, K_MOTORTYPE);
        SpeedController rightFront = new CANSparkMax(2, K_MOTORTYPE);
        SpeedController leftRear = new CANSparkMax(3, K_MOTORTYPE);
        SpeedController rightRear = new CANSparkMax(4, K_MOTORTYPE);
        drive = new TankDrive(leftFront, rightFront, leftRear, rightRear);

        // Intake
        CANSparkMax intakeMotor = new CANSparkMax(Constants.INTAKE_MOTOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        intake = new Intake(intakeMotor, new Solenoid(Constants.INTAKE_PIN_ID));

        // Climber
        CANSparkMax climbMotor = new CANSparkMax(10, CANSparkMaxLowLevel.MotorType.kBrushless);
        climber = new Climber(climbMotor, new Solenoid(Constants.CLIMB_TELESCOPE));

        // Shooter
        CANSparkMax flywheelMotor = new CANSparkMax(Constants.FLY_WHEEL_MOTOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        CANSparkMax hoodMotor = new CANSparkMax(Constants.AIM_MOTOR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        DigitalInput upperSwitch = new DigitalInput(1);
        DigitalInput lowerSwitch = new DigitalInput(2);
        shooter = new Shooter(flywheelMotor, hoodMotor, upperSwitch, lowerSwitch);
        shooter.startCalibraation();
    }

    /**
     * This method is called every robot packet, no matter the mode. Use this for items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
     * <p>
     * This runs after the mode specific periodic methods, but before LiveWindow and
     * SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        shooter.calibrate();
    }

    @Override
    public void autonomousInit() {

    }

    /** This method is called periodically during autonomous. Approx 50 times per second */
    @Override
    public void autonomousPeriodic() {

    }

    /** This function is called once when teleop is enabled. */
    @Override
    public void teleopInit() {
        intake.deployIntake();
    }

    /** This method is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {
        drive.drive(primaryJoystick.getYAxis(), primaryJoystick.getZAxis(), getDriveMode());
        handleIntake();
        handleClimb();
    }

    /* Reads which buttons are pushed and returns the appropriaate DriveMode */
    protected DriveMode getDriveMode() {
        if (primaryJoystick.isRightTriggerPressed()) {
            return DriveMode.TURBO;
        } else if (primaryJoystick.isRightBumperPressed()) {
            return DriveMode.TURTLE;
        } else {
            return DriveMode.NORMAL;
        }
    }

    /* reads the X and A buttons and starts, stops, or reverses the intake motor */
    protected void handleIntake() {
        if (secondaryJoystick.isXButtonPressed()) {
            intake.start();
        } else if (secondaryJoystick.isAButtonPressed()) {
            intake.reverse();
        } else {
            intake.stop();
        }
    }

    /* extends or collapses the climb arm
       and runs the winch to climb (or lower) */
    protected void handleClimb() {
        if (secondaryJoystick.isStartButtonPressed()) {
            climber.extendTelescope(true);
        } else if (secondaryJoystick.isBackButtonPressed()) {
            climber.extendTelescope(false);
        }

        if (secondaryJoystick.isRightTriggerPressed()) {
            climber.runWinchUp();
        } else if (secondaryJoystick.isLeftTriggerPressed()) {
            climber.runWinchDown();
        } else {
            climber.stopWinch();
        }
    }

    /** This function is called once when the robot is disabled. */
    @Override
    public void disabledInit() {}

    /** This function is called periodically when disabled. */
    @Override
    public void disabledPeriodic() {}

    /** This function is called once when test mode is enabled. */
    @Override
    public void testInit() {}

    /** This method is called periodically during test mode. */
    @Override
    public void testPeriodic() {}

    /*
      Methods to inject dependencies for unit tests
     */
    public void setPrimaryJoystick(DreadbotController controller) {
        this.primaryJoystick = controller;
    }
    public void setSecondaryJoystick(DreadbotController controller) {
        this.secondaryJoystick = controller;
    }
    public void setDrive(Drive drive) {
        this.drive = drive;
    }
}
