package umu.tds.AppChat.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.backend.utils.Usuario;
import umu.tds.AppChat.backend.services.ChatService;
import umu.tds.AppChat.backend.services.ContactsList;

public class BackendController {
    private static ChatService chatService;
    private static ContactsList contactList;

    public BackendController() {
        chatService = new ChatService(15);
        contactList  = new ContactsList();
    }

    public void nuevoMensaje(int chatID, ModelMessage mensaje) {
        chatService.addMessage(chatID, mensaje);
    }

    public List<ModelMessage> obtenerMensajesChat() {
        return chatService.getMsgChatActual();
    }
    
    public static boolean addContact(int numero, String nombre) {
    	
    	if(contactList.isContact(numero)) return false;
    	
    	String url = "/assets/ProfilePic.png"; //TODO ir a persistencia por la url de la imagen
    	EntidadComunicable contacto = new EntidadComunicable(numero, nombre, url);
    	
    	contactList.addContact(contacto); //TODO comprobar con la persistencia
    	
    	return true;
    	
    }
    
    public static EntidadComunicable getContacto(int numero) {
    	return contactList.getContacto(numero);
    }
    
    protected static boolean doRegister(String name, String number, String passwd, String birthDate, String profilePicUrl, String signature) {
		//System.out.println("[DEBUG] doRegister BackendControoller");
		boolean success = true;
    	
    	if(name.isBlank() || name.isEmpty()) {
    		//System.out.println("[DEBUG] doRegister BackendControoller 1");
    		UIController.registerErrors((byte) 1);
    		success = false;
    	} if(passwd ==  null) {
    		//System.out.println("[DEBUG] doRegister BackendControoller 2");
    		UIController.registerErrors((byte) 2);
    		success = false;
    	}else if(passwd.length() < 5){
    		//System.out.println("[DEBUG] doRegister BackendControoller 3");
    		UIController.registerErrors((byte) 3);
    		success = false;
    	} if(number.isBlank() || number.isEmpty()){
    		//System.out.println("[DEBUG] doRegister BackendControoller 4");
    		UIController.registerErrors((byte) 4);
    	} if(birthDate == null || birthDate.isBlank() || birthDate.isEmpty()) {
    		//System.out.println("[DEBUG] doRegister BackendControoller 5");
    		UIController.registerErrors((byte) 5);
    		success = false;
    	}
    	
    	try {
			int numero = Integer.parseInt(number);
			LocalDate BirthDate = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			Usuario newUser = new Usuario(name, numero, passwd, BirthDate, profilePicUrl, signature);
			//TODO escribir newUser en la BD
			
		} catch (Exception e) {
    		//System.out.println("[DEBUG] doRegister BackendControoller 6");
			UIController.registerErrors((byte) 4);
			success = false;
		}
    	return success;
    }
}

