package umu.tds.AppChat.controllers;

import java.util.List;

import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.ModelMessage;
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
    	
    	String url = "/assets/ProfilePic.png"; //TODO ir a persistenncia por la url  de la imagen
    	EntidadComunicable contacto = new EntidadComunicable(numero, nombre, url);
    	
    	contactList.addContact(contacto); //TODO comprobar con la persistencia
    	
    	return true;
    	
    }
    
    public static EntidadComunicable getContacto(int numero) {
    	return contactList.getContacto(numero);
    }
}

