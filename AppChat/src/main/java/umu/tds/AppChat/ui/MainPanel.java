package umu.tds.AppChat.ui;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import umu.tds.AppChat.controllers.UIController;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.CardLayout;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
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

	public MainPanel(UIController uiController) {
		setBackground(Color.WHITE);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(null);
		
		//boton de cierre
		closeButton = new JLabel("");
		closeButton.setIcon(new ImageIcon(getClass().getResource("/assets/UI_Exit.png")));
		closeButton.setBounds(1245, 2, 33, 33);
		this.add(closeButton);
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0); // Cierra la aplicación
				}
			});
		
		//configuración del menu1
		panelMenu1 = new JPanel();
		//panelMenu1.setBackground(new Color(54,57,63));
		panelMenu1.setBounds(120, 0, 240, 660);
		this.add(panelMenu1);
		CardLayout actualizadorMenu1 = new CardLayout(0, 0);
		panelMenu1.setLayout(actualizadorMenu1);
		
		//gestionar paneles menu1
		JPanel panelPorDefectoMenu1 = new JPanel();
		//panelPorDefectoMenu1.setBackground(new Color(54,57,63));
		panelPorDefectoMenu1.setBounds(120, 0, 240, 660);
		panelMenu1.add(panelPorDefectoMenu1, "porDefecto");
		
		ChatsList chatslist = new ChatsList();
		panelMenu1.add(chatslist, "chats");
		GroupsList groupslist = new GroupsList();
		panelMenu1.add(groupslist, "groups");
		
		actualizadorMenu1.show(panelMenu1, "porDefecto");
		
		//botonera
		panelBotonera = new JPanel();
		//panelBotonera.setBackground(new Color(54,57,63));
		panelBotonera.setBounds(0, 0, 120, 720);
		this.add(panelBotonera);
		panelBotonera.setLayout(null);
		
		buttonChats = new JButton(new ImageIcon(getClass().getResource("/assets/UI_ChapterIcon_Wanderer.png")));
		buttonChats.setOpaque(false);
		buttonChats.setContentAreaFilled(false);
		buttonChats.setBorderPainted(false);
		buttonChats.setBounds(10, 0, 100, 100);
		buttonChats.setFocusPainted(false);
		this.addHoverEffect(buttonChats);
		this.showMenu("chats", buttonChats, actualizadorMenu1, panelMenu1);
		panelBotonera.add(buttonChats);
		
		buttonGroups = new JButton(new ImageIcon(getClass().getResource("/assets/UI_ChapterIcon_Traveler.png")));
		buttonGroups.setOpaque(false);
		buttonGroups.setContentAreaFilled(false);
		buttonGroups.setBorderPainted(false);
		buttonGroups.setBounds(10, 120, 100, 100);
		buttonGroups.setFocusPainted(false);
		this.addHoverEffect(buttonGroups);
		this.showMenu("groups", buttonGroups, actualizadorMenu1, panelMenu1);
		panelBotonera.add(buttonGroups);
		
		buttonShop = new JButton(new ImageIcon(getClass().getResource("/assets/Ui_SystemOpenIcon_Shop.png")));
		buttonShop.setOpaque(false);
		buttonShop.setContentAreaFilled(false);
		buttonShop.setBorderPainted(false);
		buttonShop.setBounds(10, 240, 100, 100);
		buttonShop.setFocusPainted(false);
		this.addHoverEffect(buttonShop);
		this.showMenu("porDefecto", buttonShop, actualizadorMenu1, panelMenu1);
		panelBotonera.add(buttonShop);
		
		buttonSearch = new JButton(new ImageIcon(getClass().getResource("/assets/UI_ChapterIcon_Charlotte.png")));
		buttonSearch.setOpaque(false);
		buttonSearch.setContentAreaFilled(false);
		buttonSearch.setBorderPainted(false);
		buttonSearch.setBounds(10, 600, 100, 100);
		buttonSearch.setFocusPainted(false);
		this.addHoverEffect(buttonSearch);
		panelBotonera.add(buttonSearch);
		
		//panel del perfil
		panelMenuPerfil = new JPanel();
		//panelMenuPerfil.setBackground(new Color(47,49,54));
		panelMenuPerfil.setBounds(120, 660, 240, 60);
		this.add(panelMenuPerfil);
		panelMenuPerfil.setLayout(null);
		
		JLabel lblProfilePic = new JLabel("");
		lblProfilePic.setIcon(new ImageIcon(getClass().getResource("/assets/ProfilePic.png")));
		lblProfilePic.setBounds(0, 3, 55, 55);
		panelMenuPerfil.add(lblProfilePic);
		
		JLabel lblYourname = new JLabel("YourName");
		lblYourname.setForeground(Color.WHITE);
		lblYourname.setBounds(60, 20, 90, 20);
		panelMenuPerfil.add(lblYourname);
		
		lblsettingGear = new JLabel("");
		lblsettingGear.setBounds(200, 15, 33, 33);
		lblsettingGear.setIcon(new ImageIcon(getClass().getResource("/assets/SettingsGear.png")));
		panelMenuPerfil.add(lblsettingGear);
		
		lblLogout = new JLabel("");
		lblLogout.setIcon(new ImageIcon(getClass().getResource("/assets/Exit_Door_Reescalada.png")));
		lblLogout.setBounds(157, 14, 33, 35);
		panelMenuPerfil.add(lblLogout);
		
		//configuración de colores
		this.setBackground(new Color(64, 68, 75));      // Gris claro para el fondo principal
		panelBotonera.setBackground(new Color(40, 43, 48));    // Gris muy oscuro para la botonera   15, 15, 17 -> 32, 34, 37 -> 40, 43, 48
		panelMenuPerfil.setBackground(new Color(47, 49, 54));  // Gris oscuro para el perfil
		panelMenu1.setBackground(new Color(54, 57, 63));       // Gris medio para el menú
		panelPorDefectoMenu1.setBackground(new Color(54, 57, 63)); // Gris medio para el menú

	}
	
	private void addHoverEffect(JButton button) {
	    ImageIcon originalIcon = (ImageIcon) button.getIcon();
	    ImageIcon scaledIcon = new ImageIcon(originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));

	    
	    button.addMouseListener(new java.awt.event.MouseAdapter() {
	        @Override
	        public void mouseEntered(java.awt.event.MouseEvent evt) {
	            button.setIcon(scaledIcon);  // Cambia a icono más pequeño al pasar el ratón
	        }

	        @Override
	        public void mouseExited(java.awt.event.MouseEvent evt) {
	            button.setIcon(originalIcon);  // Vuelve al icono original al salir
	        }
	    });
	}
	
	private void showMenu(String menu, JButton button, CardLayout actualizador, JPanel panelObjetivo) {
		button.addActionListener(e -> actualizador.show(panelObjetivo, menu));
	}


}
