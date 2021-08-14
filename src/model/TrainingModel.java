package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import image.ConvertImage;
import utils.Constant;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class TrainingModel {
	public static void main(String[] args) throws IOException {
//		saverDataTrainToArff();
	}
	
	public static void saverDataTrainToArff() throws IOException {
		// Add attribute
		ArrayList<Attribute> attrs = new ArrayList<>();
		for(int i=0; i < Constant.HEIGHT_IMG * Constant.WIDTH_IMG; i++) {
			Attribute attr = new  Attribute("attr"+i);
			attrs.add(attr);
		}
		
		// add values attribute class
		ArrayList<String> classValues = new ArrayList<>();
		
		File data = new File("data");
		for(File f : data.listFiles()) {
			classValues.add(f.getName());
		}
		attrs.add(new Attribute("class", classValues));
		
		Instances ins = new Instances("number_list", attrs, 0);

		for(File f : data.listFiles()) {
			for(File img: f.listFiles()) {
				ConvertImage cv = new ConvertImage(img.getAbsolutePath());
				cv.imageToInstance(ins, f.getName());
			}
		}
		
		
		ArffSaver saver = new ArffSaver();
		saver.setFile(new File("aaa.arff"));
		saver.setInstances(ins);
		saver.writeBatch();		
	}
}
