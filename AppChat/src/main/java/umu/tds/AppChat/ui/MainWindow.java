package umu.tds.AppChat.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	int posX=0;
	int posY=0;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
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
	public MainWindow() {
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 720, 420);
		setSize(1280,720);
		setResizable(false);
		setLocationRelativeTo(null);
		setUndecorated(true);
		
		//movilidad del frame
				addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						posX = e.getX();
						posY = e.getY();
					}
				});
				addMouseMotionListener(new MouseMotionAdapter() {
					public void mouseDragged(MouseEvent e) {
						setLocation(e.getXOnScreen() - posX, e.getYOnScreen() - posY);
					}
				});
		contentPane = new JPanel();
		setContentPane(contentPane);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		panel.setBounds(0, 0, 346, 720);
		getContentPane().add(panel);
		contentPane.setLayout(null);
		
		JScrollPane panelDesp = new JScrollPane(panel);
		panelDesp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelDesp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panelDesp.setBounds(0, 0, 346, 720);
		getContentPane().add(panelDesp);
		
	}

}
