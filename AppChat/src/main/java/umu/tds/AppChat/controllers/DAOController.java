package umu.tds.AppChat.controllers;

import java.util.Optional;

import umu.tds.AppChat.backend.utils.Usuario;
import umu.tds.AppChat.dao.AbstractFactoriaDAO;
import umu.tds.AppChat.dao.DAOException;
import umu.tds.AppChat.dao.InterfaceUsuarioDAO;

public class DAOController {
	private static InterfaceUsuarioDAO userAdapter;
	
	protected static void initAdapters() {
		AbstractFactoriaDAO factoria = null;
		try {
			factoria = AbstractFactoriaDAO.getInstancia(AbstractFactoriaDAO.DAO_TDS);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		userAdapter = factoria.getUsuarioDAO();
	}
	
	public static void registerUser(Usuario user) {
		if(recuperarUser(user.getNumero()).isEmpty())userAdapter.create(user);
	}
	
	/**
	 * @return si no  existe el usuario devuelbe Optional.empty()*/
	public static Optional<Usuario> recuperarUser(int id){
		Usuario userDevuelto = userAdapter.get(id);
		//System.out.println("[DEBUG] usuario devuelto : "+userDevuelto );
		return userDevuelto != null ? Optional.of(userDevuelto) : Optional.empty();
		
	}
	
	public static boolean checkLogin(int id, String passwd) {
		return userAdapter.checkLogin(id, passwd);
	}
	
}
