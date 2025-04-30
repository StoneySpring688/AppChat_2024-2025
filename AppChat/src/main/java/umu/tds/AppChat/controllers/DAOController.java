package umu.tds.AppChat.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;

import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;
import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.backend.utils.Usuario;
import umu.tds.AppChat.dao.AbstractFactoriaDAO;
import umu.tds.AppChat.dao.DAOException;
import umu.tds.AppChat.dao.InterfaceContactoDAO;
import umu.tds.AppChat.dao.InterfaceGrupoDAO;
import umu.tds.AppChat.dao.InterfaceNoContactoDAO;
import umu.tds.AppChat.dao.InterfaceUsuarioDAO;
import umu.tds.AppChat.devtools.LoggerUtil;

/**
 * Controlador de acceso a datos (DAO) de la aplicación.
 *
 * <p>Actúa como intermediario entre la lógica de negocio y la capa de persistencia.
 * Utiliza adaptadores DAO para manejar:
 * <ul>
 *   <li>Usuarios</li>
 *   <li>Contactos y no-contactos</li>
 *   <li>Grupos</li>
 *   <li>Mensajes</li>
 * </ul>
 *
 * <p>Centraliza todas las operaciones relacionadas con almacenamiento y recuperación
 * de datos, permitiendo mantener desacoplada la lógica del backend.
 *
 * <p>Implementa el patrón Singleton para mantener consistencia en la gestión de adaptadores.
 *
 * @author StoneySpring
 */

public class DAOController {
	private InterfaceUsuarioDAO userAdapter;
	private InterfaceContactoDAO contactAdapter;
	private InterfaceGrupoDAO groupAdapter;
	private InterfaceNoContactoDAO noContactAdapter;
	//private static InterfaceMensajeDAO msgAdapter;
	
    // logger
    private static final Logger logger = LoggerUtil.getLogger(DAOController.class);
	
	// singleton
	private static DAOController unicaInstancia = null;
	
