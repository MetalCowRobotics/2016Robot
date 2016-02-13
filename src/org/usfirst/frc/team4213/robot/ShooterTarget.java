package org.usfirst.frc.team4213.robot;

public class ShooterTarget {
	public double angleX;
	public double angleY;
	public double distance;
	public boolean targetable;

	public ShooterTarget(double angleX, double angleY, double distance, boolean shootable) {
		this.angleX = angleX;
		this.angleY = angleY;
		this.distance = distance;
		this.targetable = shootable;
	}
}
