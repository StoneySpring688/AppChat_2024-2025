package umu.tds.AppChat.controllers;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;
import umu.tds.AppChat.backend.utils.Membership;
import umu.tds.AppChat.backend.utils.Membership.MembershipType;
import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.backend.utils.Usuario;

public class MainController {
    
	private UIController ui;
	
    // estados
    private static Byte actualState;
    private final static byte loggedOut = 0;
    private final static byte loggedIn = 1;
    
    // gestión de hilos
    private static ExecutorService executor;

	// singleton
	private static MainController unicaInstancia = null;
				
	public static MainController getUnicaInstancia() {
		unicaInstancia = unicaInstancia == null ? new MainController() : unicaInstancia;
		return unicaInstancia;
	}
    
    public MainController() {
        startApp(); 
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
    		return DAOController.registerUser(user.get());
    		
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
    
    protected static boolean editContact(String phone, String nombre) {
    	boolean success = true;
    	int number = 0;
    	
    	try {
		    number = Integer.parseInt(phone);
		} catch (NumberFormatException e) {
		    UIController.ContactSettingsErrors((byte) 1);
			success = false;
		}
    		
    	if(number != 0 && (int) (Math.log10(Math.abs(number)) + 1) != 9) {
    		UIController.ContactSettingsErrors((byte) 1);
    		success = false;
    	}if(nombre.length() == 0) {
    		UIController.ContactSettingsErrors((byte) 3);
    		success = false;
    	}else if(BackendController.getUserNumber() == number) {
    		UIController.ContactSettingsErrors((byte) 1);
    		success = false;
    	}else if(!BackendController.isContact(number)) {
    		UIController.ContactSettingsErrors((byte) 2);
    		success = false;
    	}if(nombre.length() == 0) {
    		UIController.ContactSettingsErrors((byte) 3);
    		success = false;
    	}
    	
    	if(success) {
    		
    		EntidadComunicable contactAux = BackendController.getContacto(number);
    		contactAux.setNombre(nombre);
    		BackendController.removeUser(number);
    		BackendController.addContact(contactAux);
    		DAOController.actualizarContacto(contactAux);
    	}
    	return success;
    }
    
    public static boolean removeContact(String numero) {
    	boolean success = true;
    	int number = 0;
    	
    	try {
		    number = Integer.parseInt(numero);
		} catch (NumberFormatException e) {
		    UIController.ContactSettingsErrors((byte) 1);
			success = false;
		}
    	
    	if(number != 0 && (int) (Math.log10(Math.abs(number)) + 1) != 9) {
    		UIController.ContactSettingsErrors((byte) 1);
    		success = false;
    	}else if(BackendController.getUserNumber() == number) {
    		UIController.ContactSettingsErrors((byte) 1);
    		success = false;
    	}else if(!BackendController.isContact(number)) {
    		UIController.ContactSettingsErrors((byte) 2);
    		success = false;
    	}
    	
    	if(success) {
    		EntidadComunicable contactAux = BackendController.getContacto(number);
    		BackendController.removeUser(number);
    		DAOController.removeContact(BackendController.getUserNumber(), contactAux);
    	}
    	
    	return success;
    	
    }
    
    public static boolean makeContactFromNoContact(EntidadComunicable noContacto) {
    	boolean success = true;
    	
    	Optional<EntidadComunicable> contactAux = DAOController.makeContactFromNoContact(noContacto);
    	success = contactAux.isPresent() ? true : false;
    	if(success) {
    		BackendController.makeContactFromNoContact(noContacto.getNumero());
    		BackendController.getContacto(noContacto.getNumero()).setId(contactAux.get().getId());
    		BackendController.getContacto(noContacto.getNumero()).setIsNoContact(false);
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
			
			// añadir al usuario como admin del grupo
			DAOController.addAdminToGrupo(newGrupo.getDBID(), BackendController.getUserNumber());
			BackendController.addAdminToGrupo(newGrupo.getID(), BackendController.getUserNumber());
			
			BackendController.getGrupo(gID).setDBID(newGrupo.getDBID());
			UIController.addGroup(gID);
		}
		
		return success;
		
    }
    
    protected static boolean editGroup(String urlProfileIcon, String nombreGrupo, List<Integer> miembros, long groupID) {
    	boolean success = true;
    		
    	if(nombreGrupo.length() == 0) {
    		UIController.groupSettingsErrors((byte) 1);
    		success = false;
    	}if(miembros.size() < 1) {
    		UIController.groupSettingsErrors((byte)2);
    		success = false;
		}
    	
    	if(success) {
    		
    		// actualizar lista de usuarios
    		Grupo grupoAux = BackendController.getGrupo(groupID);
    		List<EntidadComunicable> listAux = grupoAux.getIntegrantes(); // lista anterior de integrantes
    		List<Integer> eliminados = (listAux.stream() // lista de usuarios que ya no forman parte del grupo
    			    .filter(e -> !miembros.contains(e.getNumero()))
    			    .map(EntidadComunicable::getNumero)
    			    .collect(Collectors.toList()));
    		
    		List<EntidadComunicable> nuevosUsuarios = (listAux.stream() // lista sin los usurios eliminados
    			    .filter(e -> !eliminados.contains(e.getNumero()))
    			    .collect(Collectors.toList()));
    		
    		List<Integer> usersToAdd = (miembros.stream() // lista de miembros a añadir
    				.filter(e -> !nuevosUsuarios.stream().map(EntidadComunicable::getNumero).collect(Collectors.toList()).contains(e))
    				.collect(Collectors.toList()));
    		
    		for(int numero : usersToAdd) {
    			Usuario userAux = DAOController.recuperarUser(numero).get();
				EntidadComunicable entAux = new EntidadComunicable(userAux);
				nuevosUsuarios.add(DAOController.addMiembroToGrupo(BackendController.getGrupo(groupID).getDBID(), entAux));
				BackendController.addMiembroToGrupo(groupID, entAux);
    		}
    		
    		for(int numero : eliminados) {
    			EntidadComunicable contactAux = listAux.stream().filter(e -> e.getNumero() == numero).findFirst().get();
    			//System.out.println(contactAux.getNumero() + "," + contactAux.getId());
    			DAOController.removeMiembroFromGrupo(BackendController.getGrupo(groupID).getDBID(), contactAux);
    			BackendController.removeMiembroFromGrupo(groupID, contactAux);
    		}
    		
    		// actualizar nombre e imagen
    		BackendController.getGrupo(groupID).setIconUrl(urlProfileIcon);
    		BackendController.getGrupo(groupID).setNombre(nombreGrupo);
    		//BackendController.getGrupo(groupID).setIntegrantes(listAux);
    		DAOController.actualizarGrupo(BackendController.getGrupo(groupID));
    		
    	}
    	return success;
    }
    
    public static void removeGroup(Grupo group) {
    	//System.out.println("[DEBUG]" + " MainController " + "remove group : " + group.getDBID());
    	DAOController.removeGroup(group);
    	BackendController.removeGrupo(group.getID());
    }
    
    protected static boolean leaveGroup(Grupo group) {
    	boolean success = false;
    	if(group.getIntegrantes().size() > 2) {
    		success = true;
    		
	    	EntidadComunicable user = group.getIntegrantes().stream().filter(e  -> e.getNumero() == BackendController.getUserNumber()).findFirst().get();
	    	DAOController.removeMiembroFromGrupo(group.getDBID(), user);
	    	//DAOController.eliminarGrupoFromUser(BackendController.getUserNumber(), group);
	    	
	    	// no hace falta porque es la lista del usuario BackendController.removeMiembroFromGrupo(group.getID(), user);
	    	BackendController.removeGrupo(group.getID());
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
    		//System.out.println("[DEBUG]" + " MainController" + " añaddiendo mensaje a grupo : " + groupBDID);
    		DAOController.sendMessageToGroup(msg, groupBDID);
    	}else {
    		DAOController.sendMessageToContact(msg);
    	}
    	
    	executor.submit(() -> { BackendController.nuevoMensaje(msg.getReciver(), msg);});
    }
    
    protected static void loadChat(Optional<EntidadComunicable> contacto, Optional<Grupo> grupo) {
    	
    	//System.out.println("[DEBUG]" + " MainController" + " cargando mensajes");
    	
        executor.submit(() -> {
        	if (contacto.isPresent()) {
                List<ModelMessage> listaCaché = BackendController.getChat((long) contacto.get().getNumero());
                Optional<Integer> lastMsgId = listaCaché.isEmpty() ? Optional.empty() : Optional.of(listaCaché.get(listaCaché.size() - 1).getBDID());
                
                int startLote = 0;
                List<ModelMessage> lista = DAOController.getMessageFromAChat(contacto.get(), 0, lastMsgId);
                
                if (lastMsgId.isPresent() && lista.get(0).getBDID() != lastMsgId.get()) {
                	
                    startLote = lista.size();
                    List<ModelMessage> listaCachéAux = new ArrayList<>();
                    
                    while (!lista.isEmpty() && lista.get(0).getBDID() != lastMsgId.get()) {
                    	
                        listaCachéAux.addAll(0, lista); // Agregar nuevos mensajes al inicio para mantener orden
                        lista = DAOController.getMessageFromAChat(contacto.get(), startLote, lastMsgId);
                        startLote += lista.size();
                    }
                    
                    listaCachéAux.addAll(0, lista); // Añadir el último conjunto de mensajes
                    listaCachéAux.remove(0); // Eliminar colisión
                    
                    lastMsgId = Optional.empty(); // ya se han restaurado ppor lo tanto ya no debe tener valor asignado
                    
                    BackendController.nuevosMensajes((long) contacto.get().getNumero(), listaCachéAux); // Guardar los nuevos mensajes en la caché del BackendController
                    
                }else if(lastMsgId.isEmpty()) {
                	BackendController.nuevosMensajes((long) contacto.get().getNumero(), lista);
                }
                lastMsgId = Optional.empty();
                List <ModelMessage> mensajesCache = BackendController.getChat(contacto.get().getNumero());
                
                UIController.loadChat(mensajesCache);
                startLote = mensajesCache.size();
                
                lista = DAOController.getMessageFromAChat(contacto.get(), startLote, lastMsgId);
                BackendController.nuevosMensajesAlInicio((long) contacto.get().getNumero(), lista);
                
                startLote += lista.size();
                
                UIController.loadChat(lista);
                
                while(UIController.getActualChatOptimization() == (long)contacto.get().getNumero() && lista.size() > 0) {
                	
                	lista = DAOController.getMessageFromAChat(contacto.get(), startLote, lastMsgId);
                	startLote += lista.size();
                	
                	BackendController.nuevosMensajesAlInicio((long) contacto.get().getNumero(), lista);
                	UIController.loadChat(lista);
                }
                
            }else if(grupo.isPresent()) {
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
                 	BackendController.nuevosMensajes( grupo.get().getID(), lista);
                 }
                 lastMsgId = Optional.empty();
                 List <ModelMessage> mensajesCache = BackendController.getChat(grupo.get().getID());
                 
                 UIController.loadChat(mensajesCache);
                 startLote = mensajesCache.size();
                 
                 lista = DAOController.getMessageFromAGroup(grupo.get(), startLote, lastMsgId);
                 BackendController.nuevosMensajesAlInicio((long) grupo.get().getID(), lista);
                 
                 startLote += lista.size();
                 UIController.loadChat(lista);
                 
                 while(UIController.getActualChatOptimization() == (long)grupo.get().getID() && lista.size() > 0) { // OJO el id del grupo es distinto del id que le asigna la BD (grupo.getDBID)
                 	
                	lista = DAOController.getMessageFromAGroup(grupo.get(), startLote, lastMsgId);
                 	startLote += lista.size();
                 	
                 	BackendController.nuevosMensajesAlInicio((long) grupo.get().getID(), lista);
                 	UIController.loadChat(lista);
                 }
            }
        });
    }
    
