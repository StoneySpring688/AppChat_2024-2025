package umu.tds.AppChat.dao;

import java.util.List;

import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.ModelMessage;

public interface InterfaceContactoDAO {
	void create(EntidadComunicable contacto);
	boolean delete(EntidadComunicable contacto);
	void update(EntidadComunicable contacto);
	EntidadComunicable get(int id);
	void addMsg(int id, ModelMessage msg);
	void addMsgs(int  id, String msgs);
	List<ModelMessage> obtenerListaMsg(int id);
	List<ModelMessage> obtenerLoteMsg(int id,  int batchSize, int origin);
	void eliminarMsgs(int id);
}
