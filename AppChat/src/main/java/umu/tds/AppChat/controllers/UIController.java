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

import org.slf4j.Logger;

import com.formdev.flatlaf.FlatDarculaLaf;
import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;
import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.backend.utils.Membership.MembershipType;
import umu.tds.AppChat.devtools.LoggerUtil;
import umu.tds.AppChat.ui.AppFrame;
import umu.tds.AppChat.ui.ElementoChatOGrupo;
import umu.tds.AppChat.ui.MainPanel;
import umu.tds.AppChat.ui.MessageList;
import umu.tds.AppChat.ui.chatInterface.ChatBox.BoxType;

/**
 * Controlador de la interfaz gráfica de usuario.
 *
 * <p>Encargado de manejar la visualización de paneles,
 * reacciones de botones y efectos visuales.
 *
 * <p>Responsable de coordinar el flujo de información entre la vista
 * (UI) y los controladores del sistema.
 *
 * <p>Algunas de sus responsabilidades incluyen:
 * <ul>
 *   <li>Mostrar y cambiar entre pantallas.</li>
 *   <li>Manejar acciones del usuario (login, registro, añadir contactos...).</li>
 *   <li>Invocar métodos del controlador principal.</li>
 *   <li>Mostrar errores y mensajes contextuales.</li>
 * </ul>
 *
 * @author StoneySpring
 */

public class UIController {
    private AppFrame appFrame;
    private long actualChatOptimization = 0; // evitar usar el método cambiarChat para el chatActual
    
    // logger
    private static final Logger logger = LoggerUtil.getLogger(MainController.class);
    
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
    	logger.info("Iniciando UI.");

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
    	logger.info("Mostrando pantalla de login.");

