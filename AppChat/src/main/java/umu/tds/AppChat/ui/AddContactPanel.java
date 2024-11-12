package umu.tds.AppChat.ui;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import umu.tds.AppChat.controllers.UIController;

public class AddContactPanel extends PanelGrande {

	private static final long serialVersionUID = 1L;

	private JTextField textFieldPhone;
	private JTextField textNombreContacto;
	private JButton anyadirButton;
	
	private final Color Gray = new Color(64, 68, 75);
	
	public AddContactPanel(UIController uiController) {
		
		textFieldPhone = new JTextField();
		textFieldPhone.setBounds(20, 130, 285, 30);
		textFieldPhone.setBackground(this.Gray);
		textFieldPhone.setCaretColor(Color.WHITE);
		textFieldPhone.setForeground(Color.WHITE);
		textFieldPhone.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		this.add(textFieldPhone);
		textFieldPhone.setColumns(10);
		
		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setBounds(20, 112, 40, 20);
		lblPhone.setForeground(Color.WHITE);
		this.add(lblPhone);
		
		textNombreContacto = new JTextField();
		textNombreContacto.setBounds(20, 220, 285, 30);
		textNombreContacto.setBackground(this.Gray);
		textNombreContacto.setCaretColor(Color.WHITE);
		textNombreContacto.setForeground(Color.WHITE);
		textNombreContacto.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		this.add(textNombreContacto);
		textNombreContacto.setColumns(10);
		
		JLabel lblContacto = new JLabel("Nombre Contacto");
		lblContacto.setBounds(20, 202, 120, 20);
		lblContacto.setForeground(Color.WHITE);
		this.add(lblContacto);
		
		/*
		JTextArea textFieldMessage = new JTextArea();
		textFieldMessage.setBackground(this.Gray);
		textFieldMessage.setCaretColor(Color.WHITE);
		textFieldMessage.setForeground(Color.WHITE);
		textFieldMessage.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		textFieldMessage.setLineWrap(true);
		textFieldMessage.setWrapStyleWord(true);
		textFieldMessage.setColumns(10);
		textFieldMessage.setBounds(20, 320, 285, 70);
		JScrollPane scrollPaneMessage = new JScrollPane(textFieldMessage);
		scrollPaneMessage.setBounds(20, 302, 285, 100);
		scrollPaneMessage.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPaneMessage.setVerticalScrollBar(new ScrollBar());
		scrollPaneMessage.setHorizontalScrollBar(new ScrollBar());
		this.add(scrollPaneMessage);
		
		JLabel lblMessage = new JLabel("Message");
		lblMessage.setBounds(20, 282, 60, 20);
		lblMessage.setForeground(Color.WHITE);
		this.add(lblMessage);
		*/
		
		anyadirButton = new JButton("Search");
		anyadirButton.setForeground(Color.WHITE);
		anyadirButton.setBackground(new Color(241, 57, 83));
		anyadirButton.setBounds(69, 310, 187, 35); // x = 370+(285/2)-(187/2)
		this.add(anyadirButton);
		
	}

}