    protected static List<ModelMessage> doSearch(int num, String contact, String msg) {
    	
    	boolean f1 = false;
    	boolean f2 = false;
    	
    	//List<ModelMessage> msgs = new ArrayList<ModelMessage>(); // lista de mensajes que coinciden con los filtros
    	LinkedHashSet<ModelMessage> msgs = new LinkedHashSet<>();
    	
    	List<EntidadComunicable> contactos = BackendController.getListaContactos();
    	List<EntidadComunicable> noContactos = BackendController.getListaNoContactos();
    	List<Grupo> grupos = BackendController.getGrupos();
    	
    	EntidadComunicable ent = null;
    	EntidadComunicable ent2 = null;
    	List<Grupo> gruposPertenece = new ArrayList<Grupo>();
    	
    	// filtro numero
    	if(num!=0 && BackendController.getUserNumber() == num) {
    		ent = BackendController.getCurrentUser();
    		f1 = true;
    	}else if(num!=0 && BackendController.isContact(num)) {
    		ent = BackendController.getContacto(num);
    	}else if(num!=0 && BackendController.isNoContact(num)) {
    		ent = BackendController.getNoContacto(num);
    	}if(num!=0 && f1) {
    		for(Grupo grupo : BackendController.getGrupos()) {
    			Optional<EntidadComunicable> aux = grupo.getIntegrantes().stream().filter(e -> e.getNumero() == num).findFirst();
        		if(aux.isPresent()) gruposPertenece.add(grupo);
    		}
    	}else if(num!=0 && !f1 && ent != null) {
    		for(Grupo grupo : DAOController.getListaGrupos(ent.getNumero())) {
    			Optional<EntidadComunicable> aux = grupo.getIntegrantes().stream().filter(e -> e.getNumero() == num).findFirst();
        		if(aux.isPresent()) gruposPertenece.add(grupo);
    		}
    	}
    	
    	// filtro contacto
    	if(contact != null && BackendController.getCurrentUser().getNombre().equals(contact)) {
    		ent2 = BackendController.getCurrentUser();
    		f2 = true;
    	}else if(contact != null) {
    		Optional<EntidadComunicable> aux = contactos.stream().filter(e -> e.getNombre().equals(contact)).findFirst();
    		ent2 = aux.isEmpty() ? null : aux.get();
    	}if(contact != null && ent2 == null) {
    		Optional<EntidadComunicable> aux = noContactos.stream().filter(e -> e.getNombre().equals(contact)).findFirst();
    		ent2 = aux.isEmpty() ? null : aux.get();
    	}if(contact != null && f2) {
    		for(Grupo grupo : BackendController.getGrupos()) {
    			Optional<EntidadComunicable> aux = grupo.getIntegrantes().stream().filter(e -> e.getNombre().equals(contact)).findFirst();
        		if(aux.isPresent()) gruposPertenece.add(grupo);
    		}
    	}else if(contact != null && !f2 && ent2 != null) {
    		for(Grupo grupo : DAOController.getListaGrupos(ent2.getNumero())) {
    			Optional<EntidadComunicable> aux = grupo.getIntegrantes().stream().filter(e -> e.getNombre().equals(contact)).findFirst();
        		if(aux.isPresent()) gruposPertenece.add(grupo);
    		}
    	}
    	
    	//System.out.println("[DEBUG]" + " MainController " + "ent2 : " + ent2.getNumero() + " numGrupos : " + gruposPertenece.size());
    	
    	// tercer filtro
    	if(msg != null) {
       		if(ent != null && ent2 != null && ent.getNumero() == ent2.getNumero()) { //  entidades coinciden
    			if(ent.getNumero() == BackendController.getUserNumber()) { // en el caso de los usuarios buscamos por todos sus chats
    				for(EntidadComunicable e : contactos) {msgs.addAll(DAOController.getAllMsgsFromAChat(e, true, Optional.of(msg)));}
    				for(EntidadComunicable e : noContactos) {msgs.addAll(DAOController.getAllMsgsFromAChat(e, false, Optional.of(msg)));}
    			}else {
    				msgs.addAll(DAOController.getAllMsgsFromAChat(ent, BackendController.isContact(ent.getNumero()), Optional.of(msg)));
    			}
    		}else { // entidades no coinciden
    			if(ent != null && ent.getNumero() == BackendController.getUserNumber()) { // en el caso de los usuarios buscamos por todos sus chats
    				for(EntidadComunicable e : contactos) {msgs.addAll(DAOController.getAllMsgsFromAChat(e, true, Optional.of(msg)));}
    				for(EntidadComunicable e : noContactos) {msgs.addAll(DAOController.getAllMsgsFromAChat(e, false, Optional.of(msg)));}
    			}else if(ent != null){
    				msgs.addAll(DAOController.getAllMsgsFromAChat(ent, BackendController.isContact(ent.getNumero()), Optional.of(msg)));
    			}
    			if(ent2 != null && ent2.getNumero() == BackendController.getUserNumber()) { // en el caso de los usuarios buscamos por todos sus chats
    				for(EntidadComunicable e : contactos) {msgs.addAll(DAOController.getAllMsgsFromAChat(e, true, Optional.of(msg)));}
    				for(EntidadComunicable e : noContactos) {msgs.addAll(DAOController.getAllMsgsFromAChat(e, false, Optional.of(msg)));}
    			}else if(ent2 != null) {
    				msgs.addAll(DAOController.getAllMsgsFromAChat(ent2, BackendController.isContact(ent.getNumero()), Optional.of(msg)));
    			}
    		}if(!gruposPertenece.isEmpty()) {
    			int n1 = ent != null ? ent.getNumero() : 0;
    			int n2  = ent2 != null ? ent2.getNumero() : 0;
    			for(Grupo grupo : grupos) msgs.addAll(DAOController.getAllMsgsFromAGroup(grupo, Optional.of(msg)).stream()
    					.filter(e -> e.getSender() == n1 || e.getSender() == n2).toList());
    		}
    	}else {
    		if(ent != null && ent2 != null && ent.getNumero() == ent2.getNumero()) { //  entidades coinciden
    			if(ent.getNumero() == BackendController.getUserNumber()) { // en el caso de los usuarios buscamos por todos sus chats
    				for(EntidadComunicable e : contactos) {msgs.addAll(DAOController.getAllMsgsFromAChat(e, true, Optional.empty()));}
    				for(EntidadComunicable e : noContactos) {msgs.addAll(DAOController.getAllMsgsFromAChat(e, false, Optional.empty()));}
    			}else {
    				msgs.addAll(DAOController.getAllMsgsFromAChat(ent, BackendController.isContact(ent.getNumero()), Optional.empty()));
    			}
    		}else { // entidades no coinciden
    			if(ent != null && ent.getNumero() == BackendController.getUserNumber()) { // en el caso de los usuarios buscamos por todos sus chats
    				for(EntidadComunicable e : contactos) {msgs.addAll(DAOController.getAllMsgsFromAChat(e, true, Optional.empty()));}
    				for(EntidadComunicable e : noContactos) {msgs.addAll(DAOController.getAllMsgsFromAChat(e, false, Optional.empty()));}
    			}else if(ent != null){
    				msgs.addAll(DAOController.getAllMsgsFromAChat(ent, BackendController.isContact(ent.getNumero()), Optional.empty()));
    			}
    			if(ent2 != null && ent2.getNumero() == BackendController.getUserNumber()) { // en el caso de los usuarios buscamos por todos sus chats
    				for(EntidadComunicable e : contactos) {msgs.addAll(DAOController.getAllMsgsFromAChat(e, true, Optional.empty()));}
    				for(EntidadComunicable e : noContactos) {msgs.addAll(DAOController.getAllMsgsFromAChat(e, false, Optional.empty()));}
    			}else if(ent2 != null){
    				msgs.addAll(DAOController.getAllMsgsFromAChat(ent2, BackendController.isContact(ent2.getNumero()), Optional.empty()));
    			}
    		}if(!gruposPertenece.isEmpty()) {
    			int n1 = ent != null ? ent.getNumero() : 0;
    			int n2 = ent2 != null ? ent2.getNumero() : 0;
    			//System.out.println("[DEBUG]" + " MainController " + "n1 : " + n1 + " n2 : " + n2);
    			
    			for(Grupo grupo : grupos) msgs.addAll(DAOController.getAllMsgsFromAGroup(grupo, Optional.empty()).stream()
    					.filter(e -> e.getSender() == n1 || e.getSender() == n2).toList());
    		}
    	}
    	
    	return msgs.stream().distinct().toList();

    	
    }
    
    // ### PDF
    
    public static String makePDF() {
        if (!BackendController.currentUserIsPremium()) {
            System.out.println("[ERROR] No tienes acceso a esta función. Debes ser usuario Premium.");
            return null;
        }
        return BackendController.exportarDatosPDF();
    }

    
}
