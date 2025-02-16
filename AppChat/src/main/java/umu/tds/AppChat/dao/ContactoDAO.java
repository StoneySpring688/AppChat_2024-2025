package umu.tds.AppChat.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.controllers.DAOController;

public class ContactoDAO implements InterfaceContactoDAO {

		// entidad
		private static final String CONTACTO = "Contacto";
		
		// propiedades
		private static final String NUMERO = "numero";
		private static final String NOMBRE = "nombre";
		private static final String LISTAMSG = "listaMsg";
		
		// utils
		private ServicioPersistencia servPersistencia;
		
		// singleton
		private static ContactoDAO unicaInstancia = null;
		
		public static ContactoDAO getUnicaInstancia() {
			if (unicaInstancia == null)
				return new ContactoDAO();
			else
				return unicaInstancia;
		}
		
	public ContactoDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	private EntidadComunicable entidadToContacto(Entidad eContact) {
		String numero = servPersistencia.recuperarPropiedadEntidad(eContact, NUMERO);
		String nombre = servPersistencia.recuperarPropiedadEntidad(eContact, NOMBRE);
		
		int number = Integer.parseInt(numero);
		
		EntidadComunicable contacto = new EntidadComunicable(number, nombre, DAOController.recuperarUser(number).get().getIconUrl());
		return contacto;
	}
	
