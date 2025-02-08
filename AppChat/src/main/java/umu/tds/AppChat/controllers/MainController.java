package umu.tds.AppChat.controllers;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;
import umu.tds.AppChat.backend.utils.ModelMessage;

public class MainController {
    
    // estados
    private static Byte actualState;
    private final static Byte loggedOut = 0;
    private final static Byte loggedIn = 1;
    
    // gestión de hilos
    private static ThreadPoolExecutor executor;

    public MainController() {
        
    }

    public static void startApp() {
    	actualizarEstado(loggedOut);
    	executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    	executor.setKeepAliveTime(30, TimeUnit.SECONDS);
    	BackendController.iniciar();
    	UIController.iniciarUI();
    }
    
    public static void shutdownApp() {
    	executor.shutdown();
    	try {
			executor.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

    }
    
    public static void actualizarEstado(Byte newState) {
    	actualState = newState;
    }
    
    public static Byte getEstadoActual() {
    	return Byte.valueOf(actualState);
    }    
    
    protected static boolean doRegister(String nombre, String numero, String passwd, String birthDate, String url, String signature) {
    	return BackendController.doRegister(nombre, numero, passwd, birthDate, url, signature);
    }

    public static void onLoginSuccess() {
    	//TODO hacer comprobaciones de las credenciales proporcionadas
    	actualizarEstado(loggedIn);
    	UIController.showMainPanel();
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
    
    public static boolean makeGroup(String nombre, String profilepPicUrl, List<Integer> miembros) {
		return BackendController.makeGroup(nombre, profilepPicUrl, miembros);
    }
    
    protected static Grupo getGrupo(long id) {
    	return BackendController.getGrupo(id);
    }
    
    protected static List<Grupo> getGrupos(){
    	return BackendController.getGrupos();
    }
    
    protected static void sendMessage(ModelMessage msg) {
    	executor.submit(() -> BackendController.nuevoMensaje(msg.getReciver(), msg));
    }
    
}
