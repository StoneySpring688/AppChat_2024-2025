package umu.tds.AppChat.dao;

import java.util.List;

import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;
import umu.tds.AppChat.backend.utils.ModelMessage;

public interface InterfaceGrupoDAO {
	void create(Grupo grupo);
	boolean delete(Grupo grupo);
	void update(Grupo grupo);
	Grupo get(int id);
	void addMiembro(int id, EntidadComunicable miembro);
	void eliminarMiembro(int id, EntidadComunicable miembro);
	List<EntidadComunicable> obtenerListaMiembros(int id);
	public boolean isMiembro(int numeroUser, int groupID);
	public void addAdmin(int id, int admin);
	public void eliminarAdmin(int id, int admin);
	public List<Integer> obtenerListaAdmins(int id);
	public boolean isAdmin(int numeroUser, int groupID);
	void addMsg(int id, ModelMessage msg);
	List<ModelMessage> obtenerListaMsg(int id);
	List<ModelMessage> obtenerListaMsgFilter(int id, String filterMsg);
	List<ModelMessage> obtenerLoteMsg(int id,  int batchSize, int origin);
	void eliminarMsgs(int id);
}