	private Entidad contactToEntidad(EntidadComunicable contact) {
		Entidad eContact = new Entidad();
		eContact.setNombre(CONTACTO);
		
		eContact.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(
				new Propiedad(NUMERO, String.valueOf(contact.getNumero())),
				new Propiedad(NOMBRE, contact.getNombre()),
				new Propiedad(LISTAMSG, "")
				)));
		
		return eContact;
	}
	
	@Override
	public void create(EntidadComunicable contacto) {
		Entidad eContact = this.contactToEntidad(contacto);
		eContact = servPersistencia.registrarEntidad(eContact);
		contacto.setId(eContact.getId());
		
		//System.out.println("[DEBUG]" + "ContactoDAO" + " entidad id : "+eContact.getId() +" parametro id : "+contacto.getId());

	}

	@Override
	public boolean delete(EntidadComunicable contacto) {
		Entidad eContact;
		eContact = servPersistencia.recuperarEntidad(contacto.getId());
		
		if(eContact == null) return false;
		
		eliminarMsgs(eContact.getId());
		return servPersistencia.borrarEntidad(eContact);
	}

	@Override
	public void update(EntidadComunicable contacto) {
		Entidad eContact = servPersistencia.recuperarEntidad(contacto.getId());
		
		for(Propiedad prop : eContact.getPropiedades()) { //el numero no se puede actualizar una vez registrado
			if(prop.getNombre().equals(NOMBRE)) {
				prop.setValor(contacto.getNombre());
			}
			servPersistencia.modificarPropiedad(prop);
		}

	}

	@Override
	public EntidadComunicable get(int id) {
		Entidad eContact = servPersistencia.recuperarEntidad(id);
		if(eContact == null)return null;
		else {
			EntidadComunicable contacto = entidadToContacto(eContact);
			contacto.setId(eContact.getId());
			return contacto;
		}
	}
	
	// ### lista mensajes
	
	public void addMsg(int id, ModelMessage msg) {
		String msgs = servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(id), LISTAMSG);
		MensajeDAO adaptadorMsg = MensajeDAO.getUnicaInstancia();
		
		adaptadorMsg.create(msg);
		
		if(msgs.isBlank() || msgs ==  null) {
			msgs += "" + msg.getBDID();
		}else {
			msgs += " " + msg.getBDID();
		}
		//System.out.println("[DEBUG]" + "ContactoDAO" + " db lista mensajes : " + msgs);
		
		for(Propiedad prop : servPersistencia.recuperarEntidad(id).getPropiedades()) {
			if(prop.getNombre().equals(LISTAMSG)) {
				prop.setValor(msgs);
			}
			servPersistencia.modificarPropiedad(prop);
		}
		//System.out.println("[DEBUG]" + "ContactoDAO" + " comprobación lista de mensajes : "+servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(id), LISTAMSG));
	}
	
	public List<ModelMessage> obtenerListaMsg(int id){
		return obtenerListaMsgFromIDs(servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(id), LISTAMSG));
	}
	
	public List<ModelMessage> obtenerLoteMsg(int id,  int batchSize, int origin){
		List<ModelMessage> lista = new ArrayList<ModelMessage>();
		String msgs = servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(id), LISTAMSG);
		System.out.println("[DEBUG]" + " ContactoDAO" + " mensaje a cargar : " + '\n' + msgs);
		//System.out.println("[DEBUG]" + " ContactDAO" + " contacto : " + id + " lista de IDs : " + msgs);
		
		MensajeDAO adaptadorMsg = MensajeDAO.getUnicaInstancia();
		
		List<Integer> lote = obtenerLoteIDsMsg(msgs, batchSize, origin); //mensajes de más reciente a más antiguo (en ese orden)
		for(Integer msg : lote) System.out.println("[DEBUG]" + " ContactoDAO" + " mensaje a cargar : " + '\n' + msg);
		//System.out.println("[DEBUG]" + " ContactDAO" + " se han recuperado : " + lote.size() + " IDs");
		
		for(int i = 0; i < lote.size(); i++) {
			lista.add(adaptadorMsg.get(lote.get(i)));
		}
		for(ModelMessage msg : lista) System.out.println("[DEBUG]" + " ContactoDAO" + " mensaje a cargar : " + '\n' + msg.toString());
		return lista;
	}
	
	public void eliminarMsgs(int id) {
		Entidad eContact = servPersistencia.recuperarEntidad(id);
		
		if(eContact == null) return;
		
		String msgs= servPersistencia.recuperarPropiedadEntidad(eContact, LISTAMSG);
		
		if(msgs == null || msgs.isBlank()) return;
		
		StringTokenizer strTok = new StringTokenizer(msgs, " ");
		MensajeDAO adaptadorMsg = MensajeDAO.getUnicaInstancia();
		
		while(strTok.hasMoreTokens()) {
			String token = strTok.nextToken();
			
			try {
	            Integer msgId = Integer.valueOf(token);
	            ModelMessage mensaje = adaptadorMsg.get(msgId);
	            
	            if (mensaje != null) {
	                adaptadorMsg.delete(mensaje); // elimina el mensaje si no se ha eliminado antes
	            }
	        } catch (NumberFormatException e) {
	            System.err.println("[ERROR]" + "eliminarMsgs : " + "Token inválido en LISTAMSG -> " + token);
	        }
			
		}
		
	}
	
	// ### funciones auxiliares
	
	public List<ModelMessage> obtenerListaMsgFromIDs(String lista){
		List<ModelMessage> msgs = new ArrayList<ModelMessage>();
		if(lista.isBlank())return msgs;
		StringTokenizer strTok = new StringTokenizer(lista, " ");
		MensajeDAO adaptadorMsg = MensajeDAO.getUnicaInstancia();
		while(strTok.hasMoreTokens()) {
			msgs.add(adaptadorMsg.get(Integer.valueOf((String)strTok.nextElement())));
		}
		return msgs;
	}
	
	public List<Integer> obtenerLoteIDsMsg(String msgs, int batchSize, int origin) {
	    List<Integer> lote = new ArrayList<>();
	    if (msgs == null || msgs.isBlank()) {
	        return lote;
	    }
	    
	    String[] tokens = msgs.split(" "); // Divide la cadena en tokens
	    int totalMsgs = tokens.length;
	    int startIndex = totalMsgs - 1 - origin; // Determina la posición inicial desde el final
	    
	    // Validación de límites
	    if (startIndex < 0) {
	        return lote; // No hay suficientes mensajes para empezar desde "origin"
	    }
	    
	    // Obtener batchSize mensajes o hasta que se acaben los disponibles
	    for (int i = 0; i < batchSize && startIndex - i >= 0; i++) {
	    	//System.out.println("[DEBUG]" + " ContactoDAO " + " ID procesado : " + Integer.parseInt(tokens[startIndex - i]));
	        lote.add(0, Integer.parseInt(tokens[startIndex - i]));
	    }
	    
	    return lote;
	}

}
