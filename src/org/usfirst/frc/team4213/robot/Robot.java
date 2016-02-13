
package org.usfirst.frc.team4213.robot;

import org.team4213.lib14.AIRFLOController;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;

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
import edu.wpi.first.wpilibj.TalonSRX;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	// Connects to Airflo Controller on Port 0
	public static AIRFLOController controller = new AIRFLOController(0);
	// Creates Spark Motor Controllers for the 2 Spark Motors on the Test
	// Drivetrain
	public static Spark leftMotor = new Spark(9);
	public static Spark rightMotor = new Spark(8);
	public static TalonSRX frontIntakeMotor = new TalonSRX(6);
	public static TalonSRX turretPitch = new TalonSRX(5);
	public static TalonSRX turretYaw = new TalonSRX(4);
	public static TalonSRX cannonWheels = new TalonSRX(3);
	public static Encoder encoder;

	public static Intake intake = new Intake(1);
	public static Skis skis = new Skis(2);

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	@Override
	public void robotInit() {

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
		// tankDrive();
		// curveDrive();
		// intake();
		// skis();

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
	}
	/*
	 * public void tankDrive() {
	 * 
	 * if (controller.getLY() > 0) { leftMotor.set(Math.pow(controller.getLY(),
	 * 2)); } else { leftMotor.set(-1 * Math.pow(controller.getLY(), 2)); }
	 * 
	 * if (controller.getRY() > 0) { rightMotor.set(Math.pow(controller.getRY(),
	 * 2)); } else { rightMotor.set(-1 * Math.pow(controller.getRY(), 2)); }
	 * 
	 * }
	 * 
	 * public void intake() { double speed = 0.5;
	 * 
	 * if (controller.getButton(7)) { intake.intake(speed); }
	 * 
	 * }
	 * 
	 * public void skis() { double speed = 0.5;
	 * 
	 * if (controller.getButton(2)) { skis.setSkisUp(speed); } if
	 * (controller.getButton(1)) { skis.setSkisDown(speed); } }
	 */
}
