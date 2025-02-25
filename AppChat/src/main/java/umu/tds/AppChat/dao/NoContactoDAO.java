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
import umu.tds.AppChat.backend.utils.Usuario;
import umu.tds.AppChat.controllers.DAOController;

public class NoContactoDAO implements InterfaceNoContactoDAO {
	
	// entidad
	private static final String NOCONTACTO = "noContacto";
	
	// propiedades
	private static final String NUMERO = "numero";
	private static final String LISTAMSG = "listaMsg";
	
	// utils
	private ServicioPersistencia servPersistencia;
	
	// singleton
	private static NoContactoDAO unicaInstancia = null;
	
	public static NoContactoDAO getUnicaInstancia() {
		if (unicaInstancia == null)
			return new NoContactoDAO();
		else
			return unicaInstancia;
	}
	
	public NoContactoDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	private EntidadComunicable entidadToNoContacto(Entidad eNoContact) {
		String numero = servPersistencia.recuperarPropiedadEntidad(eNoContact, NUMERO);
		
		int number = Integer.parseInt(numero);
		Usuario userAux = DAOController.recuperarUser(number).get();
		
		EntidadComunicable noContacto = new EntidadComunicable(number, userAux.getNombre(), userAux.getIconUrl());
		noContacto.setIsNoContact(true);
		return noContacto;
	}
	
	private Entidad noContactToEntidad(EntidadComunicable noContact) {
		Entidad eNoContact = new Entidad();
		eNoContact.setNombre(NOCONTACTO);
		
		eNoContact.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(
				new Propiedad(NUMERO, String.valueOf(noContact.getNumero())),
				new Propiedad(LISTAMSG, "")
				)));
		
		return eNoContact;
	}
	
	@Override
	public void create(EntidadComunicable noContacto) {
		Entidad eNoContact = this.noContactToEntidad(noContacto);
		eNoContact = servPersistencia.registrarEntidad(eNoContact);
		noContacto.setId(eNoContact.getId());
		
		//System.out.println("[DEBUG]" + "noContactoDAO" + " entidad id : "+eNoContact.getId() +" parametro id : "+noContacto.getId());

	}

	@Override
	public boolean delete(EntidadComunicable noContacto) {
		Entidad eNoContact;
		eNoContact = servPersistencia.recuperarEntidad(noContacto.getId());
		return servPersistencia.borrarEntidad(eNoContact);
	}

	@Override
	public EntidadComunicable get(int id) {
		Entidad eNoContact = servPersistencia.recuperarEntidad(id);
		if(eNoContact == null)return null;
		else {
			EntidadComunicable noContacto = entidadToNoContacto(eNoContact);
			noContacto.setId(eNoContact.getId());
			return noContacto;
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
		MensajeDAO adaptadorMsg = MensajeDAO.getUnicaInstancia();
		
		List<Integer> lote = obtenerLoteIDsMsg(msgs, batchSize, origin); //mensajes de más reecientte a más antiguo (en ese orden)
		
		for(int i = 0; i < lote.size(); i++) {
			lista.add(adaptadorMsg.get(lote.get(i)));
		}
		for(ModelMessage msg : lista) System.out.println("[DEBUG]" + " NoContactoDAO" + " mensaje a cargar : " + '\n' + msg.toString());
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
	        lote.add(Integer.parseInt(tokens[startIndex - i]));
	    }
	    
	    return lote;
	}

}
