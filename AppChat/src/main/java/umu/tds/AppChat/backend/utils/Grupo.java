package umu.tds.AppChat.backend.utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * @implNote cada grupo tiene un identificador de 10 dígitos
 */
public class Grupo  {
	
	//long numeroAleatorio = ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L);
		
	private int DBID; // id asignado por la bd
	
	private long groupID;
	private String nombre;
	private String iconUrl;
	private List<EntidadComunicable> integrantes;
	private List<Integer> admins;
		
	public Grupo(long groupID, String nombre, String iconUrl){
		this.groupID = groupID;
		this.nombre = nombre;
		this.iconUrl = iconUrl;
		this.integrantes =  new ArrayList<EntidadComunicable>();
		this.admins = new ArrayList<Integer>();
	}
	
	public Grupo(long groupID, String nombre, String iconUrl, List<EntidadComunicable> miembros, List<Integer> admins){
		this.groupID = groupID;
		this.nombre = nombre;
		this.iconUrl = iconUrl;
		this.integrantes =  new ArrayList<EntidadComunicable>(miembros);
		this.admins = new ArrayList<Integer>(admins);
	}

	public Grupo(Grupo grupo) {
		this.groupID = grupo.getID();
		this.nombre = grupo.getNombre();
		this.iconUrl = grupo.getIconUrl();
		this.integrantes = grupo.getIntegrantes();
		this.admins = grupo.getAdmins();
		this.DBID = grupo.getDBID();
	}
	
	public int getDBID() {
		return DBID;
	}
	
	public long getID() {
		return this.groupID;
	}
		
	public String getNombre() {
		return new String(this.nombre);
	}
		
	public String getIconUrl() {
		return new String(this.iconUrl);
	}
	
	public List<EntidadComunicable> getIntegrantes(){
		return new ArrayList<EntidadComunicable>(this.integrantes);
	}
	
	public List<Integer> getAdmins(){
		return new ArrayList<Integer>(this.admins);
	}
	
	public void setDBID(int dBID) {
		DBID = dBID;
	}
	
	public void setGroupID(long groupID) {
		this.groupID = groupID;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
		
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
	public void setIntegrantes(List<EntidadComunicable> list) {
		this.integrantes = new ArrayList<EntidadComunicable>(list);
	}
	
	public void addIntegrante(EntidadComunicable miembro) {
		if(!this.isIntegrante(miembro)) this.integrantes.add(miembro);
	}
	
	public void removeIntegrante(EntidadComunicable miembro) {
		this.integrantes.removeIf(k -> k.equals(miembro));
	}
	
	public boolean isIntegrante(EntidadComunicable miembro) {
		return this.integrantes.stream().anyMatch(k -> k.equals(miembro));
	}
	
	public void addAdmin(int numero) {
		if(!this.isAdmin(numero)) this.admins.add(numero);
	}
	
	public void removeAdmin(int numero) {
		this.admins.removeIf(k -> k.equals(numero));
	}
	
	public boolean isAdmin(int numero) {
		return this.admins.stream().anyMatch(k -> k.equals(numero));
	}

	public ImageIcon actualizarImagenFromUrl() {
		URL url;
	        
		try {
			url = new URL(this.getIconUrl());
			ImageIcon imagen = new ImageIcon(url);
				
		    if (imagen.getIconWidth() > 0 && imagen.getIconHeight() > 0) { // Verifica que la imagen sea válida
		    	//System.out.println("Imagen cargada exitosamente.");
		        return imagen;
		            
		    } else {
		        //System.out.println("Texto ingresado no contiene una imagen válida."); 
		    	return new ImageIcon(getClass().getResource("/assets/ProfilePic.png"));

		    }
		        
		} catch (Exception e) {
			return new ImageIcon(getClass().getResource("/assets/ProfilePic.png"));

			}
	}
	
	@Override
	public String toString() {
	    return "Grupo{" +
	            "nombre='" + getNombre() + '\'' +
	            ", groupID=" + getID() +
	            ", iconUrl='" + getIconUrl() + '\'' +
	            ", integrantes=" + getIntegrantes() +
	            ", admins=" + getAdmins() +
	            '}';
	    }
	
}
