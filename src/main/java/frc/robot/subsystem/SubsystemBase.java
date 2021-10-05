package frc.robot.subsystem;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystem.test.TestSegment;

import java.util.ArrayList;

public abstract class SubsystemBase implements Subsystem {
	protected final String name;

	private final ArrayList<TestSegment> tests;
	private final ArrayList<Double> testDurationsSeconds;
	private final ArrayList<String> testNames;
	int currentTestIndex;
	int previousTestIndex;

	// Testing only
	private boolean testingCompleted;

	private final Timer timer;

	public SubsystemBase(String name) {
		this.name = name;
		tests = new ArrayList<>();
		testDurationsSeconds = new ArrayList<>();
		testNames = new ArrayList<>();
		currentTestIndex = 0;
		previousTestIndex = -1;

		testingCompleted = false;

		timer = new Timer();
	}

	protected void addTest(TestSegment testSegment, String testName, double testDurationSeconds) {
		tests.add(testSegment);
		testDurationsSeconds.add(testDurationSeconds);
		testNames.add(testName);
	}

	protected void addTest(TestSegment testSegment, double testDurationSeconds) {
		tests.add(testSegment);
		testDurationsSeconds.add(testDurationSeconds);
		testNames.add("");
	}

	public final void testInit() {
		try{
			System.out.printf("Starting subsystem %s test routine...%n", name);
		}
		catch(Exception e){
			System.err.println(e);
		}
		timer.start();
	}

	public void testPeriodic() {
		double testSeconds = testDurationsSeconds.get(currentTestIndex);
		if(previousTestIndex != currentTestIndex) {
			if(!testNames.get(currentTestIndex).isEmpty())
				System.out.printf("Running Test %s: %s%n", name, testNames.get(currentTestIndex));

			tests.get(currentTestIndex).perform();
			previousTestIndex = currentTestIndex;
		}

		if(currentTestIndex >= tests.size() - 1) {
			testingCompleted = true;
			return;
		}
	
		if(!timer.hasElapsed(testSeconds)){
			return;
		}
		
		++currentTestIndex;
		timer.stop();
		timer.reset();
		timer.start();
	}

	public Timer getTimer() {
		return timer;
	}

	public boolean isTestingCompleted() {
		return testingCompleted;
	}

	public void setTestingCompleted(boolean testingCompleted) {
		this.testingCompleted = testingCompleted;
	}

	public void setCurrentTestIndex(int currentTestIndex) {
		this.currentTestIndex = currentTestIndex;
	}
}
