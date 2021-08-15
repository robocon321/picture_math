package dataset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import image.ConvertImage;
import utils.Constant;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;

public class BuildDataset {
	public static void buildDataset(String input, String output) throws IOException {
		// Add attribute
		ArrayList<Attribute> attrs = new ArrayList<>();
		Attribute attr;
		for(int i=0; i < Constant.SIZE.HEIGHT_IMG * Constant.SIZE.WIDTH_IMG; i++) {
			attr = new  Attribute("attr"+i);
			attrs.add(attr);
		}
		attr = new  Attribute("border");
		attrs.add(attr);
		attr = new  Attribute("cross");
		attrs.add(attr);
		attr = new  Attribute("plus");
		attrs.add(attr);
		
		// add values attribute class
		ArrayList<String> classValues = new ArrayList<>();
		
		File data = new File(input);
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
		saver.setFile(new File(output));
		saver.setInstances(ins);
		saver.writeBatch();		
	}
}
