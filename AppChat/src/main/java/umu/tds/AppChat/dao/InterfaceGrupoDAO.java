package umu.tds.AppChat.dao;

import umu.tds.AppChat.backend.utils.Grupo;

public interface InterfaceGrupoDAO {
	void create(Grupo grupo);
	boolean delete(Grupo grupo);
	void update(Grupo grupo);
	Grupo get(int id);
}
