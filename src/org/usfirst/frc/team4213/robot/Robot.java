
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



import com.kauailabs.nav6.frc.*;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SerialPort;
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

	// Connects to Airflo Controller on Port 0
	public static AIRFLOController controller = new AIRFLOController(0);
	// Creates Spark Motor Controllers for the 2 Spark Motors on the Test Drivetrain
	//public static Spark leftMotor = new Spark(9);
	//public static Spark rightMotor = new Spark(8);
	IMU imu = new IMU(new SerialPort(57600, SerialPort.Port.kOnboard));
	
    Command autonomousCommand;
    SendableChooser chooser;
    float desiredHeading = 0;
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
    	imu.zeroYaw();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        TankDrive.rawTankDrive(controller.getLY(), controller.getRY());
    	
    	if(controller.getButtonTripped(4) == true){
    		desiredHeading = 0;
    	}
    	else if(controller.getButtonTripped(2)){
    		desiredHeading = 90;
    	}
    	else if(controller.getButtonTripped(3)) {
    		desiredHeading = -90;
    	}
    	else if(controller.getButtonTripped(1)) {
    		desiredHeading = 180;
    	}
 		DriverStation.reportError("("+imu.getCompassHeading()+","+")\n", false);
 		//TankDrive.regTankDrive(desiredHeading, imu.getYaw());
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }

}