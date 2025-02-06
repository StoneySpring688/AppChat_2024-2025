package umu.tds.AppChat.backend.utils;

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import umu.tds.AppChat.config.Config;
import umu.tds.AppChat.ui.chatInterface.Button;

public class Emoji extends Button{
	private static final long serialVersionUID = 1L;
	public static List<Emoji> emojiList = new ArrayList<Emoji>();
	
	private final int id;
	
	public Emoji(int id) {
		super();
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public static void cargarEmojis() {
		int id = 0;
        for (String fileName : Config.emojiFiles) {
        	ImageIcon icon = cargarIconoEmoji(fileName);
        	if (icon == null) continue;
        	Emoji emojiButton = new Emoji(id);
        	emojiButton.setIcon(icon);
        	emojiList.add(emojiButton);
        	id++;
        }
	}
	
	private static ImageIcon cargarIconoEmoji(String fileName) {
    	
        try {
            // Obtener el recurso desde la carpeta "emojis"
            URL imageUrl = Emoji.class.getClassLoader().getResource("emojis/" + fileName);
            if (imageUrl == null) {
                //System.err.println("No se encontró el archivo: " + fileName); //debug
                return null;
            }

            // Crear el ImageIcon
            ImageIcon icon = new ImageIcon(imageUrl);
            Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            //System.out.println("Archivo cargado exitosamente: " + fileName); //debug
            return scaledIcon;
        } catch (Exception e) {
            //System.err.println("Error al cargar el archivo: " + fileName); //debug
            e.printStackTrace();
            return null;
        }
    }
	
}
