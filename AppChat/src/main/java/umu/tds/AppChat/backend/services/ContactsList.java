package umu.tds.AppChat.backend.services;

import java.util.LinkedList;
import java.util.List;

import umu.tds.AppChat.backend.utils.EntidadComunicable;

public class ContactsList {
	private List<EntidadComunicable> listaContactos;
	
	public ContactsList() {
		this.listaContactos = new LinkedList<EntidadComunicable>();
	}
	
	public ContactsList(LinkedList<EntidadComunicable> listaContactos) {
		this.listaContactos = new LinkedList<EntidadComunicable>(listaContactos);
	}
	
	public void addContact(EntidadComunicable contacto) {
		this.listaContactos.add(contacto);
	}
	
	public void removeContact(int numero) {
		this.listaContactos.removeIf(k -> k.getNumero() == numero);
	}
	
	public boolean isContact(int numero) {
		return this.listaContactos.stream().anyMatch(k -> k.getNumero() ==  numero);
	}
	
	public EntidadComunicable getContacto(int numero) {
		return this.listaContactos.stream().filter(k -> k.getNumero() == numero).findFirst().get();
	}
	
	public List<EntidadComunicable> getContactos(){
		return List.copyOf(this.listaContactos);
	}
	
}