	public static DAOController getUnicaInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new DAOController();
        }
        return unicaInstancia;
    }
	
	private DAOController() {
        // initAdapters(); evita bucle
    }
	
	protected void initAdapters() {
		logger.info("Inicializando adaptadores DAO.");

		AbstractFactoriaDAO factoria = null;
		try {
			factoria = AbstractFactoriaDAO.getInstancia(AbstractFactoriaDAO.DAO_TDS);
		} catch (DAOException e) {
			logger.error("Error al inicializar la factoría DAO: ", e);
			//e.printStackTrace();
		}
		userAdapter = factoria.getUsuarioDAO();
		contactAdapter = factoria.getContactoDAO();
		groupAdapter = factoria.getGrupoDAO();
		noContactAdapter = factoria.getNoContactoDAO();
		//msgAdapter = factoria.getMensajeDAO();
	}
	
	// ### users
	
	public boolean registerUser(Usuario user) {
		logger.debug("Registrando nuevo usuario con número: {}", user.getNumero());

		boolean success = false;
		if(!isUser(user.getNumero())) {
			userAdapter.create(user);
			success = true;
		}else UIController.getUnicaInstancia().registerErrors((byte) 3);
		return success;
	}
	
	/**
	 * @return si no  existe el usuario devuelve Optional.empty()*/
	public Optional<Usuario> recuperarUser(int id){
		logger.debug("Recuperando usuario con número: {}", id);

		Usuario userDevuelto = userAdapter.get(id);
		//System.out.println("[DEBUG] usuario devuelto : "+userDevuelto );
		return userDevuelto != null ? Optional.of(userDevuelto) : Optional.empty();
		
	}
	
	public boolean isUser(int id) {
		logger.debug("Verificando existencia de usuario: {}", id);

		return recuperarUser(id).isEmpty() ? false : true;
	}
	
	public boolean checkLogin(int id, String passwd) {
		logger.debug("Verificando login para usuario: {}", id);

		return userAdapter.checkLogin(id, passwd);
	}
	
	public void makePremium(Usuario user) {
		logger.info("Actualizando usuario a Premium: {}", user.getNumero());

		//System.out.println("[DEBUG]" + " DAOController " + "usuario a actualizar : \n" + user);
		userAdapter.update(user);
	}
	
	public void addGrupoToUser(int number, Grupo grupo) {
		logger.debug("Añadiendo grupo a usuario: {}", number);
		
		userAdapter.addGrupoToUser(number, grupo);
	}
	
	public void eliminarGrupoFromUser(int numero, Grupo grupo) {
		logger.debug("Eliminando grupo de usuario: {}", numero);
		
		userAdapter.eliminarGrupoFromUser(numero, grupo);
	}
	
	public List<EntidadComunicable> getListaContactos(int numero){
		logger.debug("Recuperando lista de contactos para usuario: {}", numero);
		
		return userAdapter.obtenerListaContactos(numero);
	}
	
	public List<Grupo> getListaGrupos(int numero){
		logger.debug("Recuperando lista de grupos para usuario: {}", numero);
		
		return userAdapter.obtenerListaGruposFromUser(numero);
	}
	
	public List<EntidadComunicable> getListaNoContactos(int numero){
		logger.debug("Recuperando lista de noContactos para usuario: {}", numero);
		
		return userAdapter.obtenerListaNoContactos(numero);
	}
	
	// ### contacts
	
	public EntidadComunicable addContact(EntidadComunicable contact) {
		logger.debug("Añadiendo nuevo contacto: {}", contact.getNumero());

		if(isUser(contact.getNumero())) {
			//System.out.println("[DEBUG] es usuario");
			contactAdapter.create(contact);
			//System.out.println("[DEBUG] el  id del contacto es : "+ contact.getId());
			EntidadComunicable contacto = contactAdapter.get(contact.getId());
			contacto.setId(contact.getId());
			//System.out.println("[DEBUG]" + "DAOConttroller" + " anyadiendo contacto id : " + contacto.getId() );
			userAdapter.addContacto(BackendController.getUnicaInstancia().getUserNumber(), contacto);
			return contacto;
		}
		return null;
	}
	
	public EntidadComunicable recuperarContacto(int id) {
		logger.debug("Recuperando contacto con ID: {}", id);
		
		return contactAdapter.get(id);
	}
	
	public void removeContact(int user, EntidadComunicable contact) {
		logger.debug("Eliminando contacto [{}] para usuario [{}]", contact.getNumero(), user);

		userAdapter.eliminarContacto(user, contact);
		if(!isContact(user, contact.getNumero())) contactAdapter.eliminarMsgs(contact.getId()); // si el usuario no es contacto del otro usuario, elimina los mensajes
		contactAdapter.delete(contact);
	}
	
	public void actualizarContacto(EntidadComunicable contact) {
		logger.debug("Actualizando información del contacto: {}", contact.getNumero());
		
		contactAdapter.update(contact);
	}
	
	public boolean isContact(int numeroContacto, int userToTest) {
		logger.debug("Verificando si el usuario [{}] es contacto de [{}]", numeroContacto, userToTest);
		
		return userAdapter.isContact(numeroContacto, userToTest);
	}
	
	// ### groups
	
	public Grupo addGroup(Grupo grupo) {
		logger.debug("Añadiendo nuevo grupo a la base de datos: {}", grupo.getNombre());

		groupAdapter.create(grupo);
		//System.out.println("[DEBUG]" + "DAOConttroller" + " grupo anyadido : " + grupo.getDBID());
		Grupo nuevoGrupo = groupAdapter.get(grupo.getDBID());
		// nuevoGrupo.setDBID(grupo.getDBID());
		//System.out.println("[DEBUG]" + "DAOConttroller" + " se va a devolver el grupo : " + nuevoGrupo.getDBID());
		return nuevoGrupo;
	}
	
	public Grupo recuperarGrupo(int id) {
		logger.debug("Recuperando grupo con ID: {}", id);
		
		return groupAdapter.get(id);
	}
	
	public void removeGroup(Grupo grupo) {
		logger.debug("Eliminando grupo de la base de datos: {}", grupo.getNombre());
		
		groupAdapter.obtenerListaMiembros(grupo.getDBID()).forEach(u -> eliminarGrupoFromUser(u.getNumero(), grupo)); // elimina el grupo de los usuarios
		groupAdapter.delete(grupo);
	}
	
	public void actualizarGrupo(Grupo grupo) {
		logger.debug("Actualizando información del grupo: {}", grupo.getNombre());

		groupAdapter.update(grupo);
	}
	
	public EntidadComunicable addMiembroToGrupo(int id, EntidadComunicable miembro) {
		logger.debug("Añadiendo miembro [{}] al grupo con DBID [{}]", miembro.getNumero(), id);

		contactAdapter.create(miembro);
		//System.out.println("[DEBUG]" + "DAOConttroller" + " el id del nuevo miembro es : "+ miembro.getId());
		EntidadComunicable newMiembro = contactAdapter.get(miembro.getId());
		//System.out.println("[DEBUG]" + "DAOConttroller" + " anyadiendo miembro id : " + newMiembro.getId() );
		groupAdapter.addMiembro(id, newMiembro);
		userAdapter.addGrupoToUser(newMiembro.getNumero(), groupAdapter.get(id));
		return newMiembro;
	}
	
	public void removeMiembroFromGrupo(int id, EntidadComunicable miembro) {
		logger.debug("Eliminando miembro [{}] del grupo con DBID [{}]", miembro.getNumero(), id);

		groupAdapter.eliminarMiembro(id, miembro);
		userAdapter.eliminarGrupoFromUser(miembro.getNumero(), groupAdapter.get(id));
	}
	
	public boolean isMiembro(int numeroUser, int groupID) {
		logger.debug("Verificando si el usuario [{}] es miembro del grupo con ID [{}]", numeroUser, groupID);
		
		return groupAdapter.isMiembro(numeroUser, groupID);
	}
	
	public void addAdminToGrupo(int idGrupo, int numeroAdmin) {
		logger.debug("Asignando admin [{}] al grupo con DBID [{}]", numeroAdmin, idGrupo);
		
		//System.out.println("[DEBUG]" + "DAOConttroller" + " anyadiendo admin : " + numeroAdmin );
		groupAdapter.addAdmin(idGrupo, numeroAdmin);
	}
	
	public void removeAdminFromGrupo(int idGrupo, int numeroAdmin) {
		logger.debug("Eliminando admin [{}] del grupo con DBID [{}]", numeroAdmin, idGrupo);
		
		groupAdapter.eliminarAdmin(idGrupo, numeroAdmin);
	}
	
	public boolean isAdmin(int numeroAdmin, int groupID) {
		logger.debug("Verificando si el usuario [{}] es admin del grupo con ID [{}]", numeroAdmin, groupID);
		
		return groupAdapter.isAdmin(numeroAdmin, groupID);
	}
	
	// ### noContactos
	
	public EntidadComunicable addNoContact(EntidadComunicable noContact, int userToAddNoContact) {	
		logger.debug("Añadiendo nuevo noContacto [{}] para usuario [{}]", noContact.getNumero(), userToAddNoContact);

		noContactAdapter.create(noContact);
		//System.out.println("[DEBUG] el  id del no contacto es : "+ contact.getId());
		EntidadComunicable noContacto = noContactAdapter.get(noContact.getId());
		noContacto.setId(noContact.getId());
		//System.out.println("[DEBUG]" + "DAOConttroller" + " anyadiendo no contacto id : " + noContacto.getId() );
		userAdapter.addNoContacto(userToAddNoContact, noContacto);
		return noContacto;
	}
	
	public boolean isNoContact(int numeroNoContacto, int userToTest) {
		logger.debug("Verificando si el usuario [{}] es noContacto de [{}]", numeroNoContacto, userToTest);
		
		return userAdapter.isNoContact(numeroNoContacto, userToTest);
	}
	
	public Optional<EntidadComunicable> makeContactFromNoContact(EntidadComunicable noContact) {
		logger.debug("Convirtiendo noContacto [{}] en contacto.", noContact.getNumero());

		boolean success = true;
		
		String msgsAux = noContactAdapter.obtenerListaIDsMsgs(noContact.getId());
		userAdapter.eliminarNoContacto(BackendController.getUnicaInstancia().getUserNumber(), noContact);
		success = noContactAdapter.delete(noContact);
		if(success) {
			EntidadComunicable contactAux = addContact(noContact);
			contactAdapter.addMsgs(contactAux.getId(), msgsAux);
			return Optional.of(contactAux);
		}else {
			userAdapter.addNoContacto(BackendController.getUnicaInstancia().getUserNumber(), noContact); // si no se puede elimiar se restaura la referencia al nocontacto
		}
		return Optional.empty();
	}
	
	// ### message
	
	public void sendMessageToContact(ModelMessage msg) {
		logger.debug("Guardando mensaje entre [{}] y [{}]", msg.getSender(), msg.getReciver());

		int reciver = (int) msg.getReciver();
		int userNum = msg.getSender();
		
		//System.out.println("[DEBUG]" + "DAOConttroller" +" sender : " + userNum +" , reciver : " + reciver);
		
		if(isContact(userNum, reciver)) {
			
			EntidadComunicable contactoDestino = userAdapter.obtenerListaContactos(reciver).stream().filter(e -> e.getNumero() == userNum).findFirst().get();
			EntidadComunicable contactoOrgigen = null;
			
			if(isContact(reciver, userNum)) {
				contactoOrgigen = userAdapter.obtenerListaContactos(userNum).stream().filter(e -> e.getNumero() == reciver).findFirst().get();
			}else {
				contactoOrgigen = userAdapter.obtenerListaNoContactos(userNum).stream().filter(e -> e.getNumero() == reciver).findFirst().get();
			}
			
			contactAdapter.addMsg(contactoDestino.getId(), msg);
			contactAdapter.addMsg(contactoOrgigen.getId(), msg);
			
			//System.out.println("[DEBUG]" + "DAOConttroller" + " es contacto : " + userNum + " de : " + reciver);
			//return;
		}else if(isNoContact(userNum, reciver)) {
			
			EntidadComunicable contactoDestino = userAdapter.obtenerListaNoContactos(reciver).stream().filter(e -> e.getNumero() == userNum).findFirst().get();
			EntidadComunicable contactoOrgigen = null;
			
			if(isContact(reciver, userNum)) {
				contactoOrgigen = userAdapter.obtenerListaContactos(userNum).stream().filter(e -> e.getNumero() == reciver).findFirst().get();
			}else {
				contactoOrgigen = userAdapter.obtenerListaNoContactos(userNum).stream().filter(e -> e.getNumero() == reciver).findFirst().get();
			}
			
			noContactAdapter.addMsg(contactoDestino.getId(), msg);
			contactAdapter.addMsg(contactoOrgigen.getId(), msg);
			
			//System.out.println("[DEBUG]" + "DAOConttroller" + " es no contacto : " + userNum + " de : " + reciver);
			//return;
		}else {
			addNoContact(new EntidadComunicable(userNum, ""), reciver);
			//System.out.println("[DEBUG]" + "DAOConttroller" + " anyadiendo no contacto : " + userNum + " a : " + reciver);
			//userAdapter.addNoContacto(reciver,newNoContact); // solo neccesita el numero (clave ajena) el resto lo calcula  el adaptador
			//System.out.println("[DEBUG]" + "DAOConttroller" + " no contacto aniadido a :" + reciver + " nueva lista no contactos : " + getListaNoContactos(reciver));
			
			EntidadComunicable contactoDestino = userAdapter.obtenerListaNoContactos(reciver).stream().filter(e -> e.getNumero() == userNum).findFirst().get();
			EntidadComunicable contactoOrgigen = null;
			
			if(isContact(reciver, userNum)) {
				contactoOrgigen = userAdapter.obtenerListaContactos(userNum).stream().filter(e -> e.getNumero() == reciver).findFirst().get();
			}else {
				contactoOrgigen = userAdapter.obtenerListaNoContactos(userNum).stream().filter(e -> e.getNumero() == reciver).findFirst().get();
			}
			
			noContactAdapter.addMsg(contactoDestino.getId(), msg);
			contactAdapter.addMsg(contactoOrgigen.getId(), msg);
			//return;
		}
	}
	
	public void sendMessageToGroup(ModelMessage msg, int groupID) {
		logger.debug("Guardando mensaje en grupo [{}] desde usuario [{}]", groupID, msg.getSender());

		int userNum = msg.getSender();
		
		if(isMiembro(userNum, groupID)) {
			groupAdapter.addMsg(groupID, msg);
			//System.out.println("[DEBUG]" + "DAOConttroller" + " es miembro : " + userNum + " de : " + groupID);
		}
	}
	
	public List<ModelMessage> getMessageFromAChat(EntidadComunicable contacto, int startLote, Optional<Integer> lastMsgId){
		logger.debug("Recuperando lote de mensajes para contacto [{}], inicio [{}]", contacto.getNumero(), startLote);

		List<ModelMessage> lote = contactAdapter.obtenerLoteMsg(contacto.getId(), 10, startLote);
		
		//for(ModelMessage msg : lote) System.out.println("[DEBUG]" + " DaoController" + " mensaje a cargar : " + '\n' + msg.toString()); 
		//System.out.println("[DEBUG]" + " DAOConttroller" + " se ha recuperado un lote de tamaño : " +lote.size());
		
		if(lastMsgId.isPresent()) { // recuperación de mensajes recientes por diferencia respecto a caché
			List<ModelMessage> lista = new ArrayList<ModelMessage>();
			//lote = contactAdapter.obtenerLoteMsg(contacto.getId(), 10, startLote);
			int a =  lote.get(lote.size()-1).getBDID();
				
				for(int i = lote.size(); a!= lastMsgId.get() && i>0; i--) {
					//System.out.println("[DEBUG]" + " DaoController" + " actualizando mensajes con los recientes"); 
					lista.add(0, lote.get(i));
					a = i > 0 ? lote.get(i).getBDID() : lote.get(i-1).getBDID();
				}
			
			lista.add(0, lote.get(lote.size()-lista.size()-1)); // añade el último elemento o la colisión con la caché
			return lista;
		}
		
		return lote;
		
	}
	
	public List<ModelMessage> getMessageFromAGroup(Grupo grupo, int startLote, Optional<Integer> lastMsgId){
		logger.debug("Recuperando lote de mensajes para grupo [{}], inicio [{}]", grupo.getNombre(), startLote);

		List<ModelMessage> lote = groupAdapter.obtenerLoteMsg(grupo.getDBID(), 10, startLote);
		//for(ModelMessage msg : lote) System.out.println("[DEBUG]" + " DaoController" + " mensaje a cargar : " + '\n' + msg.toString()); 
		
		if(lastMsgId.isPresent()) { // recuperación de mensajes recientes por diferencia respecto a caché
			List<ModelMessage> lista = new ArrayList<ModelMessage>();
			//lote = contactAdapter.obtenerLoteMsg(contacto.getId(), 10, startLote);
			int a =  lote.get(lote.size()-1).getBDID();
				
				for(int i = lote.size(); a!= lastMsgId.get() && i>0; i--) {
					//System.out.println("[DEBUG]" + " DaoController" + " actualizando mensajes con los recientes"); 
					lista.add(0, lote.get(i));
					a = i > 0 ? lote.get(i).getBDID() : lote.get(i-1).getBDID();
				}
			
			lista.add(0, lote.get(lote.size()-lista.size()-1)); // añade el último elemento o la colisión con la caché
			return lista;
		}
		
		return lote;
	}
	
	public List<ModelMessage> getAllMsgsFromAChat(EntidadComunicable contacto, boolean isContact, Optional<String> filter){
		logger.debug("Recuperando mensajes de contacto [{}], filtro: {}", contacto.getNumero(),filter.orElse("Sin filtro"));
		
		List<ModelMessage> list = new ArrayList<ModelMessage>();
		
		if(filter.isPresent()) {
			if(isContact)list = contactAdapter.obtenerListaMsgFilter(contacto.getId(), filter.get());
			if(!isContact)list = noContactAdapter.obtenerListaMsgFilter(contacto.getId(), filter.get());
		}else {
			if(isContact)list = contactAdapter.obtenerListaMsgFilter(contacto.getId(), "");
			if(!isContact)list = noContactAdapter.obtenerListaMsgFilter(contacto.getId(), "");
		}
		return list;
	}
	
	public List<ModelMessage> getAllMsgsFromAGroup(Grupo grupo, Optional<String> filter){
		logger.debug("Recuperando mensajes de grupo [{}]", grupo.getNombre());
		
		List<ModelMessage> list = new ArrayList<ModelMessage>();
		
		if(filter.isPresent()) {
			list = groupAdapter.obtenerListaMsgFilter(grupo.getDBID(), filter.get());
		}else {
			list = groupAdapter.obtenerListaMsgFilter(grupo.getDBID(), "");
		}
		return list;
	}
	
	public List<ModelMessage> getAllMsgsFromAChat(EntidadComunicable contacto, boolean isContact){
		logger.debug("Recuperando mensajes de contacto [{}]", contacto.getNumero());
		
		List<ModelMessage> list = new ArrayList<ModelMessage>();
		
		if(isContact)list = contactAdapter.obtenerListaMsg(contacto.getId());
		if(!isContact)list = noContactAdapter.obtenerListaMsg(contacto.getId());
		
		return list;
	}
	
	// ### métodos para PDF
	
	public List<String> getMensajesDeUsuario(EntidadComunicable contacto) {
		logger.debug("Recuperando mensajes de usuario [{}] para hacer PDF", contacto.getNumero());
		
	    return getAllMsgsFromAChat(contacto, true)
	            .stream()
	            .map(m -> "[" + m.getDate() + "] " + m.getName() + ": " + m.getMessage().orElse("[Emoji]"))
	            .collect(Collectors.toList());
	}

	public List<String> getIntegrantesDeGrupo(Grupo grupo) {
		logger.debug("Recuperando integrantes de grupo [{}] para hacer PDF", grupo.getNombre());
		
	    return grupo.getIntegrantes().stream()
	            .map(i -> i.getNumero() + " - " + i.getNombre())
	            .collect(Collectors.toList());
	}

	
}
