package frc.robot.utility;

import edu.wpi.first.wpilibj.Joystick;

public class DreadbotController {
	private Joystick joystick;

	public DreadbotController(int joystickPort) {
		this.joystick = new Joystick(joystickPort);
	}

	public double getXAxis() {
		return joystick.getRawAxis(0);
	}

	public double getYAxis() {
		return joystick.getRawAxis(1);
	}

	public double getZAxis() {
		return joystick.getRawAxis(2);
	}

	public double getWAxis() {
		return joystick.getRawAxis(3);
	}

	public boolean isXButtonPressed() {
		return joystick.getRawButton(1);
	}

	public boolean isAButtonPressed() {
		return joystick.getRawButton(2);
	}

	public boolean isBButtonPressed() {
		return joystick.getRawButton(3);
	}

	public boolean isYButtonPressed() {
		return joystick.getRawButton(4);
	}

	public boolean isLeftBumperPressed() {
		return joystick.getRawButton(5);
	}

	public boolean isRightBumperPressed() {
		return joystick.getRawButton(6);
	}

	public boolean isLeftTriggerPressed() {
		return joystick.getRawButton(7);
	}

	public boolean isRightTriggerPressed() {
		return joystick.getRawButton(8);
	}

	public boolean isBackButtonPressed() {
		return joystick.getRawButton(9);
	}

	public boolean isStartButtonPressed() {
		return joystick.getRawButton(10);
	}

	public Joystick getNativeWPIJoystick() {
		return joystick;
	}

	public boolean getRawButton(int button) {
		return joystick.getRawButton(button);
	}

	public double getRawAxis(int axis) {
		return joystick.getRawAxis(axis);
	}

	public boolean isConnected() {
		return joystick.isConnected();
	}

	public String getName() {
		return joystick.getName();
	}
}
