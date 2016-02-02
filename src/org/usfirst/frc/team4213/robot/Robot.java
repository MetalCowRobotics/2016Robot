
package org.usfirst.frc.team4213.robot;

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
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import org.team4213.lib14.AIRFLOController;
import org.usfirst.frc.team4213.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {


	public static AIRFLOController controller = new AIRFLOController(0);
	public static Spark leftMotor = new Spark(9);
	public static Spark rightMotor = new Spark(8);
	
    Command autonomousCommand;
    SendableChooser chooser;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {

    }
	
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
	 * or additional comparisons to the switch structure below with additional strings & commands.
	 */
    public void autonomousInit() {
        
		/* String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
		switch(autoSelected) {
		case "My Auto":
			autonomousCommand = new MyAutoCommand();
			break;
		case "Default Auto":
		default:
			autonomousCommand = new ExampleCommand();
			break;
		} */
    	
    	// schedule the autonomous command (example)
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
        
        tankDrive();
    	//otherDrive();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
    
    public void tankDrive() {
    	
    		leftMotor.set(controller.getLX());
    		rightMotor.set(controller.getRX());
    		DriverStation.reportError("("+controller.getLY()+","+controller.getRY()+")\n", false);
    	
    }
    
    public void otherDrive() {
    	
    	if (controller.getLY() != 0) {
    		leftMotor.set(controller.getLY());
    		rightMotor.set(controller.getLY());
    	}
    	
    	if (controller.getLX() != 0) {
    		leftMotor.set(controller.getLX());
    		rightMotor.set(-controller.getLX());
    	}
    	
    	if (controller.getRY() != 0) {
    		leftMotor.set(controller.getLY());
    		rightMotor.set(controller.getLY());
    	}
    	
    	if (controller.getRX() != 0) {
    		leftMotor.set(controller.getRX());
    		rightMotor.set(-controller.getRX());
    	}
    	
    }
}
