package umu.tds.AppChat.ui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class LoginPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textFieldPhone;
	private JPasswordField password1;
	protected JButton RegisterButton;
	private JButton LoginButton;
	
	/**
	 * Create the panel.
	 */
	public LoginPanel() {
		setBackground(Color.WHITE);
		setBorder(new EmptyBorder(5, 5, 5, 5));

		setLayout(null);
		
		// Botón o etiqueta para cerrar la ventana
		JLabel closeButton = new JLabel("");
		closeButton.setIcon(new ImageIcon(LoginPanel.class.getResource("/assets/Exit_Door.png")));
		closeButton.setForeground(new Color(241, 57, 83));
		closeButton.setBounds(676, 0, 41, 44);
		closeButton.setBackground(Color.WHITE);
		add(closeButton);
				
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0); // Cierra la aplicación
				}
			});

		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		panel.setBounds(0, 0, 346, 420);
		add(panel);
		panel.setLayout(null);
		
		JLabel lblDecoration1 = new JLabel("");
		lblDecoration1.setIcon(new ImageIcon(LoginPanel.class.getResource("/assets/UI_ChapterIcon_MondstadtResized.png")));
		lblDecoration1.setBounds(141, 4, 64, 64);
		panel.add(lblDecoration1);
		
		JLabel lblDecoration2 = new JLabel("");
		lblDecoration2.setIcon(new ImageIcon(LoginPanel.class.getResource("/assets/UI_ChapterIcon_LiyueResized.png")));
		lblDecoration2.setBounds(141, 72, 64, 64);
		panel.add(lblDecoration2);
		
		JLabel lblDecoration3 = new JLabel("");
		lblDecoration3.setIcon(new ImageIcon(LoginPanel.class.getResource("/assets/UI_ChapterIcon_InazumaResized.png")));
		lblDecoration3.setBounds(141, 140, 64, 64);
		panel.add(lblDecoration3);
		
		JLabel lblDecoration4 = new JLabel("");
		lblDecoration4.setIcon(new ImageIcon(LoginPanel.class.getResource("/assets/UI_ChapterIcon_SumeruResized.png")));
		lblDecoration4.setBounds(141, 208, 64, 64);
		panel.add(lblDecoration4);
		
		JLabel lblDecoration5 = new JLabel("");
		lblDecoration5.setIcon(new ImageIcon(LoginPanel.class.getResource("/assets/UI_ChapterIcon_FontaineResized.png")));
		lblDecoration5.setBounds(141, 276, 64, 64);
		panel.add(lblDecoration5);
		
		JLabel lblDecoration5_1 = new JLabel("");
		lblDecoration5_1.setIcon(new ImageIcon(LoginPanel.class.getResource("/assets/UI_ChapterIcon_NatlanResized.png")));
		lblDecoration5_1.setBounds(141, 344, 64, 64);
		panel.add(lblDecoration5_1);
		
		LoginButton = new JButton("Login");
		LoginButton.setForeground(Color.WHITE);
		LoginButton.setBackground(new Color(241, 57, 83));
		LoginButton.setBounds(370, 360, 120, 35); // x = 370+(285/2)-(187/2)
		add(LoginButton);
		
		RegisterButton = new JButton("Register");
		RegisterButton.setForeground(Color.WHITE);
		RegisterButton.setBackground(new Color(241, 57, 83));
		RegisterButton.setBounds(535, 360, 120, 35);
		add(RegisterButton);
		
		textFieldPhone = new JTextField();
		textFieldPhone.setBounds(370, 160, 285, 30);
		add(textFieldPhone);
		textFieldPhone.setColumns(10);
		
		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setBounds(370, 142, 40, 20);
		add(lblPhone);
		
		password1 = new JPasswordField();
		password1.setColumns(10);
		password1.setBounds(370, 235, 285, 30);
		add(password1);
		
		JLabel lblPassword1 = new JLabel("Password");
		lblPassword1.setBounds(370, 217, 60, 20);
		add(lblPassword1);
		
		JRadioButton rdbtnshowPassword1 = new JRadioButton("show Password");
		rdbtnshowPassword1.setBackground(Color.WHITE);
		rdbtnshowPassword1.setBounds(370, 270, 130, 25);
		rdbtnshowPassword1.addItemListener (e -> {
			if(rdbtnshowPassword1.isSelected()) {
				password1.setEchoChar((char)0);
			}else {
				password1.setEchoChar('•');
			}
		});
		add(rdbtnshowPassword1);
		
		JLabel lblBanner = new JLabel("");
		lblBanner.setIcon(new ImageIcon(LoginPanel.class.getResource("/assets/Login_Banner.png")));
		lblBanner.setBounds(370, 30, 285, 80);
		add(lblBanner);		
	}
	

}
