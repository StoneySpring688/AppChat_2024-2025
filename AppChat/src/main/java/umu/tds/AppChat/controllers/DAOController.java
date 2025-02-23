package umu.tds.AppChat.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

public class DAOController {
	private static InterfaceUsuarioDAO userAdapter;
	private static InterfaceContactoDAO contactAdapter;
	private static InterfaceGrupoDAO groupAdapter;
	private static InterfaceNoContactoDAO noContactAdapter;
	//private static InterfaceMensajeDAO msgAdapter;
	
	protected static void initAdapters() {
		AbstractFactoriaDAO factoria = null;
		try {
			factoria = AbstractFactoriaDAO.getInstancia(AbstractFactoriaDAO.DAO_TDS);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		userAdapter = factoria.getUsuarioDAO();
		contactAdapter = factoria.getContactoDAO();
		groupAdapter = factoria.getGrupoDAO();
		noContactAdapter = factoria.getNoContactoDAO();
		//msgAdapter = factoria.getMensajeDAO();
	}
	
	// ### users
	
	public static void registerUser(Usuario user) {
		if(!isUser(user.getNumero()))userAdapter.create(user);
	}
	
	/**
	 * @return si no  existe el usuario devuelve Optional.empty()*/
	public static Optional<Usuario> recuperarUser(int id){
		Usuario userDevuelto = userAdapter.get(id);
		//System.out.println("[DEBUG] usuario devuelto : "+userDevuelto );
		return userDevuelto != null ? Optional.of(userDevuelto) : Optional.empty();
		
	}
	
	public static boolean isUser(int id) {
		return recuperarUser(id).isEmpty() ? false : true;
	}
	
	public static boolean checkLogin(int id, String passwd) {
		return userAdapter.checkLogin(id, passwd);
	}
	
	public static void makePremium(Usuario user) {
		userAdapter.update(user);
	}
	
	public static void addGrupoToUser(int number, Grupo grupo) {
		userAdapter.addGrupoToUser(number, grupo);
	}
	
	public static void eliminarGrupoFromUser(int numero, Grupo grupo) {
		userAdapter.eliminarGrupoFromUser(numero, grupo);
	}
	
	public static List<EntidadComunicable> getListaContactos(int numero){
		return userAdapter.obtenerListaContactos(numero);
	}
	
	public static List<Grupo> getListaGrupos(int numero){
		return userAdapter.obtenerListaGruposFromUser(numero);
	}
	
	public static List<EntidadComunicable> getListaNoContactos(int numero){
		return userAdapter.obtenerListaNoContactos(numero);
	}
	
	// ### contacts
	
	public static EntidadComunicable addContact(EntidadComunicable contact) {				
		if(isUser(contact.getNumero())) {
			//System.out.println("[DEBUG] es usuario");
			contactAdapter.create(contact);
			//System.out.println("[DEBUG] el  id del contacto es : "+ contact.getId());
			EntidadComunicable contacto = contactAdapter.get(contact.getId());
			contacto.setId(contact.getId());
			System.out.println("[DEBUG]" + "DAOConttroller" + " anyadiendo contacto id : " + contacto.getId() );
			userAdapter.addContacto(BackendController.getUserNumber(), contacto);
			return contacto;
		}
		return null;
	}
	
	public static EntidadComunicable recuperarContacto(int id) {
		return contactAdapter.get(id);
	}
	
	public static void removeContact(int user, EntidadComunicable contact) {
		userAdapter.eliminarContacto(user, contact);
		if(!isContact(user, contact.getNumero())) contactAdapter.eliminarMsgs(contact.getId());
		contactAdapter.delete(contact);
	}
	
	public static void actualizarContacto(EntidadComunicable contact) {
		contactAdapter.update(contact);
	}
	
	public static boolean isContact(int numeroContacto, int userToTest) {
		return userAdapter.isContact(numeroContacto, userToTest);
	}
	
	// ### groups
	
	public static Grupo addGroup(Grupo grupo) {
		groupAdapter.create(grupo);
		System.out.println("[DEBUG]" + "DAOConttroller" + " grupo anyadido : " + grupo.getDBID());
		Grupo nuevoGrupo = groupAdapter.get(grupo.getDBID());
		// nuevoGrupo.setDBID(grupo.getDBID());
		System.out.println("[DEBUG]" + "DAOConttroller" + " se va a devolver el grupo : " + nuevoGrupo.getDBID());
		return nuevoGrupo;
	}
	
	public static Grupo recuperarGrupo(int id) {
		return groupAdapter.get(id);
	}
	
	public static void removeGroup(Grupo grupo) {
		groupAdapter.obtenerListaMiembros(grupo.getDBID()).forEach(u -> eliminarGrupoFromUser(u.getNumero(), grupo)); // elimina el grupo de los usuarios
		groupAdapter.delete(grupo);
	}
	
	public static void actualizarGrupo(Grupo grupo) {
		groupAdapter.update(grupo);
	}
	
	public static EntidadComunicable addMiembroToGrupo(int id, EntidadComunicable miembro) {
		contactAdapter.create(miembro);
		System.out.println("[DEBUG]" + "DAOConttroller" + " el id del nuevo miembro es : "+ miembro.getId());
		EntidadComunicable newMiembro = contactAdapter.get(miembro.getId());
		System.out.println("[DEBUG]" + "DAOConttroller" + " anyadiendo miembro id : " + newMiembro.getId() );
		groupAdapter.addMiembro(id, newMiembro);
		userAdapter.addGrupoToUser(newMiembro.getNumero(), groupAdapter.get(id));
		return newMiembro;
	}
	
	public static void removeMiembroFromGrupo(int id, EntidadComunicable miembro) {
		groupAdapter.eliminarMiembro(id, miembro);
		userAdapter.eliminarGrupoFromUser(miembro.getNumero(), groupAdapter.get(id));
	}
	
	public static boolean isMiembro(int numeroUser, int groupID) {
		return groupAdapter.isMiembro(numeroUser, groupID);
	}
	
	// ### noContactos
	
	public static EntidadComunicable addNoContact(EntidadComunicable noContact, int userToAddNoContact) {				
		noContactAdapter.create(noContact);
		//System.out.println("[DEBUG] el  id del no contacto es : "+ contact.getId());
		EntidadComunicable noContacto = noContactAdapter.get(noContact.getId());
		noContacto.setId(noContact.getId());
		System.out.println("[DEBUG]" + "DAOConttroller" + " anyadiendo no contacto id : " + noContacto.getId() );
		userAdapter.addNoContacto(userToAddNoContact, noContacto);
		return noContacto;
	}
	
	public static boolean isNoContact(int numeroNoContacto, int userToTest) {
		return userAdapter.isNoContact(numeroNoContacto, userToTest);
	}
	
	// ### message
	
	public static void sendMessageToContact(ModelMessage msg) {
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
	
	public static void sendMessageToGroup(ModelMessage msg, int groupID) {
		int userNum = msg.getSender();
		
		if(isMiembro(userNum, groupID)) {
			groupAdapter.addMsg(groupID, msg);
			//System.out.println("[DEBUG]" + "DAOConttroller" + " es miembro : " + userNum + " de : " + groupID);
		}
	}
	
	public static List<ModelMessage> getMessageFromAChat(EntidadComunicable contacto, int startLote, Optional<Integer> lastMsgId){
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
	
	public static List<ModelMessage> getMessageFromAGroup(Grupo grupo, int startLote, Optional<Integer> lastMsgId){
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
	
}
