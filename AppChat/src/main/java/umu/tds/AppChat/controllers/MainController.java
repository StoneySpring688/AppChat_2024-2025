package umu.tds.AppChat.controllers;

import umu.tds.AppChat.backend.utils.EntidadComunicable;

public class MainController {
    private static UIController uiController;
    private static BackendController backendController;
    
    //estados
    private Byte actualState;
    private final static Byte loggedOut = 0;
    private final static Byte loggedIn = 1;

    public MainController() {
        uiController = new UIController();
        backendController = new BackendController();
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
    
    protected static boolean anyadirContacto(int numero, String nombre) {
    	
    	return BackendController.addContact(numero, nombre); //TODO comprobar con la persistencia
    	
    }
    
    protected static EntidadComunicable getContacto(int numero) {
    	return BackendController.getContacto(numero);
    }
}
