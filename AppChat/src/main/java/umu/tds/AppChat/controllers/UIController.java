package umu.tds.AppChat.controllers;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatDarculaLaf;
import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;
import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.backend.utils.Membership.MembershipType;
import umu.tds.AppChat.ui.AppFrame;
import umu.tds.AppChat.ui.ElementoChatOGrupo;
import umu.tds.AppChat.ui.MainPanel;
import umu.tds.AppChat.ui.MessageList;
import umu.tds.AppChat.ui.chatInterface.ChatBox.BoxType;

public class UIController {
    private AppFrame appFrame;
    private long actualChatOptimization = 0; // evitar usar el método cambiarChat para el chatActual
    
    // singleton
 	private static UIController unicaInstancia = null;
 			
 	public static UIController getUnicaInstancia() {
 		unicaInstancia = unicaInstancia == null ? new UIController() : unicaInstancia;
 		return unicaInstancia;
 	}

    private UIController() {  
    	// iniciarUI(); evita bucle
    }

    public void iniciarUI() {
    	UIManager.put( "Button.arc", 300 );
    	UIManager.put( "TextComponent.arc", 100 );
    	UIManager.put( "Component.arc", 100 );
    	FlatDarculaLaf.setup();
    	
    	appFrame = new AppFrame();
    	
    	showLogin();
    }
    
    public void setActualChatOptimization(long actualChat) {
    	actualChatOptimization = actualChat;
    }
    
    public long getActualChatOptimization() {
    	return actualChatOptimization;
    }
    
    // ### show methods
    public void showLogin() {
        appFrame.showLoginPanel();
        appFrame.setVisible(true);
    }
    
    public void showRegister() {
    	appFrame.showRegisterPanel();
    }

    public void showMainPanel() {
        appFrame.showMainPanel();
    }
    
    public void prepareMainPanel() {
        appFrame.setUserInfo(BackendController.getUnicaInstancia().getUserName(), BackendController.getUnicaInstancia().getUserIcon() );
        addChats();
        addGroups();
        actualizarPremiumExpireDate();
        appFrame.llamarMetodo(13, Optional.of(BackendController.getUnicaInstancia().getOfertas()), Optional.empty()); // cargar ofertas    
    }
    
    public void showPanelIntermedio() {
    	appFrame.showPanelIntermedio();
    }
    
    public void showPanelAnyadirContacto() {
    	appFrame.showAnyadirContactoPanel();
    }
    
    public void showPanelCrearGrupo() {
    	List<EntidadComunicable> lista = MainController.getUnicaInstancia().getListaContactos() ;
    	appFrame.llamarMetodo(1, Optional.empty(), Optional.of(lista));
    	appFrame.showCrearGrupoPanel();
    }
    
    public void showSettingsPanel() {
    	MainPanel.getUnicaInstancia().showSettings(MainController.getUnicaInstancia().getListaContactos(), MainController.getUnicaInstancia().getGrupos(),BackendController.getUnicaInstancia().currentUserIsPremium());
    	//OptionsPane.getUnicaInstancia().prepareOptions(MainController.getListaContactos(), MainController.getGrupos(), BackendController.currentUserIsPremium());
    	//appFrame.llamarMetodo(14, Optional.of(MainController.getGrupos()), Optional.of(MainController.getListaContactos()));
    }
    
    // ### gestión errores
    
 	public void registerErrors(byte code) {
 		//System.out.println("[DEBUG] registerErrors" + code);
     	appFrame.llamarMetodo(3, Optional.of((byte) code), Optional.empty());
     }
 	
 	public void loginErrors(byte code) {
 		appFrame.loginErrors(code);
 	}
     
     public void addContactErrors(byte code) {
     	appFrame.llamarMetodo(4, Optional.of((byte) code), Optional.empty());
     }
     
     public void makeGroupErrors(byte code) {
      	appFrame.llamarMetodo(5, Optional.of((byte) code), Optional.empty());
      }
     
     public void ContactSettingsErrors(byte code) {
    	 appFrame.llamarMetodo(15, Optional.of((byte) code), Optional.empty());
     }
     
     public void groupSettingsErrors(byte code) {
    	 appFrame.llamarMetodo(17, Optional.of((byte) code), Optional.empty());
     }
    
     // ### registro
     
    public void doRegister(String nombre, String numero, String passwd, String birthDate, String url, String signature){
    	//System.out.println("[DEBUG] doRegister UIController");
    	if(MainController.getUnicaInstancia().doRegister(nombre, numero, passwd, birthDate, url, signature)) {
    		appFrame.registerReset();
    		showLogin();
    	}
    }
    
    // ### login/logout
    
    public void doLogin(int numero, String passwd) {
    	MainController.getUnicaInstancia().doLogin(numero, passwd);
    }
    
    public void onLoginSuccess() {
    	appFrame.loginReset();
    	showPanelIntermedio();
    	prepareMainPanel();
        appFrame.resizeForMainPanel();
        showMainPanel();
    }
    
