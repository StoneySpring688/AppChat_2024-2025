package registro;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
//import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JTree;

public class Register extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldName;
	private JTextField textFieldLastName;
	private JTextField textFieldPhone;
	
	int posX=0;
	int posY=0;
	private JPasswordField password1;
	private JPasswordField password2;
	private JTextField textFieldSignature;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Register frame = new Register();
					//frame.setUndecorated(true);
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
	public Register() {
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 720, 420);
		setSize(720,420);
		setResizable(false);
		setLocationRelativeTo(null);
		setUndecorated(true);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//movilidad del frame
		contentPane.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				posX = e.getX();
				posY = e.getY();
			}
		});
		contentPane.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				setLocation(e.getXOnScreen() - posX, e.getYOnScreen() - posY);
			}
		});
		
		// Botón o etiqueta para cerrar la ventana
		JLabel closeButton = new JLabel("");
		closeButton.setIcon(new ImageIcon(Register.class.getResource("/resources/Exit_Door.png")));
		closeButton.setForeground(new Color(241, 57, 83));
		closeButton.setBounds(676, 0, 41, 44);
		closeButton.setBackground(Color.WHITE);
		contentPane.add(closeButton);
				
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0); // Cierra la aplicación
				}
			});
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		panel.setBounds(0, 0, 346, 420);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblDecoration1 = new JLabel("");
		lblDecoration1.setIcon(new ImageIcon(Register.class.getResource("/resources/UI_ChapterIcon_MondstadtResized.png")));
		lblDecoration1.setBounds(141, 4, 64, 64);
		panel.add(lblDecoration1);
		
		JLabel lblDecoration2 = new JLabel("");
		lblDecoration2.setIcon(new ImageIcon(Register.class.getResource("/resources/UI_ChapterIcon_LiyueResized.png")));
		lblDecoration2.setBounds(141, 72, 64, 64);
		panel.add(lblDecoration2);
		
		JLabel lblDecoration3 = new JLabel("");
		lblDecoration3.setIcon(new ImageIcon(Register.class.getResource("/resources/UI_ChapterIcon_InazumaResized.png")));
		lblDecoration3.setBounds(141, 140, 64, 64);
		panel.add(lblDecoration3);
		
		JLabel lblDecoration4 = new JLabel("");
		lblDecoration4.setIcon(new ImageIcon(Register.class.getResource("/resources/UI_ChapterIcon_SumeruResized.png")));
		lblDecoration4.setBounds(141, 208, 64, 64);
		panel.add(lblDecoration4);
		
		JLabel lblDecoration5 = new JLabel("");
		lblDecoration5.setIcon(new ImageIcon(Register.class.getResource("/resources/UI_ChapterIcon_FontaineResized.png")));
		lblDecoration5.setBounds(141, 276, 64, 64);
		panel.add(lblDecoration5);
		
		JLabel lblDecoration5_1 = new JLabel("");
		lblDecoration5_1.setIcon(new ImageIcon(Register.class.getResource("/resources/UI_ChapterIcon_NatlanResized.png")));
		lblDecoration5_1.setBounds(141, 344, 64, 64);
		panel.add(lblDecoration5_1);
		
		JLabel backButton = new JLabel("");
		backButton.setIcon(new ImageIcon(Register.class.getResource("/resources/UI_Icon_RoleCombat_Com_Back.png")));
		backButton.setBounds(0, 0, 42, 42);
		panel.add(backButton);
		backButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//System.exit(0); // TODO actualizar el estado del programa a login
				}
			});
		
		
		JButton RegisterButton = new JButton("Register");
		RegisterButton.setForeground(Color.WHITE);
		RegisterButton.setBackground(new Color(241, 57, 83));
		RegisterButton.setBounds(419, 360, 187, 35); // x = 370+(285/2)-(187/2)
		contentPane.add(RegisterButton);
		
		textFieldName = new JTextField();
		textFieldName.setBounds(370, 25, 285, 30);
		contentPane.add(textFieldName);
		textFieldName.setColumns(10);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(370, 53, 285, 2);
		contentPane.add(separator);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(370, 10, 40, 15);
		contentPane.add(lblName);
		
		textFieldLastName = new JTextField();
		textFieldLastName.setBounds(370, 80, 285, 30);
		contentPane.add(textFieldLastName);
		textFieldLastName.setColumns(10);
		
		JLabel lblLastName = new JLabel("LastName");
		lblLastName.setBounds(370, 62, 65, 20);
		contentPane.add(lblLastName);
		
		textFieldPhone = new JTextField();
		textFieldPhone.setBounds(370, 135, 285, 30);
		contentPane.add(textFieldPhone);
		textFieldPhone.setColumns(10);
		
		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setBounds(370, 117, 40, 20);
		contentPane.add(lblPhone);
		
		password1 = new JPasswordField();
		password1.setColumns(10);
		password1.setBounds(370, 190, 130, 30);
		contentPane.add(password1);
		
		JLabel lblPassword1 = new JLabel("Password");
		lblPassword1.setBounds(370, 172, 60, 20);
		contentPane.add(lblPassword1);
		
		password2 = new JPasswordField();
		password2.setColumns(10);
		password2.setBounds(525, 190, 130, 30);
		contentPane.add(password2);
		
		JLabel lblPassword2 = new JLabel("Repeat Password");
		lblPassword2.setBounds(525, 172, 120, 20);
		contentPane.add(lblPassword2);
		
		textFieldSignature = new JTextField();
		textFieldSignature.setColumns(10);
		textFieldSignature.setBounds(370, 270, 190, 70);
		contentPane.add(textFieldSignature);
		
		JLabel lblSignature = new JLabel("Signature");
		lblSignature.setBounds(370, 250, 60, 20);
		contentPane.add(lblSignature);
		
		JRadioButton rdbtnshowPassword1 = new JRadioButton("show Password");
		rdbtnshowPassword1.setBackground(Color.WHITE);
		rdbtnshowPassword1.setBounds(370, 225, 130, 25);
		rdbtnshowPassword1.addItemListener (e -> {
			if(rdbtnshowPassword1.isSelected()) {
				password1.setEchoChar((char)0);
			}else {
				password1.setEchoChar('•');
			}
		});
		contentPane.add(rdbtnshowPassword1);
		
		JRadioButton rdbtnshowPassword2 = new JRadioButton("show Password");
		rdbtnshowPassword2.setBackground(Color.WHITE);
		rdbtnshowPassword2.setBounds(525, 225, 130, 25);
		rdbtnshowPassword2.addItemListener (e -> {
			if(rdbtnshowPassword2.isSelected()) {
				password2.setEchoChar((char)0);
			}else {
				password2.setEchoChar('•');
			}
		});
		contentPane.add(rdbtnshowPassword2);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Register.class.getResource("/resources/ProfilePic.png")));
		lblNewLabel.setBounds(600, 285, 55, 55);
		contentPane.add(lblNewLabel);
	}
}
