package org.usfirst.frc.team4213.robot;

// Import Various Java Utilities
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Import the Custom Extension Library Items
import org.team4213.lib14.AIRFLOController;
import org.team4213.lib14.CowCamController;
import org.team4213.lib14.CowCamServer;

import edu.wpi.first.wpilibj.Encoder;

import edu.wpi.first.wpilibj.DriverStation;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TalonSRX;

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
	public static AIRFLOController driverController = new AIRFLOController(0);

	// Creates Spark Motor Controllers for the 2 Spark Motors on the Test
	public static Spark leftMotor = new Spark(9);
	public static Spark rightMotor = new Spark(8);
	public static TalonSRX frontIntakeMotor = new TalonSRX(6);
	public static TalonSRX cannonAngle = new TalonSRX(5);
	public static TalonSRX cannonSpin = new TalonSRX(4);
	public static TalonSRX cannonMotors = new TalonSRX(3);
	public static Encoder encoder;

	// Create robot parts
	public static Intake intake = new Intake(1);
	public static Skis skis = new Skis(2);

	/*
	 * Create the cameras
	 */
	public static CowCamServer camServer = new CowCamServer(1180);
	public CowCamController<int[]> shooterCamController = new CowCamController<int[]>(0, 20,
			Optional.of(new ShooterImageProcessor()));

	// The Thread Pool / Executor of Tasks to Use
	public ExecutorService executor = Executors.newWorkStealingPool();
	// The Task Run to Handle the Shooter Camera ( Aim at Tower )

	/*
	 * We added the OpenCV libraries to the RoboRIO manually over FTP ( Specific
	 * Builds for the Roborio / ARMV7 )
	 */
	static {
		// Loads the OpenCV Library from The RoboRIO's Local Lib Directory
		System.load("/usr/local/lib/lib_OpenCV/java/libopencv_java2410.so");
	}

	/*
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
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
	@Override
	public void disabledInit() {
	}

	@Override
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
	@Override
	public void autonomousInit() {
		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); }
		 */

		// schedule the autonomous command (example)

	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void teleopInit() {
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		encoder = new Encoder(0, 3, false, Encoder.EncodingType.k4X);

		DriverStation.reportError("" + encoder + "", false);
		
		//This code will loop again and again
		tankDrive();
		intake();
		skis();

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}

	
	
	
	//TODO: JavaDoc is missing
	public void tankDrive() {
		
		//TODO: Does this come back as a -1 to 1 value??
		//TODO: if the controller values come back as other numbers we will need to adjust
		
		//TODO: Here is how we want this:
		/*
		 * The sticks at normal need to have a MAXSPEED_NORMAL=50% robot top speed
		 * If the user presses the front bumper (top) buttons then the MAXSPEED_SPRINT=100% robot top speed
		 * If the user pressed the from trigger (bottom) buttons then the MAXSPEED_CREEP=25% robot top speed
		 * Bumper and Trigger buttons should be mirrored, so left or right bumper is Sprint
		 * and left OR right trigger is Creep
		 * If both bumpers and triggers are pressed at the same time, then the robot should creep
		 * 
		 * Otherwise, speed from 0 should be a linear proportion of the % of angle on the stick
		 * Because it will be difficult to 
		 * 
		 * ATTENTION: Look at AIRFLOController.getThrottle();
		 * 
		 * MAXSPEED_NORMAL=50%
		 * MAXSPEED_SPRINT=100%
		 * MAXSPEED_CREEP=25%
		 * 
		 * if(LTrigger or RTrigger is pressed){
		 *    currentMaxSpeed = MAXSPEED_CREEP;
		 * }elseif(LBumper or RBumper is pressed){
		 *    currentMaxSpeed = MAXSPEED_SPRINT;
		 * }else{
		 *    currentMaxSpeed = MAXSPEED_NORMAL;
		 * }
		 * 
		 * ALSO: In the future we need to extend AIRFLOController to make
		 * a tank controller, slide controller... etc... so we have standard controller rigs.
		 * 
		 * 
		 */
		
		
		
		if (driverController.getLY() > 0) {
			leftMotor.set(driverController.getLY());
		} else {
			leftMotor.set(driverController.getLY());
		}
		
		

		if (driverController.getRY() > 0) {
			rightMotor.set(driverController.getRY());
		} else {
			rightMotor.set(driverController.getRY());
		}
		
		
	}
	
	
	
	
	
	
	
	//TODO: JavaDoc is missing
	public void intake() {
		double speed = 0.5;
		if (driverController.getButton(7)) {
			intake.intake(speed);
		}
	}

	
	//TODO: JavaDoc is missing
	public void skis() {
		double speed = 0.5;
		if (driverController.getButton(2)) {
			skis.setSkisUp(speed);
		}
		if (driverController.getButton(1)) {
			skis.setSkisDown(speed);
		}
	}

}
