package umu.tds.AppChat.backend.utils;

import java.net.URL;
import javax.swing.ImageIcon;

public class EntidadComunicable {
	
	private int numero;
	private String nombre;
	private String IconUrl;
	
	public EntidadComunicable(int numero, String nombre){
		this.numero = numero;
		this.nombre = nombre;
	}

	public int getNumero() {
		return numero;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getIconUrl() {
		return IconUrl;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setIconUrl(String iconUrl) {
		IconUrl = iconUrl;
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
	        	url = new URL("/assets/ProfilePic.png");
	            imagen = new ImageIcon(url);
	            return imagen;

	        }
	        
		} catch (Exception e) {
			ImageIcon imagen = new ImageIcon(EntidadComunicable.class.getResource("/assets/ProfilePic.png"));
            return imagen;

			}
    }
	
}
