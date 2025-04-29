package umu.tds.AppChat.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

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
	
	public AddContactPanel() {
		
		this.fondo = new Background();
		this.fondo.setBounds(0, 60, 920, 660);
		this.add(fondo);
		
		textFieldPhone = new JTextField();
		textFieldPhone.setBounds(20, 130, 285, 30);
		textFieldPhone.setBackground(this.Gray);
		textFieldPhone.setCaretColor(Color.WHITE);
		textFieldPhone.setForeground(Color.WHITE);
		textFieldPhone.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textFieldPhone.getForeground().equals(Color.RED)) {
                    textFieldPhone.setText(""); // Borrar el mensaje de error
                    textFieldPhone.setForeground(Color.WHITE); // Restaurar color normal
                }
            }
        });
		
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
		textNombreContacto.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textNombreContacto.getForeground().equals(Color.RED)) {
                	textNombreContacto.setText(""); // Borrar el mensaje de error
                	textNombreContacto.setForeground(Color.WHITE); // Restaurar color normal
                }
            }
        });
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
		this.anyadirButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//numero inexistente o ya en contactos
				if(addContact()) reset();	
			}
		});
		
		this.fondo.add(anyadirButton);
		
	}
	
	public String getNumero() {
		return textFieldPhone.getForeground().equals(Color.RED) ? "" : this.textFieldPhone.getText();
	}
	
	public String getNombre() {
		return textNombreContacto.getForeground().equals(Color.RED) ? "" : this.textNombreContacto.getText();
	}
	
	private boolean addContact() {
		    return UIController.getUnicaInstancia().verificarContactoYAnyadirContacto( getNumero(), getNombre());		
	}
	
	private void reset() {
		textFieldPhone.setText("");
        textFieldPhone.setForeground(Color.WHITE);
        textNombreContacto.setText("");
        textNombreContacto.setForeground(Color.WHITE);
	}
	
	public void Errors(byte code) {
		switch (code) {
		case 1: {
			//System.err.println("[Error] : El valor no es un número válido.");
		    textFieldPhone.setForeground(Color.RED);
            textFieldPhone.setText("Invalid number");
			break;
		}
		case 2: {
			//System.err.println("[Error] : contacto ya existente.");
		    textFieldPhone.setForeground(Color.RED);
            textFieldPhone.setText("contact already exists");
            textNombreContacto.setForeground(Color.RED);
            textNombreContacto.setText("contact already exists");
			break;
		}
		case 3: {
			//System.err.println("[Error] : nombre invalido.");
			textNombreContacto.setForeground(Color.RED);
			textNombreContacto.setText("invalid name");
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + code);
		}
	}

}
