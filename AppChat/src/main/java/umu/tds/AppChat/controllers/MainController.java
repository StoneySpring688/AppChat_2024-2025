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

import org.slf4j.Logger;

import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;
import umu.tds.AppChat.backend.utils.Membership;
import umu.tds.AppChat.backend.utils.Membership.MembershipType;
import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.backend.utils.Usuario;
import umu.tds.AppChat.devtools.LoggerUtil;

/**
 * Controlador principal de la aplicación.
 *
 * <p>Gestiona el ciclo de vida general de la app, incluyendo:
 * <ul>
 *   <li>Inicio y cierre de la aplicación.</li>
 *   <li>Autenticación (login/logout).</li>
 *   <li>Gestión de usuarios, contactos y grupos.</li>
 *   <li>Envío y recepción de mensajes.</li>
 *   <li>Exportación de historial en PDF.</li>
 * </ul>
 *
 * <p>Coordina la interacción entre:
 * <ul>
 *  <li>UIController</li>
 *  <li>BackendController</li>
 *  <li>DAOController</li>
 * </ul>
 * Siguiendo el patrón Singleton.
 *
 * @author StoneySpring
 */

public class MainController {
    
	private UIController ui;
	private BackendController backend;
	private DAOController dao;
	
    // estados
    private Byte actualState;
    private final byte loggedOut = 0;
    private final byte loggedIn = 1;
    
    // gestión de hilos
    private ExecutorService executor;
    
    // logger
    private static final Logger logger = LoggerUtil.getLogger(MainController.class);


	// singleton
	private static MainController unicaInstancia = null;
				
	public static MainController getUnicaInstancia() {
		unicaInstancia = unicaInstancia == null ? new MainController() : unicaInstancia;
		return unicaInstancia;
	}
    
    private MainController() {
    	ui = UIController.getUnicaInstancia();
    	backend = BackendController.getUnicaInstancia();
    	dao = DAOController.getUnicaInstancia();
        // startApp();
    }

	public void startApp() {
		logger.info("Iniciando aplicación - startApp()");


		actualizarEstado(loggedOut);
		executor = new ThreadPoolExecutor(2, 20, 30, TimeUnit.SECONDS, new SynchronousQueue<>()); //min hilos, max hilos, tiempo hasta eliminar hilo, unidades, cola de espera
		dao.initAdapters();
		backend.iniciar();
		
		ArrayList<Membership> ofertas = new ArrayList<Membership>(); // TODO esta es la parte quue se cargaría con información del servidor par reflejar este tipo de añadidos
		ofertas.add(new Membership(MembershipType.STANDAR, "Standar", 9.99));
		ofertas.add(new Membership(MembershipType.SPECIAL, "High Social Credit", 1.99));
		ofertas.add(new Membership(MembershipType.CELEBRATION, "Snake Year", 4.99));
		
		backend.loadOfertas(ofertas);
		
		ui.iniciarUI();
	}
    
