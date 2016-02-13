package org.usfirst.frc.team4213.robot;

import java.util.concurrent.Callable;

import org.opencv.core.Mat;

public abstract class ImageProcessingTask<T> implements Callable<T> {
	protected Mat camImage;

	public void setImage(Mat img){
		camImage = img;
	}
}
