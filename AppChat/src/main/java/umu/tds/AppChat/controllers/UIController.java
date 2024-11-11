package umu.tds.AppChat.controllers;

import umu.tds.AppChat.ui.AppFrame;

public class UIController {
    private MainController mainController;
    private AppFrame appFrame;

    public UIController(MainController mainController) {
        this.mainController = mainController;
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
    
}
