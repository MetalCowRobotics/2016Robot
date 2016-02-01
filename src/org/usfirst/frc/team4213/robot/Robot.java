package org.usfirst.frc.team4213.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
//import edu.wpi.first.wpilibj.command.Scheduler;
//import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import org.team4213.lib14.AIRFLOController;
//import org.usfirst.frc.team4213.robot.commands.ExampleCommand;
import org.usfirst.frc.team4213.robot.subsystems.ExampleSubsystem;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.RawData;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.opencv.core.Mat;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class Robot extends IterativeRobot{
	int session;
	Image frame;
	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();

	public static AIRFLOController controller = new AIRFLOController(1);
	public static Spark leftMotor = new Spark(9);
	public static Spark rightMotor = new Spark(8);
	
    Command autonomousCommand;
    SendableChooser chooser;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

        // the camera name (ex "cam0") can be found through the roborio web interface
        session = NIVision.IMAQdxOpenCamera("cam0",
                NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        NIVision.IMAQdxConfigureGrab(session);
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
    	NIVision.IMAQdxStartAcquisition(session);

        /**
         * grab an image, draw the circle, and provide it for the camera server
         * which will in turn send it to the dashboard.
         */
    	
        Mat m = new Mat(320,640, session);
        byte[] bb = new byte[320*640];
        RawData data = NIVision.imaqReadCustomData(frame, "");
       // m.put
        data.getBuffer().get(bb);
        
        //NIVision.getBytes(bb, dst, 0, size)
        m.put(320, 640, bb);
        
        //NIVision.Rect rect = new NIVision.Rect(200, 250, 100, 100);

        while (isOperatorControl() && isEnabled()) {

            NIVision.IMAQdxGrab(session, frame, 1);
            
            
            /*NIVision.imaqDrawShapeOnImage(frame, frame, rect,
                    DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 0.0f);*/
            		
            CameraServer.getInstance().setImage(frame);
            Timer.delay(0.005);		// wait for a motor update time
        }
        NIVision.IMAQdxStopAcquisition(session);
        tankDrive();
    	//otherDrive();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    }
    
    public void tankDrive() {
    	
    	if (controller.getLY() != 0 && controller.getRY() != 0) {
    		leftMotor.set(controller.getLY());
    		rightMotor.set(controller.getRY());
    	}
    	
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
