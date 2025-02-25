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
import umu.tds.AppChat.ui.chatInterface.ChatBox.BoxType;

public class UIController {
    private static AppFrame appFrame;
    private static long actualChatOptimization = 0; // evitar usar el método cambiarChat para el chatActual

    public UIController() {  
            
    }

    public static void iniciarUI() {
    	UIManager.put( "Button.arc", 300 );
    	UIManager.put( "TextComponent.arc", 100 );
    	UIManager.put( "Component.arc", 100 );
    	FlatDarculaLaf.setup();
    	
    	appFrame = new AppFrame();
    	
    	UIController.showLogin();
    }
    
    public static void setActualChatOptimization(long actualChat) {
    	actualChatOptimization = actualChat;
    }
    
    public static long getActualChatOptimization() {
    	return actualChatOptimization;
    }
    
    // ### show methods
    public static void showLogin() {
        appFrame.showLoginPanel();
        appFrame.setVisible(true);
    }
    
    public static void showRegister() {
    	appFrame.showRegisterPanel();
    }

    public static void showMainPanel() {
        appFrame.showMainPanel();
    }
    
    public static void prepareMainPanel() {
        appFrame.setUserInfo(BackendController.getUserName(), BackendController.getUserIcon() );
        addChats();
        addGroups();
        actualizarPremiumExpireDate();
        appFrame.llamarMetodo(13, Optional.of(BackendController.getOfertas()), Optional.empty()); // cargar ofertas    
    }
    
    public static void showPanelIntermedio() {
    	appFrame.showPanelIntermedio();
    }
    
    public static void showPanelAnyadirContacto() {
    	appFrame.showAnyadirContactoPanel();
    }
    
    public static void showPanelCrearGrupo() {
    	List<EntidadComunicable> lista = MainController.getListaContactos() ;
    	appFrame.llamarMetodo(1, Optional.empty(), Optional.of(lista));
    	appFrame.showCrearGrupoPanel();
    }
    
    public static void showSettingsPanel() {
    	appFrame.llamarMetodo(14, Optional.of(MainController.getGrupos()), Optional.of(MainController.getListaContactos()));
    }
    
    // ### gestión errores
    
 	public static void registerErrors(byte code) {
 		//System.out.println("[DEBUG] registerErrors" + code);
     	appFrame.llamarMetodo(3, Optional.of((byte) code), Optional.empty());
     }
 	
 	public static void loginErrors(byte code) {
 		appFrame.loginErrors(code);
 	}
     
     public static void addContactErrors(byte code) {
     	appFrame.llamarMetodo(4, Optional.of((byte) code), Optional.empty());
     }
     
     public static void makeGroupErrors(byte code) {
      	appFrame.llamarMetodo(5, Optional.of((byte) code), Optional.empty());
      }
     
     public static void ContactSettingsErrors(byte code) {
    	 appFrame.llamarMetodo(15, Optional.of((byte) code), Optional.empty());
     }
    
     // ### registro
     
    public static void doRegister(String nombre, String numero, String passwd, String birthDate, String url, String signature){
    	//System.out.println("[DEBUG] doRegister UIController");
    	if(MainController.doRegister(nombre, numero, passwd, birthDate, url, signature)) {
    		appFrame.registerReset();
    		showLogin();
    	}
    }
    
    // ### login/logout
    
    public static void doLogin(int numero, String passwd) {
    	MainController.doLogin(numero, passwd);
    }
    
    public static void onLoginSuccess() {
    	appFrame.loginReset();
    	showPanelIntermedio();
    	prepareMainPanel();
        appFrame.resizeForMainPanel();
        showMainPanel();
    }
    
    public static void doLogout() {
    	setActualChatOptimization(0);
    	MainController.doLogout(); 
    	appFrame.resetMainPanel();
    	
    	showPanelIntermedio();
    	
    	appFrame.resizeForLoginPanel();
    	showLogin();
    }
    
    // ### add contacts and noContacts
    
    public static void anyadirContacto() {
    	showPanelAnyadirContacto();
    }
    
	public static boolean verificarContactoYAnyadirContacto(String numero, String nombre) {
    	return MainController.anyadirContacto(numero, nombre);
    }
	
	public static boolean verificarContactoYEditarContacto(String phone, String nombreContacto) {
		if(phone.isBlank() || nombreContacto.isBlank()) return false;
		boolean success = MainController.editContact(phone, nombreContacto);
		appFrame.llamarMetodo(16, Optional.empty(), Optional.empty());
		addChats(); // actualizar la lista de chats con las ediciones
		return success;
	}
	
	public static boolean removeContact(String numero) {
		if(numero.isBlank()) return false;
		boolean success = MainController.removeContact(numero);
		appFrame.llamarMetodo(16, Optional.empty(), Optional.empty());
		addChats(); // actualizar la lista de chats con las ediciones
		return success;
	}
    