    public void doLogout() {
    	setActualChatOptimization(0);
    	MainController.getUnicaInstancia().doLogout(); 
    	appFrame.resetMainPanel();
    	
    	showPanelIntermedio();
    	
    	appFrame.resizeForLoginPanel();
    	showLogin();
    }
    
    // ### add contacts and noContacts
    
    public void anyadirContacto() {
    	showPanelAnyadirContacto();
    }
    
	public boolean verificarContactoYAnyadirContacto(String numero, String nombre) {
    	return MainController.getUnicaInstancia().anyadirContacto(numero, nombre);
    }
	
	public boolean verificarContactoYEditarContacto(String phone, String nombreContacto) {
		if(phone.isBlank() || nombreContacto.isBlank()) return false;
		boolean success = MainController.getUnicaInstancia().editContact(phone, nombreContacto);
		appFrame.llamarMetodo(16, Optional.empty(), Optional.empty());
		addChats(); // actualizar la lista de chats con las ediciones
		return success;
	}
	
	public boolean removeContact(String numero) {
		if(numero.isBlank()) return false;
		boolean success = MainController.getUnicaInstancia().removeContact(numero);
		appFrame.llamarMetodo(16, Optional.empty(), Optional.empty());
		addChats(); // actualizar la lista de chats con las ediciones
		return success;
	}
    
	// ### groups
	
	public boolean verificarContactoYEditarGrupo(String urlProfileIcon, String nombreGrupo, List<Integer> miembros, long groupID) {
		if(urlProfileIcon.isBlank() || nombreGrupo.isBlank()) return false;
		boolean success = MainController.getUnicaInstancia().editGroup(urlProfileIcon, nombreGrupo, miembros, groupID);
		appFrame.llamarMetodo(18, Optional.empty(), Optional.empty());
		addGroups(); // actualizar la lista de chats con las ediciones
		return success;
	}
	
	public boolean leaveGroup(Grupo group) {
		boolean success = false;
		if(MainController.getUnicaInstancia().leaveGroup(group)) {
			success = true;
			appFrame.llamarMetodo(18, Optional.empty(), Optional.empty());
			addGroups();
		}
		return success;
	}
	
	public void removeGroup(Grupo group) {
		MainController.getUnicaInstancia().removeGroup(group);
		appFrame.llamarMetodo(18, Optional.empty(), Optional.empty());
		addGroups();
	}
	
	// ### chats
	
    public void addChat(EntidadComunicable ent) {
    	//EntidadComunicable ent = MainController.getContacto(number);
		appFrame.llamarMetodo(2, Optional.empty(), Optional.of(new EntidadComunicable(ent)));
    }
    
    public void addChats(){
    	for(EntidadComunicable ent : BackendController.getUnicaInstancia().getListaContactos()) {
    		//System.out.println("cuenta contacto");
    		addChat(ent);
    	}
    	for(EntidadComunicable ent : BackendController.getUnicaInstancia().getListaNoContactos()) {
    		//System.out.println("cuenta noContacto");
    		addChat(ent);
    	}
    }

    public boolean addContactFromNoContact(ElementoChatOGrupo noContact) {
    	boolean success = MainController.getUnicaInstancia().makeContactFromNoContact(noContact.getContacto().get());
    	if(success) {
    		noContact.getContacto().get().setIsNoContact(false);
    		appFrame.llamarMetodo(16, Optional.empty(), Optional.empty());
    		addChats();
    	}
    	
    	return success; 
    }
    
    // ### add groups
    
	public boolean makeGroup(String nombre, String profilepPicUrl, List<Integer> miembros) {
		return MainController.getUnicaInstancia().makeGroup(nombre, profilepPicUrl, miembros);
    }
	
	public void addGroup(long id) {
		Grupo gp = MainController.getUnicaInstancia().getGrupo(id);
		appFrame.llamarMetodo(6, Optional.empty(), Optional.of(gp));
	}
	
	public void addGroups() {
		for(Grupo grupo : BackendController.getUnicaInstancia().getGrupos()) {
			addGroup(grupo.getID());
		}
	}
	
	// ### message render
	
    public void renderMessage(List<ModelMessage> msgs) {
    	appFrame.llamarMetodo(8, Optional.of(BackendController.getUnicaInstancia().getUserNumber()), Optional.of(msgs));
    }
    
	// ### chats change and load algorithm
    
    public void changeChat(ElementoChatOGrupo chat) {
    	
    	if(actualChatOptimization != (chat.isGrupo() ? chat.getGroupID() : chat.getNumero())) {
    		setActualChatOptimization(chat.isGrupo() ? chat.getGroupID() : chat.getNumero());
        	appFrame.llamarMetodo(7, Optional.of(chat), Optional.empty());
        	appFrame.showChatPanel();
        	
        	//System.out.println("[DEBUG]" + " UIController" + " cambiando de chat");
        	
        	MainController.getUnicaInstancia().loadChat(chat.getContacto(), chat.getGrupo());
        	
    	}

    }
    
