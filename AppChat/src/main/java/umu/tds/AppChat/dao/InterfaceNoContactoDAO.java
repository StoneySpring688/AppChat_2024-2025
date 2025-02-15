package umu.tds.AppChat.dao;

import java.util.List;

import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.ModelMessage;

public interface InterfaceNoContactoDAO {
	void create(EntidadComunicable noContacto);
	boolean delete(EntidadComunicable noContacto);
	void update(EntidadComunicable noContacto);
	EntidadComunicable get(int id);
	void addMsg(int id, ModelMessage msg);
	List<ModelMessage> obtenerListaMsg(int id);
	List<ModelMessage> obtenerLoteMsg(int id,  int batchSize, int origin);
	void eliminarMsgs(int id);
}
