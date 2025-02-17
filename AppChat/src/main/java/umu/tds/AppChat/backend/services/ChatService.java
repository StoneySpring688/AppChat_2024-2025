package umu.tds.AppChat.backend.services;

import java.util.ArrayList;
import java.util.List;

import umu.tds.AppChat.backend.utils.LRUCache;
import umu.tds.AppChat.backend.utils.ModelMessage;

/**
 * Repositorio de mensajes en memoria*/
public class ChatService {
	private LRUCache<Long, ModelMessage> cacheMensajes;
	private long ChatActualID;
	
	/**
	 * constructor de ChatService
	 * 
	 * @param tamanoCache tamaño de la caché LRU*/
	public ChatService(int tamanoCache) {
		this.cacheMensajes = new LRUCache<Long, ModelMessage>(tamanoCache, 10, 0.75f, true);
		this.ChatActualID = 0;
	}
	
	/**
	 * Añade un mensaje a la lista de mensajes del chat actual y a  su  entrada de caché
	 * 
	 * @param msg mensaje a enviar
	 * @param chatID numero asociado al chat
	 */
	public void addMessage(long chatID, ModelMessage msg) {
		//this.mensajesChatActual.add(msg);
		this.cacheMensajes.addMessageToChat(chatID, msg);
		this.setChatActualID(chatID);
	}
	
	public void addMessageAlInicio(long id, List<ModelMessage> msgs) {
		System.out.println(msgs.size() + "," + id);
		this.cacheMensajes.getMessages(id).addAll(0, msgs);
	}
	
	/**
     * Obtiene la lista de mensajes del chat actual.
     * 
     * @return Lista de valores asociada al chat actual.
     */
	public List<ModelMessage> getMsgChatActual(){
		return new ArrayList<ModelMessage>(this.getLRUChat(this.getChatActualID()));		
	}
	
	/**
     * Obtiene la lista de mensajes asociados a un número.
     * 
     * @param chatID numero asociado al chat.
     * @return Lista de valores asociada al chat o una lista vacía si no está en caché.
     */
	public List<ModelMessage> getLRUChat(long chatID){
		return new ArrayList<ModelMessage>(this.cacheMensajes.getMessages(chatID));
	}
	
	public boolean isInLRU(long chatID) {
		return this.cacheMensajes.containsKey(chatID);
	}
	
	public void setChatActualID(long id) {
		this.ChatActualID = id;
	}
	
	public long getChatActualID() {
		return this.ChatActualID;
	}
	
}
