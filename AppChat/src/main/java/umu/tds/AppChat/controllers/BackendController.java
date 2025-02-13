package umu.tds.AppChat.controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.ImageIcon;

import umu.tds.AppChat.backend.utils.Emoji;
import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;
import umu.tds.AppChat.backend.utils.Membership;
import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.backend.utils.Usuario;
import umu.tds.AppChat.backend.services.ChatService;
import umu.tds.AppChat.backend.services.ChatsAndGroupsRepository;

public class BackendController {
    private static ChatService chatService;
    private static ChatsAndGroupsRepository chatsRepository;
    private static Usuario user;
    private static ImageIcon userIconCached;
    private static List<Membership> ofertas;

    public BackendController() {
        
    }
    
    public static void iniciar() {
    	chatService = new ChatService(15);
        chatsRepository = new ChatsAndGroupsRepository();
        Emoji.cargarEmojis();
    }
    
    // ### ofertas
    
    public static List<Membership> getOfertas() {
    	return new ArrayList<Membership>(ofertas);
    }
    
    public static void loadOfertas(List<Membership> listOfertas) {
    	ofertas = new ArrayList<Membership>(listOfertas);
    }
    
    // ### usuario
    
    public static int getUserNumber() {
    	return user.getNumero();
    }
    
    public static Optional<LocalDate> getEndPremium(){
    	return user.getEndPremiumDate();
    }
    
    public static String getUserName() {
    	return new String(user.getNombre());
    }
    
    public static String getUserIconUrl() {
    	return new String(user.getIconUrl());
    }
    
    public static ImageIcon getUserIcon() {
    	return new ImageIcon(userIconCached.getImage());
    }
    
    public static void makePremiumUser() {
    	user.mekePremium();
    }

    public static void loadCurrentUser(Usuario usuario) {
    	user = new Usuario(usuario, true);        
    	URL url;
        userIconCached = null;
        String profilePicUrl = BackendController.getUserIconUrl();
		try {
			url = new URL(profilePicUrl);
			userIconCached = new ImageIcon(url);
			
	        if (userIconCached.getIconWidth() <= 0 && userIconCached.getIconHeight() <= 0) { // Verifica si la imagen no es válida         
	            //System.out.println("Imagen inválida.");
	        	userIconCached = new ImageIcon(UIController.class.getResource("/assets/ProfilePic.png"));
	        }
	        
		} catch (MalformedURLException e) {
			userIconCached = new ImageIcon(UIController.class.getResource("/assets/ProfilePic.png"));
		}
    }
    
    public static Usuario getCurrentUser() {
    	return new Usuario(user, false);
    }
    
    public static void removeCurrentUser() {
    	user = null;
    	userIconCached = null;
    }
    
    // ### mensajes
    
    public static void nuevoMensaje(long chatID, ModelMessage mensaje) {
        chatService.addMessage(chatID, mensaje);
        //System.out.println(obtenerMensajesChat().get(obtenerMensajesChat().size() - 1));
    }
    
    public static List<ModelMessage> obtenerMensajesChat() {
        return chatService.getMsgChatActual();
    }
    
    public static List<ModelMessage> getChat(long chatID){
    	return chatService.getLRUChat(chatID);
    }
    
    // ### contactos
    
    public static void loadContactList(List<EntidadComunicable> contactList) {
    	chatsRepository.loadContactList(contactList);
    }
    
    public static EntidadComunicable getContacto(int numero) {
    	return chatsRepository.getContacto(numero);
    }
    
    public static List<EntidadComunicable> getListaContactos() {
    	return chatsRepository.getContactos();
    }
    
    public static boolean isContact(int numero) {
    	return chatsRepository.isContact(numero);
    }
    
    public static void addContact(EntidadComunicable contact) { 	
        		chatsRepository.addContact(contact);
    }
    
    // ### grupos
    
    public static Grupo getGrupo(long id) {
    	return chatsRepository.getGrupo(id);
    }
    
    public static List<Grupo> getGrupos() {
    	return chatsRepository.getGrupos();
    }
    
    public static long makeGroup(String nombre, String profilepPicUrl, List<EntidadComunicable> miembros) {
		
	   SecureRandom secureRandom = new SecureRandom();
	   long idAleatorio;
	   //TODO añadir la comprobación con la BD
	   do{idAleatorio = 1_000_000_000L + (long) (secureRandom.nextDouble() * 9_000_000_000L);}while(chatsRepository.isGroup(idAleatorio));
	   chatsRepository.addGroup(new Grupo(idAleatorio, nombre, profilepPicUrl, miembros));
	   
		
		return idAleatorio;
		
    }
    
    // ### registro
    
    protected static Optional <Usuario> doRegister(String name, String number, String passwd, String birthDate, String profilePicUrl, String signature) {
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
    	
    	if(success) {
        	try {
    			int numero = Integer.parseInt(number);
    			LocalDate BirthDate = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    			Usuario newUser = new Usuario(name, numero, passwd, BirthDate, profilePicUrl, signature);
    			return Optional.of(newUser);
    			
    		} catch (Exception e) {
        		//System.out.println("[DEBUG] doRegister BackendControoller 6");
    			UIController.registerErrors((byte) 4);
    			success = false;
    			return Optional.empty();
    		}
    	}
    	
    	return Optional.empty();
    	
    }
    
}

