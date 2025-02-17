package umu.tds.AppChat.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;
import umu.tds.AppChat.backend.utils.Membership;
import umu.tds.AppChat.backend.utils.Membership.MembershipType;
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
    	DAOController.initAdapters();
    	BackendController.iniciar();
    	
    	ArrayList<Membership> ofertas = new ArrayList<Membership>(); // TODO esta es la parte quue se cargaría con información del servidor par reflejar este tipo de añadidos
    	ofertas.add(new Membership(MembershipType.STANDAR, "Standar", 9.99));
    	ofertas.add(new Membership(MembershipType.SPECIAL, "High Social Credit", 1.99));
    	ofertas.add(new Membership(MembershipType.CELEBRATION, "Snake Year", 4.99));
    	
    	BackendController.loadOfertas(ofertas);
    	
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
    
    // ### estados
    
    public static void actualizarEstado(Byte newState) {
    	actualState = newState;
    }
    
    public static Byte getEstadoActual() {
    	return Byte.valueOf(actualState);
    }    
    
    // ### registro
    
    protected static boolean doRegister(String nombre, String numero, String passwd, String birthDate, String url, String signature) {
    	//System.out.println("[DEBUG] haciendo registro");
    	Optional<Usuario> user = BackendController.doRegister(nombre, numero, passwd, birthDate, url, signature);
    	if(user.isPresent()) {
    		//System.out.println("[DEBUG] escribiendo usuario");
    		DAOController.registerUser(user.get());
    		return true;
    	}
    	return false;
    }
    
    // ### login/logout
    
    protected static void doLogin(int numero, String passwd) {
    	//Optional<Usuario> user = DAOController.recuperarUser(numero);
    	if(getEstadoActual() == loggedOut) {
        	if (!DAOController.isUser(numero)) {
        		UIController.loginErrors((byte)4);
        	}else if(!DAOController.checkLogin(numero, passwd)) {
        		UIController.loginErrors((byte)4);
        	}else {
        		onLoginSuccess(numero);
        	}
    	}

    }
    
    protected static void doLogout() {
    	if(getEstadoActual() == loggedIn) {
    		BackendController.doLogout();
        	actualizarEstado(loggedOut);
    	}
    	
    }
    
    public static void onLoginSuccess(int numero) {
    	actualizarEstado(loggedIn);
    	BackendController.loadCurrentUser(DAOController.recuperarUser(numero).get());
    	BackendController.loadContactList(DAOController.getListaContactos(numero));
    	BackendController.loadGroupList(DAOController.getListaGrupos(numero));
    	BackendController.loadNoContactList(DAOController.getListaNoContactos(numero));
    	UIController.onLoginSuccess();
    }
    
    // ### usuario
    
    protected static void makePremiumUser() {
    	BackendController.makePremiumUser();
    	DAOController.makePremium(BackendController.getCurrentUser());
    	UIController.actualizarPremiumExpireDate();
    }
    
    // ### contactos
    
    protected static boolean anyadirContacto(String numero, String nombre) {
    	
    	boolean success = true;
    	int number = 0;
    	
    	try {
		    number = Integer.parseInt(numero);
		} catch (NumberFormatException e) {
		    UIController.addContactErrors((byte) 1);
			success = false;
		}
    		
    	if(number != 0 && (int) (Math.log10(Math.abs(number)) + 1) != 9) {
    		UIController.addContactErrors((byte) 1);
    		success = false;
    	}if(nombre.length() == 0) {
    		UIController.addContactErrors((byte) 3);
    		success = false;
    	}else if(BackendController.getUserNumber() == number) {
    		UIController.addContactErrors((byte) 1);
    		success = false;
    	}else if(BackendController.isContact(number)) {
    		UIController.addContactErrors((byte) 2);
    		success = false;
    	}if(nombre.length() == 0) {
    		UIController.addContactErrors((byte) 3);
    		success = false;
    	}
    	
    	if(success) {
    		
    		EntidadComunicable contact =  new EntidadComunicable(number, nombre);
    		EntidadComunicable contacto = DAOController.addContact(contact);
        	
        	if(contacto != null) {
        		BackendController.addContact(contacto);
        		UIController.addChat(contacto);
        	}
    	}
    	return success;
    }
    
    protected static EntidadComunicable getContacto(int numero) {
    	return BackendController.getContacto(numero);
    }
    
    protected static List<EntidadComunicable> getListaContactos(){
    	return BackendController.getListaContactos();
    }
    
    // ### grupos
    
    public static boolean makeGroup(String nombre, String profilepPicUrl, List<Integer> miembros) {
    	
    	boolean success = true;
		
		if(nombre.isBlank() || nombre.isEmpty()) {
			UIController.makeGroupErrors((byte) 1);
			success = false;
		}if(miembros == null || miembros.isEmpty()) {
			UIController.makeGroupErrors((byte) 2);
			success = false;
		}
    	
		if(success) {
			List<EntidadComunicable> miembrosGrupo = new ArrayList<EntidadComunicable>();
			
			long gID = BackendController.makeGroup(nombre, profilepPicUrl, miembrosGrupo);
			Grupo newGrupo = DAOController.addGroup(new Grupo(gID, nombre, profilepPicUrl));
			
			for(int numero : miembros) { // hacer contactos para el grupo y añadirlos
				Usuario userAux = DAOController.recuperarUser(numero).get();
				EntidadComunicable entAux = new EntidadComunicable(userAux);
				miembrosGrupo.add(DAOController.addMiembroToGrupo(newGrupo.getDBID(), entAux));
				BackendController.addMiembroToGrupo(newGrupo.getID(), entAux);
			} 
			
			Usuario userAux = DAOController.recuperarUser(BackendController.getUserNumber()).get(); // anyadir al usuario al grupo
			EntidadComunicable entAux = new EntidadComunicable(userAux);
			miembrosGrupo.add(DAOController.addMiembroToGrupo(newGrupo.getDBID(), entAux));
			BackendController.addMiembroToGrupo(newGrupo.getID(), entAux);
			
			UIController.addGroup(gID);
		}
		
		return success;
		
    }
    
    protected static Grupo getGrupo(long id) {
    	return BackendController.getGrupo(id);
    }
    
    protected static List<Grupo> getGrupos(){
    	return BackendController.getGrupos();
    }
    
    // ### mensajes
    
    protected static void sendMessage(ModelMessage msg) {
    	
    	if(BackendController.isGroup(msg.getReciver())) {
    		int groupBDID = BackendController.getGrupo(msg.getReciver()).getDBID();
    		System.out.println("[DEBUG]" + " MainController" + " añaddiendo mensaje a grupo : " + groupBDID);
    		DAOController.sendMessageToGroup(msg, groupBDID);
    	}else {
    		DAOController.sendMessageToContact(msg);
    	}
    	
    	executor.submit(() -> { BackendController.nuevoMensaje(msg.getReciver(), msg);});
    }
    
    protected static void loadChat(Optional<EntidadComunicable> contacto, Optional<Grupo> grupo) {
    	
    	//System.out.println("[DEBUG]" + " MainController" + " cargando mensajes");
    	
        if (contacto.isPresent()) {
            List<ModelMessage> listaCaché = BackendController.getChat((long) contacto.get().getNumero());
            Optional<Integer> lastMsgId = listaCaché.isEmpty() ? Optional.empty() : Optional.of(listaCaché.get(listaCaché.size() - 1).getBDID());
            
            //System.out.println("[DEBUG]" + " MainController" + " es contacto");
            
            int startLote = 0;
            List<ModelMessage> lista = DAOController.getMessageFromAChat(contacto.get(), 0, lastMsgId);
            
            if (lastMsgId.isPresent() && lista.get(0).getBDID() != lastMsgId.get()) {
            	
            	//System.out.println("[DEBUG]" + " MainController" + " lastMsgLastId : " + lastMsgId);
            	
                startLote = lista.size();
                List<ModelMessage> listaCachéAux = new ArrayList<>();
                
                while (!lista.isEmpty() && lista.get(0).getBDID() != lastMsgId.get()) {
                	
                	//System.out.println("[DEBUG]" + "  MainController" + " cargando : " + lista.size() + " mensajes nuevos");
                	
                    listaCachéAux.addAll(0, lista); // Agregar nuevos mensajes al inicio para mantener orden
                    lista = DAOController.getMessageFromAChat(contacto.get(), startLote, lastMsgId);
                    startLote += lista.size();
                }
                
                //System.out.println("[DEBUG]" + " MainController" + " añadiendo el lote de la colisión");
                
                listaCachéAux.addAll(0, lista); // Añadir el último conjunto de mensajes
                listaCachéAux.remove(0); // Eliminar colisión
                
                
                lastMsgId = Optional.empty(); // ya se han restaurado ppor lo tanto ya no debe tener valor asignado
                //System.out.println("[DEBUG]" + " MainController" + " renderizando los mensajes nuevos");
                
                BackendController.nuevosMensajes((long) contacto.get().getNumero(), listaCachéAux); // Guardar los nuevos mensajes en la caché del BackendController
                //executor.submit(() -> UIController.renderMessage(listaCachéAux)); // Enviar los mensajes actualizados a la UI
                //System.out.println("[DEBUG]" + " MainController" + " nuevos mensajes cargados" + " ###################################");
                
            }else if(lastMsgId.isEmpty()) {
            	//System.out.println("[DEBUG]" + " MainController" + " cargando caché por primera vez" + " ###################################" );
            	BackendController.nuevosMensajesAlInicio((long) contacto.get().getNumero(), lista);
            }
            executor.submit(() -> UIController.loadChat(BackendController.getChat(contacto.get().getNumero())));
            
            lista = DAOController.getMessageFromAChat(contacto.get(), startLote, lastMsgId);
            
            //for(ModelMessage msg : lista) System.out.println("[DEBUG]" + "MainController" + " mensaje a cargar : " + '\n' + msg.toString()); 
            
            //BackendController.nuevosMensajesAlInicio((long) contacto.get().getNumero(), lista);
            //executor.submit(() -> UIController.loadChat(BackendController.getChat(contacto.get().getNumero())));
            
            //System.out.println("[DEBUG]" + " MainController" + " cargando mensajes");
            
            
            while(UIController.getActualChatOptimization() == (long)contacto.get().getNumero() && lista.size() > 0) {
            	
            	//System.out.println("[DEBUG]" + "  MainController" + " cargando : " + lista.size() + " mensajes");
            	//BackendController.nuevosMensajesAlInicio((long) contacto.get().getNumero(), lista);
            	//System.out.println("[DEBUG]" + " MainController" + " solicitando otro lote");
            	
            	lista = DAOController.getMessageFromAChat(contacto.get(), startLote, lastMsgId);
            	startLote += lista.size();
            	
            	//System.out.println("[DEBUG]" + " MainController" + " renderizando los mensajes");
            	
            	executor.submit(() -> UIController.loadChat(BackendController.getChat(contacto.get().getNumero())));
            }
            
            //System.out.println("[DEBUG]" + " MainController" + " carga finalizada" + " ###################################");
            
        }if(grupo.isPresent()) {
        	 List<ModelMessage> listaCaché = BackendController.getChat((long) grupo.get().getID());
             Optional<Integer> lastMsgId = listaCaché.isEmpty() ? Optional.empty() : Optional.of(listaCaché.get(listaCaché.size() - 1).getBDID());
             
             int startLote = 0;
             List<ModelMessage> lista = DAOController.getMessageFromAGroup(grupo.get(), 0, lastMsgId);
             
             if (lastMsgId.isPresent() && lista.get(0).getBDID() != lastMsgId.get()) {
             	
                 startLote = lista.size();
                 List<ModelMessage> listaCachéAux = new ArrayList<>();
                 
                 while (!lista.isEmpty() && lista.get(0).getBDID() != lastMsgId.get()) {
                 	
                     listaCachéAux.addAll(0, lista); // Agregar nuevos mensajes al inicio para mantener orden
                     lista = DAOController.getMessageFromAGroup(grupo.get(), startLote, lastMsgId);
                     startLote += lista.size();
                 }
                 
                 listaCachéAux.addAll(0, lista); // Añadir el último conjunto de mensajes
                 listaCachéAux.remove(0); // Eliminar colisión
                 
                 
                 lastMsgId = Optional.empty(); // ya se han restaurado ppor lo tanto ya no debe tener valor asignado
                 
                 BackendController.nuevosMensajes((long) grupo.get().getID(), listaCachéAux); // Guardar los nuevos mensajes en la caché del BackendController
                 
             }else if(lastMsgId.isEmpty()) {
             	BackendController.nuevosMensajesAlInicio((long) grupo.get().getID(), lista);
             }
             executor.submit(() -> UIController.loadChat(BackendController.getChat(grupo.get().getID())));
             
             lista = DAOController.getMessageFromAGroup(grupo.get(), startLote, lastMsgId);
             
             while(UIController.getActualChatOptimization() == (long)grupo.get().getID() && lista.size() > 0) { // OJO el id del grupo es distinto del id que le asigna la BD (grupo.getDBID)
             	
            	lista = DAOController.getMessageFromAGroup(grupo.get(), startLote, lastMsgId);
             	startLote += lista.size();
             	
             	executor.submit(() -> UIController.loadChat(BackendController.getChat(grupo.get().getID())));
             }
        }
    }


    
}
