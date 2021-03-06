package model;

import utils.Constant;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class NaiveBayesModel implements Model{
	private Object buildModel(Instances instances) {		
		NaiveBayes model = new NaiveBayes();
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
			s.write(Constant.MODEL.BAYES, model);
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
