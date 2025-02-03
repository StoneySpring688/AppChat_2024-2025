package umu.tds.AppChat.ui;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.controllers.UIController;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

public class CreateGroupPanel  extends PanelGrande {

	private static final long serialVersionUID = 1L;
	
	private Background fondo;
	private JTextField textNombreGrupo;
	private JButton anyadirButton;
	private JTextField urlField;
	private DefaultListModel<ElementoChatOGrupo> contactos;
	private JList<ElementoChatOGrupo> listaContactos;
	private DefaultListModel<ElementoChatOGrupo> miembros;
	private JList<ElementoChatOGrupo> listaMiembros;
	
    private ImageAvatar lblProfile;
    private boolean validImage;
	
	private final Color darkPorDefecto = new Color(54, 57, 63);
	private final Color Gray = new Color(64, 68, 75);
	
	private final static String defaultProfileImage = "/assets/ProfilePic.png";
	private final static String defaultGroupUrl = "https://github.com/StoneySpring688/AppChat_2024-2025/blob/main/AppChat/src/main/resources/assets/ProfilePic.png?raw=true";
	
	public CreateGroupPanel(UIController uiController) {
		
		this.fondo = new Background();
		this.fondo.setBounds(0, 60, 920, 660);
		this.add(fondo);
		
		//campo para el nombre del grupo
		textNombreGrupo = new JTextField();
		textNombreGrupo.setBounds(20, 104, 285, 30);
		textNombreGrupo.setBackground(this.Gray);
		textNombreGrupo.setCaretColor(Color.WHITE);
		textNombreGrupo.setForeground(Color.WHITE);
		//textNombreGrupo.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		this.fondo.add(textNombreGrupo);
		textNombreGrupo.setColumns(10);
		
		//texto indicativo
		JLabel lblGrupo = new JLabel("Nombre Grupo");
		lblGrupo.setBounds(20, 87, 120, 20);
		lblGrupo.setForeground(Color.WHITE);
		this.fondo.add(lblGrupo);
		
		//boton para aceptar la operación
		anyadirButton = new JButton("Accept");
		anyadirButton.setForeground(Color.WHITE);
		anyadirButton.setBackground(new Color(241, 57, 83));
		anyadirButton.setBounds(342, 582, 187, 35); // x = 305 + (262/2) - (187/2)
		this.fondo.add(anyadirButton);
		
		//texto indicativo
		JLabel lblUrlImage = new JLabel("ImageUrl");
		lblUrlImage.setBounds(627, 87, 60, 20);
		lblUrlImage.setForeground(Color.WHITE);
		this.fondo.add(lblUrlImage);
		
		// label para ver la imagen del grupo
		lblProfile = new ImageAvatar();
		ImageIcon image = new ImageIcon( new ImageIcon(RegisterPanel.class.getResource(defaultProfileImage)).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH) );
		lblProfile.setImage(image);
		lblProfile.setBorderSize(1);
		lblProfile.setBorderSpace(1);
		lblProfile.setBounds(567, 90, 44, 44);
		this.fondo.add(lblProfile);
		
		//campo para la url de la imagen del grupo
		urlField = new JTextField();
		urlField.setBackground(this.Gray);
		urlField.setCaretColor(Color.WHITE);
		urlField.setForeground(Color.WHITE);
		//urlField.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		urlField.setBounds(627, 104, 202, 30);
		this.fondo.add(urlField);
		
