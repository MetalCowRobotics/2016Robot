package org.usfirst.frc.team4213.robot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;





// Import the Custom Extension Library Items
import org.team4213.lib14.AIRFLOController;
import org.team4213.lib14.CowCamController;
import org.team4213.lib14.CowCamServer;
import org.team4213.lib14.EncMotorTalon;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DriverStation;
/* @Authors:


Ball flapper = 0
Turret (lazySusan) = 1 
Intake = 2
ShooterLifter = 3 
BallShooter = 4
BoomClimberLift = 5
Telescope(boom) = 6
Skis = 7
Left = 
Right = 

// BOOM : POSITIVE == UP









 */



import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Spark;





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
	AIRFLOController driverController;
	AIRFLOController gunnerController;
	
	//Physical Robot
	RobotDrive myDrive;  //drivetrain this is tank style but with wheels
	Intake intake; //intake, we load the balls in with this
	//public static Skis skis = new Skis(1);

	Shooter shooter;
	Turret turret;
	Encoder fancyEncoder;
	CANTalon shootsBalls;
	CANTalon ballKicker;
	CANTalon boomLifter;
	
	
	
	
	// Camera Controller
	public static CowCamServer camServer = new CowCamServer(1180);
	// The Thread Pool / Executor of Tasks to Use
	public ExecutorService executor = Executors.newWorkStealingPool();

	// A new Camera Controller for the Shooter
	public CowCamController shooterCamController = new CowCamController(0, 20,CowCamController.ImageTask.SHOOTER);
	
	// The Task Run to Handle the Shooter Camera ( Aim at Tower )

	
	
	
	
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
	@Override
	public void robotInit() {
	
		// Connects to Airflo Controller on Port 0
		driverController = new AIRFLOController(0);
		//gunnerController = new AIRFLOController(0);  //TODO: We need the second controller for the other stuff
		
		/*intake = new Intake(new CANTalon(2));
		
		Spark leftMotor = new Spark(9);
		Spark rightMotor = new Spark(8);
		
		myDrive = new RobotDrive(leftMotor, rightMotor);
		
		shootsBalls = new CANTalon(4);
		ballKicker = new CANTalon(0);
		boomLifter = new CANTalon(5);
		
		
		
		shooter = new Shooter();
		
		*/
		// Runs the Camera
		fancyEncoder = new Encoder(4,5,false,CounterBase.EncodingType.k4X);
		ballKicker = new CANTalon(0);
		//ballKicker.setEncPosition(0);
		
		//turret = new Turret();
		camServer.start(shooterCamController,executor);

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
		
		ballKicker.set(driverController.getRX());
		DriverStation.reportError("" + fancyEncoder.get(), false);
	/*	if(driverController.getButton(2)){
			ballKicker.set(7*27*3*(180/360));
		}
		ballKicker.update();
		DriverStation.reportError("\n"+ballKicker.getEncPosition(), false);*/
/*
		if(driverController.getButton(7)){
			if(driverController.getButton(4)){
				turret.raiseTurret();
			}else if(driverController.getButton(1)){
				turret.dropTurret();
			}
		}else{
			turret.stop();
		}
		
*/
		//DriverStation.reportError(""+ turret.getAngle(), false);
		
		/*
		if(!driverController.getButton(8)){
			tankDrive();
		}
		
		
		
		
		intakeDriver();
		//DriverStation.reportError("/n Spee"+shooter.motorPitch.getSpeed(), false);
		//DriverStation.reportError("/n"+shooter.motorPitch.getEncPosition(), false);
		//DriverStation.reportError("        "+shooter.motorPitch.get(), false);
		//DriverStation.reportError("/n"+shooter.motorPitch.getPosition(), false);
		//shooter.motorPitch.set(100);

//		if(driverController.getButton(2)){
//			shooter.angleSet(90);
//			DriverStation.reportError("        1:",false);
//		}
		
		
		if(driverController.getButton(8)){

			
			//up/down on turret
			if(driverController.getLY()>0){
				shooter.angleIncrease();
			}
			if(driverController.getLY()<0){
				shooter.angleDecrease();
			}
			
			
			//up/down on boom
			if(driverController.getRY()>0){
				boomLifter.set(.25); //up
			} else if(driverController.getRY()<0){ //down
				boomLifter.set(-.25);
			}else{ //stop
				boomLifter.set(0);
			}
			
			
			
		}

		
		
		if(driverController.getButton(3)){ //shoot ball
			shootsBalls.set(.6);
		}else if(driverController.getButton(2)){ //out???? ball
			shootsBalls.set(-1.0);
		}else{
			shootsBalls.set(0);
		}
		
		
		if(driverController.getButton(5)){ //kicker out
			ballKicker.set(.25);
		}else if(driverController.getButton(6)){ //kicker in
			ballKicker.set(-.5);
		}else{
			ballKicker.set(0);
		}
		
	
		
		
		
		
//		if(driverController.getButton(1)){
//			shooter.tiltUp(0);
//		}else if(driverController.getButton(2)){
//			shooter.tiltUp(40);
//		}else if(driverController.getButton(3)){
//			shooter.tiltUp(90);
//		}else if(driverController.getButton(4)){
//			shooter.tiltUp(360);
//		}
		*/
		
		

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		DriverStation.reportError("/n Encoder : "+fancyEncoder.get(),false);
	}

	/**
	 * Sets motors to Appropriate Speeds Based on Controller Input ( Tank Style
	 */
	public void tankDrive() {
		
		//Calculate Max Speed Adjustment Ratio
		double topSpeedSprint = 1.00;
		double topSpeedCrawl = 0.3;
		double topSpeedNormal = 0.6;
		double topSpeedRatio = driverController.getThrottle(topSpeedNormal, topSpeedCrawl, topSpeedSprint);
		
		
		//OPTION_1 - BASIC TANK
		//Can we do this and then 'straighten' it with the IMU feedback?
		//leftMotor.set(controller.getLY()*topSpeedRatio);
		//rightMotor.set(controller.getRY()*topSpeedRatio);
		
		
		//OPTION_2 - BASIC ARCADE --- Untested (recomended)
		//https://wpilib.screenstepslive.com/s/3120/m/7912/l/95588-getting-your-robot-to-drive-with-the-robotdrive-class
		//Using one stick to turn and one to throttle should help with steering?
		//Using WPI is more stable on this first go for us maybe?  We can get fancy later?!
		double throttle = driverController.getLY()*topSpeedRatio;
		double spin = driverController.getRX()*topSpeedRatio;
		boolean squareUnits = true;
		myDrive.arcadeDrive(throttle, spin, squareUnits);
		
		//TODO: Dashboard Feedback and Status Charts

	}
	
	
	
	/**
	 * Doing the Intake stuff here.
	 * There is no encoder it is just on and off
	 */
	public void intakeDriver(){
		
		if(driverController.getButton(4)){ //forward
			intake.intake(1.0);
		} else if(driverController.getButton(1)){
			intake.intake(-1.0); //backwards
		} else{
			intake.intakeStop(); //stops it if there is no button pushed
		}
		
		//TODO: Dashboard feedback and status
		
		
	}

}
