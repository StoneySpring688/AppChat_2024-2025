package umu.tds.AppChat.backend.utils;

import java.net.URL;
import javax.swing.ImageIcon;

public class EntidadComunicable {
	
	private int numero;
	private String nombre;
	private String iconUrl;
	
	public EntidadComunicable(int numero, String nombre, String iconUrl){
		this.numero = numero;
		this.nombre = nombre;
		this.iconUrl = iconUrl;
	}
	
	public EntidadComunicable(EntidadComunicable ent){
		this.numero = ent.getNumero();
		this.nombre = ent.getNombre();
		this.iconUrl = ent.getIconUrl();
	}

	public int getNumero() {
		return this.numero;
	}
	
	public String getNombre() {
		return new String(this.nombre);
	}
	
	public String getIconUrl() {
		return new String(this.iconUrl);
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
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
	
}
