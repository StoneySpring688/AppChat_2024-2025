package umu.tds.AppChat.controllers;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
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
import umu.tds.AppChat.ui.AppFrame;
import umu.tds.AppChat.ui.ElementoChatOGrupo;

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
    
 	public static void registerErrors(byte code) {
 		//System.out.println("[DEBUG] registerErrors" + code);
     	appFrame.llamarMetodo(3, Optional.of((byte) code), Optional.empty());
     }
     
     public static void addContactErrors(byte code) {
     	appFrame.llamarMetodo(4, Optional.of((byte) code), Optional.empty());
     }
     
     public static void makeGroupErrors(byte code) {
      	appFrame.llamarMetodo(5, Optional.of((byte) code), Optional.empty());
      }
    
    public static boolean doRegister(String nombre, String numero, String passwd, String birthDate, String url, String signature){
    	//System.out.println("[DEBUG] doRegister UIController");
    	return MainController.doRegister(nombre, numero, passwd, birthDate, url, signature);
    }
    
    public static void doLogin() {
    	MainController.doLogin(); // TODO aquí se procesa (o no) y se llama al MainController para que el haga las comprobaciones, y ya solicite mostrar el panel principal si hace falta
    	showPanelIntermedio();
    	prepareMainPanel();
        appFrame.resizeForMainPanel();
        showMainPanel();
    }
    
    public static void doLogout() {
    	//TODO gestionar el logout con el MainController
    	showPanelIntermedio();
    	appFrame.resizeForLoginPanel();
    	showLogin();
    }
    
    public static void anyadirContacto() {
    	showPanelAnyadirContacto();
    	
    }
    
	public static boolean verificarContactoYAnyadirContacto(String numero, String nombre) {
    	//TODO verificar los datos con la persistencia
    	return MainController.anyadirContacto(numero, nombre);

    }
    
    public static void addChat(int number) {
    	EntidadComunicable ent = MainController.getContacto(number);
		appFrame.llamarMetodo(2, Optional.empty(), Optional.of(ent));
    }
    
	public static boolean makeGroup(String nombre, String profilepPicUrl, List<Integer> miembros) {
		return MainController.makeGroup(nombre, profilepPicUrl, miembros);
    }
	
	public static void addGroup(long id) {
		Grupo gp = MainController.getGrupo(id);
		appFrame.llamarMetodo(6, Optional.empty(), Optional.of(gp));
	}
	
    public static void renderMessage(List<ModelMessage> msgs) {
    	appFrame.llamarMetodo(8, Optional.of(BackendController.getUserNumber()), Optional.of(msgs));
    }
    
    public static void changeChat(ElementoChatOGrupo chat) {
    	
    	if(actualChatOptimization != (chat.isGrupo() ? chat.getGroupID() : chat.getNumero())) {
    		actualChatOptimization = chat.isGrupo() ? chat.getGroupID() : chat.getNumero();
        	appFrame.llamarMetodo(7, Optional.of(chat), Optional.empty());
        	appFrame.showChatPanel();
        	MainController.loadChat(chat.isGrupo() ? chat.getGroupID() : chat.getNumero());
    	}

    }
    
    protected static void loadChat(List<ModelMessage> msgs) {
    		appFrame.llamarMetodo(9, Optional.of(BackendController.getUserNumber()), Optional.of(msgs));
    }
    
    public static void sendMessage(long reciver, Optional<String> message, Optional<Integer> emoji) {
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
	            icono = new ImageIcon(MainController.class.getResource("/assets/ProfilePic.png"));
	        }
	        
		} catch (MalformedURLException e) {
            icono = new ImageIcon(MainController.class.getResource("/assets/ProfilePic.png"));
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
     
    //efectos
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
