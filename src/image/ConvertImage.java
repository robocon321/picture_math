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
		Imgproc.threshold(mat, mat, 200, 1, Imgproc.THRESH_BINARY);
		
		double[] result = new double[Constant.SIZE.HEIGHT_IMG * Constant.SIZE.WIDTH_IMG + 3];
		int index = 0;
		int sumArea = 0;
		int border = 0;
		int cross = 0;
		int plus = 0;

		
		for(int m=0; m < Constant.SIZE.HEIGHT_IMG; m++) {
			for(int n=0; n < Constant.SIZE.WIDTH_IMG; n++) {
				for(int i=0; i < 45 / Constant.SIZE.HEIGHT_IMG; i++) {
					for(int j=0; j < 45 / Constant.SIZE.WIDTH_IMG; j++) {
						double k = mat.get(n*(45 / Constant.SIZE.WIDTH_IMG) + j, m*(45 / Constant.SIZE.HEIGHT_IMG) + i)[0] == 1 ? 0 : 1;
						sumArea += k;
						if(m == 0 || n == 0 || m == Constant.SIZE.HEIGHT_IMG - 1 || n == Constant.SIZE.WIDTH_IMG - 1) {
							border += k;
						}
						if(m == n || m + n == Constant.SIZE.WIDTH_IMG - 1) {
							cross += k;							
						}
						if(m == Constant.SIZE.HEIGHT_IMG / 2 || n == Constant.SIZE.WIDTH_IMG / 2) {
							plus += k;														
						}
					}
				}
				result[index] = sumArea;
				index++;
				sumArea = 0;
			}
		}
		result[index] = border;
		index++;
		result[index] = cross;
		index++;
		result[index] = plus;
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
