package image;


import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import utils.Constant;
import weka.core.DenseInstance;
import weka.core.Instances;


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
				
		double[] result = new double[Constant.HEIGHT_IMG * Constant.WIDTH_IMG];
		int index = 0;
		int sumArea = 0;
		
		for(int m=0; m < Constant.HEIGHT_IMG; m++) {
			for(int n=0; n < Constant.WIDTH_IMG; n++) {
				for(int i=0; i < 45 / Constant.WIDTH_IMG; i++) {
					for(int j=0; j < 45 / Constant.HEIGHT_IMG; j++) {
						sumArea += mat.get(n*(45 / Constant.WIDTH_IMG) + j, m*(45 / Constant.HEIGHT_IMG) + i)[0];
					}
				}
				result[index] = sumArea*Math.pow(index, 2);
				index++;
				sumArea = 0;
			}
		}
		
		return result;
	}
	
	public void imageToInstance(Instances ins, String label) {
		double[] arr = imageToArray();
		if(label == null) ins.add(new DenseInstance(1, arr));
		else {
			int size = arr.length;
			double[] result = new double[size + 1];
			for(int i=0;i<arr.length;i++) {
				result[i] = arr[i];
			}
			result[size] = ins.attribute(size).indexOfValue(label);
			ins.add(new DenseInstance(1, result));
		}
	}
	
//	public static void main(String[] args) throws IOException {
//		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//		ConvertImage img = new ConvertImage("output/haha.jpg");
//		Instances ins = img.imageToInstance();
//		ArffSaver saver = new ArffSaver();
//		saver.setFile(new File("data.arff"));
//		saver.setInstances(ins);
//		saver.writeBatch();
//	}
}
