package model;

import utils.Constant;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class J48Model implements Model{

	private Object buildModel(Instances instances) {		
		J48 model = new J48();
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
			s.write(Constant.MODEL.J48, model);
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
