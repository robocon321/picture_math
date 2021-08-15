import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import dataset.BuildDataset;
import image.ProcessingImage;
import model.Model;
import model.MultiPerceptronModel;
import predict.Predict;
import utils.Constant;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Flow;

public class GUI extends JFrame {

	private final int WIDTH = 900;
	private final int HEIGHT = 800;
	private JButton btnChooser;
	private JLabel lblImageSource, lblImageProcess, lblImageBoundNumber;
	private JPanel pnCrop;
	private JTextField txtAccuracy, txtCal, txtResult;
	private JComboBox<String> cbb;
	private TextArea txtConsole;

	Font font = new Font("Segoe UI", Font.PLAIN, 18);
	private String path = "image";

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, WIDTH + 30, HEIGHT);
		getContentPane().setLayout(new BorderLayout());
		initGUI();
		setEvent();
	}

	public void initGUI() {
		JLabel lblTitle = new JLabel("Thực hiện bài toán thông qua hình ảnh");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
		getContentPane().add(lblTitle, BorderLayout.NORTH);

		txtConsole = new TextArea("Hello everybody. My name is Console. I hope that I can support you \n");
		txtConsole.setEditable(false);
		txtConsole.setBackground(Color.WHITE);
		JScrollPane scrollConsole = new JScrollPane(txtConsole, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		getContentPane().add(scrollConsole, BorderLayout.SOUTH);

		JPanel pnMain = new JPanel();
		pnMain.setLayout(new GridLayout(1, 2));
		getContentPane().add(pnMain, BorderLayout.CENTER);

		JPanel pnLeft = new JPanel();
		pnLeft.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnMain.add(pnLeft);

		JPanel pnSource = new JPanel();
		pnSource.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnSource.setPreferredSize(new Dimension(WIDTH, 35));
		pnLeft.add(pnSource);

		JLabel lblTitleChooser = new JLabel("File: ");
		lblTitleChooser.setFont(font);
		pnSource.add(lblTitleChooser);

		btnChooser = new JButton("Choose");

		pnSource.add(btnChooser);

		JLabel lblTitleSource = new JLabel("Ảnh gốc:");
		lblTitleSource.setFont(font);
		lblTitleSource.setPreferredSize(new Dimension(WIDTH, 30));
		pnLeft.add(lblTitleSource);

		lblImageSource = new JLabel();
		lblImageSource.setSize(new Dimension(250, 180));
		lblImageSource.setBorder(BorderFactory.createLineBorder(Color.black));
		pnLeft.add(lblTitleSource);

		lblImageSource.setPreferredSize(new Dimension(lblImageSource.getWidth(), lblImageSource.getHeight()));
		pnLeft.add(lblImageSource);

		JLabel lblTitleProcess = new JLabel("Ảnh đã qua xử lí trung gian:");
		lblTitleProcess.setFont(font);
		lblTitleProcess.setPreferredSize(new Dimension(WIDTH, 30));
		pnLeft.add(lblTitleProcess);

		lblImageProcess = new JLabel();
		lblImageProcess.setSize(new Dimension(250, 180));
		lblImageProcess.setPreferredSize(new Dimension(lblImageProcess.getWidth(), lblImageProcess.getHeight()));
		lblImageProcess.setBorder(BorderFactory.createLineBorder(Color.black));
		pnLeft.add(lblImageProcess);

		JPanel pnRight = new JPanel();
		pnRight.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnMain.add(pnRight);

		JPanel pnEmpty = new JPanel();
		pnEmpty.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnEmpty.setPreferredSize(new Dimension(WIDTH, 35));
		pnRight.add(pnEmpty);

		JLabel lblTitleBoundNumber = new JLabel("Xác định các kí tự:");
		lblTitleBoundNumber.setFont(font);
		lblTitleBoundNumber.setPreferredSize(new Dimension(WIDTH, 30));
		pnRight.add(lblTitleBoundNumber);

		lblImageBoundNumber = new JLabel();
		lblImageBoundNumber.setSize(new Dimension(250, 180));
		lblImageBoundNumber
				.setPreferredSize(new Dimension(lblImageBoundNumber.getWidth(), lblImageBoundNumber.getHeight()));
		lblImageBoundNumber.setBorder(BorderFactory.createLineBorder(Color.black));
		pnRight.add(lblImageBoundNumber);

		JLabel lblStatistic = new JLabel("Dự đoán các kết quả:");
		lblStatistic.setFont(font);
		lblStatistic.setPreferredSize(new Dimension(WIDTH, 50));
		pnRight.add(lblStatistic);

		JPanel pnGrid = new JPanel();
		pnGrid.setLayout(new GridLayout(4, 2, 0, 10));
		pnGrid.setPreferredSize(new Dimension(WIDTH / 2 - 10, 120));
		pnRight.add(pnGrid);

		Font fontAttribute = new Font("Segoe UI", Font.BOLD, 18);

		JLabel lblAlgo = new JLabel("Thuật toán: ");
		lblAlgo.setFont(fontAttribute);
		pnGrid.add(lblAlgo);

		String[] algos = new String[] { "Multi-layer Perceptron", "SimpleLogistic", "SVM", "k-Nearest Neighbor" };
		cbb = new JComboBox<>(algos);
		cbb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		pnGrid.add(cbb);

		JLabel lblAccuracy = new JLabel("Accuracy: ");
		lblAccuracy.setFont(fontAttribute);
		pnGrid.add(lblAccuracy);

		txtAccuracy = new JTextField();
		txtAccuracy.setFont(font);
		txtAccuracy.setEditable(false);
		pnGrid.add(txtAccuracy);

		JLabel lblCal = new JLabel("Bài toán: ");
		lblCal.setFont(fontAttribute);
		pnGrid.add(lblCal);
		txtCal = new JTextField(10);
		txtCal.setFont(font);
		txtCal.setEditable(false);
		pnGrid.add(txtCal);

		JLabel lblResult = new JLabel("Kết quả: ");
		lblResult.setFont(fontAttribute);
		pnGrid.add(lblResult);

		txtResult = new JTextField(10);
		txtResult.setFont(font);
		txtResult.setEditable(false);
		pnGrid.add(txtResult);
	}

	public void setEvent() {
		btnChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(path);
				int status = chooser.showDialog(getParent(), "Choose file");
				if (status == JFileChooser.APPROVE_OPTION) {
					txtConsole.setText(txtConsole.getText()+"Processing image ... \n");
					long start = System.currentTimeMillis();
					File file = chooser.getSelectedFile();
					path = file.getPath();
					btnChooser.setText(file.getName());
					loadImageSource();
					loadImageProcess();
					loadImageBoundNumber();
					long end = System.currentTimeMillis();
					txtConsole.setText(txtConsole.getText()+(end - start) / 1000 + "s\n");
					predictResult();
				}
			}
		});

		cbb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				predictResult();
			}
		});
	}

	public void loadImageSource() {
		BufferedImage imgSource = null;
		try {
			imgSource = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image dimgSource = imgSource.getScaledInstance(lblImageSource.getWidth(), lblImageSource.getHeight(),
				Image.SCALE_SMOOTH);
		ImageIcon imageIconSource = new ImageIcon(dimgSource);
		lblImageSource.setIcon(imageIconSource);
	}

	public void loadImageProcess() {
		ProcessingImage.getInstance().loadImage(path).
//				buildRangeImage().
//				buildMorph(new Size(3,3), Imgproc.MORPH_RECT, Imgproc.MORPH_DILATE).
//				buildMorph(new Size(7,7), Imgproc.MORPH_ELLIPSE, Imgproc.MORPH_CLOSE).
//				buildMorph(new Size(7,7), Imgproc.MORPH_ELLIPSE, Imgproc.MORPH_CLOSE).
//				buildMorph(new Size(3,3), Imgproc.MORPH_ELLIPSE, Imgproc.MORPH_ERODE).
//				buildBlur(7).
				buildRangeImage().save();

		BufferedImage imgProcess = null;
		try {
			imgProcess = ImageIO.read(new File("process/result.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image dimgProcess = imgProcess.getScaledInstance(lblImageProcess.getWidth(), lblImageProcess.getHeight(),
				Image.SCALE_SMOOTH);
		ImageIcon imageIconProcess = new ImageIcon(dimgProcess);
		lblImageProcess.setIcon(imageIconProcess);
	}

	public void loadImageBoundNumber() {
		ProcessingImage.getInstance().saveBound();
		BufferedImage imgBound = null;
		try {
			imgBound = ImageIO.read(new File(Constant.PATH.BOUND));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image dimgBound = imgBound.getScaledInstance(lblImageBoundNumber.getWidth(), lblImageBoundNumber.getHeight(),
				Image.SCALE_SMOOTH);
		ImageIcon imageIconBound = new ImageIcon(dimgBound);
		lblImageBoundNumber.setIcon(imageIconBound);
	}
	
	public void predictResult() {
		txtAccuracy.setText("");
		txtCal.setText("");
		long start = 0;
		long end = 0;
		// Train dataset
		File trainDataset = new File(Constant.PATH.TRAIN_DATASET);
		if (!trainDataset.exists()) {
			txtConsole.setText(txtConsole.getText()
					+ "You don't have train dataset, the system will load train dataset \nLoading... \n");

			start = System.currentTimeMillis();
			try {
				BuildDataset.buildDataset(Constant.PATH.TRAIN_IMAGE, Constant.PATH.TRAIN_DATASET);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			end = System.currentTimeMillis();

			txtConsole.setText(txtConsole.getText() + "Build train dataset: " + (end - start) / 1000 + "s \n");
		}

		// Test dataset
		File testDataset = new File(Constant.PATH.TEST_DATASET);
		if (!testDataset.exists()) {
			txtConsole.setText(txtConsole.getText()
					+ "You don't have test dataset, the system will load test dataset \nLoading... \n");

			start = System.currentTimeMillis();
			try {
				BuildDataset.buildDataset(Constant.PATH.TEST_IMAGE, Constant.PATH.TEST_DATASET);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			end = System.currentTimeMillis();

			txtConsole.setText(txtConsole.getText() + "Build test dataset: " + (end - start) / 1000 + "s \n");
		}

		// get dataset training
		ArffLoader trainLoader = new ArffLoader();
		try {
			trainLoader.setFile(new File(Constant.PATH.TRAIN_DATASET));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Instances trainIns = null;
		try {
			trainIns = trainLoader.getDataSet();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		trainIns.setClassIndex(trainIns.numAttributes() - 1);

		// get dataset testing
		ArffLoader testLoader = new ArffLoader();
		try {
			testLoader.setFile(new File(Constant.PATH.TEST_DATASET));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Instances testIns = null;
		try {
			testIns = testLoader.getDataSet();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		testIns.setClassIndex(testIns.numAttributes() - 1);

		// setup model and evaluation
		SerializationHelper helper = new SerializationHelper();

		String modelPath = Constant.MODEL.PERCEPTRON;
		String evalPath = Constant.EVALUATION.PERCEPTRON;
		Model model = new MultiPerceptronModel();

		switch (cbb.getSelectedIndex()) {
		case 0: {
			modelPath = Constant.MODEL.PERCEPTRON;
			evalPath = Constant.EVALUATION.PERCEPTRON;
			model = new MultiPerceptronModel();
			break;
		}
		case 1: {
			txtConsole.setText(txtConsole.getText() + "Nothing. \n");
			break;
		}
		case 2: {
			txtConsole.setText(txtConsole.getText() + "Nothing. \n");
			break;
		}
		case 3: {
			txtConsole.setText(txtConsole.getText() + "Nothing. \n");
			break;
		}
		default:
			txtConsole.setText(txtConsole.getText() + "Nothing. \n");
		}
		
		// Build model
		File fileModel = new File(modelPath);
		if (!fileModel.exists()) {
			txtConsole
					.setText(txtConsole.getText() + "You don't have perceptron model file. \nBuiding...\n");
			start = System.currentTimeMillis();
			model.excute(trainIns);
			end = System.currentTimeMillis();
			txtConsole.setText(txtConsole.getText() + "Build model time: " + (end - start) / 1000 + "s\n");
		}


		// Evaluation
		Evaluation evaluation = null;
		
		File fileEvaluation = new File(evalPath);
		if (!fileEvaluation.exists()) {
			txtConsole.setText(
					txtConsole.getText() + "You don't have perceptron evaluation file. \nBuiding...\n");
			start = System.currentTimeMillis();
			try {
				Evaluation evalSave = new Evaluation(trainIns);
				evalSave.evaluateModel((Classifier) helper.read(Constant.MODEL.PERCEPTRON), testIns);
				helper.write(Constant.EVALUATION.PERCEPTRON, evalSave);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			end = System.currentTimeMillis();
			txtConsole.setText(
					txtConsole.getText() + "Build avaluation time: " + (end - start) / 1000 + "s\n");
		}

		try {
			evaluation = (Evaluation) helper.read(Constant.EVALUATION.PERCEPTRON);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (evaluation != null)
			txtAccuracy.setText(evaluation.pctCorrect() + "%");
		
		// Predict
		txtConsole.setText(txtConsole.getText() + "Predicting ... \n");
		String calc = "";
		start = System.currentTimeMillis();
		File output = new File(Constant.PATH.OUTPUT);
		for(File f : output.listFiles()) {
			try {
				calc += Predict.predict(modelPath, f.getAbsolutePath()) + " ";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		end = System.currentTimeMillis();
		txtConsole.setText(txtConsole.getText() + "Predict time: " + (end - start) / 1000 + "s \n");
		txtCal.setText(calc);
	}
}
