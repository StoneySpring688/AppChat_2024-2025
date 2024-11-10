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

    public void showMainPanel() {
        appFrame.resizeForMainPanel();
        appFrame.showMainPanel();
    }
    
    public void doLogin() {
        // Aquí podrías realizar una verificación del login
        // Si el login es exitoso, cambia al MainPanel
        showMainPanel(); // TODO aquí se procesa (o no) y se llama al MainController para que el haga las comprobaciones, y ya solicite mostrar el panel principal si hace falta
    }
}
