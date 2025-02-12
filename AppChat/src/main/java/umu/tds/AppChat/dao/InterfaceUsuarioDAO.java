package umu.tds.AppChat.dao;

import umu.tds.AppChat.backend.utils.Usuario;

public interface InterfaceUsuarioDAO {
	
	void create(Usuario usuario);
	boolean delete(Usuario usuario);
	void update(Usuario usuario);
	Usuario get(int id);
	boolean checkLogin(int id, String passwd);
	
}
