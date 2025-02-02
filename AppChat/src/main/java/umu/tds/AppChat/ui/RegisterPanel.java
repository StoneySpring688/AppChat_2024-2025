package umu.tds.AppChat.ui;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import raven.datetime.DatePicker;
import raven.datetime.DateSelectionAble;
import raven.datetime.event.DateSelectionEvent;
import raven.datetime.event.DateSelectionListener;
import umu.tds.AppChat.controllers.UIController;


public class RegisterPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textFieldName;
	private DatePicker datePicker;
	private JTextField textFieldPhone;
	private JPasswordField password1;
	private JPasswordField password2;
	private JTextArea textFieldSignature;
	protected JLabel backButton;
	private JTextField urlField;
	private ImageAvatar lblProfile;
	
	
	private final Color defaultDark = new Color(40, 43, 48);
	private final Color Gray = new Color(64, 68, 75);
	private final static String defaultProfileImage = "/assets/ProfilePic.png";
	private final static String defaultProfileUrl = "https://github.com/StoneySpring688/AppChat_2024-2025/blob/main/AppChat/src/main/resources/assets/ProfilePic.png?raw=true";

	public RegisterPanel(UIController uiController) {
		setBackground(this.Gray);
		setBorder(new EmptyBorder(5, 5, 5, 5));

		setLayout(null);
		
		// Botón o etiqueta para cerrar la ventana
		JLabel closeButton = new JLabel("");
		closeButton.setIcon(new ImageIcon(RegisterPanel.class.getResource("/assets/UI_Exit.png")));
		closeButton.setForeground(new Color(241, 57, 83));
		closeButton.setBounds(685, 2, 33, 33);
		closeButton.setBackground(Color.WHITE);
		this.add(closeButton);
				
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0); // Cierra la aplicación
				}
			});
		
		JPanel panel = new JPanel();
		panel.setBackground(this.defaultDark);
		panel.setBounds(0, 0, 346, 420);
		this.add(panel);
		panel.setLayout(null);
		
		JLabel lblDecoration1 = new JLabel("");
		lblDecoration1.setIcon(new ImageIcon(RegisterPanel.class.getResource("/assets/UI_ChapterIcon_MondstadtResized.png")));
		lblDecoration1.setBounds(141, 4, 64, 64);
		panel.add(lblDecoration1);
		
		JLabel lblDecoration2 = new JLabel("");
		lblDecoration2.setIcon(new ImageIcon(RegisterPanel.class.getResource("/assets/UI_ChapterIcon_LiyueResized.png")));
		lblDecoration2.setBounds(141, 72, 64, 64);
		panel.add(lblDecoration2);
		
		JLabel lblDecoration3 = new JLabel("");
		lblDecoration3.setIcon(new ImageIcon(RegisterPanel.class.getResource("/assets/UI_ChapterIcon_InazumaResized.png")));
		lblDecoration3.setBounds(141, 140, 64, 64);
		panel.add(lblDecoration3);
		
		JLabel lblDecoration4 = new JLabel("");
		lblDecoration4.setIcon(new ImageIcon(RegisterPanel.class.getResource("/assets/UI_ChapterIcon_SumeruResized.png")));
		lblDecoration4.setBounds(141, 208, 64, 64);
		panel.add(lblDecoration4);
		
		JLabel lblDecoration5 = new JLabel("");
		lblDecoration5.setIcon(new ImageIcon(RegisterPanel.class.getResource("/assets/UI_ChapterIcon_FontaineResized.png")));
		lblDecoration5.setBounds(141, 276, 64, 64);
		panel.add(lblDecoration5);
		
		JLabel lblDecoration6 = new JLabel("");
		lblDecoration6.setIcon(new ImageIcon(RegisterPanel.class.getResource("/assets/UI_ChapterIcon_NatlanResized.png")));
		lblDecoration6.setBounds(141, 344, 64, 64);
		panel.add(lblDecoration6);
		
		backButton = new JLabel("");
		backButton.setIcon(new ImageIcon(RegisterPanel.class.getResource("/assets/UI_Icon_RoleCombat_Com_Back.png")));
		backButton.setBounds(0, 0, 42, 42);
		panel.add(backButton);
		
		JButton RegisterButton = new JButton("Register");
		RegisterButton.setForeground(Color.WHITE);
		RegisterButton.setBackground(new Color(241, 57, 83));
		RegisterButton.setBounds(419, 360, 187, 35); // x = 370+(285/2)-(187/2)
		this.add(RegisterButton);
		
		textFieldName = new JTextField();
		textFieldName.setBounds(370, 25, 285, 30);
		textFieldName.setBackground(this.Gray);
		textFieldName.setCaretColor(Color.WHITE);
		textFieldName.setForeground(Color.WHITE);
		///textFieldName.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		this.add(textFieldName);
		textFieldName.setColumns(10);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(370, 10, 40, 15);
		lblName.setForeground(Color.WHITE);
		this.add(lblName);
		
		/*
		textFieldLastName = new JTextField();
		textFieldLastName.setBounds(370, 80, 285, 30);
		textFieldLastName.setBackground(this.Gray);
		textFieldLastName.setCaretColor(Color.WHITE);
		textFieldLastName.setForeground(Color.WHITE);
		//textFieldLastName.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		this.add(textFieldLastName);
		textFieldLastName.setColumns(10);
		*/
		
		JLabel lblDate = new JLabel("Birth Date");
		lblDate.setBounds(370, 62, 70, 20);
		lblDate.setForeground(Color.WHITE);
		this.add(lblDate);
		
		
		JFormattedTextField editor = new JFormattedTextField();
		editor.setBounds(370, 80, 285, 30);
		editor.setBackground(this.Gray);
		editor.setCaretColor(Color.WHITE);
		editor.setForeground(Color.WHITE);
		
		this.datePicker  = new DatePicker();
		this.datePicker.setEditor(editor);
		this.datePicker.setDateSelectionAble(new DateSelectionAble() {
			
			@Override
			public boolean isDateSelectedAble(LocalDate date) {
				return !date.isAfter(LocalDate.now());
			}
		});
		
		this.datePicker.addDateSelectionListener(new DateSelectionListener() {
			
			@Override
			public void dateSelected(DateSelectionEvent dateSelectionEvent) {
				datePicker.closePopup();
				/*
				DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				LocalDate date = datePicker.getSelectedDate();
				if(date != null) {
					System.out.println("[DEBUG]" + df.format(date));
				}
				*/
			}
		});
		
		this.add(editor);	
		
		textFieldPhone = new JTextField();
		textFieldPhone.setBounds(370, 135, 285, 30);
		textFieldPhone.setBackground(this.Gray);
		textFieldPhone.setCaretColor(Color.WHITE);
		textFieldPhone.setForeground(Color.WHITE);
		//textFieldPhone.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		this.add(textFieldPhone);
		textFieldPhone.setColumns(10);
		
		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setBounds(370, 117, 50, 20);
		lblPhone.setForeground(Color.WHITE);
		this.add(lblPhone);
		
		password1 = new JPasswordField();
		password1.setBackground(this.Gray);
		password1.setCaretColor(Color.WHITE);
		password1.setForeground(Color.WHITE);
		//password1.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		password1.setColumns(10);
		password1.setBounds(370, 190, 130, 30);
		this.add(password1);
		
		JLabel lblPassword1 = new JLabel("Password");
		lblPassword1.setBounds(370, 172, 70, 20);
		lblPassword1.setForeground(Color.WHITE);
		this.add(lblPassword1);
		
		password2 = new JPasswordField();
		password2.setBackground(this.Gray);
		password2.setCaretColor(Color.WHITE);
		password2.setForeground(Color.WHITE);
		//password2.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		password2.setColumns(10);
		password2.setBounds(525, 190, 130, 30);
		this.add(password2);
		
		JLabel lblPassword2 = new JLabel("Repeat Password");
		lblPassword2.setBounds(525, 172, 120, 20);
		lblPassword2.setForeground(Color.WHITE);
		this.add(lblPassword2);
		
		textFieldSignature = new JTextArea();
		textFieldSignature.setBackground(this.Gray);
		textFieldSignature.setCaretColor(Color.WHITE);
		textFieldSignature.setForeground(Color.WHITE);
		//textFieldSignature.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		textFieldSignature.setLineWrap(true);
		textFieldSignature.setWrapStyleWord(true);
		textFieldSignature.setColumns(10);
		textFieldSignature.setBounds(370, 270, 155, 70);
		//this.add(textFieldSignature);
		JScrollPane scrollPaneSignature = new JScrollPane(textFieldSignature);
		scrollPaneSignature.setBounds(370, 270, 155, 70);
		scrollPaneSignature.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		ScrollBar VScroll = new ScrollBar();
		VScroll.setBackground(this.Gray);
		scrollPaneSignature.setVerticalScrollBar(VScroll);
		scrollPaneSignature.setHorizontalScrollBar(new ScrollBar());
		this.add(scrollPaneSignature);
		
		JLabel lblSignature = new JLabel("Signature");
		lblSignature.setBounds(370, 250, 70, 20);
		lblSignature.setForeground(Color.WHITE);
		this.add(lblSignature);
		
		JRadioButton rdbtnshowPassword1 = new JRadioButton("show Password");
		rdbtnshowPassword1.setBackground(this.Gray);
		rdbtnshowPassword1.setForeground(Color.WHITE);
		rdbtnshowPassword1.setBounds(370, 225, 130, 25);
		rdbtnshowPassword1.addItemListener (e -> {
			if(rdbtnshowPassword1.isSelected()) {
				password1.setEchoChar((char)0);
			}else {
				password1.setEchoChar('•');
			}
		});
		this.add(rdbtnshowPassword1);
		
		JRadioButton rdbtnshowPassword2 = new JRadioButton("show Password");
		rdbtnshowPassword2.setBackground(this.Gray);
		rdbtnshowPassword2.setForeground(Color.WHITE);
		rdbtnshowPassword2.setBounds(525, 225, 130, 25);
		rdbtnshowPassword2.addItemListener (e -> {
			if(rdbtnshowPassword2.isSelected()) {
				password2.setEchoChar((char)0);
			}else {
				password2.setEchoChar('•');
			}
		});
		this.add(rdbtnshowPassword2);
		
		/*
		lblProfile = new JLabel("");
		ImageIcon image = new ImageIcon( new ImageIcon(RegisterPanel.class.getResource(defaultProfileImage)).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH) );
		lblProfile.setIcon(image);
		lblProfile.setBounds(611, 270, 44, 44);
		this.add(lblProfile);
		*/
		
		lblProfile = new ImageAvatar();
		lblProfile.setImage(new ImageIcon(getClass().getResource("/assets/ProfilePic.png")));
		lblProfile.setBorderSize(1);
		lblProfile.setBorderSpace(1);
		lblProfile.setBounds(611, 270, 44, 44);
        this.add(lblProfile);
		
		urlField = new JTextField();
		urlField.setBackground(this.Gray);
		urlField.setCaretColor(Color.WHITE);
		urlField.setForeground(Color.WHITE);
		//urlField.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		urlField.setBounds(543, 315, 112, 25);
		this.add(urlField);
		
		JLabel lblUrlImage = new JLabel("ImageUrl");
		lblUrlImage.setBounds(543, 294, 60, 20);
		lblUrlImage.setForeground(Color.WHITE);
		this.add(lblUrlImage);
		
		/*
		urlField.getDocument().addDocumentListener(new DocumentListener() {
		    private final String defaultProfileImage = RegisterPanel.getDefaultProfileImage();

			@Override
		    public void insertUpdate(DocumentEvent e) {
		        try {
					actualizarImagen();
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
		    }

		    @Override
		    public void removeUpdate(DocumentEvent e) {
		        try {
					actualizarImagen();
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
		    }

		    @Override
		    public void changedUpdate(DocumentEvent e) {
		        try {
					actualizarImagen();
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
		    }

		    
		});
		*/
		
		//boton para borra directamente la url
		JLabel buttonDeleteUrl = new JLabel();
		ImageIcon deleteIcon = new ImageIcon(new ImageIcon(getClass().getResource("/assets/Btn_FairyBook_Cancel.png")).getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH) );
		buttonDeleteUrl.setIcon(deleteIcon);
		buttonDeleteUrl.setBounds(655, 324, 22, 22);
		buttonDeleteUrl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				urlField.setText(""); //eliminar el texto en la url
				try {
					actualizarImagen();
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
			}
		});
		UIController.addHoverEffect(buttonDeleteUrl, 20, 20);
		this.add(buttonDeleteUrl);
		
		//boton para actualizar la imagen
		JLabel buttonUpdateImage = new JLabel();
		ImageIcon updateProfileIcon = new ImageIcon(new ImageIcon(getClass().getResource("/assets/UI_MarkPoint_Sign_Inside.png")).getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH));
		buttonUpdateImage.setIcon(updateProfileIcon);
		buttonUpdateImage.setBounds(677, 324, 22, 22);
		buttonUpdateImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					actualizarImagen();
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
			}
		});
		UIController.addHoverEffect(buttonUpdateImage, 20, 20);
		this.add(buttonUpdateImage);
		
	}
	
	public static String getDefaultProfileImage() {
		return new String(defaultProfileUrl);
	}
	
	public JLabel getBackButton() {
		return this.backButton;
	}
	
	private void actualizarImagen() throws MalformedURLException {
        String urlText = urlField.getText();
        URL url;
        
		try {
			url = new URL(urlText);
			ImageIcon icono = new ImageIcon(url);
			
	        if (icono.getIconWidth() > 0 && icono.getIconHeight() > 0) { // Verifica que la imagen sea válida
	            Image imagenEscalada = icono.getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH);
	            //image.setImage(imagenEscalada);
	            lblProfile.setImage(new ImageIcon(imagenEscalada));
	            lblProfile.revalidate();
	            lblProfile.repaint();
	            //System.out.println("Imagen cargada exitosamente.");
	            
	        } else {
	            //System.out.println("Texto ingresado no contiene una imagen válida.");
	            ImageIcon image = new ImageIcon( new ImageIcon(RegisterPanel.class.getResource(defaultProfileImage)).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH) );
	    		lblProfile.setImage(image);
	    		lblProfile.revalidate();
	            lblProfile.repaint();
	        }
	        
		} catch (MalformedURLException e) {
            ImageIcon image = new ImageIcon( new ImageIcon(RegisterPanel.class.getResource(defaultProfileImage)).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH) );
            lblProfile.setImage(image);
            lblProfile.revalidate();
            lblProfile.repaint();
		}catch (NullPointerException e){
            ImageIcon image = new ImageIcon( new ImageIcon(RegisterPanel.class.getResource(defaultProfileImage)).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH) );
            lblProfile.setImage(image);
            lblProfile.revalidate();
            lblProfile.repaint();
		} catch (Exception e) {
            ImageIcon image = new ImageIcon( new ImageIcon(RegisterPanel.class.getResource(defaultProfileImage)).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH) );
            lblProfile.setImage(image);
            lblProfile.revalidate();
            lblProfile.repaint();
		}
    }
	
	protected void reset() {
		this.textFieldName.setText("");
		this.datePicker.clearSelectedDate();
		this.textFieldPhone.setText("");
		this.password1.setText("");
		this.password2.setText("");
		this.textFieldSignature.setText("");
		this.urlField.setText("");
		try {
			actualizarImagen();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	
}
