package predict;

import java.io.File;
import java.util.ArrayList;

import image.ConvertImage;
import model.Model;
import utils.Constant;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;

public class Predict {
	public static String predict(String pathModel, String characterPath) throws Exception {		
		SerializationHelper s = new SerializationHelper();
		Classifier model = (Classifier) s.read(pathModel);
		
		// Add Attribute
		ArrayList<Attribute> attrs = new ArrayList<>();
		for(int i=0; i < Constant.SIZE.HEIGHT_IMG * Constant.SIZE.WIDTH_IMG; i++) {
			attrs.add(new Attribute("attr"+i));
		}
		attrs.add(new Attribute("border"));
		attrs.add(new Attribute("cross"));
		attrs.add(new Attribute("plus"));
		
		ArrayList<String> classValues = new ArrayList<>();
		
		File parent = new File(Constant.PATH.TRAIN_IMAGE);
		for(File f : parent.listFiles()) {
			classValues.add(f.getName());
		}
		attrs.add(new Attribute("class", classValues));

		
		Instances ins = new Instances("number_list", attrs, 0);
		ins.setClassIndex(ins.numAttributes() - 1);

		ConvertImage img = new ConvertImage(characterPath);
		img.imageToInstance(ins, null);

		// predict
        
		double label =  model.classifyInstance(ins.instance(0));
        ins.instance(0).setClassValue(label);
        return ins.instance(0).stringValue(ins.numAttributes() - 1);
	}
	
}
