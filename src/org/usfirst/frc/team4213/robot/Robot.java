package org.usfirst.frc.team4213.robot;

// Import Various Java Utilities
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import edu.wpi.first.wpilibj.DriverStation;

/* @Authors:
 * --
 * 
 * 
 * @Mentors
 * --Tim Robert
 * --
 */
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// Import the Custom Extension Library Items
import org.team4213.lib14.AIRFLOController;
import org.team4213.lib14.CowCamController;
import org.team4213.lib14.CowCamServer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 * 
 *
 */

public class Robot extends IterativeRobot {

	// Connects to Airflo Controller on Port 0
	public static AIRFLOController controller = new AIRFLOController(0);
	// Creates Spark Motor Controllers for the 2 Spark Motors on the Test
	// Drivetrain
	public static Spark leftMotor = new Spark(9);
	public static Spark rightMotor = new Spark(8);
	// Camera Controller
	public CowCamServer camServer = new CowCamServer(1180);
	// The Thread Pool / Executor of Tasks to Use
	public ExecutorService executor = Executors.newWorkStealingPool();

	// The Task Run to Handle the Shooter Camera ( Aim at Tower )
	public Callable<int[]> shooterImageProcess = () -> {

		return new int[3];

	};

	// A new Camera Controller for the Shooter
	public CowCamController<int[]> shooterCamController = new CowCamController<int[]>(0, 20,
			Optional.of(shooterImageProcess));;

	/*
	 * We added the OpenCV libraries to the RoboRIO manually over FTP ( Specific
	 * Builds for the Roborio / ARMV7 )
	 */
	static {
		// Loads the OpenCV Library from The RoboRIO's Local Lib Directory
		System.load("/usr/local/lib/lib_OpenCV/java/libopencv_java2410.so");
	}

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {

		// Runs the Camera
		camServer.start(shooterCamController, executor);

		DriverStation.reportError("got past thread init", false);

	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	public void disabledInit() {

	}

	public void disabledPeriodic() {
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	public void autonomousInit() {

	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
	}

	public void teleopInit() {
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {

		DriverStation.reportError("telop period reached", false);

		tankDrive();

	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
	}

	/**
	 * Sets motors to Appropriate Speeds Based on Controller Input ( Tank Style
	 * )
	 */
	public void tankDrive() {
		leftMotor.set(controller.getLX());
		rightMotor.set(controller.getRX());
		DriverStation.reportError("(" + controller.getLY() + "," + controller.getRY() + ")\n", false);

	}

}
