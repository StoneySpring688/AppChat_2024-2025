package umu.tds.AppChat.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @autor StoneySpring688
 * 
 */
public class LRUCache<K, V> extends LinkedHashMap<K, List<V>>{
	
	private static final long serialVersionUID = 1L;
	private int numLRU; // máx chats memoria
	
	/**
	 * LRUCache constructor
	 * 
	 * @param max número máximo de elementos en caché
	 * @param initialCapacity número inicial de cubetas
	 * @param loadFactor porcentaje de uso de la tabla en el que se duplica el número de cubetas (EJ : 0.75f 75%) para prevenir muchas colisiones
	 * @param accesOrder if True LRU por acceso, if false LRU por inserción
	 * 
	 */
	public LRUCache(int max, int initialCapacity, float loadFactor, boolean accesOrder) { 
		
		super(initialCapacity, loadFactor, accesOrder); //javaDoc ---> LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder) 
		this.numLRU = max;
		
	}
	
	@Override
	protected boolean removeEldestEntry(Map.Entry<K, List<V>> eldest) {
		
		return this.size() > this.numLRU;
	}
	
	/**
     * Agrega un valor a la lista asociada a una clave, si no existe la clave, la crea.
     * 
     * @param key Clave del elemento.
     * @param value Valor a agregar en la lista asociada a la clave.
     */
	public void addMessageToChat(K clave, V valor) { //si ya está presente, solo agrega los nuevos mensajes, agrega clave valor si no está presente
		
		this.computeIfAbsent(clave, k -> new ArrayList<>()).add(valor);
	}
	
	/**
     * Obtiene la lista de valores asociada a una clave.
     * 
     * @param Clave a buscar.
     * @return Lista de valores asociada a la clave o una lista vacía si no existe.
     */
	public List<V> getMessages(K clave){
		
		return this.getOrDefault(clave, Collections.emptyList());
		
	}
	
}
