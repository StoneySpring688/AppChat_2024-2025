package umu.tds.AppChat.controllers;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;
import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.backend.utils.Usuario;
import umu.tds.AppChat.backend.services.ChatService;
import umu.tds.AppChat.backend.services.ChatsAndGroupsRepository;

public class BackendController {
    private static ChatService chatService;
    private static ChatsAndGroupsRepository chatsRepository;

    public BackendController() {
        
    }
    
    public static void iniciar() {
    	chatService = new ChatService(15);
        chatsRepository = new ChatsAndGroupsRepository();
    }

    public void nuevoMensaje(int chatID, ModelMessage mensaje) {
        chatService.addMessage(chatID, mensaje);
    }

    public List<ModelMessage> obtenerMensajesChat() {
        return chatService.getMsgChatActual();
    }
    
    public static EntidadComunicable getContacto(int numero) {
    	return chatsRepository.getContacto(numero);
    }
    
    public static List<EntidadComunicable> getListaContactos() {
    	return chatsRepository.getContactos();
    }
    
    public static boolean addContact(String numero, String nombre) {
    	
    	boolean success = true;
    	int number = 0;
    	
    	try {
		    number = Integer.parseInt(numero);
		} catch (NumberFormatException e) {
		    UIController.addContactErrors((byte) 1);
			success = false;
		}
    	
    	//TODO comprobar que no sea el número del usuario
    	
    	if(number != 0 && (int) (Math.log10(Math.abs(number)) + 1) != 9) {
    		UIController.addContactErrors((byte) 1);
    		success = false;
    	}else if(chatsRepository.isContact(number)) {
    		UIController.addContactErrors((byte) 2);
    		success = false;
    	}if(nombre.length() == 0) {
    		UIController.addContactErrors((byte) 3);
    		success = false;
    	}
    	
    	if(success) {
    		String url = "/assets/ProfilePic.png"; //TODO ir a persistencia por la url de la imagen
        	EntidadComunicable contacto = new EntidadComunicable(number, nombre, url);
        	
        	boolean bdOk = true; //TODO comprobar con la persistencia
        	
        	if(bdOk) {
        		chatsRepository.addContact(contacto);
        		UIController.addChat(number);
        	}
        	
    	}
    	
    	return success;
    	
    }
    
    public static Grupo getGrupo(long id) {
    	return chatsRepository.getGrupo(id);
    }
    
    public static List<Grupo> getGrupos() {
    	return chatsRepository.getGrupos();
    }
    
    public static boolean makeGroup(String nombre, String profilepPicUrl, List<Integer> miembros) {
		boolean success = true;
		
		if(nombre.isBlank() || nombre.isEmpty()) {
			UIController.makeGroupErrors((byte) 1);
			success = false;
		}if(miembros == null || miembros.isEmpty()) {
			UIController.makeGroupErrors((byte) 2);
			success = false;
		}
		
		if(success) {
			System.out.println("[DEBUG] make Group BackendController");
	        SecureRandom secureRandom = new SecureRandom();
	        long idAleatorio;
	        //TODO añadir la comprobación con la BD
	        do{idAleatorio = 1_000_000_000L + (long) (secureRandom.nextDouble() * 9_000_000_000L);}while(chatsRepository.isGroup(idAleatorio));
	        System.out.println("[DEBUG] make Group BackendController 2 " + idAleatorio);
			chatsRepository.addGroup(new Grupo(idAleatorio, nombre, profilepPicUrl, miembros));
			UIController.addGroup(idAleatorio);
		}
		
		return success;
		
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

