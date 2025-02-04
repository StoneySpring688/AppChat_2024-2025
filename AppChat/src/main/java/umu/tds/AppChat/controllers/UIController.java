package umu.tds.AppChat.controllers;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import umu.tds.AppChat.ui.chatInterface.ChatArea;
import umu.tds.AppChat.ui.chatInterface.ChatBox;
import umu.tds.AppChat.ui.chatInterface.ChatBox.BoxType;

public class UIController {
    private static AppFrame appFrame;

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
    	// TODO aquí se procesa (o no) y se llama al MainController para que el haga las comprobaciones, y ya solicite mostrar el panel principal si hace falta
    	showPanelIntermedio();
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
		System.out.println("[DEBUG] addGroup UIController");
		Grupo gp = MainController.getGrupo(id);
		System.out.println("[DEBUG] addGroup UIController 2 " + gp.getID());
		appFrame.llamarMetodo(6, Optional.empty(), Optional.of(gp));
	}
	
    public static void addMessage(ChatArea chat, ModelMessage MMsg, BoxType type) {
    	chat.addChatBox(MMsg, ChatBox.BoxType.RIGHT);
		chat.resetText();
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
    
}
