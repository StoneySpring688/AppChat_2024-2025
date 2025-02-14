package umu.tds.AppChat.backend.services;

import java.util.LinkedList;
import java.util.List;

import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;

public class ChatsAndGroupsRepository {
	private ContactsList listaContactos;
	private ContactsList usuariosNoContactos;
	private GroupsList listaGrupos;
	
	public ChatsAndGroupsRepository() {
		this.listaContactos = new ContactsList();
		this.usuariosNoContactos = new ContactsList();
		this.listaGrupos = new GroupsList();
	}
	
	// contactos
	public void addContact(EntidadComunicable contacto) {
		this.listaContactos.addContact(contacto);
	}
	
	public void removeContact(int numero) {
		this.listaContactos.removeContact(numero);
	}
	
	public boolean isContact(int numero) {
		return this.listaContactos.isContact(numero);
	}
	
	public EntidadComunicable getContacto(int numero) {
		return this.listaContactos.getContacto(numero);
	}
	
	public List<EntidadComunicable> getContactos(){
		return this.listaContactos.getContactos();
	}
	
	public void loadContactList(List<EntidadComunicable> contactList) {
		this.listaContactos = new ContactsList((LinkedList<EntidadComunicable>)contactList);
	}
	
	//no contactos
	public void addNoContact(EntidadComunicable contacto) {
		this.usuariosNoContactos.addContact(contacto);
	}
	
	public void removeNoContact(int numero) {
		this.usuariosNoContactos.removeContact(numero);
	}
	
	public boolean isNotContact(int numero) {
		return this.usuariosNoContactos.isContact(numero);
	}
	
	public EntidadComunicable getNotContact(int numero) {
		return this.usuariosNoContactos.getContacto(numero);
	}
	
	public List<EntidadComunicable> getUsuariosNoContactos(){
		return this.usuariosNoContactos.getContactos();
	}
	
	/**
	 * hace contacto a un usuario de la lista de no contactos
	 */
	public void makeContact(int numero) {
		if(isNotContact(numero)) {
			addContact(getNotContact(numero));
			removeNoContact(numero);
		}
	}
	
	//grupos
	public void addGroup(Grupo grupo) {
		this.listaGrupos.addGroup(grupo);
	}
	
	public void removeGroup(long id) {
		this.listaGrupos.removeGroup(id);
	}
	
	public boolean isGroup(long id) {
		return this.listaGrupos.isMember(id);
	}
	
	public Grupo getGrupo(long id) {
		return this.listaGrupos.getGrupo(id);
	}
	
	public List<Grupo> getGrupos(){
		return this.listaGrupos.getGrupos();
	}
	
	public void loadGroupList(List<Grupo> groupList) {
		this.listaGrupos = new GroupsList((LinkedList<Grupo>)groupList);
	}
	
}