    public void shutdownApp() {
    	logger.info("Cerrando aplicación - shutdownApp()");

    	executor.shutdown();
    	try {
			executor.awaitTermination(5, TimeUnit.SECONDS); // 5 seg de gracia para terminar tareas
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

    }
    
    // ### estados
    
    public void actualizarEstado(Byte newState) {
    	actualState = newState;
    }
    
    public Byte getEstadoActual() {
    	return Byte.valueOf(actualState);
    }    
    
    // ### registro
    
	public boolean doRegister(String nombre, String numero, String passwd, String birthDate, String url, String signature) {
		logger.info("Iniciando registro de nuevo usuario");

		//System.out.println("[DEBUG] haciendo registro");
		Optional<Usuario> user = backend.doRegister(nombre, numero, passwd, birthDate, url, signature);
		if(user.isPresent()) {
			//System.out.println("[DEBUG] escribiendo usuario");
			logger.debug("Registro exitoso, escribiendo usuario en base de datos");
			return dao.registerUser(user.get());
			
		}
		return false;
	}
    
    // ### login/logout
    
	public void doLogin(int numero, String passwd) {
		logger.info("Intentando iniciar sesión");

		//Optional<Usuario> user = dao.recuperarUser(numero);
		if(getEstadoActual() == loggedOut) {
			if (!dao.isUser(numero)) {
				logger.warn("Intento de login fallido: usuario no encontrado");
				ui.loginErrors((byte)4);
			}else if(!dao.checkLogin(numero, passwd)) {
				logger.warn("Intento de login fallido: contraseña incorrecta");
				ui.loginErrors((byte)4);
			}else {
				onLoginSuccess(numero);
			}
		}

	}
    
	public void doLogout() {
		logger.info("Cerrando sesión de usuario");

		if(getEstadoActual() == loggedIn) {
			backend.doLogout();
			actualizarEstado(loggedOut);
		}
		
	}
    
	public void onLoginSuccess(int numero) {
		logger.info("Login exitoso para usuario número {}", numero);

		actualizarEstado(loggedIn);
		backend.loadCurrentUser(dao.recuperarUser(numero).get());
		backend.loadContactList(dao.getListaContactos(numero));
		backend.loadGroupList(dao.getListaGrupos(numero));
		backend.loadNoContactList(dao.getListaNoContactos(numero));
		ui.onLoginSuccess();
	}
    
    // ### usuario
    
	public void makePremiumUser() {
		logger.info("Actualizando usuario a Premium");

		backend.makePremiumUser();
		dao.makePremium(backend.getCurrentUser());
		ui.actualizarPremiumExpireDate();
	}
    
    // ### contactos
    
	public boolean anyadirContacto(String numero, String nombre) {
		logger.info("Añadiendo nuevo contacto: {}", numero);
		
		boolean success = true;
		int number = 0;
		
		try {
			number = Integer.parseInt(numero);
		} catch (NumberFormatException e) {
			logger.error("Error al añadir contacto: número inválido '{}'", numero);
			ui.addContactErrors((byte) 1);
			success = false;
		}
			
		if(number != 0 && (int) (Math.log10(Math.abs(number)) + 1) != 9) {
			ui.addContactErrors((byte) 1);
			success = false;
		}if(nombre.length() == 0) {
			ui.addContactErrors((byte) 3);
			success = false;
		}else if(backend.getUserNumber() == number) {
			ui.addContactErrors((byte) 1);
			success = false;
		}else if(backend.isContact(number)) {
			ui.addContactErrors((byte) 2);
			success = false;
		}if(nombre.length() == 0) {
			ui.addContactErrors((byte) 3);
			success = false;
		}
		
		if(success) {
			
			EntidadComunicable contact =  new EntidadComunicable(number, nombre);
			EntidadComunicable contacto = dao.addContact(contact);
			
			if(contacto != null) {
				backend.addContact(contacto);
				ui.addChat(contacto);
			}
		}
		return success;
	}
    
	public boolean editContact(String phone, String nombre) {
		logger.info("Editando contacto: {}", phone);

		boolean success = true;
		int number = 0;
		
		try {
			number = Integer.parseInt(phone);
		} catch (NumberFormatException e) {
			logger.error("Error al editar contacto: número inválido '{}'", phone);
			ui.ContactSettingsErrors((byte) 1);
			success = false;
		}
			
		if(number != 0 && (int) (Math.log10(Math.abs(number)) + 1) != 9) {
			logger.error("Error al editar contacto: número inválido '{}'", phone);
			ui.ContactSettingsErrors((byte) 1);
			success = false;
		}if(nombre.length() == 0) {
			logger.error("Error al editar contacto: nombre vacío");
			ui.ContactSettingsErrors((byte) 3);
			success = false;
		}else if(backend.getUserNumber() == number) {
			logger.error("Error al editar contacto: no se puede editar el propio número");
			ui.ContactSettingsErrors((byte) 1);
			success = false;
		}else if(!backend.isContact(number)) {
			logger.error("Error al editar contacto: número no encontrado");
			ui.ContactSettingsErrors((byte) 2);
			success = false;
		}if(nombre.length() == 0) {
			logger.error("Error al editar contacto: nombre vacío");
			ui.ContactSettingsErrors((byte) 3);
			success = false;
		}
		
		if(success) {
			
			EntidadComunicable contactAux = backend.getContacto(number);
			contactAux.setNombre(nombre);
			backend.removeUser(number);
			backend.addContact(contactAux);
			dao.actualizarContacto(contactAux);
		}
		return success;
	}
    
	public boolean removeContact(String numero) {
		logger.info("Eliminando contacto: {}", numero);

		boolean success = true;
		int number = 0;
		
		try {
			number = Integer.parseInt(numero);
		} catch (NumberFormatException e) {
			logger.error("Error al eliminar contacto: número inválido '{}'", numero);
			ui.ContactSettingsErrors((byte) 1);
			success = false;
		}
		
		if(number != 0 && (int) (Math.log10(Math.abs(number)) + 1) != 9) {
			ui.ContactSettingsErrors((byte) 1);
			success = false;
		}else if(backend.getUserNumber() == number) {
			ui.ContactSettingsErrors((byte) 1);
			success = false;
		}else if(!backend.isContact(number)) {
			ui.ContactSettingsErrors((byte) 2);
			success = false;
		}
		
		if(success) {
			EntidadComunicable contactAux = backend.getContacto(number);
			backend.removeUser(number);
			dao.removeContact(backend.getUserNumber(), contactAux);
		}
		
		return success;
		
	}
    
	public boolean makeContactFromNoContact(EntidadComunicable noContacto) {
		logger.info("Haciendo contacto de noContacto: {}", noContacto.getNumero());
		
		boolean success = true;
		
		Optional<EntidadComunicable> contactAux = dao.makeContactFromNoContact(noContacto);
		success = contactAux.isPresent() ? true : false;
		if(success) {
			backend.makeContactFromNoContact(noContacto.getNumero());
			backend.getContacto(noContacto.getNumero()).setId(contactAux.get().getId());
			backend.getContacto(noContacto.getNumero()).setIsNoContact(false);
		}
		
		return success;
	}
    
	protected EntidadComunicable getContacto(int numero) {
		return backend.getContacto(numero);
	}
    
	protected List<EntidadComunicable> getListaContactos(){
		return backend.getListaContactos();
	}
    
    // ### grupos
    
	public boolean makeGroup(String nombre, String profilepPicUrl, List<Integer> miembros) {
		logger.info("Creando nuevo grupo: {}", nombre);
		
		boolean success = true;
		
		if(nombre.isBlank() || nombre.isEmpty()) {
			ui.makeGroupErrors((byte) 1);
			success = false;
		}if(miembros == null || miembros.isEmpty()) {
			ui.makeGroupErrors((byte) 2);
			success = false;
		}
		
		if(success) {
			List<EntidadComunicable> miembrosGrupo = new ArrayList<EntidadComunicable>();
			
			long gID = backend.makeGroup(nombre, profilepPicUrl, miembrosGrupo);
			Grupo newGrupo = dao.addGroup(new Grupo(gID, nombre, profilepPicUrl));
			
			for(int numero : miembros) { // hacer contactos para el grupo y añadirlos
				Usuario userAux = dao.recuperarUser(numero).get();
				EntidadComunicable entAux = new EntidadComunicable(userAux);
				miembrosGrupo.add(dao.addMiembroToGrupo(newGrupo.getDBID(), entAux));
				backend.addMiembroToGrupo(newGrupo.getID(), entAux);
			} 
			
			Usuario userAux = dao.recuperarUser(backend.getUserNumber()).get(); // anyadir al usuario al grupo
			EntidadComunicable entAux = new EntidadComunicable(userAux);
			miembrosGrupo.add(dao.addMiembroToGrupo(newGrupo.getDBID(), entAux));
			backend.addMiembroToGrupo(newGrupo.getID(), entAux);
			
			// añadir al usuario como admin del grupo
			dao.addAdminToGrupo(newGrupo.getDBID(), backend.getUserNumber());
			backend.addAdminToGrupo(newGrupo.getID(), backend.getUserNumber());
			
			backend.getGrupo(gID).setDBID(newGrupo.getDBID());
			ui.addGroup(gID);
		}
		
		return success;
		
	}
    
	protected boolean editGroup(String urlProfileIcon, String nombreGrupo, List<Integer> miembros, long groupID) {
		logger.info("Editando grupo: {}", nombreGrupo);

		boolean success = true;
			
		if(nombreGrupo.length() == 0) {
			ui.groupSettingsErrors((byte) 1);
			success = false;
		}if(miembros.size() < 1) {
			ui.groupSettingsErrors((byte)2);
			success = false;
		}
		
		if(success) {
			
			// actualizar lista de usuarios
			Grupo grupoAux = backend.getGrupo(groupID);
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
				Usuario userAux = dao.recuperarUser(numero).get();
				EntidadComunicable entAux = new EntidadComunicable(userAux);
				nuevosUsuarios.add(dao.addMiembroToGrupo(backend.getGrupo(groupID).getDBID(), entAux));
				backend.addMiembroToGrupo(groupID, entAux);
			}
			
			for(int numero : eliminados) {
				EntidadComunicable contactAux = listAux.stream().filter(e -> e.getNumero() == numero).findFirst().get();
				//System.out.println(contactAux.getNumero() + "," + contactAux.getId());
				dao.removeMiembroFromGrupo(backend.getGrupo(groupID).getDBID(), contactAux);
				backend.removeMiembroFromGrupo(groupID, contactAux);
			}
			
			// actualizar nombre e imagen
			backend.getGrupo(groupID).setIconUrl(urlProfileIcon);
			backend.getGrupo(groupID).setNombre(nombreGrupo);
			//BackendController.getGrupo(groupID).setIntegrantes(listAux);
			dao.actualizarGrupo(backend.getGrupo(groupID));
			
		}
		return success;
	}
    