	// ### chats
	
    public static void addChat(EntidadComunicable ent) {
    	//EntidadComunicable ent = MainController.getContacto(number);
		appFrame.llamarMetodo(2, Optional.empty(), Optional.of(new EntidadComunicable(ent)));
    }
    
    public static void addChats(){
    	for(EntidadComunicable ent : BackendController.getListaContactos()) {
    		//System.out.println("cuenta contacto");
    		addChat(ent);
    	}
    	for(EntidadComunicable ent : BackendController.getListaNoContactos()) {
    		//System.out.println("cuenta noContacto");
    		addChat(ent);
    	}
    }

    public static boolean addContactFromNoContact(ElementoChatOGrupo noContact) {
    	boolean success = MainController.makeContactFromNoContact(noContact.getContacto().get());
    	if(success) {
    		noContact.getContacto().get().setIsNoContact(false);
    		appFrame.llamarMetodo(16, Optional.empty(), Optional.empty());
    		addChats();
    	}
    	
    	return success; 
    }
    
    // ### add groups
    
	public static boolean makeGroup(String nombre, String profilepPicUrl, List<Integer> miembros) {
		return MainController.makeGroup(nombre, profilepPicUrl, miembros);
    }
	
	public static void addGroup(long id) {
		Grupo gp = MainController.getGrupo(id);
		appFrame.llamarMetodo(6, Optional.empty(), Optional.of(gp));
	}
	
	public static void addGroups() {
		for(Grupo grupo : BackendController.getGrupos()) {
			addGroup(grupo.getID());
		}
	}
	
	// ### message render
	
    public static void renderMessage(List<ModelMessage> msgs) {
    	appFrame.llamarMetodo(8, Optional.of(BackendController.getUserNumber()), Optional.of(msgs));
    }
    
	// ### chats change and load algorithm
    
    public static void changeChat(ElementoChatOGrupo chat) {
    	
    	if(actualChatOptimization != (chat.isGrupo() ? chat.getGroupID() : chat.getNumero())) {
    		setActualChatOptimization(chat.isGrupo() ? chat.getGroupID() : chat.getNumero());
        	appFrame.llamarMetodo(7, Optional.of(chat), Optional.empty());
        	appFrame.showChatPanel();
        	
        	//System.out.println("[DEBUG]" + " UIController" + " cambiando de chat");
        	
        	MainController.loadChat(chat.getContacto(), chat.getGrupo());
        	
    	}

    }
    
    protected static void loadChat(List<ModelMessage> msgs) {
    		appFrame.llamarMetodo(9, Optional.of(BackendController.getUserNumber()), Optional.of(msgs));
    }
    
    // ### send messages
    
    public static void sendMessage(long reciver, Optional<String> message, Optional<Integer> emoji) {
    	System.out.println("[DEBUG]" + " UIController " + "sendMessage");
		SimpleDateFormat fechaAux = new SimpleDateFormat("dd/MM/yyyy, hh:mmaa");
		String fecha =fechaAux.format(new Date());
		String nombre = BackendController.getUserName();
        String urlText = BackendController.getUserIconUrl();
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
		
		int sender = BackendController.getUserNumber();
		ModelMessage msg;
		if(message.isPresent()) {
			msg = new ModelMessage(icono, nombre, fecha, sender, reciver, message, Optional.empty());
		}else {
			msg = new ModelMessage(icono, nombre, fecha, sender, reciver, Optional.empty(), emoji);
		}
		
    	MainController.sendMessage(msg);
    	List<ModelMessage> msgs = new ArrayList<ModelMessage>();
		msgs.add(msg);
		renderMessage(msgs);
    }
    
    // ### search messages methods
    
    public static void previewMessage(ModelMessage msg) {
    	appFrame.llamarMetodo(10, Optional.of(msg), Optional.of(BackendController.getUserNumber() == msg.getSender() ? BoxType.RIGHT : BoxType.LEFT));
    }
    
    // ### shop methods
    
    public static void changeMembershipShop(double precio, MembershipType type) {
    	appFrame.llamarMetodo(12, Optional.of(precio), Optional.of(type));
    	appFrame.showShopPanel();
    }
    
    public static void buyPremium() {
    	Optional<LocalDate> expireDate = BackendController.getEndPremium();
    	if(expireDate.isEmpty() || LocalDate.now().isAfter(expireDate.get())) {
    		MainController.makePremiumUser();
    		appFrame.showDefaultPanel();
    	}
    }
    
    public static void actualizarPremiumExpireDate() {
    	appFrame.llamarMetodo(11, Optional.empty(), Optional.empty()); // actualizar la fecha de expiración del premium en ui
    }
    
    // ### efectos
    
    public static void addHoverEffect(JButton button) {
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
	
	public static void addHoverEffect(JLabel button, int xHover, int yHover) {
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
