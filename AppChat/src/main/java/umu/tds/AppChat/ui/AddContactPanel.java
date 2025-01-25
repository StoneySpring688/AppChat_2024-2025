package umu.tds.AppChat.ui;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import umu.tds.AppChat.controllers.UIController;

public class AddContactPanel extends PanelGrande {

	private static final long serialVersionUID = 1L;
	
	private Background fondo;
	private JTextField textFieldPhone;
	private JTextField textNombreContacto;
	private JButton anyadirButton;
	
	private final Color Gray = new Color(64, 68, 75); // TODO pasar a config
	
	public AddContactPanel(UIController uiController) {
		
		this.fondo = new Background();
		this.fondo.setBounds(0, 60, 920, 660);
		this.add(fondo);
		
		textFieldPhone = new JTextField();
		textFieldPhone.setBounds(20, 130, 285, 30);
		textFieldPhone.setBackground(this.Gray);
		textFieldPhone.setCaretColor(Color.WHITE);
		textFieldPhone.setForeground(Color.WHITE);
		//textFieldPhone.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		this.fondo.add(textFieldPhone);
		textFieldPhone.setColumns(10);
		
		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setBounds(20, 112, 42, 20);
		lblPhone.setForeground(Color.WHITE);
		this.fondo.add(lblPhone);
		
		textNombreContacto = new JTextField();
		textNombreContacto.setBounds(20, 220, 285, 30);
		textNombreContacto.setBackground(this.Gray);
		textNombreContacto.setCaretColor(Color.WHITE);
		textNombreContacto.setForeground(Color.WHITE);
		//textNombreContacto.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		this.fondo.add(textNombreContacto);
		textNombreContacto.setColumns(10);
		
		JLabel lblContacto = new JLabel("Nombre Contacto");
		lblContacto.setBounds(20, 202, 120, 20);
		lblContacto.setForeground(Color.WHITE);
		this.fondo.add(lblContacto);
		
		anyadirButton = new JButton("Añadir");
		anyadirButton.setForeground(Color.WHITE);
		anyadirButton.setBackground(new Color(241, 57, 83));
		anyadirButton.setBounds(69, 310, 187, 35); // x = 370+(285/2)-(187/2)
		this.fondo.add(anyadirButton);
		
	}

}