        appFrame.showLoginPanel();
        appFrame.setVisible(true);
    }
    
    public void showRegister() {
    	logger.info("Mostrando pantalla de registro.");

    	appFrame.showRegisterPanel();
    }

    public void showMainPanel() {
    	logger.info("Mostrando panel principal.");

        appFrame.showMainPanel();
    }
    
    public void prepareMainPanel() {
    	logger.info("Preparando panel principal: configurando usuario, cargando chats y grupos.");

        appFrame.setUserInfo(BackendController.getUnicaInstancia().getUserName(), BackendController.getUnicaInstancia().getUserIcon() );
        addChats();
        addGroups();
        actualizarPremiumExpireDate();
        appFrame.llamarMetodo(13, Optional.of(BackendController.getUnicaInstancia().getOfertas()), Optional.empty()); // cargar ofertas    
    }
    
    public void showPanelIntermedio() {
    	logger.info("Mostrando panel intermedio.");

    	appFrame.showPanelIntermedio();
    }
    
    public void showPanelAnyadirContacto() {
    	logger.info("Mostrando panel para añadir contacto.");

    	appFrame.showAnyadirContactoPanel();
    }
    
    public void showPanelCrearGrupo() {
    	logger.info("Mostrando panel para crear un grupo.");

    	List<EntidadComunicable> lista = MainController.getUnicaInstancia().getListaContactos() ;
    	appFrame.llamarMetodo(1, Optional.empty(), Optional.of(lista));
    	appFrame.showCrearGrupoPanel();
    }
    
    public void showSettingsPanel() {
    	logger.info("Mostrando panel de configuración de usuario.");

    	MainPanel.getUnicaInstancia().showSettings(MainController.getUnicaInstancia().getListaContactos(), MainController.getUnicaInstancia().getGrupos(),
    											   BackendController.getUnicaInstancia().currentUserIsPremium());
    	//OptionsPane.getUnicaInstancia().prepareOptions(MainController.getListaContactos(), MainController.getGrupos(), BackendController.currentUserIsPremium());
    	//appFrame.llamarMetodo(14, Optional.of(MainController.getGrupos()), Optional.of(MainController.getListaContactos()));
    }
    
    // ### gestión errores
    
 	public void registerErrors(byte code) {
 		logger.warn("Error en registro detectado con código: {}", code);

 		//System.out.println("[DEBUG] registerErrors" + code);
     	appFrame.llamarMetodo(3, Optional.of((byte) code), Optional.empty());
     }
 	
 	public void loginErrors(byte code) {
 		logger.warn("Error en login detectado con código: {}", code);

 		appFrame.loginErrors(code);
 	}
     
     public void addContactErrors(byte code) {
    	logger.warn("Error al añadir contacto detectado con código: {}", code);
    	
     	appFrame.llamarMetodo(4, Optional.of((byte) code), Optional.empty());
     }
     
     public void makeGroupErrors(byte code) {
    	logger.warn("Error al crear grupo detectado con código: {}", code);
    	
      	appFrame.llamarMetodo(5, Optional.of((byte) code), Optional.empty());
      }
     
     public void ContactSettingsErrors(byte code) {
    	 logger.warn("Error en configuración de contacto detectado con código: {}", code);
    	 
    	 appFrame.llamarMetodo(15, Optional.of((byte) code), Optional.empty());
     }
     
     public void groupSettingsErrors(byte code) {
    	 logger.warn("Error en configuración de grupo detectado con código: {}", code);
    	 
    	 appFrame.llamarMetodo(17, Optional.of((byte) code), Optional.empty());
     }
    
     // ### registro
     
    public void doRegister(String nombre, String numero, String passwd, String birthDate, String url, String signature){
    	logger.debug("Procesando registro de usuario: {}", nombre);

    	//System.out.println("[DEBUG] doRegister UIController");
    	if(MainController.getUnicaInstancia().doRegister(nombre, numero, passwd, birthDate, url, signature)) {
    		appFrame.registerReset();
    		showLogin();
    	}
    }
    
    // ### login/logout
    
    public void doLogin(int numero, String passwd) {
    	logger.debug("Procesando login para número: {}", numero);

    	MainController.getUnicaInstancia().doLogin(numero, passwd);
    }
    
    public void onLoginSuccess() {
    	logger.debug("Login exitoso. Preparando panel principal.");

    	appFrame.loginReset();
    	showPanelIntermedio();
    	prepareMainPanel();
        appFrame.resizeForMainPanel();
        showMainPanel();
    }
    
    public void doLogout() {
    	logger.debug("Procesando logout de usuario.");

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
		boolean success = MainController.getUnicaInstancia().anyadirContacto(numero, nombre);
		logger.debug("Resultado de verificar y añadir contacto [{}]: {}", numero, success);

    	return success;
    }
	
	public boolean verificarContactoYEditarContacto(String phone, String nombreContacto) {
		if(phone.isBlank() || nombreContacto.isBlank()) return false;
		boolean success = MainController.getUnicaInstancia().editContact(phone, nombreContacto);
		appFrame.llamarMetodo(16, Optional.empty(), Optional.empty());
		addChats(); // actualizar la lista de chats con las ediciones
		logger.debug("Resultado de editar contacto [{}]: {}", phone, success);

		return success;
	}
	
	public boolean removeContact(String numero) {
		if(numero.isBlank()) return false;
		boolean success = MainController.getUnicaInstancia().removeContact(numero);
		appFrame.llamarMetodo(16, Optional.empty(), Optional.empty());
		addChats(); // actualizar la lista de chats con las ediciones
		logger.debug("Resultado de eliminar contacto [{}]: {}", numero, success);

		return success;
	}
    
	// ### groups
	
	public boolean verificarContactoYEditarGrupo(String urlProfileIcon, String nombreGrupo, List<Integer> miembros, long groupID) {
		if(urlProfileIcon.isBlank() || nombreGrupo.isBlank()) return false;
		boolean success = MainController.getUnicaInstancia().editGroup(urlProfileIcon, nombreGrupo, miembros, groupID);
		appFrame.llamarMetodo(18, Optional.empty(), Optional.empty());
		addGroups(); // actualizar la lista de chats con las ediciones
		logger.debug("Resultado de editar grupo [{}]: {}", groupID, success);
		return success;
	}
	
	public boolean leaveGroup(Grupo group) {
		boolean success = false;
		if(MainController.getUnicaInstancia().leaveGroup(group)) {
			success = true;
			appFrame.llamarMetodo(18, Optional.empty(), Optional.empty());
			addGroups();
		}
		logger.debug("Resultado de abandonar grupo [{}]: {}", group.getNombre(), success);

		return success;
	}
	
	public void removeGroup(Grupo group) {
		logger.debug("Eliminando grupo [{}]", group.getNombre());

		MainController.getUnicaInstancia().removeGroup(group);
		appFrame.llamarMetodo(18, Optional.empty(), Optional.empty());
		addGroups();
	}
	
	// ### chats
	
    public void addChat(EntidadComunicable ent) {
    	logger.debug("Añadiendo chat: {}", ent.getNumero());
    	
    	//EntidadComunicable ent = MainController.getContacto(number);
		appFrame.llamarMetodo(2, Optional.empty(), Optional.of(new EntidadComunicable(ent)));
    }
    
    public void addChats(){
    	logger.debug("Añadiendo todos los chats y grupos del usuario.");
    	
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
    	logger.debug("Haciendo contacto al noContacto: {}", noContact.getContacto().get().getNumero());
    	
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
		logger.debug("Haciendo grupo: {}", nombre);
		
		return MainController.getUnicaInstancia().makeGroup(nombre, profilepPicUrl, miembros);
    }
	
	public void addGroup(long id) {
		logger.debug("Añadiendo grupo: {}", id);
		
		Grupo gp = MainController.getUnicaInstancia().getGrupo(id);
		appFrame.llamarMetodo(6, Optional.empty(), Optional.of(gp));
	}
	
	public void addGroups() {
		logger.debug("Añadiendo todos los grupos del usuario.");
		
		for(Grupo grupo : BackendController.getUnicaInstancia().getGrupos()) {
			addGroup(grupo.getID());
		}
	}
	
	// ### message render
	
    public void renderMessage(List<ModelMessage> msgs) {
    	logger.debug("Renderizando {} mensajes", msgs.size());
    	
    	appFrame.llamarMetodo(8, Optional.of(BackendController.getUnicaInstancia().getUserNumber()), Optional.of(msgs));
    }
    
	// ### chats change and load algorithm
    
    public void changeChat(ElementoChatOGrupo chat) {
    	logger.debug("Cambiando a chat: {}", chat.getContacto().get().getNumero());
    	
    	if(actualChatOptimization != (chat.isGrupo() ? chat.getGroupID() : chat.getNumero())) {
    		setActualChatOptimization(chat.isGrupo() ? chat.getGroupID() : chat.getNumero());
        	appFrame.llamarMetodo(7, Optional.of(chat), Optional.empty());
        	appFrame.showChatPanel();
        	
        	//System.out.println("[DEBUG]" + " UIController" + " cambiando de chat");
        	
        	MainController.getUnicaInstancia().loadChat(chat.getContacto(), chat.getGrupo());
        	
    	}

    }
    
    protected void loadChat(List<ModelMessage> msgs) {
    	logger.debug("Cargando chat con {} mensajes", msgs.size());
    	
    	appFrame.llamarMetodo(9, Optional.of(BackendController.getUnicaInstancia().getUserNumber()), Optional.of(msgs));
    }
    
    // ### send messages
    
    public void sendMessage(long reciver, Optional<String> message, Optional<Integer> emoji) {
    	logger.debug("Enviando mensaje a [{}]: {}", reciver, message.orElse("Emoji : " + emoji.orElse(0)));
    	
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
    	logger.debug("Previsualizando mensaje: {}", msg.getMessage().orElse("Emoji : " + msg.getEmoji().orElse(0)));
    	
    	appFrame.llamarMetodo(10, Optional.of(msg), Optional.of(BackendController.getUnicaInstancia().getUserNumber() == msg.getSender() ? BoxType.RIGHT : BoxType.LEFT));
    }
    
    public void doSearch(int num, String contact, String msg) {
    	logger.debug("Buscando mensajes con número: {}, contacto: {}, mensaje: {}", num, contact, msg);
    	
    	List<ModelMessage> list =  MainController.getUnicaInstancia().doSearch(num, contact, msg);
    	MessageList.getUnicaInstancia().reset();
    	MessageList.getUnicaInstancia().addMsgs(list);
    }
    
    // ### shop methods
    
    public void changeMembershipShop(double precio, MembershipType type) {
    	logger.debug("Cambiando membresía en la tienda: precio {}, tipo {}", precio, type);
    	
    	appFrame.llamarMetodo(12, Optional.of(precio), Optional.of(type));
    	appFrame.showShopPanel();
    }
    
    public void buyPremium() {
    	logger.debug("Comprando membresía premium.");
    	
    	Optional<LocalDate> expireDate = BackendController.getUnicaInstancia().getEndPremium();
    	if(expireDate.isEmpty() || LocalDate.now().isAfter(expireDate.get())) {
    		MainController.getUnicaInstancia().makePremiumUser();
    		appFrame.showDefaultPanel();
    	}
    }
    
    public void actualizarPremiumExpireDate() {
    	logger.debug("Actualizando fecha de expiración del premium.");
    	
    	appFrame.llamarMetodo(11, Optional.empty(), Optional.empty()); // actualizar la fecha de expiración del premium en ui
    }
    
    // ### PDF
    
    public void makePDF() {
    	logger.debug("Generando PDF de mensajes.");
    	
        String ruta = MainController.getUnicaInstancia().makePDF();
        if (ruta != null) {
        	logger.info("[OK] PDF generado en: " + ruta);
        } else {
        	logger.error("[ERROR] No se pudo generar el PDF. Verifica si eres usuario Premium.");
        }
    }

    
    // ### efectos
    
    public void addHoverEffect(JButton button) {
    	logger.debug("Añadiendo efecto hover al botón: {}", button.getText());
    	
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
		logger.debug("Añadiendo efecto hover al JLabel: {}", button.getText());
		
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
