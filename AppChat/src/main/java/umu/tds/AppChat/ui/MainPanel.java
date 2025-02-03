package umu.tds.AppChat.ui;

import java.awt.Color;
import java.util.List;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.controllers.UIController;
import umu.tds.AppChat.ui.chatInterface.Button;
import umu.tds.AppChat.ui.chatInterface.ChatPanel;

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
	private Button buttonGroups;
	private Button buttonSearch;
	private Button buttonChats;
	private Button buttonShop;
	private CardLayout actualizadorUiPrincipal;
	private ChatsList chatslist;
	private GroupsList groupslist;

	public MainPanel() {
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
		Background fondoPorDefecto = new Background();
		fondoPorDefecto.setBounds(0, 60, 920, 660);
		PanelGrandePorDefecto.add(fondoPorDefecto);
		searchPanel = new SearchPanel();
		panelAnyadirContacto = new AddContactPanel();
		panelCrearGrupo = new CreateGroupPanel();
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
		
		chatslist = new ChatsList();
		panelMenu1.add(chatslist, "chats");
		groupslist = new GroupsList();
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
		
		buttonChats = new Button();
		buttonChats.setIcon(new ImageIcon(getClass().getResource("/assets/UI_ChapterIcon_Wanderer.png")));
		buttonChats.setRound(100);
		//buttonChats.applyEffectSettings(0.3f, 600, 1.5, new Color(173, 173, 173, 80), 0.3f, 0.3f);
		buttonChats.setOpaque(false);
		buttonChats.setContentAreaFilled(false);
		buttonChats.setBorderPainted(false);
		buttonChats.setBounds(10, 0, 100, 100);
		buttonChats.setFocusPainted(false);
		UIController.addHoverEffect(buttonChats);
		//this.showMenu("porDefecto", buttonChats, this.actualizadorUiPrincipal, principal);
		this.showMenu("chats", buttonChats, actualizadorMenu1, panelMenu1);
		this.showMenu("chat", buttonChats, this.actualizadorUiPrincipal, principal);
		panelBotonera.add(buttonChats);
		
		buttonGroups = new Button();
		buttonGroups.setIcon(new ImageIcon(getClass().getResource("/assets/UI_ChapterIcon_Traveler.png")));
		buttonGroups.setRound(100);
		buttonGroups.setOpaque(false);
		buttonGroups.setContentAreaFilled(false);
		buttonGroups.setBorderPainted(false);
		buttonGroups.setBounds(10, 120, 100, 100);
		buttonGroups.setFocusPainted(false);
		UIController.addHoverEffect(buttonGroups);
		this.showMenu("porDefecto", buttonGroups, this.actualizadorUiPrincipal, principal);
		this.showMenu("groups", buttonGroups, actualizadorMenu1, panelMenu1);
		panelBotonera.add(buttonGroups);
		
		buttonShop = new Button();
		buttonShop.setIcon(new ImageIcon(getClass().getResource("/assets/Ui_SystemOpenIcon_Shop.png")));
		buttonShop.setRound(100);
		buttonShop.setOpaque(false);
		buttonShop.setContentAreaFilled(false);
		buttonShop.setBorderPainted(false);
		buttonShop.setBounds(10, 240, 100, 100);
		buttonShop.setFocusPainted(false);
		UIController.addHoverEffect(buttonShop);
		this.showMenu("porDefecto", buttonShop, this.actualizadorUiPrincipal, principal);
		this.showMenu("porDefecto", buttonShop, actualizadorMenu1, panelMenu1);
		panelBotonera.add(buttonShop);
		
		buttonSearch = new Button();
		buttonSearch.setIcon(new ImageIcon(getClass().getResource("/assets/UI_ChapterIcon_Charlotte.png")));
		buttonSearch.setRound(100);
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
		panelMenuPerfil = new JPanel(new MigLayout("align left, insets 5","[][]20[]10[]"));
		panelMenuPerfil.setBounds(120, 660, 240, 60);
		this.add(panelMenuPerfil);
		
		ImageAvatar avatar = new ImageAvatar();
		avatar.setImage(new ImageIcon(getClass().getResource("/assets/ProfilePic.png")));
        avatar.setBorderSize(1);
        avatar.setBorderSpace(1);
		panelMenuPerfil.add(avatar,"cell 0 0, height 50, width 50");
		
		JLabel lblYourname = new JLabel("YourName");
		lblYourname.setForeground(Color.WHITE);
		panelMenuPerfil.add(lblYourname, "growx, wmax 100");
		
		lblLogout = new JLabel("");
		lblLogout.setIcon(new ImageIcon(getClass().getResource("/assets/Exit_Door_Reescalada.png")));
		UIController.addHoverEffect(lblLogout, 30, 32);
		panelMenuPerfil.add(lblLogout,"cell 2 0, height 35, width 35");
		
		lblsettingGear = new JLabel("");
		lblsettingGear.setIcon(new ImageIcon(getClass().getResource("/assets/SettingsGear.png")));
		UIController.addHoverEffect(lblsettingGear, 30, 30);
		panelMenuPerfil.add(lblsettingGear,"cell 3 0, height 35, width 35");
		
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
	public void accederMetodoNoVisible(int numMetodo, Optional<Object> arg) {
		
		switch (numMetodo) {
		// inicializar lista de usuarios de crearGrupo
		case 1: {

			if (arg.isPresent() && arg.get() instanceof List<?>) {
				List<EntidadComunicable> lista = (List<EntidadComunicable>) arg.get();
				panelCrearGrupo.iniciar(lista);
			}
			break;
			
		}
		case 2: {
			if (arg.isPresent() && arg.get() instanceof EntidadComunicable) {
				this.chatslist.addChat(new EntidadComunicable((EntidadComunicable) arg.get()));
			}
			break;
		}
		case 3: {
			if (arg.isPresent() && arg.get() instanceof Byte) {
				this.panelAnyadirContacto.Errors((byte) arg.get());;
			}
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + numMetodo);
		}
		
	}

}
