package image;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

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
		Imgproc.threshold(mat, mat, 100, 255, Imgproc.THRESH_BINARY);
		double[] result = new double[mat.rows()*mat.cols()];
		for(int i=0;i<mat.rows();i++) {
			for(int j=0;j<mat.cols();j++) {
				result[i*mat.cols() + j] = mat.get(i, j)[0];
			}
			System.out.println();
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
