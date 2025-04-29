package umu.tds.AppChat.ui;

import java.awt.Color;
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

    private static MainPanel unicaInstancia = null;

    public static MainPanel getUnicaInstancia() {
        unicaInstancia = unicaInstancia == null ? new MainPanel() : unicaInstancia;
        return unicaInstancia;
    }

    public MainPanel() {
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(null);

        crearPanelPrincipal();
        crearPanelMenu1();
        crearPanelBotonera();
        crearPanelPerfil();
        configurarColores();
    }

    private void crearPanelPrincipal() {
        principal = new JPanel();
        principal.setBounds(360, 0, 920, 720);
        add(principal);

        actualizadorUiPrincipal = new CardLayout();
        principal.setLayout(actualizadorUiPrincipal);

        PanelGrande PanelGrandePorDefecto = new PanelGrande();
        Background fondoPorDefecto = new Background();
        fondoPorDefecto.setBounds(0, 60, 920, 660);
        PanelGrandePorDefecto.add(fondoPorDefecto);

        searchPanel = new SearchPanel();
        shopPanel = new PremiumShopPanel();
        panelAnyadirContacto = new AddContactPanel();
        panelCrearGrupo = new CreateGroupPanel();
        chat = new ChatPanel();
        options = new OptionsPane();

        principal.add(PanelGrandePorDefecto, "porDefecto");
        principal.add(searchPanel, "search");
        principal.add(shopPanel, "shop");
        principal.add(panelAnyadirContacto, "anyadirContacto");
        principal.add(panelCrearGrupo, "crearGrupo");
        principal.add(chat, "chat");
        principal.add(options, "options");

        actualizadorUiPrincipal.show(principal, "porDefecto");
    }

    private void crearPanelMenu1() {
        panelMenu1 = new JPanel();
        panelMenu1.setBounds(120, 0, 240, 660);
        add(panelMenu1);

        actualizadorMenu1 = new CardLayout(0, 0);
        panelMenu1.setLayout(actualizadorMenu1);

        JPanel panelPorDefectoMenu1 = new JPanel();
        panelPorDefectoMenu1.setBackground(new Color(54, 57, 63));
        panelPorDefectoMenu1.setBounds(120, 0, 240, 660);
        panelMenu1.add(panelPorDefectoMenu1, "porDefecto");

        chatslist = new ChatsList();
        panelMenu1.add(chatslist, "chats");

        groupslist = new GroupsList();
        panelMenu1.add(groupslist, "groups");

        messageList = MessageList.getUnicaInstancia();
        panelMenu1.add(messageList, "messages");

        mshipsList = new MembershipList();
        panelMenu1.add(mshipsList, "mships");

        actualizadorMenu1.show(panelMenu1, "porDefecto");
    }

    private void crearPanelBotonera() {
        panelBotonera = new JPanel();
        panelBotonera.setBounds(0, 0, 120, 720);
        add(panelBotonera);
        panelBotonera.setLayout(null);

        buttonChats = crearBoton("/assets/UI_ChapterIcon_Wanderer.png", 0, e -> UIController.getUnicaInstancia().setActualChatOptimization(0));
        showMenu("porDefecto", buttonChats, actualizadorUiPrincipal, principal);
        showMenu("chats", buttonChats, actualizadorMenu1, panelMenu1);
        panelBotonera.add(buttonChats);

        buttonGroups = crearBoton("/assets/UI_ChapterIcon_Traveler.png", 120, e -> UIController.getUnicaInstancia().setActualChatOptimization(0));
        showMenu("porDefecto", buttonGroups, actualizadorUiPrincipal, principal);
        showMenu("groups", buttonGroups, actualizadorMenu1, panelMenu1);
        panelBotonera.add(buttonGroups);

        buttonShop = crearBoton("/assets/Ui_SystemOpenIcon_Shop.png", 240, e -> UIController.getUnicaInstancia().setActualChatOptimization(0));
        showMenu("porDefecto", buttonShop, actualizadorUiPrincipal, principal);
        showMenu("mships", buttonShop, actualizadorMenu1, panelMenu1);
        panelBotonera.add(buttonShop);

        buttonSearch = crearBoton("/assets/UI_ChapterIcon_Charlotte.png", 600, e -> UIController.getUnicaInstancia().setActualChatOptimization(0));
        showMenu("search", buttonSearch, actualizadorUiPrincipal, principal);
        showMenu("messages", buttonSearch, actualizadorMenu1, panelMenu1);
        panelBotonera.add(buttonSearch);
    }

    private Button crearBoton(String iconPath, int y, ActionListener action) {
        Button button = new Button();
        button.setIcon(new ImageIcon(getClass().getResource(iconPath)));
        button.setRound(100);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setBounds(10, y, 100, 100);
        button.setFocusPainted(false);
        UIController.getUnicaInstancia().addHoverEffect(button);
        button.addActionListener(action);
        return button;
    }

    private void crearPanelPerfil() {
        panelMenuPerfil = new JPanel(new MigLayout("align left, insets 5", "[][]20[]10[]"));
        panelMenuPerfil.setBounds(120, 660, 240, 60);
        add(panelMenuPerfil);

        yourAvatar = new ImageAvatar();
        yourAvatar.setImage(new ImageIcon(getClass().getResource("/assets/ProfilePic.png")));
        yourAvatar.setBorderSize(1);
        yourAvatar.setBorderSpace(1);
        panelMenuPerfil.add(yourAvatar, "cell 0 0, height 50, width 50");

        lblYourname = new JLabel("YourName");
        lblYourname.setForeground(Color.WHITE);
        panelMenuPerfil.add(lblYourname, "growx, wmax 100");

        lblLogout = new JLabel();
        lblLogout.setIcon(new ImageIcon(getClass().getResource("/assets/Exit_Door_Reescalada.png")));
        lblLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                UIController.getUnicaInstancia().doLogout();
            }
        });
        UIController.getUnicaInstancia().addHoverEffect(lblLogout, 30, 32);
        panelMenuPerfil.add(lblLogout, "cell 2 0, height 35, width 35");

        lblsettingGear = new JLabel();
        lblsettingGear.setIcon(new ImageIcon(getClass().getResource("/assets/SettingsGear.png")));
        lblsettingGear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                UIController.getUnicaInstancia().showSettingsPanel();
            }
        });
        UIController.getUnicaInstancia().addHoverEffect(lblsettingGear, 30, 30);
        panelMenuPerfil.add(lblsettingGear, "cell 3 0, height 35, width 35");
    }

    private void configurarColores() {
        setBackground(new Color(64, 68, 75));
        panelBotonera.setBackground(new Color(40, 43, 48));
        panelMenuPerfil.setBackground(new Color(47, 49, 54));
        panelMenu1.setBackground(new Color(54, 57, 63));
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
