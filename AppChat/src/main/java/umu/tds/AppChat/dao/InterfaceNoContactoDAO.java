package umu.tds.AppChat.dao;

import umu.tds.AppChat.backend.utils.EntidadComunicable;

public interface InterfaceNoContactoDAO {
	void create(EntidadComunicable noContacto);
	boolean delete(EntidadComunicable noContacto);
	void update(EntidadComunicable noContacto);
	EntidadComunicable get(int id);
}
