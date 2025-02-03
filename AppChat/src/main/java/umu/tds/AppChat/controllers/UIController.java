package umu.tds.AppChat.controllers;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;

import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.ui.AppFrame;
import umu.tds.AppChat.ui.ElementoChatOGrupo;
import umu.tds.AppChat.ui.chatInterface.ChatArea;
import umu.tds.AppChat.ui.chatInterface.ChatBox;
import umu.tds.AppChat.ui.chatInterface.ChatBox.BoxType;

public class UIController {
    private static AppFrame appFrame;

    public UIController() {  
        iniciarUI();     
    }

    public void iniciarUI() {
    	UIManager.put( "Button.arc", 300 );
    	UIManager.put( "TextComponent.arc", 100 );
    	UIManager.put( "Component.arc", 100 );
    	FlatDarculaLaf.setup();
    	
    	appFrame = new AppFrame(this);
    }
    
    
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
    
    public void showPanelIntermedio() {
    	appFrame.showPanelIntermedio();
    }
    
    public void showPanelAnyadirContacto() {
    	appFrame.showAnyadirContactoPanel();
    }
    
    public void showPanelCrearGrupo() {
    	appFrame.showAnyadirCrearGrupoPanel();
    }
    
    public static boolean doRegister(String nombre, String numero, String passwd, String birthDate, String url, String signature){
    	//System.out.println("[DEBUG] doRegister UIController");
    	return MainController.doRegister(nombre, numero, passwd, birthDate, url, signature);
    }
    
    @SuppressWarnings("unchecked")
	public static void registerErrors(byte code) {
		//System.out.println("[DEBUG] registerErrors" + code);
    	appFrame.llamarMetodo(3, Optional.of((byte) code), Optional.empty());
    }
    
    public void doLogin() {
    	// TODO aquí se procesa (o no) y se llama al MainController para que el haga las comprobaciones, y ya solicite mostrar el panel principal si hace falta
    	showPanelIntermedio();
        appFrame.resizeForMainPanel();
        showMainPanel();
    }
    
    public void doLogout() {
    	//TODO gestionar el logout con el MainController
    	showPanelIntermedio();
    	appFrame.resizeForLoginPanel();
    	showLogin();
    }
    
    public void anyadirContacto() {
    	showPanelAnyadirContacto();
    	
    }
    
    @SuppressWarnings("unchecked")
	public static boolean verificarContactoYAnyadirContacto(int numero, String nombre) {
    	//TODO verificar los datos con la persistencia
    	if(MainController.anyadirContacto(numero, nombre)) {
    		EntidadComunicable ent = MainController.getContacto(numero);
    		appFrame.llamarMetodo(2, Optional.empty(), Optional.of(ent));
    		return true;
    	} 
    	System.err.println("[ERROR] no se pudo añadir usuario");
    	return false;
    }
    
    @SuppressWarnings("unchecked")
	public void crearGrupo() {
    	//TODO cargar la lista de contactos
    	List<EntidadComunicable> lista = MainController.getListaContactos() ;
    	
    	appFrame.llamarMetodo(1, Optional.empty(), Optional.of(lista));
    	showPanelCrearGrupo();
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
