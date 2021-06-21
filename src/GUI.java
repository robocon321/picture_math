import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Flow;

public class GUI extends JFrame {

	private JPanel contentPane;
	private final int WIDTH = 733;
	private final int HEIGHT = 600;
	private final int ROW = 30;
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
		setBounds(100, 100, WIDTH, HEIGHT);
		getContentPane().setLayout(new BorderLayout());

		JLabel lblTitle = new JLabel("Thực hiện bài toán thông qua hình ảnh");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
		getContentPane().add(lblTitle, BorderLayout.NORTH);

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
		lblTitleChooser.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		pnSource.add(lblTitleChooser);

		JButton btnChooser = new JButton("Choose");
		btnChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				int status = chooser.showDialog(getParent(), "Choose file");
				if(status == JFileChooser.APPROVE_OPTION){
					File file = chooser.getSelectedFile();
					System.out.println(file.getPath());
				}
			}
		});

		pnSource.add(btnChooser);

		JLabel lblTitleSource = new JLabel("Ảnh gốc:");
		lblTitleSource.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		lblTitleSource.setPreferredSize(new Dimension(WIDTH, 30));
		pnLeft.add(lblTitleSource);

		JLabel lblImageSource = new JLabel();
		lblImageSource.setSize(new Dimension(250,180));
		lblImageSource.setBorder(new CompoundBorder(lblImageSource.getBorder(), new EmptyBorder(10,50,10,10)));
		pnLeft.add(lblTitleSource);

		// fit image

		BufferedImage imgSource = null;
		try {
			imgSource = ImageIO.read(new File("image/toan_1.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image dimgSource = imgSource.getScaledInstance(lblImageSource.getWidth(), lblImageSource.getHeight(),
				Image.SCALE_SMOOTH);
		ImageIcon imageIconSource = new ImageIcon(dimgSource);
		lblImageSource.setIcon(imageIconSource);
		pnLeft.add(lblImageSource);

		JLabel lblTitleProcess = new JLabel("Ảnh đã qua xử lí trung gian:");
		lblTitleProcess.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		lblTitleProcess.setPreferredSize(new Dimension(WIDTH, 30));
		pnLeft.add(lblTitleProcess);

		JLabel lblImageProcess = new JLabel();
		lblImageProcess.setSize(new Dimension(250,180));
		lblImageProcess.setBorder(new CompoundBorder(lblImageProcess.getBorder(), new EmptyBorder(10,50,10,10)));

		// fit image

		BufferedImage imgProcess = null;
		try {
			imgProcess = ImageIO.read(new File("image/toan_2.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image dimgProcess = imgProcess.getScaledInstance(lblImageProcess.getWidth(), lblImageProcess.getHeight(),
				Image.SCALE_SMOOTH);
		ImageIcon imageIconProcess = new ImageIcon(dimgProcess);
		lblImageProcess.setIcon(imageIconProcess);
		pnLeft.add(lblImageProcess);

		JPanel pnRight = new JPanel();
		pnRight.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnMain.add(pnRight);
	}

}
