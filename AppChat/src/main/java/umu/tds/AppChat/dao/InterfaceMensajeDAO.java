package umu.tds.AppChat.dao;

import umu.tds.AppChat.backend.utils.ModelMessage;

public interface InterfaceMensajeDAO {
	void create(ModelMessage msg);
	boolean delete(ModelMessage msg);
	//void update(ModelMessage msg);
	ModelMessage get(int id);
}
