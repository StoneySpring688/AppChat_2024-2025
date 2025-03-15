package umu.tds.AppChat.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;
import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.backend.utils.Membership;
import umu.tds.AppChat.backend.utils.Membership.MembershipType;
import umu.tds.AppChat.controllers.UIController;
import umu.tds.AppChat.ui.chatInterface.Button;
import umu.tds.AppChat.ui.chatInterface.ChatBox.BoxType;
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
	private PremiumShopPanel shopPanel;
	private AddContactPanel panelAnyadirContacto;
	private CreateGroupPanel panelCrearGrupo;
	private ChatPanel chat;
	private OptionsPane options;
	private JLabel lblYourname;
	private ImageAvatar yourAvatar;
	private JLabel lblLogout;
	private JLabel lblsettingGear;
	private Button buttonGroups;
	private Button buttonSearch;
	private Button buttonChats;
	private Button buttonShop;
	private CardLayout actualizadorUiPrincipal;
	private CardLayout actualizadorMenu1;
	private ChatsList chatslist;
	private GroupsList groupslist;
	private MessageList messageList;
	private MembershipList mshipsList;

	// singleton
	private static MainPanel unicaInstancia = null;
	 			
	public static MainPanel getUnicaInstancia() {
		unicaInstancia = unicaInstancia == null ? new MainPanel() : unicaInstancia;
	 	return unicaInstancia;
	}
	
	public MainPanel() {
		setBackground(Color.WHITE);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(null);
		
		// ### panelesPrincipales
		PanelGrande PanelGrandePorDefecto = new PanelGrande();
		Background fondoPorDefecto = new Background();
		fondoPorDefecto.setBounds(0, 60, 920, 660);
		PanelGrandePorDefecto.add(fondoPorDefecto);
		this.searchPanel = new SearchPanel();
		this.shopPanel = new PremiumShopPanel();
		this.panelAnyadirContacto = new AddContactPanel();
		this.panelCrearGrupo = new CreateGroupPanel();
		this.chat = new ChatPanel();
		this.options = new OptionsPane();
		
		// ### panelPrincipal
		principal = new JPanel();
		principal.setBounds(360, 0, 920, 720);
		this.add(principal);
		
		// ### actualizador principal
		actualizadorUiPrincipal = new CardLayout();
		principal.setLayout(this.actualizadorUiPrincipal);
		
		principal.add(PanelGrandePorDefecto, "porDefecto");
		principal.add(searchPanel, "search");
		principal.add(shopPanel, "shop");
		principal.add(panelAnyadirContacto, "anyadirContacto");
		principal.add(panelCrearGrupo, "crearGrupo");
		principal.add(chat, "chat");
		principal.add(options, "options");
		
		this.actualizadorUiPrincipal.show(principal, "porDefecto");
		
		// ### configuración del menu1
		panelMenu1 = new JPanel();
		//panelMenu1.setBackground(new Color(54,57,63));
		panelMenu1.setBounds(120, 0, 240, 660);
		this.add(panelMenu1);
		actualizadorMenu1 = new CardLayout(0, 0);
		panelMenu1.setLayout(actualizadorMenu1);
		
		// ### gestionar paneles menu1
		JPanel panelPorDefectoMenu1 = new JPanel();
		//panelPorDefectoMenu1.setBackground(new Color(54,57,63));
		panelPorDefectoMenu1.setBounds(120, 0, 240, 660);
		panelMenu1.add(panelPorDefectoMenu1, "porDefecto");
		
		chatslist = new ChatsList();
		panelMenu1.add(chatslist, "chats");
		groupslist = new GroupsList();
		panelMenu1.add(groupslist, "groups");
		messageList = MessageList.getUnicaInstancia(); //messageList = new MessageList();
		panelMenu1.add(messageList,"messages");
		mshipsList = new MembershipList();
		panelMenu1.add(mshipsList,"mships");
		
		actualizadorMenu1.show(panelMenu1, "porDefecto");
		
		// ### botonera
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
		buttonChats.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIController.setActualChatOptimization(0);
			}
		});
		this.showMenu("porDefecto", buttonChats, this.actualizadorUiPrincipal, principal);
		this.showMenu("chats", buttonChats, actualizadorMenu1, panelMenu1);
		//this.showMenu("chat", buttonChats, this.actualizadorUiPrincipal, principal);
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
		buttonGroups.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIController.setActualChatOptimization(0);
			}
		});
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
		buttonShop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIController.setActualChatOptimization(0);
			}
		});
		this.showMenu("porDefecto", buttonShop, this.actualizadorUiPrincipal, principal);
		this.showMenu("mships", buttonShop, actualizadorMenu1, panelMenu1);
		
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
		buttonSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UIController.setActualChatOptimization(0);
			}
		});
		this.showMenu("search", buttonSearch, this.actualizadorUiPrincipal, principal);
		this.showMenu("messages", buttonSearch, actualizadorMenu1, panelMenu1);
		panelBotonera.add(buttonSearch);
		
		// ### panel del perfil
		panelMenuPerfil = new JPanel(new MigLayout("align left, insets 5","[][]20[]10[]"));
		panelMenuPerfil.setBounds(120, 660, 240, 60);
		this.add(panelMenuPerfil);
		
		yourAvatar = new ImageAvatar();
		yourAvatar.setImage(new ImageIcon(getClass().getResource("/assets/ProfilePic.png")));
		yourAvatar.setBorderSize(1);
		yourAvatar.setBorderSpace(1);
		panelMenuPerfil.add(yourAvatar,"cell 0 0, height 50, width 50");
		
		lblYourname = new JLabel("YourName");
		lblYourname.setForeground(Color.WHITE);
		panelMenuPerfil.add(lblYourname, "growx, wmax 100");
		
		lblLogout = new JLabel("");
		lblLogout.setIcon(new ImageIcon(getClass().getResource("/assets/Exit_Door_Reescalada.png")));
		lblLogout.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	UIController.doLogout();
		    }
		});
		UIController.addHoverEffect(lblLogout, 30, 32);
		panelMenuPerfil.add(lblLogout,"cell 2 0, height 35, width 35");
		
		lblsettingGear = new JLabel("");
		lblsettingGear.setIcon(new ImageIcon(getClass().getResource("/assets/SettingsGear.png")));
		UIController.addHoverEffect(lblsettingGear, 30, 30);
		lblsettingGear.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	UIController.showSettingsPanel();
		    }
		});
		panelMenuPerfil.add(lblsettingGear,"cell 3 0, height 35, width 35");
		
		
		// ### configuración de colores
		this.setBackground(new Color(64, 68, 75));      // Gris claro para el fondo principal
		panelBotonera.setBackground(new Color(40, 43, 48));    // Gris muy oscuro para la botonera   15, 15, 17 -> 32, 34, 37 -> 40, 43, 48
		panelMenuPerfil.setBackground(new Color(47, 49, 54));  // Gris oscuro para el perfil
		panelMenu1.setBackground(new Color(54, 57, 63));       // Gris medio para el menú
		panelPorDefectoMenu1.setBackground(new Color(54, 57, 63)); // Gris medio para el menú

	}
	
	private void showMenu(String menu, JButton button, CardLayout actualizador, JPanel panelObjetivo) {
		button.addActionListener(e -> actualizador.show(panelObjetivo, menu));
	}
	
	public void showSettings(List<EntidadComunicable> contactos, List<Grupo> grupos, boolean isPremium) {
		this.options.reset();
		this.options.prepareOptions(contactos, grupos, isPremium);
		//this.options.loadContactsAndGroups(contactos, grupos);
		actualizadorUiPrincipal.show(principal, "options");
    	actualizadorMenu1.show(panelMenu1, "porDefecto");
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
	
	public void setUserInfo(String name, ImageIcon profilePic) {
		this.lblYourname.setText(name);	
		this.yourAvatar.setImage(profilePic);
	}
	
	public void reset() {
		this.actualizadorMenu1.show(this.panelMenu1, "porDefecto");
		changePanelPrincipal("porDefecto");
		
		this.chatslist.reset();
		this.groupslist.reset();
		this.messageList.reset();
		this.mshipsList.reset();
		this.options.reset();
		this.searchPanel.reset();
		this.repaint();
		this.revalidate();
	}
	
	@SuppressWarnings("unchecked")
	public void accederMetodoNoVisible(int numMetodo, Optional<Object> arg2, Optional<Object> arg) {
		
		switch (numMetodo) {
		case 1: { // inicializar lista de usuarios de crearGrupo

			if (arg.isPresent() && arg.get() instanceof List<?>) {
				List<EntidadComunicable> lista = (List<EntidadComunicable>) arg.get();
				panelCrearGrupo.reset();
				panelCrearGrupo.iniciar(lista);
			}
			break;
			
		}
		case 2: { // añadir un nuevo chat a la lista de chats
			if (arg.isPresent() && arg.get() instanceof EntidadComunicable) {
				this.chatslist.addChat(new EntidadComunicable((EntidadComunicable) arg.get()));
			}
			break;
		}
		case 3: { // gestionar los errores en el formulario para añadir contactos
			if (arg.isPresent() && arg.get() instanceof Byte) {
				this.panelAnyadirContacto.Errors((byte) arg.get());;
			}
			break;
		}
		case 4: { // gestionar los errores en el formulario para hacer grupos
			if (arg.isPresent() && arg.get() instanceof Byte) {
				this.panelCrearGrupo.Errors((byte) arg.get());;
			}
			break;
		}
		case 5: { // añadir un nuevo grupo a la lista de grupos
			if (arg.isPresent() && arg.get() instanceof Grupo) {
				this.groupslist.addGroup(new Grupo((Grupo) arg.get()));
			}
			break;
		}
		case 6: { // cambiar de chat al seleccionado
			if(arg.isPresent() && arg.get() instanceof ElementoChatOGrupo) {
				this.chat.changeChat((ElementoChatOGrupo) arg.get());
			}
			break;
		}
		case 7: { // renderizar mensaje
			if(arg.isPresent() && arg.get() instanceof List <?> && arg2.isPresent() && arg2.get() instanceof Integer) {
				this.chat.addMessage((List<ModelMessage>)arg.get(), (int)arg2.get());
				this.chat.resetText();
			}
			break;
		}
		case 8: { // renderizar mensaje
			if(arg.isPresent() && arg.get() instanceof List <?> && arg2.isPresent() && arg2.get() instanceof Integer) {
				this.chat.addMessageAtTop((List<ModelMessage>)arg.get(), (int)arg2.get());
				this.chat.resetText();
			}
			break;
		}
		case 9: { // preview search message
			if(arg2.isPresent() && arg2.get() instanceof ModelMessage && arg.isPresent() && arg.get() instanceof BoxType) {
				this.searchPanel.previewMessage((ModelMessage)arg2.get(), (BoxType)arg.get());
			}
			break;
		}
		case 10: { // actualizar la fecha de expiración del premium en ui
			this.mshipsList.loadPremiumExpireDate();
			break;
		}
		case 11: { // cambiar el panel de la tienda
			if(arg2.isPresent() && arg2.get() instanceof Double && arg.isPresent() && arg.get() instanceof MembershipType) {
				this.shopPanel.showShop((double)arg2.get(), (MembershipType)arg.get());
			}
			break;
		}
		case 12: { // cargar las ofertas en la lista
			if(arg2.isPresent() && arg2.get() instanceof List<?>) {
				this.mshipsList.loadMships((List<Membership>)arg2.get());
			}
			break;
		}
		case 13: { // gestionar los errores en el formulario para editar contactos
			if (arg.isPresent() && arg.get() instanceof Byte) {
				this.options.contactSettingsErrors((byte) arg.get());
			}
			break;
		}
		case 14: { // resetear la lista de chats
			if (arg.isEmpty() && arg2.isEmpty()) {
				this.chatslist.reset();
			}
			break;
		}
		case 15: { // gestionar los errores en el formulario para editar grupos
			if (arg.isPresent() && arg.get() instanceof Byte) {
				this.options.groupSettingsErrors((byte) arg.get());
			}
			break;
		}
		case 16: { // resetear la lista de grupos
			if (arg.isEmpty() && arg2.isEmpty()) {
				this.groupslist.reset();
			}
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + numMetodo);
		}
		
	}

}
