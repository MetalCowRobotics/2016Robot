package org.usfirst.frc.team4213.robot;

import edu.wpi.first.wpilibj.*;


import com.kauailabs.nav6.frc.*;

import org.team4213.lib14.AIRFLOController;
public class TankDrive {
	public static Spark leftMotor = new Spark(9);
	public static Spark rightMotor = new Spark(8);
	SerialPort serial_port = new SerialPort(57600,SerialPort.Port.kOnboard);
	  IMUAdvanced imu;
	 public boolean isRegulated = false;
	 
	 
	 
	 
	  public void zeroGyro() {
			imu.zeroYaw();
		}
	  
	 public static void rawTankDrive(double right, double left) {
	    	
 		leftMotor.set(left);
 		rightMotor.set(right);
 		//DriverStation.reportError("("+controller.getLY()+","+controller.getRY()+")\n", false);
 	
 }
	 

	public static void regTankDrive(float desiredHeading, float currentHeading) {
		double error = desiredHeading-currentHeading;
		
		 //if (error > 180)
		//		error -= 360;
		// else if (error < -180)
		//		error += 360;
		 rightMotor.set(error/100);
		 leftMotor.set((error)/100);	
	 		//DriverStation.reportError("("+error/75+","+")\n", false);

	}
}
