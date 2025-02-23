package umu.tds.AppChat.ui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;
import umu.tds.AppChat.controllers.BackendController;
import umu.tds.AppChat.controllers.UIController;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Optional;

public class OptionsPane extends PanelGrande {

	private static final long serialVersionUID = 1L;

	private Background fondo;
	private JScrollPane scrollPanel;
	
	// contacts settings
	private DefaultListModel<ElementoChatOGrupo> contactos;
	private JList<ElementoChatOGrupo> listaContactos;
	private JPanel contactsPreview;
	private JTextField textFieldPhone;
	private JTextField textNombreContacto;
	private JButton editButton;
	
	// groups settings
	private DefaultListModel<ElementoChatOGrupo> grupos;
	private JList<ElementoChatOGrupo> listaGrupos;
	private JPanel groupsPreview;
	
	
	private final Color Gray = new Color(64, 68, 75);
	private final Color darkPorDefecto = new Color(54, 57, 63);
	
	public OptionsPane() {

		this.fondo = new Background();
		this.fondo.setBounds(0, 60, 920, 660);
		
		this.scrollPanel = new JScrollPane(this.fondo);
		this.scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.scrollPanel.setVerticalScrollBar(new ScrollBar());
		this.scrollPanel.setBorder(BorderFactory.createEmptyBorder());
		this.scrollPanel.setBackground(this.Gray);
		this.scrollPanel.setBounds(0, 60, 920, 660);
		
		this.add(scrollPanel);this.add(scrollPanel);
		
		// ### header
		JLabel lblOptions = new JLabel("Options");
		lblOptions.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 15));
		lblOptions.setBounds(435, 5, 60, 20); //this.fondo.getWidth()/2-lblOptions.getWidth()/2
		lblOptions.setForeground(Color.WHITE);
		
		this.fondo.add(lblOptions);
		
		// ### contacts settings
		JLabel lblContacts = new JLabel("Contacts Settings");
		lblContacts.setFont(new Font("Dialog", Font.BOLD, 12));
		lblContacts.setBounds(10, 40, 120, 20); //this.fondo.getWidth()/2-lblOptions.getWidth()/2
		lblContacts.setForeground(Color.WHITE);
		
		this.fondo.add(lblContacts);
		
		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setBounds(410, 111, 42, 20);
		lblPhone.setForeground(Color.WHITE);
		this.fondo.add(lblPhone);
		
		textFieldPhone = new JTextField();
		textFieldPhone.setBounds(410, 129, 285, 30);
		textFieldPhone.setBackground(this.Gray);
		textFieldPhone.setCaretColor(Color.WHITE);
		textFieldPhone.setForeground(Color.WHITE);
		textFieldPhone.setColumns(10);
		textFieldPhone.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textFieldPhone.getForeground().equals(Color.RED)) {
                    textFieldPhone.setText(""); // Borrar el mensaje de error
                    textFieldPhone.setForeground(Color.WHITE); // Restaurar color normal
                }
            }
        });
		
		this.fondo.add(textFieldPhone);
		
		JLabel lblContacto = new JLabel("Nombre Contacto");
		lblContacto.setBounds(410, 201, 120, 20);
		lblContacto.setForeground(Color.WHITE);
		this.fondo.add(lblContacto);
		
		textNombreContacto = new JTextField();
		textNombreContacto.setBounds(410, 220, 285, 30);
		textNombreContacto.setBackground(this.Gray);
		textNombreContacto.setCaretColor(Color.WHITE);
		textNombreContacto.setForeground(Color.WHITE);
		textNombreContacto.setColumns(10);
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
		
		editButton = new JButton("Edit");
		editButton.setForeground(Color.WHITE);
		editButton.setBackground(new Color(241, 57, 83));
		editButton.setBounds(459, 310, 187, 35); // x = 370+(285/2)-(187/2)
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//numero inexistente o ya en contactos
				if(editContact()) {
					resetContactSettings();
				}
			}
		});
		
		this.fondo.add(editButton);
		
		contactsPreview = new JPanel();
		contactsPreview.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contactsPreview.setBounds(10, 80, 285, 385);
		this.fondo.add(contactsPreview);
		
		this.contactos = new DefaultListModel<>();
		contactsPreview.setLayout(new BoxLayout(contactsPreview, BoxLayout.X_AXIS));
		this.listaContactos = new JList<>(contactos);
		this.listaContactos.setCellRenderer(new ElementoChatOGrupoRender(this.listaContactos));
		this.listaContactos.setBackground(this.darkPorDefecto);
		JScrollPane scrollListaContactos = new JScrollPane(this.listaContactos);
		scrollListaContactos.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollListaContactos.setVerticalScrollBar(new ScrollBar());
		scrollListaContactos.setBorder(BorderFactory.createEmptyBorder());
		scrollListaContactos.setBackground(this.darkPorDefecto);
		contactsPreview.add(scrollListaContactos);
		listaContactos.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int selectedIndex = listaContactos.getSelectedIndex();
		        if (selectedIndex != -1) {
		            // Obtener el elemento seleccionado
		            ElementoChatOGrupo selectedContact = contactos.getElementAt(selectedIndex);
		            textFieldPhone.setText(Integer.toString(selectedContact.getNumero()));
		            textFieldPhone.setForeground(Color.WHITE);
		            textNombreContacto.setText(selectedContact.getNombre());
                	textNombreContacto.setForeground(Color.WHITE);

		            
		        }
		    }
		});
		

		
		// ### groups settings
		JLabel lblGroups = new JLabel("Groups Settings");
		lblGroups.setFont(new Font("Dialog", Font.BOLD, 12));
		lblGroups.setBounds(10, 520, 120, 20); //this.fondo.getWidth()/2-lblOptions.getWidth()/2
		lblGroups.setForeground(Color.WHITE);
		
		this.fondo.add(lblGroups);
	}
	
	public String getNumero() {
		return textFieldPhone.getForeground().equals(Color.RED) ? "" : this.textFieldPhone.getText();
	}
	
	public String getNombre() {
		return textNombreContacto.getForeground().equals(Color.RED) ? "" : this.textNombreContacto.getText();
	}
	
	public void loadContactsAndGroups(List<EntidadComunicable> contactos, List<Grupo> grupos) {
		this.contactos.clear();
		contactos.forEach(e -> this.contactos.addElement(new ElementoChatOGrupo(Optional.of(e), Optional.empty())));
	    this.listaContactos.setModel(this.contactos);
	    this.repaint();
	    this.revalidate();
		
	}
	
	private boolean editContact() {
	    return UIController.verificarContactoYEditarContacto(getNumero(), getNombre());		
	}
	
	private void resetContactSettings() {
		textFieldPhone.setText("");
        textFieldPhone.setForeground(Color.WHITE);
        textNombreContacto.setText("");
        textNombreContacto.setForeground(Color.WHITE);
        
        this.contactos = new DefaultListModel<>();
		this.listaContactos.setModel(this.contactos);
		
		this.contactos.clear();
		BackendController.getListaContactos().forEach(e -> this.contactos.addElement(new ElementoChatOGrupo(Optional.of(e), Optional.empty())));
	    this.listaContactos.setModel(this.contactos);
	    this.repaint();
	    this.revalidate();
		
	}
	
	public void contactSettingsErrors(byte code) {
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
            textFieldPhone.setText("contact do not exists");
            textNombreContacto.setForeground(Color.RED);
            textNombreContacto.setText("contact do not exists");
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