	public void removeGroup(Grupo group) {
		logger.info("Eliminando grupo ID: {}", group.getID());

		//System.out.println("[DEBUG]" + " MainController " + "remove group : " + group.getDBID());
		dao.removeGroup(group);
		backend.removeGrupo(group.getID());
	}
    
	protected boolean leaveGroup(Grupo group) {
		logger.info("Saliendo del grupo ID: {}", group.getID());

		boolean success = false;
		if(group.getIntegrantes().size() > 2) {
			success = true;
			
			EntidadComunicable user = group.getIntegrantes().stream().filter(e  -> e.getNumero() == backend.getUserNumber()).findFirst().get();
			dao.removeMiembroFromGrupo(group.getDBID(), user);
			//dao.eliminarGrupoFromUser(backend.getUserNumber(), group);
			
			// no hace falta porque es la lista del usuario backend.removeMiembroFromGrupo(group.getID(), user);
			backend.removeGrupo(group.getID());
		}
		return success;
	}
    
	protected Grupo getGrupo(long id) {
		return backend.getGrupo(id);
	}
    
	public List<Grupo> getGrupos(){
		return backend.getGrupos();
	}
    
    // ### mensajes
    
	public void sendMessage(ModelMessage msg) {
		logger.info("Enviando mensaje a {}", msg.getReciver());
		
		if(backend.isGroup(msg.getReciver())) {
			int groupBDID = backend.getGrupo(msg.getReciver()).getDBID();
			//System.out.println("[DEBUG]" + " MainController" + " añaddiendo mensaje a grupo : " + groupBDID);
			dao.sendMessageToGroup(msg, groupBDID);
		}else {
			dao.sendMessageToContact(msg);
		}
		
		executor.submit(() -> { backend.nuevoMensaje(msg.getReciver(), msg); });
	}
    
