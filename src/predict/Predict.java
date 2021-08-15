package predict;

import java.io.File;
import java.util.ArrayList;

import image.ConvertImage;
import model.Model;
import utils.Constant;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;

public class Predict {
	public double predict(String pathModel, String characterPath) throws Exception {		
		SerializationHelper s = new SerializationHelper();
		MultilayerPerceptron model = (MultilayerPerceptron) s.read(pathModel);
		
		// Add Attribute
		ArrayList<Attribute> attrs = new ArrayList<>();
		for(int i=0; i < Constant.SIZE.HEIGHT_IMG * Constant.SIZE.WIDTH_IMG; i++) {
			attrs.add(new Attribute("attr"+i));
		}
		attrs.add(new Attribute("border"));
		attrs.add(new Attribute("cross"));
		attrs.add(new Attribute("plus"));
		
		ArrayList<String> classValues = new ArrayList<>();
		
		File parent = new File("data");
		for(File f : parent.listFiles()) {
			classValues.add(f.getName());
		}
		attrs.add(new Attribute("class", classValues));

		
		Instances ins = new Instances("number_list", attrs, 0);
		ins.setClassIndex(ins.numAttributes() - 1);

		ConvertImage img = new ConvertImage(characterPath);
		img.imageToInstance(ins, null);

		// predict 
		return model.classifyInstance(ins.instance(0));
	}
}
