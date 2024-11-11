package umu.tds.AppChat.controllers;

public class MainController {
    private UIController uiController;
    
    //estados
    private Byte actualState;
    private final static Byte loggedOut = 0;
    private final static Byte loggedIn = 1;

    public MainController() {
        this.uiController = new UIController(this);
    }

    public void startApp() {
    	actualizarEstado(loggedOut);
        uiController.showLogin();
    }

    public void onLoginSuccess() {
    	//TODO hacer comprobaciones de las credenciales proporcionadas
    	actualizarEstado(loggedIn);
        uiController.showMainPanel();
    }
    
    public void actualizarEstado(Byte newState) {
    	this.actualState = newState;
    }
    
    public Byte getEstadoActual() {
    	return Byte.valueOf(this.actualState);
    }
}
