package image;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import utils.Constant;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;


public class ConvertImage {
	private String path;
	public ConvertImage(String path) { 
		this.path = path;
	}
	private double[] imageToArray() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat mat = Imgcodecs.imread(path);
		Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);
		Imgproc.threshold(mat, mat, 100, 1, Imgproc.THRESH_BINARY);
		
		Core.bitwise_not(mat, mat);
				
		double[] result = new double[Constant.SIZE.HEIGHT_IMG * Constant.SIZE.WIDTH_IMG + 3];
		int index = 0;
		int sumArea = 0;
		int border = 0;
		int cross = 0;
		int plus = 0;
		
		for(int m=0; m < Constant.SIZE.HEIGHT_IMG; m++) {
			for(int n=0; n < Constant.SIZE.WIDTH_IMG; n++) {
				for(int i=0; i < 45 / Constant.SIZE.WIDTH_IMG; i++) {
					for(int j=0; j < 45 / Constant.SIZE.HEIGHT_IMG; j++) {						
						sumArea += mat.get(n*(45 / Constant.SIZE.WIDTH_IMG) + j, m*(45 / Constant.SIZE.HEIGHT_IMG) + i)[0];
						if(m == 0 || n == 0 || m == Constant.SIZE.HEIGHT_IMG - 1 || n == Constant.SIZE.WIDTH_IMG - 1) {
							border += mat.get(n*(45 / Constant.SIZE.WIDTH_IMG) + j, m*(45 / Constant.SIZE.HEIGHT_IMG) + i)[0];
						}
						if(m == n || m + n == Constant.SIZE.WIDTH_IMG - 1) {
							cross += mat.get(n*(45 / Constant.SIZE.WIDTH_IMG) + j, m*(45 / Constant.SIZE.HEIGHT_IMG) + i)[0];							
						}
						if(m == Constant.SIZE.HEIGHT_IMG / 2 || n == Constant.SIZE.WIDTH_IMG / 2) {
							plus += mat.get(n*(45 / Constant.SIZE.WIDTH_IMG) + j, m*(45 / Constant.SIZE.HEIGHT_IMG) + i)[0];														
						}
					}
				}
				result[index] = sumArea*Math.pow(index, 2);
				index++;
				sumArea = 0;
			}
		}
		result[index] = border*Math.pow(index, 2);
		index++;
		result[index] = cross*Math.pow(index, 2);
		index++;
		result[index] = plus*Math.pow(index, 2);
		index++;
		
		return result;
	}
	
	public void imageToInstance(Instances ins, String label) {
		double[] arr = imageToArray();
		int size = arr.length;
		double[] result = new double[size + 1];
		for(int i=0;i<arr.length;i++) {
			result[i] = arr[i];
		}
		if(label == null) result[arr.length] = 0;
		else result[size] = ins.attribute(size).indexOfValue(label);
		ins.add(new DenseInstance(1, result));
	}	
}
