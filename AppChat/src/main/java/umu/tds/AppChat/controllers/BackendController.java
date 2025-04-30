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

import org.slf4j.Logger;

import umu.tds.AppChat.backend.utils.Emoji;
import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.GeneradorPDF;
import umu.tds.AppChat.backend.utils.Grupo;
import umu.tds.AppChat.backend.utils.Membership;
import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.backend.utils.Usuario;
import umu.tds.AppChat.devtools.LoggerUtil;
import umu.tds.AppChat.backend.services.ChatService;
import umu.tds.AppChat.backend.services.ChatsAndGroupsRepository;

/**
 * Controlador del backend de la aplicación.
 *
 * <p>Encargado de la lógica de negocio interna, incluyendo:
 * <ul>
 *   <li>Gestión de usuarios y su sesión actual.</li>
 *   <li>Caché de mensajes y contactos en memoria.</li>
 *   <li>Manejo de grupos y contactos (localmente).</li>
 *   <li>Exportación de mensajes y estructura de datos a PDF.</li>
 * </ul>
 *
 * <p>Utiliza servicios auxiliares como {@code ChatService} y {@code ChatsAndGroupsRepository}
 * para optimizar operaciones de acceso rápido y minimizar la dependencia directa de la capa DAO.
 *
 * <p>Implementado como Singleton para asegurar una única instancia activa.
 *
 * @author StoneySpring
 */

public class BackendController {
    private ChatService chatService;
    private ChatsAndGroupsRepository chatsRepository;
    private Usuario user;
    private ImageIcon userIconCached;
    private List<Membership> ofertas;
    
    // logger
    private static final Logger logger = LoggerUtil.getLogger(MainController.class);
    
    // singleton
    private static BackendController unicaInstancia = null;