		//boton para borra directamente la url
		JLabel buttonDeleteUrl = new JLabel();
		ImageIcon deleteIcon = new ImageIcon(new ImageIcon(getClass().getResource("/assets/Btn_FairyBook_Cancel.png")).getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH) );
		buttonDeleteUrl.setIcon(deleteIcon);
		buttonDeleteUrl.setBounds(830, 112, 22, 22);
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
		this.fondo.add(buttonDeleteUrl);
		
		//boton para actualizar la imagen
		JLabel buttonUpdateImage = new JLabel();
		ImageIcon updateProfileIcon = new ImageIcon(new ImageIcon(getClass().getResource("/assets/UI_MarkPoint_Sign_Inside.png")).getImage().getScaledInstance(22, 22,  Image.SCALE_SMOOTH));
		buttonUpdateImage.setIcon(updateProfileIcon);
		buttonUpdateImage.setBounds(855, 112, 22, 22);
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
		this.fondo.add(buttonUpdateImage);
		
		//panel de contactos
		JPanel panelContactos = new JPanel();
		panelContactos.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelContactos.setBounds(20, 163, 285, 385);
		this.fondo.add(panelContactos);
		
		//lista de contactos
		this.contactos = new DefaultListModel<>();
		panelContactos.setLayout(new BoxLayout(panelContactos, BoxLayout.X_AXIS));
		this.listaContactos = new JList<>(contactos);
		this.listaContactos.setCellRenderer(new ElementoChatOGrupoRender(this.listaContactos));
		this.listaContactos.setBackground(this.darkPorDefecto);
		JScrollPane scrollListaContactos = new JScrollPane(this.listaContactos);
		scrollListaContactos.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollListaContactos.setVerticalScrollBar(new ScrollBar());
		scrollListaContactos.setBorder(BorderFactory.createEmptyBorder());
		scrollListaContactos.setBackground(this.darkPorDefecto);
		panelContactos.add(scrollListaContactos);
		listaContactos.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int selectedIndex = listaContactos.getSelectedIndex();
		        if (selectedIndex != -1) {
		            // Obtener el elemento seleccionado
		            ElementoChatOGrupo selectedContact = contactos.getElementAt(selectedIndex);

		            // Removerlo de la lista de contactos y añadirlo a la lista de miembros
		            contactos.removeElementAt(selectedIndex);
		            miembros.add(0, selectedContact);
		        }
		    }
		});
		
		//panel de miembros
		JPanel panelMiembros = new JPanel();
		panelMiembros.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelMiembros.setBounds(567, 163, 285, 385);
		this.fondo.add(panelMiembros);
		
		//lista de miembros
		this.miembros = new DefaultListModel<>();
		panelMiembros.setLayout(new BoxLayout(panelMiembros, BoxLayout.X_AXIS));
		this.listaMiembros = new JList<>(miembros);
		this.listaMiembros.setCellRenderer(new ElementoChatOGrupoRender(this.listaMiembros));
		this.listaMiembros.setBackground(this.darkPorDefecto);
		JScrollPane scrollListaMiembros = new JScrollPane(this.listaMiembros);
		scrollListaMiembros.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollListaMiembros.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollListaMiembros.setVerticalScrollBar(new ScrollBar());
		scrollListaMiembros.setBorder(BorderFactory.createEmptyBorder());
		scrollListaContactos.setBackground(this.darkPorDefecto);
		panelMiembros.add(scrollListaMiembros);
		listaMiembros.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int selectedIndex = listaMiembros.getSelectedIndex();
		        if (selectedIndex != -1) {
		            // Obtener el elemento seleccionado
		            ElementoChatOGrupo selectedContact = miembros.getElementAt(selectedIndex);

		            // Removerlo de la lista de contactos y añadirlo a la lista de miembros
		            miembros.removeElementAt(selectedIndex);
		            contactos.add(0, selectedContact);
		        }
		    }
		});
		
	}
	
	public void iniciar(List<EntidadComunicable> lista) {
		this.miembros.clear();
		this.contactos.clear();
		lista.forEach(e -> this.contactos.addElement(new ElementoChatOGrupo(Optional.of(e), Optional.empty())));
	    this.listaContactos.setModel(this.contactos);
	    this.repaint();
	    this.revalidate();
		
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
	            
	            validImage = true;
	            //System.out.println("Imagen cargada exitosamente.");
	            
	        } else {
	            //System.out.println("Texto ingresado no contiene una imagen válida.");
	            ImageIcon image = new ImageIcon( new ImageIcon(RegisterPanel.class.getResource(defaultProfileImage)).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH) );
	    		lblProfile.setImage(image);
	    		lblProfile.revalidate();
	            lblProfile.repaint();
	            validImage = false;
	        }
	        
		} catch (MalformedURLException e) {
            ImageIcon image = new ImageIcon( new ImageIcon(RegisterPanel.class.getResource(defaultProfileImage)).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH) );
            lblProfile.setImage(image);
            lblProfile.revalidate();
            lblProfile.repaint();
            validImage = false;
		}catch (NullPointerException e){
            ImageIcon image = new ImageIcon( new ImageIcon(RegisterPanel.class.getResource(defaultProfileImage)).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH) );
            lblProfile.setImage(image);
            lblProfile.revalidate();
            lblProfile.repaint();
            validImage = false;
		} catch (Exception e) {
            ImageIcon image = new ImageIcon( new ImageIcon(RegisterPanel.class.getResource(defaultProfileImage)).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH) );
            lblProfile.setImage(image);
            lblProfile.revalidate();
            lblProfile.repaint();
            validImage = false;
		}
    }
	
}