	protected void loadChat(Optional<EntidadComunicable> contacto, Optional<Grupo> grupo) {
		logger.info("Cargando chat para {}", contacto.isPresent() ? contacto.get().getNumero() : grupo.get().getID());
		
		executor.submit(() -> {
			if (contacto.isPresent()) {
				logger.trace("Cargando lote de mensajes para optimización de chat: {}", contacto.get().getNumero());

				List<ModelMessage> listaCaché = backend.getChat((long) contacto.get().getNumero());
				Optional<Integer> lastMsgId = listaCaché.isEmpty() ? Optional.empty() : Optional.of(listaCaché.get(listaCaché.size() - 1).getBDID());
				
				int startLote = 0;
				List<ModelMessage> lista = dao.getMessageFromAChat(contacto.get(), 0, lastMsgId);
				
				if (lastMsgId.isPresent() && lista.get(0).getBDID() != lastMsgId.get()) {
					
					startLote = lista.size();
					List<ModelMessage> listaCachéAux = new ArrayList<>();
					
					while (!lista.isEmpty() && lista.get(0).getBDID() != lastMsgId.get()) {
						
						listaCachéAux.addAll(0, lista); // Agregar nuevos mensajes al inicio para mantener orden
						lista = dao.getMessageFromAChat(contacto.get(), startLote, lastMsgId);
						startLote += lista.size();
					}
					
					listaCachéAux.addAll(0, lista); // Añadir el último conjunto de mensajes
					listaCachéAux.remove(0); // Eliminar colisión
					
					lastMsgId = Optional.empty(); // ya se han restaurado por lo tanto ya no debe tener valor asignado
					
					backend.nuevosMensajes((long) contacto.get().getNumero(), listaCachéAux); // Guardar los nuevos mensajes en la caché del BackendController
					
				} else if (lastMsgId.isEmpty()) {
					backend.nuevosMensajes((long) contacto.get().getNumero(), lista);
				}
				lastMsgId = Optional.empty();
				List<ModelMessage> mensajesCache = backend.getChat(contacto.get().getNumero());
				
				ui.loadChat(mensajesCache);
				startLote = mensajesCache.size();
				
				lista = dao.getMessageFromAChat(contacto.get(), startLote, lastMsgId);
				backend.nuevosMensajesAlInicio((long) contacto.get().getNumero(), lista);
				
				startLote += lista.size();
				
				ui.loadChat(lista);
				
				while (ui.getActualChatOptimization() == (long) contacto.get().getNumero() && lista.size() > 0) {
					//logger.trace("Iteración de carga de mensajes, inicio del lote: {}", startLote);
					
					lista = dao.getMessageFromAChat(contacto.get(), startLote, lastMsgId);
					startLote += lista.size();
					
					backend.nuevosMensajesAlInicio((long) contacto.get().getNumero(), lista);
					ui.loadChat(lista);
				}
				
			} else if (grupo.isPresent()) {
				logger.trace("Cargando lote de mensajes para optimización de grupo: {}", grupo.get().getID());
				
				List<ModelMessage> listaCaché = backend.getChat((long) grupo.get().getID());
				 Optional<Integer> lastMsgId = listaCaché.isEmpty() ? Optional.empty() : Optional.of(listaCaché.get(listaCaché.size() - 1).getBDID());
				 
				 int startLote = 0;
				 List<ModelMessage> lista = dao.getMessageFromAGroup(grupo.get(), 0, lastMsgId);
				 
				 if (lastMsgId.isPresent() && lista.get(0).getBDID() != lastMsgId.get()) {
					
					 startLote = lista.size();
					 List<ModelMessage> listaCachéAux = new ArrayList<>();
					 
					 while (!lista.isEmpty() && lista.get(0).getBDID() != lastMsgId.get()) {
						
						 listaCachéAux.addAll(0, lista); // Agregar nuevos mensajes al inicio para mantener orden
						 lista = dao.getMessageFromAGroup(grupo.get(), startLote, lastMsgId);
						 startLote += lista.size();
					 }
					 
					 listaCachéAux.addAll(0, lista); // Añadir el último conjunto de mensajes
					 listaCachéAux.remove(0); // Eliminar colisión
					 
					 lastMsgId = Optional.empty(); // ya se han restaurado por lo tanto ya no debe tener valor asignado
					 
					 backend.nuevosMensajes((long) grupo.get().getID(), listaCachéAux); // Guardar los nuevos mensajes en la caché del BackendController
					 
				 } else if (lastMsgId.isEmpty()) {
					backend.nuevosMensajes(grupo.get().getID(), lista);
				 }
				 lastMsgId = Optional.empty();
				 List<ModelMessage> mensajesCache = backend.getChat(grupo.get().getID());
				 
				 ui.loadChat(mensajesCache);
				 startLote = mensajesCache.size();
				 
				 lista = dao.getMessageFromAGroup(grupo.get(), startLote, lastMsgId);
				 backend.nuevosMensajesAlInicio((long) grupo.get().getID(), lista);
				 
				 startLote += lista.size();
				 ui.loadChat(lista);
				 
				 while (ui.getActualChatOptimization() == (long) grupo.get().getID() && lista.size() > 0) { // OJO el id del grupo es distinto del id que le asigna la BD (grupo.getDBID)
					 //logger.trace("Iteración de carga de mensajes, inicio del lote: {}", startLote);
					
					lista = dao.getMessageFromAGroup(grupo.get(), startLote, lastMsgId);
					startLote += lista.size();
					
					backend.nuevosMensajesAlInicio((long) grupo.get().getID(), lista);
					ui.loadChat(lista);
				 }
			}
		});
	}
    