    public static BackendController getUnicaInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new BackendController();
        }
        return unicaInstancia;
    }
    
    private BackendController() {
    	// iniciar(); evita bucle
    }
    
    public void iniciar() {
    	logger.info("Inicializando BackendController: servicios y repositorios.");

    	chatService = new ChatService(15);
        chatsRepository = new ChatsAndGroupsRepository();
        Emoji.cargarEmojis();
    }
    
    protected void doLogout() {
    	logger.debug("Cerrando sesión en BackendController. Reiniciando estado.");

    	
    	//System.out.println("backend do logout");
    	
    	chatService = new ChatService(15);
    	chatsRepository.reset();
    	removeCurrentUser();
    } 
    
    // ### ofertas
    
    public List<Membership> getOfertas() {
    	return new ArrayList<Membership>(ofertas);
    }
    
    public void loadOfertas(List<Membership> listOfertas) {
    	logger.debug("Cargando lista de ofertas ({} elementos).", listOfertas.size());

    	ofertas = new ArrayList<Membership>(listOfertas);
    }
    
    // ### usuario
    
    public int getUserNumber() {
    	return user.getNumero();
    }
    
    public Optional<LocalDate> getEndPremium(){
    	return user.getEndPremiumDate();
    }
    
    public String getUserName() {
    	return new String(user.getNombre());
    }
    
    public String getUserIconUrl() {
    	return new String(user.getIconUrl());
    }
    
    public ImageIcon getUserIcon() {
    	return new ImageIcon(userIconCached.getImage());
    }
    
    public void makePremiumUser() {
    	user.mekePremium();
    }

    public void loadCurrentUser(Usuario usuario) {
    	logger.info("Cargando datos del usuario actual: {} ({})", usuario.getNombre(), usuario.getNumero());

    	user = new Usuario(usuario, false);        
    	URL url;
        userIconCached = null;
        String profilePicUrl = this.getUserIconUrl();
		try {
			url = new URL(profilePicUrl);
			userIconCached = new ImageIcon(url);
			
	        if (userIconCached.getIconWidth() <= 0 && userIconCached.getIconHeight() <= 0) { // Verifica si la imagen no es válida         
	            //System.out.println("Imagen inválida.");
	        	userIconCached = new ImageIcon(UIController.class.getResource("/assets/ProfilePic.png"));
	        }
	        
		} catch (MalformedURLException e) {
			logger.warn("URL de imagen de perfil inválida. Usando imagen por defecto.");

			userIconCached = new ImageIcon(UIController.class.getResource("/assets/ProfilePic.png"));
		}
		
		//System.out.println("[DEBUG] " + "BackendController " + "usuario cargado : " + usuario.getNombre() +" , " + usuario.getNumero());
		
    }
    
    public boolean currentUserIsPremium() {
    	return user.isPremium();
    }
    
    public Usuario getCurrentUser() {
    	return new Usuario(user, false);
    }
    
    public void removeCurrentUser() {
    	user = null;
    	userIconCached = null;
    }
    
    // ### mensajes
    
    public void nuevoMensaje(long chatID, ModelMessage mensaje) {
    	logger.debug("Registrando nuevo mensaje para chat ID: {}", chatID);

        chatService.addMessage(chatID, mensaje);
        //System.out.println(obtenerMensajesChat().get(obtenerMensajesChat().size() - 1));
    }
    
    public void nuevosMensajes(long chatID, List<ModelMessage> mensajes) {
    	logger.debug("Añadiendo {} nuevos mensajes al chat ID: {}", mensajes.size(), chatID);

    	for(ModelMessage mensaje : mensajes) {
    		nuevoMensaje(chatID, mensaje);
    	}   
        //System.out.println(obtenerMensajesChat().get(obtenerMensajesChat().size() - 1));
    }
    
    public void nuevosMensajesAlInicio(long chatID, List<ModelMessage> mensajes) {
    	logger.debug("nuevosMensajesAlInicio() - tamaño del lote: {}, chatID: {}", mensajes.size(), chatID);
    	
        if (mensajes == null || mensajes.isEmpty()) {
            return;
        }
        /*
        List<ModelMessage> mensajesEnCache = chatService.getLRUChat(chatID);
        
        if (mensajesEnCache.isEmpty()) {
            nuevosMensajes(chatID, mensajes); // Si la caché está vacía, simplemente añadimos los mensajes
        } else {
            List<ModelMessage> nuevaLista = new ArrayList<>(mensajes);
            nuevaLista.addAll(mensajesEnCache); // Añadir los mensajes existentes al final de los nuevos
            
            nuevosMensajes(chatID, nuevaLista); // Guardar la lista actualizada
        }*/
        chatService.addMessageAlInicio(chatID, mensajes);
    }
    
    public List<ModelMessage> obtenerMensajesChat() {
        return chatService.getMsgChatActual();
    }
    
    public List<ModelMessage> getChat(long chatID){
    	logger.debug("Obteniendo mensajes de caché para chat ID: {}", chatID);

    	//List<ModelMessage> lista = chatService.getLRUChat(chatID);
    	//System.out.println("[DEBUG]" + " backendController " + "lista de : " + lista.size() + " chats");
    	
    	return chatService.getLRUChat(chatID);
    }
    
    // ### contactos
    
    public void loadContactList(List<EntidadComunicable> contactList) {
    	logger.debug("Cargando lista de contactos ({} elementos).", contactList.size());

    	chatsRepository.loadContactList(contactList);
    }
    
    public EntidadComunicable getContacto(int numero) {
    	return chatsRepository.getContacto(numero);
    }
    
    public List<EntidadComunicable> getListaContactos() {
    	logger.debug("Obteniendo lista de contactos ({} elementos).", chatsRepository.getContactos().size());
    	
    	//System.out.println("num contactos : " + chatsRepository.getContactos().size());
    	return chatsRepository.getContactos();
    }
    
    public boolean isContact(int numero) {
    	return chatsRepository.isContact(numero);
    }
    
    public void addContact(EntidadComunicable contact) { 	
        	chatsRepository.addContact(contact);
    }
    
    public void removeUser(int numero) {
    	chatsRepository.removeContact(numero);
    }
    
    // ### noContactos
    
    public void loadNoContactList(List<EntidadComunicable> noContactList) {
    	logger.debug("Cargando lista de noContactos ({} elementos).", noContactList.size());

    	chatsRepository.loadNoContactList(noContactList);
    }
    
    public EntidadComunicable getNoContacto(int numero) {
    	return chatsRepository.getNotContact(numero);
    }
    
    public List<EntidadComunicable> getListaNoContactos(){
    	logger.debug("Obteniendo lista de noContactos ({} elementos).", chatsRepository.getUsuariosNoContactos().size());
    	
    	//System.out.println("num no contactos : " + chatsRepository.getUsuariosNoContactos().size());
    	return chatsRepository.getUsuariosNoContactos();
    }
    
    public boolean isNoContact(int numero) {
    	return chatsRepository.isNotContact(numero);
    }
    
    public void addNoContact(EntidadComunicable noContact) {
    	chatsRepository.addNoContact(noContact);
    }
    
    public void makeContactFromNoContact(int numero) {
    	chatsRepository.makeContact(numero);
    }
    
    // ### grupos
    
    public void loadGroupList(List<Grupo> groupList) {
    	logger.debug("Cargando lista de grupos ({} elementos).", groupList.size());

    	chatsRepository.loadGroupList(groupList);
    }
    
    public Grupo getGrupo(long id) {
    	return chatsRepository.getGrupo(id);
    }
    
    public List<Grupo> getGrupos() {
    	return chatsRepository.getGrupos();
    }
    
    public long makeGroup(String nombre, String profilepPicUrl, List<EntidadComunicable> miembros) {
    	
	   SecureRandom secureRandom = new SecureRandom();
	   long idAleatorio;
	   // como no es una bd hecha a medida no se puede comprobar el id de forma eficiente, se seguirá usando porque está hecha la implementación, pero si no se migra a otra bd su uso será solo parcial
	   do{idAleatorio = 1_000_000_000L + (long) (secureRandom.nextDouble() * 9_000_000_000L);}while(chatsRepository.isGroup(idAleatorio));
	   List<Integer> admins = new ArrayList<Integer>();
	   ///admins.add(getUserNumber());
	   chatsRepository.addGroup(new Grupo(idAleatorio, nombre, profilepPicUrl, miembros, admins));
	   
	   logger.debug("Creando nuevo grupo: {} con ID generado: {}", nombre, idAleatorio);

		return idAleatorio;
		
    }
    
    public boolean isAdmin(long groupID, int numero) {
    	return getGrupo(groupID).isAdmin(numero);
    }
    
    public boolean isGroup(long groupID) {
    	return chatsRepository.isGroup(groupID);
    }
    
    public void addMiembroToGrupo(long id, EntidadComunicable miembro) {
    	chatsRepository.getGrupo(id).addIntegrante(miembro);
    }
    
    public void removeMiembroFromGrupo(long id, EntidadComunicable miembro) {
    	chatsRepository.getGrupo(id).removeIntegrante(miembro);
    }
    
    public void addAdminToGrupo(long groupId, int numeroAdmin) {
    	getGrupo(groupId).addAdmin(numeroAdmin);
    }
    
    public void removeAdminFromGrupo(long groupId, int numeroAdmin) {
    	getGrupo(groupId).removeAdmin(numeroAdmin);
    }
    
    public void removeGrupo(long id) {
    	chatsRepository.removeGroup(id);
    }
    
    // ### registro
    
    protected Optional <Usuario> doRegister(String name, String number, String passwd, String birthDate, String profilePicUrl, String signature) {
    	logger.debug("Intentando registrar usuario: {}", name);
    	
    	//System.out.println("[DEBUG] doRegister BackendControoller");
		boolean success = true;
    	
    	if(name.isBlank() || name.isEmpty()) {
    		//System.out.println("[DEBUG] doRegister BackendControoller 1");
    		UIController.getUnicaInstancia().registerErrors((byte) 1);
    		success = false;
    	} if(passwd ==  null) {
    		//System.out.println("[DEBUG] doRegister BackendControoller 2");
    		UIController.getUnicaInstancia().registerErrors((byte) 2);
    		success = false;
    	}else if(passwd.length() < 5){
    		//System.out.println("[DEBUG] doRegister BackendControoller 3");
    		UIController.getUnicaInstancia().registerErrors((byte) 3);
    		success = false;
    	} if(number.isBlank() || number.isEmpty() || number.length() != 9){
    		//System.out.println("[DEBUG] doRegister BackendControoller 4");
    		UIController.getUnicaInstancia().registerErrors((byte) 4);
    	} if(birthDate == null || birthDate.isBlank() || birthDate.isEmpty()) {
    		//System.out.println("[DEBUG] doRegister BackendControoller 5");
    		UIController.getUnicaInstancia().registerErrors((byte) 5);
    		success = false;
    	}
    	
    	if(success) {
        	try {
    			int numero = Integer.parseInt(number);
    			LocalDate BirthDate = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    			Usuario newUser = new Usuario(name, numero, passwd, BirthDate, profilePicUrl, signature);
    			return Optional.of(newUser);
    			
    		} catch (Exception e) {
    			logger.error("Error al procesar datos de registro. Puede que el formato sea inválido: ", e);

        		//System.out.println("[DEBUG] doRegister BackendControoller 6");
    			UIController.getUnicaInstancia().registerErrors((byte) 4);
    			success = false;
    			return Optional.empty();
    		}
    	}
    	
    	return Optional.empty();
    	
    }
    
    // ### PDF
    public String exportarDatosPDF() {
    	logger.debug("Exportando historial a PDF.");

        List<EntidadComunicable> contactos = getListaContactos();
        List<Grupo> grupos = getGrupos();
        
        List<String> contenido = new ArrayList<>();
        contenido.add("Historial de Contactos y Grupos");
        contenido.add("\n=====================================\n");

        // Agregar información de contactos
        logger.debug("Agregando información y chats de contactos al PDF.");
        
        contenido.add("Contactos:");
        for (EntidadComunicable contacto : contactos) {
            contenido.add("• " + contacto.getNombre() + " - " + contacto.getNumero());
            //System.out.println("[DEBUG]" + " BackendController " + "exportarDatosPDF " + "id contacto : " + contacto.getId());
            List<String> mensajes = DAOController.getUnicaInstancia().getMensajesDeUsuario(contacto);
            if (!mensajes.isEmpty()) {
                contenido.add("  Mensajes:");
                contenido.addAll(mensajes);
            }
            contenido.add("\n-------------------------------------\n");
        }

        // Agregar información de grupos
        logger.debug("Agregando información y chats de grupos al PDF.");
        
        contenido.add("\nGrupos:");
        for (Grupo grupo : grupos) {
            contenido.add("• Grupo: " + grupo.getNombre());
            contenido.add("  Integrantes:");
            List<String> integrantes = DAOController.getUnicaInstancia().getIntegrantesDeGrupo(grupo);
            contenido.addAll(integrantes);
            contenido.add("\n-------------------------------------\n");
        }

        return GeneradorPDF.generarPDF("Historial_AppChat", contenido);
    }

}

