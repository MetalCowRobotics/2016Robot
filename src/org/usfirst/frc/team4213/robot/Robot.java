package org.usfirst.frc.team4213.robot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
//import edu.wpi.first.wpilibj.CameraServer.CameraData;
import edu.wpi.first.wpilibj.command.Command;

//import edu.wpi.first.wpilibj.command.Scheduler;
//import edu.wpi.first.wpilibj.livewindow.LiveWindow;


/* this is whitespace to deter vish karthikeyan 
 * 
 * 
 * 
 *  HULLO . It's me . I was wondering if after all these years
 *  you'd like to meet . To go over . Everything . They say that
 *  times supposed to heal ya . but I ain't done with healing. 
 *  HULLO :D :D :D :D :D :D :D " STOP STOP " - VISH VISH VISH
 * 
 * 
 * 
 * */








import org.opencv.core.Core;
import org.opencv.core.MatOfByte;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;


import org.team4213.lib14.AIRFLOController;
//import org.usfirst.frc.team4213.robot.commands.ExampleCommand;





import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.RawData;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;


//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


//import org.opencv.core.Core;
//import org.opencv.core.Mat;

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
	
	private static final byte[] kMagicNumber = {0x01, 0x00, 0x00, 0x00};
	
	public static AIRFLOController controller = new AIRFLOController(1);
	public static Spark leftMotor  = new Spark(9);
	public static Spark rightMotor = new Spark(8);
	
    Command autonomousCommand;
    SendableChooser chooser;

    VideoCapture vc = new VideoCapture();
    
    
    /*
     * We added the OpenCV  libraries to the RoboRIO manually over FTP 
     * ( Specific Builds for the Roborio / ARMV7 )
     */
    
    // Loads the OpenCV Library from The RoboRIO's Local Lib Directory 
    static {
    	System.load("/usr/local/lib/lib_OpenCV/java/libopencv_java2410.so");
    }
    
    
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
    	DriverStation.reportError("telop period reached", false);
    	try {
			serve();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {	
			e.printStackTrace();
		}
    	//NIVision.IMAQdxStartAcquisition(session);
        
 
       /*
       Mat m = new Mat(320,640, session);
       RawData data =
                NIVision.imaqFlatten(frame, NIVision.FlattenType.FLATTEN_IMAGE,
                    NIVision.CompressionType.COMPRESSION_JPEG, 10 * 50);
            ByteBuffer buffer = data.getBuffer();
            
       byte[] bb = new byte[buffer.limit()];

        
       buffer.get(bb);
         
        
       m.put(320, 640, bb);
        
        //NIVision.Rect rect = new NIVision.Rect(200, 250, 100, 100);

        while (isOperatorControl() && isEnabled()) {

            NIVision.IMAQdxGrab(session, frame, 1);
            
            
            //NIVision.imaqDrawShapeOnImage(frame, frame, rect, DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 0.0f);
            		
            CameraServer.getInstance().setImage(frame);
            Timer.delay(0.005);		// wait for a motor update time
        }
        NIVision.IMAQdxStopAcquisition(session);
        */
    	
    	
    	
    	
        tankDrive();
    	//otherDrive();
    }
    void serve() throws IOException, InterruptedException {
    	Thread.sleep(15*1000);
    	vc.open(0,320,240,7.5);
    	
        ServerSocket socket = new ServerSocket();
        socket.setReuseAddress(true);
        InetSocketAddress address = new InetSocketAddress(1180);
        socket.bind(address);

        while (true) {
          try {
            Socket s = socket.accept();

            DataInputStream is  = new DataInputStream(s.getInputStream());
            DataOutputStream os = new DataOutputStream(s.getOutputStream());

            int fps         = is.readInt();
            int compression = is.readInt();
            int size        = is.readInt();

            if (compression != -1) {
              DriverStation.reportError("Choose \"USB Camera HW\" on the dashboard", false);
              s.close();
              continue;
            }

            // Wait for the camera


            long period = (long) (1000 / (1.0 * fps));
            while (true) {
              long t0 = System.currentTimeMillis();
              MatOfInt params = new MatOfInt(Highgui.IMWRITE_JPEG_QUALITY, 10*50);
              Mat m = new Mat();
              vc.read(m);
              vc.read(m);
              Highgui.imwrite("/home/lvuser/fancy.jpg", m);
          	  vc.read(m);
          	  MatOfByte matByte = new MatOfByte();
          	  Highgui.imencode(".jpg", m, matByte, params);
          	  DriverStation.reportError("It's Working", false);
          	  
          	  byte[] videoBits = matByte.toArray();
          	 
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
          	  
              

              // write numbers
              try {
                os.write(kMagicNumber);
                os.writeInt(videoBits.length);
                os.write(videoBits);
                os.flush();
                long dt = System.currentTimeMillis() - t0;

                if (dt < period) {
                  Thread.sleep(period - dt);
                }
              } catch (IOException | UnsupportedOperationException ex) {
                DriverStation.reportError(ex.getMessage(), true);
                break;
              } finally {
                
              }
            }
          } catch (IOException ex) {
            DriverStation.reportError(ex.getMessage(), true);
            continue;
          }
        }
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
    

}
