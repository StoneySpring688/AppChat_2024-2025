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
		if(recuperarUser(user).isEmpty())userAdapter.create(user);
	}
	
	public static Optional<Usuario> recuperarUser(Usuario user){
		Usuario userDevuelto = userAdapter.get(user.getNumero());
		System.out.println("[DEBUG] usuario devuelto : "+userDevuelto );
		return userDevuelto != null ? Optional.of(userDevuelto) : Optional.empty();
		
	}
	
}
