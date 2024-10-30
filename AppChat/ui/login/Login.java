package login;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.Box;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel buttons;
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
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
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 609, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		buttons = new JPanel();
		contentPane.add(buttons, BorderLayout.SOUTH);
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		
		JButton btnNewButton = new JButton("Register");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		buttons.add(btnNewButton);
		
		Component glue = Box.createGlue();
		buttons.add(glue);
		
		JButton btnNewButton_1 = new JButton("Cancel");
		buttons.add(btnNewButton_1);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		buttons.add(horizontalStrut_1);
		
		JButton btnNewButton_2 = new JButton("Acept");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		buttons.add(btnNewButton_2);
		
		JPanel login = new JPanel();
		login.setBorder(new TitledBorder(null, "Login", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(login, BorderLayout.CENTER);
		
		textField = new JTextField();
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		
		JLabel lblPhone = new JLabel("phone");
		
		JLabel lblPassword = new JLabel("password");
		
		JRadioButton rdbtnShowPassword = new JRadioButton("show  password");
		rdbtnShowPassword.addItemListener (e -> {
			if(rdbtnShowPassword.isSelected()) {
				passwordField.setEchoChar((char)0);
			}else {
				passwordField.setEchoChar('•');
			}
		});
		
		
		GroupLayout gl_login = new GroupLayout(login);
		gl_login.setHorizontalGroup(
			gl_login.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_login.createSequentialGroup()
					.addGap(204)
					.addGroup(gl_login.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_login.createSequentialGroup()
							.addComponent(rdbtnShowPassword)
							.addContainerGap())
						.addGroup(gl_login.createSequentialGroup()
							.addGroup(gl_login.createParallelGroup(Alignment.TRAILING)
								.addComponent(passwordField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
								.addComponent(textField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
								.addComponent(lblPhone, Alignment.LEADING)
								.addComponent(lblPassword, Alignment.LEADING))
							.addGap(188))))
		);
		gl_login.setVerticalGroup(
			gl_login.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_login.createSequentialGroup()
					.addGap(37)
					.addComponent(lblPhone)
					.addGap(18)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(34)
					.addComponent(lblPassword)
					.addGap(18)
					.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(rdbtnShowPassword)
					.addContainerGap(16, Short.MAX_VALUE))
		);
		login.setLayout(gl_login);
		
		JPanel banner = new JPanel();
		contentPane.add(banner, BorderLayout.NORTH);
		
		JLabel lblBanner = new JLabel("");
		lblBanner.setHorizontalAlignment(SwingConstants.CENTER);
		lblBanner.setIcon(new ImageIcon(Login.class.getResource("/resources/Genshin Impact Logo.png")));
		GroupLayout gl_banner = new GroupLayout(banner);
		gl_banner.setHorizontalGroup(
			gl_banner.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_banner.createSequentialGroup()
					.addGap(171)
					.addComponent(lblBanner, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
					.addGap(180))
		);
		gl_banner.setVerticalGroup(
			gl_banner.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_banner.createSequentialGroup()
					.addGap(24)
					.addComponent(lblBanner, GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE))
		);
		banner.setLayout(gl_banner);
	}
}