    protected void loadChat(List<ModelMessage> msgs) {
    		appFrame.llamarMetodo(9, Optional.of(BackendController.getUnicaInstancia().getUserNumber()), Optional.of(msgs));
    }
    
    // ### send messages
    
    public void sendMessage(long reciver, Optional<String> message, Optional<Integer> emoji) {
    	//System.out.println("[DEBUG]" + " UIController " + "sendMessage");
		SimpleDateFormat fechaAux = new SimpleDateFormat("dd/MM/yyyy, hh:mmaa");
		String fecha =fechaAux.format(new Date());
		String nombre = BackendController.getUnicaInstancia().getUserName();
        String urlText = BackendController.getUnicaInstancia().getUserIconUrl();
        URL url;
        ImageIcon icono = null;
        
		try {
			url = new URL(urlText);
			icono = new ImageIcon(url);
			
	        if (icono.getIconWidth() <= 0 && icono.getIconHeight() <= 0) { // Verifica si la imagen no es válida         
	            //System.out.println("Imagen inválida.");
	            icono = new ImageIcon(UIController.class.getResource("/assets/ProfilePic.png"));
	        }
	        
		} catch (MalformedURLException e) {
            icono = new ImageIcon(UIController.class.getResource("/assets/ProfilePic.png"));
		}
		
		int sender = BackendController.getUnicaInstancia().getUserNumber();
		ModelMessage msg;
		if(message.isPresent()) {
			msg = new ModelMessage(icono, nombre, fecha, sender, reciver, message, Optional.empty());
		}else {
			msg = new ModelMessage(icono, nombre, fecha, sender, reciver, Optional.empty(), emoji);
		}
		
    	MainController.getUnicaInstancia().sendMessage(msg);
    	List<ModelMessage> msgs = new ArrayList<ModelMessage>();
		msgs.add(msg);
		renderMessage(msgs);
    }
    
    // ### search messages methods
    
    public void previewMessage(ModelMessage msg) {
    	appFrame.llamarMetodo(10, Optional.of(msg), Optional.of(BackendController.getUnicaInstancia().getUserNumber() == msg.getSender() ? BoxType.RIGHT : BoxType.LEFT));
    }
    
    public void doSearch(int num, String contact, String msg) {
    	List<ModelMessage> list =  MainController.getUnicaInstancia().doSearch(num, contact, msg);
    	MessageList.getUnicaInstancia().reset();
    	MessageList.getUnicaInstancia().addMsgs(list);
    }
    
    // ### shop methods
    
    public void changeMembershipShop(double precio, MembershipType type) {
    	appFrame.llamarMetodo(12, Optional.of(precio), Optional.of(type));
    	appFrame.showShopPanel();
    }
    
    public void buyPremium() {
    	Optional<LocalDate> expireDate = BackendController.getUnicaInstancia().getEndPremium();
    	if(expireDate.isEmpty() || LocalDate.now().isAfter(expireDate.get())) {
    		MainController.getUnicaInstancia().makePremiumUser();
    		appFrame.showDefaultPanel();
    	}
    }
    
    public void actualizarPremiumExpireDate() {
    	appFrame.llamarMetodo(11, Optional.empty(), Optional.empty()); // actualizar la fecha de expiración del premium en ui
    }
    
    // ### PDF
    
    public void makePDF() {
        String ruta = MainController.getUnicaInstancia().makePDF();
        if (ruta != null) {
            System.out.println("[OK] PDF generado en: " + ruta);
        } else {
            System.out.println("[ERROR] No se pudo generar el PDF. Verifica si eres usuario Premium.");
        }
    }

    
    // ### efectos
    
    public void addHoverEffect(JButton button) {
	    ImageIcon originalIcon = (ImageIcon) button.getIcon();
	    ImageIcon scaledIcon = new ImageIcon(originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));

	    
	    button.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent evt) {
	            button.setIcon(scaledIcon);  // Cambia a icono más pequeño al pasar el ratón
	        }

	        @Override
	        public void mouseExited(MouseEvent evt) {
	            button.setIcon(originalIcon);  // Vuelve al icono original al salir
	        }
	    });
	}
	
	public void addHoverEffect(JLabel button, int xHover, int yHover) {
	    ImageIcon originalIcon = (ImageIcon) button.getIcon();
	    ImageIcon scaledIcon = new ImageIcon(originalIcon.getImage().getScaledInstance(xHover, yHover, Image.SCALE_SMOOTH));

	    
	    button.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent evt) {
	            button.setIcon(scaledIcon);  // Cambia a icono más pequeño al pasar el ratón
	        }

	        @Override
	        public void mouseExited(MouseEvent evt) {
	            button.setIcon(originalIcon);  // Vuelve al icono original al salir
	        }
	    });
	}
    
}