	public List<ModelMessage> doSearch(int num, String contact, String msg) {
		
		if (num == 0 && (contact == null || contact.isBlank()) && (msg == null || msg.isBlank())) { // si no hay criterios de busqueda, no se busca nada
		    logger.warn("Búsqueda vacía: no se proporcionaron número, contacto ni mensaje.");
		    return new ArrayList<>();
		}

		
		logger.info("Iniciando búsqueda de mensajes");
		
		boolean f1 = false;
		boolean f2 = false;
		
		LinkedHashSet<ModelMessage> msgs = new LinkedHashSet<>();
		
		List<EntidadComunicable> contactos = backend.getListaContactos();
		List<EntidadComunicable> noContactos = backend.getListaNoContactos();
		List<Grupo> grupos = backend.getGrupos();
		
		EntidadComunicable ent = null;
		EntidadComunicable ent2 = null;
		List<Grupo> gruposPertenece = new ArrayList<Grupo>();
		
		// filtro numero
		logger.trace("Filtro por número: {}", num);
		
		if(num != 0 && backend.getUserNumber() == num) {
			ent = backend.getCurrentUser();
			f1 = true;
		} else if(num != 0 && backend.isContact(num)) {
			ent = backend.getContacto(num);
		} else if(num != 0 && backend.isNoContact(num)) {
			ent = backend.getNoContacto(num);
		} 
		if(num != 0 && f1) {
			for(Grupo grupo : backend.getGrupos()) {
				Optional<EntidadComunicable> aux = grupo.getIntegrantes().stream().filter(e -> e.getNumero() == num).findFirst();
				if(aux.isPresent()) gruposPertenece.add(grupo);
			}
		} else if(num != 0 && !f1 && ent != null) {
			for(Grupo grupo : dao.getListaGrupos(ent.getNumero())) {
				Optional<EntidadComunicable> aux = grupo.getIntegrantes().stream().filter(e -> e.getNumero() == num).findFirst();
				if(aux.isPresent()) gruposPertenece.add(grupo);
			}
		}
		
		// filtro contacto
		logger.trace("Filtro por contacto: {}", contact);
		
		if(contact != null && backend.getCurrentUser().getNombre().equals(contact)) {
			ent2 = backend.getCurrentUser();
			f2 = true;
		} else if(contact != null) {
			Optional<EntidadComunicable> aux = contactos.stream().filter(e -> e.getNombre().equals(contact)).findFirst();
			ent2 = aux.isEmpty() ? null : aux.get();
		} 
		if(contact != null && ent2 == null) {
			Optional<EntidadComunicable> aux = noContactos.stream().filter(e -> e.getNombre().equals(contact)).findFirst();
			ent2 = aux.isEmpty() ? null : aux.get();
		} 
		if(contact != null && f2) {
			for(Grupo grupo : backend.getGrupos()) {
				Optional<EntidadComunicable> aux = grupo.getIntegrantes().stream().filter(e -> e.getNombre().equals(contact)).findFirst();
				if(aux.isPresent()) gruposPertenece.add(grupo);
			}
		} else if(contact != null && !f2 && ent2 != null) {
			for(Grupo grupo : dao.getListaGrupos(ent2.getNumero())) {
				Optional<EntidadComunicable> aux = grupo.getIntegrantes().stream().filter(e -> e.getNombre().equals(contact)).findFirst();
				if(aux.isPresent()) gruposPertenece.add(grupo);
			}
		}
		
		// tercer filtro
		logger.trace("Filtro por mensaje: {}", msg);
		
		if(msg != null) {
			if(ent != null && ent2 != null && ent.getNumero() == ent2.getNumero()) { //  entidades coinciden
				if(ent.getNumero() == backend.getUserNumber()) { // en el caso de los usuarios buscamos por todos sus chats
					for(EntidadComunicable e : contactos) {msgs.addAll(dao.getAllMsgsFromAChat(e, true, Optional.of(msg)));}
					for(EntidadComunicable e : noContactos) {msgs.addAll(dao.getAllMsgsFromAChat(e, false, Optional.of(msg)));}
				} else {
					msgs.addAll(dao.getAllMsgsFromAChat(ent, backend.isContact(ent.getNumero()), Optional.of(msg)));
				}
			} else { // entidades no coinciden
				if(ent != null && ent.getNumero() == backend.getUserNumber()) { // en el caso de los usuarios buscamos por todos sus chats
					for(EntidadComunicable e : contactos) {msgs.addAll(dao.getAllMsgsFromAChat(e, true, Optional.of(msg)));}
					for(EntidadComunicable e : noContactos) {msgs.addAll(dao.getAllMsgsFromAChat(e, false, Optional.of(msg)));}
				} else if(ent != null) {
					msgs.addAll(dao.getAllMsgsFromAChat(ent, backend.isContact(ent.getNumero()), Optional.of(msg)));
				}
				if(ent2 != null && ent2.getNumero() == backend.getUserNumber()) { // en el caso de los usuarios buscamos por todos sus chats
					for(EntidadComunicable e : contactos) {msgs.addAll(dao.getAllMsgsFromAChat(e, true, Optional.of(msg)));}
					for(EntidadComunicable e : noContactos) {msgs.addAll(dao.getAllMsgsFromAChat(e, false, Optional.of(msg)));}
				} else if(ent2 != null && ent != null) {
					msgs.addAll(dao.getAllMsgsFromAChat(ent2, backend.isContact(ent.getNumero()), Optional.of(msg)));
				} else if(ent2 != null) {
					Optional<Integer> entNum = backend.getNumeroDesdeNombreContacto(contact);
					if(entNum.isPresent()) {
						msgs.addAll(dao.getAllMsgsFromAChat(ent2, backend.isContact(entNum.get()), Optional.of(msg)));
					}else {
						logger.error("Error al buscar mensajes: contacto no encontrado");
					}
				}
			} 
			if(!gruposPertenece.isEmpty()) {
				int n1 = ent != null ? ent.getNumero() : 0;
				int n2  = ent2 != null ? ent2.getNumero() : 0;
				for(Grupo grupo : grupos) msgs.addAll(dao.getAllMsgsFromAGroup(grupo, Optional.of(msg)).stream()
						.filter(e -> e.getSender() == n1 || e.getSender() == n2).toList());
			}
			if ((num == 0 && (contact == null || contact.isBlank())) && ent == null && ent2 == null)  {
				// Buscar en todos los contactos
				for (EntidadComunicable c : contactos) {
					msgs.addAll(dao.getAllMsgsFromAChat(c, true, Optional.ofNullable(msg)));
				}
				for (EntidadComunicable c : noContactos) {
					msgs.addAll(dao.getAllMsgsFromAChat(c, false, Optional.ofNullable(msg)));
				}
				// Buscar en todos los grupos
				for (Grupo g : grupos) {
					msgs.addAll(dao.getAllMsgsFromAGroup(g, Optional.ofNullable(msg)));
				}
			}
		} else {
			if(ent != null && ent2 != null && ent.getNumero() == ent2.getNumero()) { //  entidades coinciden
				if(ent.getNumero() == backend.getUserNumber()) { // en el caso de los usuarios buscamos por todos sus chats
					for(EntidadComunicable e : contactos) {msgs.addAll(dao.getAllMsgsFromAChat(e, true, Optional.empty()));}
					for(EntidadComunicable e : noContactos) {msgs.addAll(dao.getAllMsgsFromAChat(e, false, Optional.empty()));}
				} else {
					msgs.addAll(dao.getAllMsgsFromAChat(ent, backend.isContact(ent.getNumero()), Optional.empty()));
				}
			} else { // entidades no coinciden
				if(ent != null && ent.getNumero() == backend.getUserNumber()) { // en el caso de los usuarios buscamos por todos sus chats
					for(EntidadComunicable e : contactos) {msgs.addAll(dao.getAllMsgsFromAChat(e, true, Optional.empty()));}
					for(EntidadComunicable e : noContactos) {msgs.addAll(dao.getAllMsgsFromAChat(e, false, Optional.empty()));}
				} else if(ent != null) {
					msgs.addAll(dao.getAllMsgsFromAChat(ent, backend.isContact(ent.getNumero()), Optional.empty()));
				}
				if(ent2 != null && ent2.getNumero() == backend.getUserNumber()) { // en el caso de los usuarios buscamos por todos sus chats
					for(EntidadComunicable e : contactos) {msgs.addAll(dao.getAllMsgsFromAChat(e, true, Optional.empty()));}
					for(EntidadComunicable e : noContactos) {msgs.addAll(dao.getAllMsgsFromAChat(e, false, Optional.empty()));}
				} else if(ent2 != null) {
					msgs.addAll(dao.getAllMsgsFromAChat(ent2, backend.isContact(ent2.getNumero()), Optional.empty()));
				}
			} 
			if(!gruposPertenece.isEmpty()) {
				int n1 = ent != null ? ent.getNumero() : 0;
				int n2 = ent2 != null ? ent2.getNumero() : 0;
				for(Grupo grupo : grupos) msgs.addAll(dao.getAllMsgsFromAGroup(grupo, Optional.empty()).stream()
						.filter(e -> e.getSender() == n1 || e.getSender() == n2).toList());
			}
			if ((num == 0 && (contact == null || contact.isBlank())) && ent == null && ent2 == null)  {
				// Buscar en todos los contactos
				for (EntidadComunicable c : contactos) {
					msgs.addAll(dao.getAllMsgsFromAChat(c, true, Optional.ofNullable(msg)));
				}
				for (EntidadComunicable c : noContactos) {
					msgs.addAll(dao.getAllMsgsFromAChat(c, false, Optional.ofNullable(msg)));
				}
				// Buscar en todos los grupos
				for (Grupo g : grupos) {
					msgs.addAll(dao.getAllMsgsFromAGroup(g, Optional.ofNullable(msg)));
				}
			}

		}
		
		return msgs.stream().distinct().toList();
	}
    
    // ### PDF
    
	public String makePDF() {
		logger.info("Haciendo PDF");
		if (!backend.currentUserIsPremium()) {
			logger.error("Acceso denegado a exportar PDF: usuario no es premium");
			// System.out.println("[ERROR] No tienes acceso a esta función. Debes ser usuario Premium.");
			return null;
		}
		return backend.exportarDatosPDF();
	}
    
}
