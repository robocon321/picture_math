package utils;

public class Constant {
	public static class SIZE {
		public static int WIDTH_IMG = 5;
		public static int HEIGHT_IMG = 5;		
	}
	
	public static class MODEL {
		public static String PERCEPTRON = "model/perceptron.txt";
		public static String J48 = "model/j48.txt";
		public static String BAYES = "model/bayes.txt";
		public static String SVM = "model/svm.txt";
	}
	
	public static class EVALUATION {
		public static String PERCEPTRON = "evaluation/perceptron.txt";
		public static String J48 = "evaluation/j48.txt";
		public static String BAYES = "evaluation/bayes.txt";
		public static String SVM = "evaluation/svm.txt";
	}
	
	public static class PATH {
		public static String TRAIN_DATASET = "train_dataset.arff";
		public static String TEST_DATASET = "test_dataset.arff";
		public static String TRAIN_IMAGE = "data/train2";
		public static String TEST_IMAGE = "data/test";
		public static String BOUND = "process/bound.jpg";
		public static String RESULT = "process/result.jpg";
		public static String OUTPUT = "output";
	}
}
