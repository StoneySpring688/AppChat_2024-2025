package umu.tds.AppChat.backend.services;

import java.util.LinkedList;
import java.util.List;
import umu.tds.AppChat.backend.utils.Grupo;

public class GroupsList {
	private List<Grupo> listaGrupos;
	
	public GroupsList() {
		this.listaGrupos =  new LinkedList<Grupo>();
	}
	
	public GroupsList(LinkedList<Grupo> listaGrupos) {
		this.listaGrupos =  new  LinkedList<Grupo>(listaGrupos);
	}
	
	public void addGroup(Grupo grupo) {
		this.listaGrupos.add(grupo);
	}
	
	public void removeGroup(long id) {
		this.listaGrupos.removeIf(k -> k.getID() == id);
	}
	
	public boolean isMember(long id) {
		return this.listaGrupos.stream().anyMatch(k -> k.getID() ==  id);
	}
	
	public Grupo getGrupo(long id) {
		return this.listaGrupos.stream().filter(k -> k.getID() == id).findFirst().get();
	}
	
	public List<Grupo> getGrupos(){
		return List.copyOf(this.listaGrupos);
	}
}
