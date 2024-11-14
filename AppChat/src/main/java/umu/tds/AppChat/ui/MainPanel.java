package umu.tds.AppChat.ui;

import java.awt.Color;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import umu.tds.AppChat.controllers.UIController;
import umu.tds.AppChat.ui.chatInterface.ChatPanel;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.CardLayout;

public class MainPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JPanel panelBotonera;
	private JPanel panelMenu1;
	private JPanel panelMenuPerfil;
	private JPanel principal;
	private SearchPanel searchPanel;
	private AddContactPanel panelAnyadirContacto;
	private CreateGroupPanel panelCrearGrupo;
	private JLabel lblLogout;
	private JLabel lblsettingGear;
	private JButton buttonGroups;
	private JButton buttonSearch;
	private JButton buttonChats;
	private JButton buttonShop;
	private CardLayout actualizadorUiPrincipal;

	public MainPanel(UIController uiController) {
		setBackground(Color.WHITE);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(null);
		
		//panelPrincipal
		principal = new JPanel();
		principal.setBounds(360, 0, 920, 720);
		this.add(principal);
		
		//actualizador principal
		actualizadorUiPrincipal = new CardLayout();
		principal.setLayout(this.actualizadorUiPrincipal);
		
		//panelesPrincipales
		PanelGrande PanelGrandePorDefecto = new PanelGrande();
		searchPanel = new SearchPanel(uiController);
		panelAnyadirContacto = new AddContactPanel(uiController);
		panelCrearGrupo = new CreateGroupPanel(uiController);
		ChatPanel chat = new ChatPanel();
		
		principal.add(PanelGrandePorDefecto, "porDefecto");
		principal.add(searchPanel, "search");
		principal.add(panelAnyadirContacto, "anyadirContacto");
		principal.add(panelCrearGrupo, "crearGrupo");
		principal.add(chat, "chat");
		
		this.actualizadorUiPrincipal.show(principal, "porDefecto");
		
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
		
		ChatsList chatslist = new ChatsList(uiController);
		panelMenu1.add(chatslist, "chats");
		GroupsList groupslist = new GroupsList(uiController);
		panelMenu1.add(groupslist, "groups");
		MessageList messageList = new MessageList();
		panelMenu1.add(messageList,"messages");
		
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
		UIController.addHoverEffect(buttonChats);
		this.showMenu("porDefecto", buttonChats, this.actualizadorUiPrincipal, principal);
		this.showMenu("chats", buttonChats, actualizadorMenu1, panelMenu1);
		panelBotonera.add(buttonChats);
		
		buttonGroups = new JButton(new ImageIcon(getClass().getResource("/assets/UI_ChapterIcon_Traveler.png")));
		buttonGroups.setOpaque(false);
		buttonGroups.setContentAreaFilled(false);
		buttonGroups.setBorderPainted(false);
		buttonGroups.setBounds(10, 120, 100, 100);
		buttonGroups.setFocusPainted(false);
		UIController.addHoverEffect(buttonGroups);
		this.showMenu("porDefecto", buttonGroups, this.actualizadorUiPrincipal, principal);
		this.showMenu("groups", buttonGroups, actualizadorMenu1, panelMenu1);
		panelBotonera.add(buttonGroups);
		
		buttonShop = new JButton(new ImageIcon(getClass().getResource("/assets/Ui_SystemOpenIcon_Shop.png")));
		buttonShop.setOpaque(false);
		buttonShop.setContentAreaFilled(false);
		buttonShop.setBorderPainted(false);
		buttonShop.setBounds(10, 240, 100, 100);
		buttonShop.setFocusPainted(false);
		UIController.addHoverEffect(buttonShop);
		this.showMenu("chat", buttonShop, this.actualizadorUiPrincipal, principal);
		this.showMenu("porDefecto", buttonShop, actualizadorMenu1, panelMenu1);
		panelBotonera.add(buttonShop);
		
		buttonSearch = new JButton(new ImageIcon(getClass().getResource("/assets/UI_ChapterIcon_Charlotte.png")));
		buttonSearch.setOpaque(false);
		buttonSearch.setContentAreaFilled(false);
		buttonSearch.setBorderPainted(false);
		buttonSearch.setBounds(10, 600, 100, 100);
		buttonSearch.setFocusPainted(false);
		UIController.addHoverEffect(buttonSearch);
		this.showMenu("search", buttonSearch, this.actualizadorUiPrincipal, principal);
		this.showMenu("messages", buttonSearch, actualizadorMenu1, panelMenu1);
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
		UIController.addHoverEffect(lblLogout, 30, 32);
		panelMenuPerfil.add(lblLogout);
		
		//configuración de colores
		this.setBackground(new Color(64, 68, 75));      // Gris claro para el fondo principal
		panelBotonera.setBackground(new Color(40, 43, 48));    // Gris muy oscuro para la botonera   15, 15, 17 -> 32, 34, 37 -> 40, 43, 48
		panelMenuPerfil.setBackground(new Color(47, 49, 54));  // Gris oscuro para el perfil
		panelMenu1.setBackground(new Color(54, 57, 63));       // Gris medio para el menú
		panelPorDefectoMenu1.setBackground(new Color(54, 57, 63)); // Gris medio para el menú

	}
	
	private void showMenu(String menu, JButton button, CardLayout actualizador, JPanel panelObjetivo) {
		button.addActionListener(e -> actualizador.show(panelObjetivo, menu));
	}
	
	public void changePanelPrincipal(String panel) {
    	this.actualizadorUiPrincipal.show(this.principal, panel);
    }
	
	public JLabel getLogoutBotton() {
		return this.lblLogout;
	}
	
	public CardLayout getActualizadorUiPrincipal() {
		return this.actualizadorUiPrincipal;
	}
	
	@SuppressWarnings("unchecked")
	public void accederMetodoNoVisible(int numMetodo, Optional<Object> ... arg) {
		
		switch (numMetodo) {
		// inicializar lista de usuarios de crearGrupo
		case 1: {

			if (arg[0].isPresent() && arg[0].get() instanceof DefaultListModel) {
				DefaultListModel<ElementoChatOGrupo> lista = (DefaultListModel<ElementoChatOGrupo>) arg[0].get();
				panelCrearGrupo.iniciar(lista);
			}
			break;
			
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + numMetodo);
		}
		
	}


}
