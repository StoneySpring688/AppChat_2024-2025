package umu.tds.AppChat.controllers;

import java.awt.Image;
import java.util.Optional;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatDarculaLaf;
import umu.tds.AppChat.ui.AppFrame;
import umu.tds.AppChat.ui.ElementoChatOGrupo;

public class UIController {
    private MainController mainController;
    private AppFrame appFrame;

    public UIController(MainController mainController) {
        this.mainController = mainController;
        
        iniciarUI();
        
    }

    public void iniciarUI() {
    	UIManager.put( "Button.arc", 300 );
    	UIManager.put( "TextComponent.arc", 100 );
    	UIManager.put( "Component.arc", 100 );
    	FlatDarculaLaf.setup();
    	
    	this.appFrame = new AppFrame(this);
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
	public void crearGrupo() {
    	//TODO cargar la lista de contactos
    	DefaultListModel<ElementoChatOGrupo> lista = new DefaultListModel<>();
    	
    	for(int i=0 ;i<10 ;i++) {
			lista.addElement(new ElementoChatOGrupo("User" +(i+1), 648564523+(i+5), new ImageIcon(getClass().getResource("/assets/ProfilePic.png")),Optional.empty()));
		}
    	
    	appFrame.llamarMetodo(1, Optional.of(lista));
    	showPanelCrearGrupo();
    }
    
    //efectos
    public static void addHoverEffect(JButton button) {
	    ImageIcon originalIcon = (ImageIcon) button.getIcon();
	    ImageIcon scaledIcon = new ImageIcon(originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));

	    
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
