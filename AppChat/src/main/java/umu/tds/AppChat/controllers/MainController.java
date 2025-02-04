package umu.tds.AppChat.controllers;

import java.util.List;

import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;

public class MainController {
    
    //estados
    private static Byte actualState;
    private final static Byte loggedOut = 0;
    private final static Byte loggedIn = 1;

    public MainController() {
        
    }

    public static void startApp() {
    	actualizarEstado(loggedOut);
    	BackendController.iniciar();
    	UIController.iniciarUI();
    }

    public static void onLoginSuccess() {
    	//TODO hacer comprobaciones de las credenciales proporcionadas
    	actualizarEstado(loggedIn);
    	UIController.showMainPanel();
    }
    
    public static void actualizarEstado(Byte newState) {
    	actualState = newState;
    }
    
    public static Byte getEstadoActual() {
    	return Byte.valueOf(actualState);
    }
    
    protected static boolean anyadirContacto(String numero, String nombre) {
    	
    	return BackendController.addContact(numero, nombre); //TODO comprobar con la persistencia
    	
    }
    
    protected static EntidadComunicable getContacto(int numero) {
    	return BackendController.getContacto(numero);
    }
    
    protected static List<EntidadComunicable> getListaContactos(){
    	return BackendController.getListaContactos();
    }
    
    protected static boolean doRegister(String nombre, String numero, String passwd, String birthDate, String url, String signature) {
    	return BackendController.doRegister(nombre, numero, passwd, birthDate, url, signature);
    }
    
    public static boolean makeGroup(String nombre, String profilepPicUrl, List<Integer> miembros) {
		return BackendController.makeGroup(nombre, profilepPicUrl, miembros);
    }
    
    protected static Grupo getGrupo(long id) {
    	return BackendController.getGrupo(id);
    }
    
    protected static List<Grupo> getGrupos(){
    	return BackendController.getGrupos();
    }
    
}
