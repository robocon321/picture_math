package model;

import utils.Constant;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class SVMModel implements Model{
	private Object buildModel(Instances instances) {	
		LibSVM model = new LibSVM();
		
		try {
			model.buildClassifier(instances);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	private void saveModelToPath(Object model) {
		SerializationHelper s = new SerializationHelper();
		try {
			s.write(Constant.MODEL.SVM, model);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void excute(Instances instances) {
		Object model = buildModel(instances);
		saveModelToPath(model);
	}
}
