package umu.tds.AppChat.controllers;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;
import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.backend.utils.Usuario;

public class MainController {
    
    // estados
    private static Byte actualState;
    private final static byte loggedOut = 0;
    private final static byte loggedIn = 1;
    
    // gestión de hilos
    private static ExecutorService executor;

    public MainController() {
        
    }

    public static void startApp() {
    	actualizarEstado(loggedOut);
    	executor = new ThreadPoolExecutor(2, 20, 30, TimeUnit.SECONDS, new SynchronousQueue<>()); //min hilos, max hilos, tiempo hasta eliminar hilo, unidades, cola de espera
    	BackendController.iniciar();
    	UIController.iniciarUI();
    }
    
    public static void shutdownApp() {
    	executor.shutdown();
    	try {
			executor.awaitTermination(5, TimeUnit.SECONDS); // 5 seg de gracia para terminar tareas
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
    
    protected static void doLogin() {
    	// TODO hacer comprobaciones de las credenciales proporcionadas
    	onLoginSuccess();
    }
    
    protected static void doLogout() {
    	// TODO
    	actualizarEstado((byte) 0);
    }
    
    public static void onLoginSuccess() {
    	actualizarEstado(loggedIn);
    	BackendController.loadCurrentUser(new Usuario("StoneySpring", 111222333, null, null, "https://i.pinimg.com/736x/1c/ff/0b/1cff0b33f92ffd7f34f5cc80adbbf9af.jpg", null));//TODO cambiar a datos tras login
    	//UIController.showMainPanel();
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
    	// TODO escribir en la bd
    	executor.submit(() -> BackendController.nuevoMensaje(msg.getReciver(), msg));
    }
    
    protected static void loadChat(long chatID) {
    	// TODO Comprobar si está en caché y/o cargar de la bd de forma asincrona en lotes de 10
    	executor.submit(() -> UIController.loadChat(BackendController.getChat(chatID)));
    }
    
}
