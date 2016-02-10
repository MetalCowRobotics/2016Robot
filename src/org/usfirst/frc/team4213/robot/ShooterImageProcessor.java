package org.usfirst.frc.team4213.robot;

import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class ShooterImageProcessor extends ImageProcessingTask<double[]> {
	public final static int thresh = 100;
	public final static int thresh_max = 255;
	public List<MatOfPoint> contours;

	@Override
	public double[] call() {

		filterImage();
		Imgproc.threshold(camImage, camImage, thresh, thresh_max,
				Imgproc.THRESH_BINARY);
		Imgproc.findContours(camImage, contours, new Mat(), Imgproc.RETR_TREE,
				2, new Point(0, 0)); // 2 is Chain_Approx_Simple

		MatOfPoint2f[] contours_poly = new MatOfPoint2f[contours.size()];
		Rect[] boundingRects = new Rect[contours.size()];
		Point[] center = new Point[contours.size()];
		MatOfPoint contour_point = new MatOfPoint();

		int biggestRectIndex;
		double biggestRectArea = 0;

		for (int i = 0; i < contours.size(); i++) {
			contours_poly[i] = new MatOfPoint2f();
			Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i)),
					contours_poly[i], 3, true);
			contour_point.fromList(contours_poly[i].toList());
			boundingRects[i] = Imgproc.boundingRect(contour_point);
			if (boundingRects[i].area() > biggestRectArea) {
				biggestRectArea = boundingRects[i].area();
				biggestRectIndex = i;
			}
		}

		final double angleX;
		final double angleY;
		final double distance;

		double[] output = { angleX, angleY, distance };
		return output;
	}

	private void filterImage() {
		Imgproc.medianBlur(camImage, camImage, 3);
		Imgproc.cvtColor(camImage, camImage, Imgproc.COLOR_BGR2HSV);
		Core.inRange(camImage, new Scalar(30, 30, 30), new Scalar(0, 0, 0),
				camImage);
		Imgproc.cvtColor(camImage, camImage, Imgproc.COLOR_BGR2GRAY);
	}

}
