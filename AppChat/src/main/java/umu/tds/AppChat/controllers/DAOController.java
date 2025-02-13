package umu.tds.AppChat.controllers;

import java.util.List;
import java.util.Optional;

import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Usuario;
import umu.tds.AppChat.dao.AbstractFactoriaDAO;
import umu.tds.AppChat.dao.DAOException;
import umu.tds.AppChat.dao.InterfaceContactoDAO;
import umu.tds.AppChat.dao.InterfaceGrupoDAO;
import umu.tds.AppChat.dao.InterfaceUsuarioDAO;

public class DAOController {
	private static InterfaceUsuarioDAO userAdapter;
	private static InterfaceContactoDAO contactAdapter;
	private static InterfaceGrupoDAO groupAdapter;
	
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
	
	// ### contacts
	
	public static EntidadComunicable addContact(EntidadComunicable contact) {				
		if(isUser(contact.getNumero())) {
			//System.out.println("[DEBUG] es usuario");
			contactAdapter.create(contact);
			//System.out.println("[DEBUG] el  id del contacto es : "+ contact.getId());
			EntidadComunicable contacto = contactAdapter.get(contact.getId());
			contacto.setId(contact.getId());
			System.out.println("[DEBUG]  anyadiendo contacto id : " + contacto.getId() );
			userAdapter.addContacto(BackendController.getUserNumber(), contacto);
			return contacto;
		}
		return null;
	}
	
	public static EntidadComunicable recuperarContacto(int id) {
		return contactAdapter.get(id);
	}
	
	public static void removeContact(EntidadComunicable contact) {
		userAdapter.eliminarContacto(BackendController.getUserNumber(), contact);
		contactAdapter.delete(contact);
	}
	
	public static void actualizarContacto(EntidadComunicable contact) {
		contactAdapter.update(contact);
	}
	
	public static List<EntidadComunicable> getListaContactos(int numero){
		return userAdapter.obtenerListaContactos(numero);
	}
	
	// ### groups
	
}
