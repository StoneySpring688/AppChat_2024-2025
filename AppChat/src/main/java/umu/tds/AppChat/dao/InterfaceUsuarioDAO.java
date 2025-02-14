package umu.tds.AppChat.dao;

import java.util.List;

import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;
import umu.tds.AppChat.backend.utils.Usuario;

public interface InterfaceUsuarioDAO {
	
	void create(Usuario usuario);
	boolean delete(Usuario usuario);
	void update(Usuario usuario);
	Usuario get(int id);
	boolean checkLogin(int id, String passwd);
	void addContacto(int number, EntidadComunicable contact);
	void eliminarContacto(int number, EntidadComunicable contact);
	List<EntidadComunicable> obtenerListaContactos(int numero);
	public boolean isContact(int numeroContacto, int userToTest);
	void addGrupoToUser(int number, Grupo grupo);
	void eliminarGrupoFromUser(int number, Grupo grupo);
	List<Grupo> obtenerListaGruposFromUser(int numero);
	void addNoContacto(int number, EntidadComunicable noContact);
	void eliminarNoContacto(int number, EntidadComunicable noContact);
	List<EntidadComunicable> obtenerListaNoContactos(int numero);
	boolean isNoContact(int numeroNoContacto, int userToTest);
}
