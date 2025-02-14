package umu.tds.AppChat.dao;

import java.util.List;

import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;

public interface InterfaceGrupoDAO {
	void create(Grupo grupo);
	boolean delete(Grupo grupo);
	void update(Grupo grupo);
	Grupo get(int id);
	void addMiembro(int id, EntidadComunicable miembro);
	void eliminarMiembro(int id, EntidadComunicable miembro);
	List<EntidadComunicable> obtenerListaMiembros(int id);
}
