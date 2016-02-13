package org.usfirst.frc.team4213.robot;

import java.util.Optional;

import org.opencv.core.Mat;

public interface ImageProcessingTask{

	public void setImage(Mat img);

	abstract Optional<Target> call();
}
