package umu.tds.AppChat.dao;

import umu.tds.AppChat.backend.utils.EntidadComunicable;

public interface InterfaceContactoDAO {
	void create(EntidadComunicable contacto);
	boolean delete(EntidadComunicable contacto);
	void update(EntidadComunicable contacto);
	EntidadComunicable get(int id);
}
