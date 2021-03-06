package com.doit.opencv;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import com.doit.calibrate.CamCalibrate;
import com.doit.common.*;
import com.doit.detect.AlgorithmFactory;
import com.doit.detect.AlgorithmType;
import com.doit.detect.BaseDetect;
import com.doit.detect.OpencvDemo;

@SuppressWarnings("serial")
public class MyWindow extends BaseWindow {	
	protected JLabel imageBox;
	protected JLabel originBox;
	protected DebugWindow dbgFrame;
		
	public MyWindow(String name, int width, int height) {
		super(name, width, height);
		setResizable(false);
		
		windowLayout();
		setVisible(true);
		
		// close window operation
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				System.out.println("Exit app");
				System.exit(0);
			}
		});
		
		dbgFrame = DebugWindow.shareInstance();
		if (!Config.DEBUG_ENABLE) {
			dbgFrame.setVisible(false);
		}
		
		DebugWindow.shareInstance().println("OpenCV version: " + Core.VERSION);
	}
	
	private void windowLayout() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setToolTipText("File");
		JMenu aboutMenu = new JMenu("About");
		aboutMenu.setToolTipText("About");
		JMenu viewMenu = new JMenu("View");
		viewMenu.setToolTipText("View");
		
		JMenuItem openItem = new JMenuItem("Open");
		fileMenu.add(openItem);
		fileMenu.addSeparator();
		JMenuItem quitItem = new JMenuItem("Quit");
		fileMenu.add(quitItem);
		
		JMenuItem verItem = new JMenuItem("Version");
		aboutMenu.add(verItem);
		JMenuItem doitItem = new JMenuItem("DOIT");
		aboutMenu.add(doitItem);
		
		JMenuItem dbgItem = new JMenuItem("Debug window");
		viewMenu.add(dbgItem);
		
		menuBar.add(fileMenu);
		menuBar.add(viewMenu);
		menuBar.add(aboutMenu);
		setJMenuBar(menuBar);
		
		openItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		quitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		
		verItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Utils.messageBox(null, Constants.VERSION, "Version");
			}
		});
		
		doitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Utils.messageBox(null, Constants.INFO, "DOIT");
			}
		});
		
		dbgItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (!dbgFrame.isVisible()) {
					dbgFrame.println("debug window is visible now");
					dbgFrame.setVisible(true);
				}
			}
		});
		
		mainPane = new JPanel(new CardLayout());
		mainPane.setBounds(0, 0, Constants.DEF_FRAME_WIDTH, Constants.DEF_FRAME_HEIGHT-Constants.DEF_STATUS_BAR_HEIGHT*4);
		getContentPane().add(mainPane, BorderLayout.CENTER);
		
		//GridLayout grid=new GridLayout(1,2);
		JPanel picPane = new JPanel();
		picPane.setLayout(null);
		picPane.setBackground(Color.GRAY);
		//picPane.setSize(640, 320);
		mainPane.add(picPane, "picPane");
		
		int boxWidth = (Constants.DEF_FRAME_WIDTH / 2 - Constants.IMAGE_BOX_PAD * 2);
		int boxHeight = boxWidth;
		
		imageBox = new JLabel("Test");
		//add(imageBox, BorderLayout.CENTER);
		imageBox.setBounds(Constants.DEF_FRAME_WIDTH/2-boxWidth-Constants.IMAGE_BOX_PAD, 50, boxWidth, boxHeight);
		imageBox.setHorizontalAlignment(SwingConstants.CENTER);
		imageBox.setBackground(Color.GRAY);
		picPane.add(imageBox);
		
		originBox = new JLabel("origin");
		originBox.setBounds(Constants.DEF_FRAME_WIDTH/2+Constants.IMAGE_BOX_PAD, 50, boxWidth, boxHeight);
		originBox.setHorizontalAlignment(SwingConstants.CENTER);
		originBox.setBackground(Color.GRAY);
		picPane.add(originBox);
		
		// status bar
		/*JPanel statusPane = new JPanel();
		JToolBar toolBar = new JToolBar();
		toolBar.add(new JLabel("state"));
		//toolBar.setFloatable(false);
		toolBar.setSize(Constants.DEF_FRAME_WIDTH, Constants.DEF_STATUS_BAR_HEIGHT);
		statusPane.add(toolBar, BorderLayout.NORTH);
		getContentPane().add(statusPane, BorderLayout.SOUTH);*/
		
		JPanel bottomPane = new JPanel(new GridLayout(0, 5));
		//bottomPane.setLayout(new BorderLayout());
		JButton detectBtn = new JButton("Detect");
		bottomPane.add(detectBtn);
		JButton grayBtn = new JButton("Gray");
		bottomPane.add(grayBtn);
		JButton cannyBtn = new JButton("Canny");
		bottomPane.add(cannyBtn);
		JButton houghBtn = new JButton("Hough");
		bottomPane.add(houghBtn);
		JButton stitchBtn = new JButton("Stitch");
		bottomPane.add(stitchBtn);
		JButton binaryBtn = new JButton("Binary");
		bottomPane.add(binaryBtn);
		JButton binary2Btn = new JButton("Binary2");
		bottomPane.add(binary2Btn);
		JButton centroidBtn = new JButton("Centroid");
		bottomPane.add(centroidBtn);
		JButton findCircleBtn = new JButton("Find Circle");
		bottomPane.add(findCircleBtn);
		JButton calibrateBtn = new JButton("Calibrate");
		bottomPane.add(calibrateBtn);
		JButton erodeBtn = new JButton("Erode");
		bottomPane.add(erodeBtn);
		JButton delateBtn = new JButton("Delate");
		bottomPane.add(delateBtn);
		JButton edgeBtn = new JButton("Edge");
		bottomPane.add(edgeBtn);
		JButton morphologyBtn = new JButton("Morphology");
		bottomPane.add(morphologyBtn);
		JButton burrBtn = new JButton("Burr");
		bottomPane.add(burrBtn);
		JButton thinningBtn = new JButton("Thinning");
		bottomPane.add(thinningBtn);
		JButton matchBtn = new JButton("Match");
		bottomPane.add(matchBtn);
		getContentPane().add(bottomPane, BorderLayout.SOUTH);
		
		detectBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				BaseDetect method = AlgorithmFactory.create(AlgorithmType.MY_DETECT);
				Image img = method.detect(Constants.testFile);
				imageBox.setIcon(new ImageIcon(img));
				dbgFrame.println("Detect start");
			}
		});
		
		grayBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Image origin = OpencvDemo.shareInstance().getOriginImg(Constants.testFile);
				originBox.setIcon(Utils.setImageSize(origin, originBox.getSize().width, originBox.getSize().height));
				Image gray = OpencvDemo.shareInstance().getGrayImg(Constants.testFile);
				imageBox.setIcon(Utils.setImageSize(gray, imageBox.getSize().width, imageBox.getSize().height));
				dbgFrame.println(Utils.usedMemory());
			}
		});
		
		cannyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Image origin = OpencvDemo.shareInstance().getOriginImg(Constants.testFile);
				originBox.setIcon(Utils.setImageSize(origin, originBox.getSize().width, originBox.getSize().height));
				Image canny = OpencvDemo.shareInstance().getCannyImg(Constants.testFile);
				imageBox.setIcon(Utils.setImageSize(canny, imageBox.getSize().width, imageBox.getSize().height));
				dbgFrame.println(Utils.usedMemory());
			}
		});
		
		houghBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Image origin = OpencvDemo.shareInstance().getOriginImg(Constants.testFile2);
				originBox.setIcon(Utils.setImageSize(origin, originBox.getSize().width, originBox.getSize().height));
				Image hough = OpencvDemo.shareInstance().getHoughImg(Constants.testFile2);
				imageBox.setIcon(Utils.setImageSize(hough, imageBox.getSize().width, imageBox.getSize().height));
				dbgFrame.println(Utils.usedMemory());
			}
		});
		
		stitchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Vector<String> fileList = new Vector<String>();
				for (int i = 0; i < Constants.STITCH_IMG_NUM; i++) {
					fileList.add("D:\\w\\workspace\\JavaOpenCV\\img\\p" + (i + 1) + ".jpg");
				}
				Mat stitched = OpencvDemo.shareInstance().getStichImage(fileList);
				Highgui.imwrite("D:\\w\\workspace\\JavaOpenCV\\img\\stitched.jpg", stitched);
				Utils.messageBox(null, "Stitched image generated", "OpenCV");
			}
		});
		
		binaryBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Image origin = OpencvDemo.shareInstance().getOriginImg(Constants.testFile2);
				originBox.setIcon(Utils.setImageSize(origin, originBox.getSize().width, originBox.getSize().height));
				Image binary = OpencvDemo.shareInstance().getBinaryImg(Constants.testFile2);
				imageBox.setIcon(Utils.setImageSize(binary, imageBox.getSize().width, imageBox.getSize().height));
				dbgFrame.println(Utils.usedMemory());
			}
		});
		
		binary2Btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Image origin = OpencvDemo.shareInstance().getOriginImg(Constants.testFile);
				originBox.setIcon(Utils.setImageSize(origin, originBox.getSize().width, originBox.getSize().height));
				Image binary = OpencvDemo.shareInstance().getBinaryImg2(Constants.testFile);
				imageBox.setIcon(Utils.setImageSize(binary, imageBox.getSize().width, imageBox.getSize().height));
				dbgFrame.println(Utils.usedMemory());
			}
		});
		
		centroidBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Image origin = OpencvDemo.shareInstance().getOriginImg(Constants.testFile2);
				originBox.setIcon(Utils.setImageSize(origin, originBox.getSize().width, originBox.getSize().height));
				Mat img = Highgui.imread(Constants.testFile2);
				// set ROI to crop image
				Rect roi = new Rect(70, 70, 100, 100);
				Mat cropImg = img.submat(roi);
				Image res = OpencvDemo.shareInstance().findCentroid(cropImg);
				imageBox.setIcon(Utils.setImageSize(res, imageBox.getSize().width, imageBox.getSize().height));
				dbgFrame.println(Utils.usedMemory());
			}
		});
		
		findCircleBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Image origin = OpencvDemo.shareInstance().getOriginImg(Constants.testFile2);
				originBox.setIcon(Utils.setImageSize(origin, originBox.getSize().width, originBox.getSize().height));
				Mat img = Highgui.imread(Constants.testFile2);
				// set ROI to crop image
				Rect roi = new Rect(70, 70, 100, 100);
				Mat cropImg = img.submat(roi);
				Mat res = new Mat();
				Imgproc.cvtColor(cropImg, res, Imgproc.COLOR_BGR2GRAY);
				Mat temp = new Mat();
				Imgproc.pyrDown(res, temp);
				Imgproc.pyrUp(temp, res);
				Imgproc.Canny(res, res, 45, 170);
				imageBox.setIcon(Utils.setImageSize(Utils.toBufferedImage(res), imageBox.getSize().width, imageBox.getSize().height));
				dbgFrame.println(Utils.usedMemory());
			}
		});
		
		calibrateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Vector<String> filelist = new Vector<String>();
				for (int i = 0; i < Constants.CALIB_IMG_NUM; i++) {
					filelist.add("D:\\w\\workspace\\JavaOpenCV\\img\\distort" + (i + 1) + ".jpg");
				}
				Mat img = Highgui.imread("D:\\w\\workspace\\JavaOpenCV\\img\\distort1.jpg", 0);
				Size boardSize = new Size(9, 6);
				CamCalibrate.shareInstance().addChessboardPoints(filelist, boardSize);
				CamCalibrate.shareInstance().calibrate(img.size());
				
				// remap
				Mat uImg = CamCalibrate.shareInstance().remap(img);
				Highgui.imwrite("D:\\w\\workspace\\JavaOpenCV\\img\\undistort.jpg", uImg);
				Utils.messageBox(null, "Undistorted image generated", "OpenCV");
			}
		});
		
		erodeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Image origin = OpencvDemo.shareInstance().getOriginImg(Constants.testFile2);
				originBox.setIcon(Utils.setImageSize(origin, originBox.getSize().width, originBox.getSize().height));
				Image erodeImg = OpencvDemo.shareInstance().getErodeImage(Constants.testFile2);
				imageBox.setIcon(Utils.setImageSize(erodeImg, imageBox.getSize().width, imageBox.getSize().height));
			}
		});
		
		delateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Image origin = OpencvDemo.shareInstance().getOriginImg(Constants.testFile2);
				originBox.setIcon(Utils.setImageSize(origin, originBox.getSize().width, originBox.getSize().height));
				Image dilateImg = OpencvDemo.shareInstance().getDilateImage(Constants.testFile2);
				imageBox.setIcon(Utils.setImageSize(dilateImg, imageBox.getSize().width, imageBox.getSize().height));
			}
		});
		
		edgeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Image origin = OpencvDemo.shareInstance().getOriginImg(Constants.testFile);
				originBox.setIcon(Utils.setImageSize(origin, originBox.getSize().width, originBox.getSize().height));
				Image dilateImg = OpencvDemo.shareInstance().getEdgeImage(Constants.testFile);
				imageBox.setIcon(Utils.setImageSize(dilateImg, imageBox.getSize().width, imageBox.getSize().height));
			}
		});
		
		morphologyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Image origin = OpencvDemo.shareInstance().getOriginImg(Constants.testFile);
				originBox.setIcon(Utils.setImageSize(origin, originBox.getSize().width, originBox.getSize().height));
				Image morphImg = OpencvDemo.shareInstance().getMorphImage(Constants.testFile);
				imageBox.setIcon(Utils.setImageSize(morphImg, imageBox.getSize().width, imageBox.getSize().height));
			}
		});
		
		burrBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Mat res = OpencvDemo.shareInstance().burrDetect(Constants.burrTest);
				Highgui.imwrite("D:\\w\\workspace\\JavaOpenCV\\img\\burrFilter.png", res);
				Utils.messageBox(null, "Complete", "OpenCV");
			}
		});
		
		thinningBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Mat src = Highgui.imread("D:\\w\\workspace\\JavaOpenCV\\img\\burr2.png");
				Mat res = OpencvDemo.shareInstance().thinning(src, 15, false);
				Highgui.imwrite("D:\\w\\workspace\\JavaOpenCV\\img\\thinning.png", res);
				Utils.messageBox(null, "Complete", "OpenCV");
			}
		});
		
		matchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Mat scene = Highgui.imread(Constants.matchImg2);
				Mat object = Highgui.imread(Constants.matchImg1);
				OpencvDemo.shareInstance().match(scene, object);
				Utils.messageBox(null, "Complete", "OpenCV");
			}
		});
	}
}
