package umu.tds.AppChat.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	int posX=0;
	int posY=0;
	private JPanel contentPane;
	private JPanel panelBotonera;
	private JPanel panelMenu1;
	private JPanel panelMenuPerfil;
	private JLabel lblLogout;
	private JLabel lblsettingGear;
	private JButton buttonGroups;
	private JButton buttonSearch;
	private JButton buttonChats;
	private JButton buttonShop;
	private JLabel closeButton;

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
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		getContentPane().setLayout(null);
		contentPane.setLayout(null);
		
		closeButton = new JLabel("");
		closeButton.setIcon(new ImageIcon(MainWindow.class.getResource("/assets/UI_Exit.png")));
		closeButton.setBounds(1245, 2, 33, 33);
		contentPane.add(closeButton);
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0); // Cierra la aplicación
				}
			});
		
		panelBotonera = new JPanel();
		panelBotonera.setBackground(Color.DARK_GRAY);
		panelBotonera.setBounds(0, 0, 120, 720);
		contentPane.add(panelBotonera);
		panelBotonera.setLayout(null);
		
		buttonChats = new JButton(new ImageIcon(MainWindow.class.getResource("/assets/UI_ChapterIcon_Wanderer.png")));
		buttonChats.setBounds(10, 0, 100, 100);
		buttonChats.setContentAreaFilled(false);
		buttonChats.setBorderPainted(false);
		buttonChats.setOpaque(false);
		panelBotonera.add(buttonChats);
		
		buttonGroups = new JButton(new ImageIcon(MainWindow.class.getResource("/assets/UI_ChapterIcon_Traveler.png")));
		buttonGroups.setOpaque(false);
		buttonGroups.setContentAreaFilled(false);
		buttonGroups.setBorderPainted(false);
		buttonGroups.setBounds(10, 120, 100, 100);
		panelBotonera.add(buttonGroups);
		
		buttonShop = new JButton(new ImageIcon(MainWindow.class.getResource("/assets/Ui_SystemOpenIcon_Shop.png")));
		buttonShop.setOpaque(false);
		buttonShop.setContentAreaFilled(false);
		buttonShop.setBorderPainted(false);
		buttonShop.setBounds(10, 240, 100, 100);
		panelBotonera.add(buttonShop);
		
		buttonSearch = new JButton(new ImageIcon(MainWindow.class.getResource("/assets/UI_ChapterIcon_Charlotte.png")));
		buttonSearch.setOpaque(false);
		buttonSearch.setContentAreaFilled(false);
		buttonSearch.setBorderPainted(false);
		buttonSearch.setBounds(10, 600, 100, 100);
		panelBotonera.add(buttonSearch);
		
		panelMenu1 = new JPanel();
		panelMenu1.setBackground(Color.GRAY);
		panelMenu1.setBounds(120, 0, 240, 660);
		contentPane.add(panelMenu1);
		
		panelMenuPerfil = new JPanel();
		panelMenuPerfil.setBackground(new Color(78,78,78));
		panelMenuPerfil.setBounds(120, 660, 240, 60);
		contentPane.add(panelMenuPerfil);
		panelMenuPerfil.setLayout(null);
		
		JLabel lblProfilePic = new JLabel("");
		lblProfilePic.setIcon(new ImageIcon(MainWindow.class.getResource("/assets/ProfilePic.png")));
		lblProfilePic.setBounds(0, 3, 55, 55);
		panelMenuPerfil.add(lblProfilePic);
		
		JLabel lblYourname = new JLabel("YourName");
		lblYourname.setBounds(60, 20, 90, 20);
		panelMenuPerfil.add(lblYourname);
		
		lblsettingGear = new JLabel("");
		lblsettingGear.setBounds(200, 15, 33, 33);
		lblsettingGear.setIcon(new ImageIcon(MainWindow.class.getResource("/assets/SettingsGear.png")));
		panelMenuPerfil.add(lblsettingGear);
		
		lblLogout = new JLabel("");
		lblLogout.setIcon(new ImageIcon(MainWindow.class.getResource("/assets/Exit_Door_Reescalada.png")));
		lblLogout.setBounds(157, 14, 33, 35);
		panelMenuPerfil.add(lblLogout);
		
	}
}