package model;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class MultiPerceptronModel implements Model{

	private Object buildModel(Instances instances) {		
		MultilayerPerceptron model = new MultilayerPerceptron();
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
			s.write("model/perceptron_model.txt", model);
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
