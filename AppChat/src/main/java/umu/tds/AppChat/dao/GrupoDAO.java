package umu.tds.AppChat.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;
import umu.tds.AppChat.backend.utils.ModelMessage;

public class GrupoDAO implements InterfaceGrupoDAO{
	
	// entidad
	private static final String GRUPO = "Grupo";
	
	// propiedades
	private static final String GRUPOID = "GrupoId";
	private static final String NOMBRE = "nombre";
	private static final String ICONURL = "iconUrl";
	private static final String MIEMBROS = "miembros";
	private static final String ADMINS = "admins";
	private static final String LISTAMSG = "listaMsg";
	
	// utils
	private ServicioPersistencia servPersistencia;
	
	// singleton
	private static GrupoDAO unicaInstancia = null;
	
	public static GrupoDAO getUnicaInstancia() {
		if (unicaInstancia == null)
			return new GrupoDAO();
		else
			return unicaInstancia;
	}
	
	public GrupoDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	private Grupo entidadToGrupo(Entidad eGroup) {
		String grupoId = servPersistencia.recuperarPropiedadEntidad(eGroup, GRUPOID);
		String nombre = servPersistencia.recuperarPropiedadEntidad(eGroup, NOMBRE);
		String iconUrl = servPersistencia.recuperarPropiedadEntidad(eGroup, ICONURL);
		String miembros = servPersistencia.recuperarPropiedadEntidad(eGroup, MIEMBROS);
		String admins = servPersistencia.recuperarPropiedadEntidad(eGroup, ADMINS);
		
		long gId = Long.parseLong(grupoId);
		Grupo grupo = new Grupo(gId, nombre, iconUrl, obtenerMiembrosFomIDs(miembros), obtenerAdminsFomIDs(admins));
		
		return grupo;
	}
	
	private Entidad grupoToEntidad(Grupo grupo) {
		Entidad eGrupo = new Entidad();
		eGrupo.setNombre(GRUPO);
		
		eGrupo.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(
				new Propiedad(GRUPOID, Long.toString(grupo.getID())),
				new Propiedad(NOMBRE, grupo.getNombre()),
				new Propiedad(ICONURL, grupo.getIconUrl()),
				new Propiedad(MIEMBROS, obtenerIDsMiembros(grupo.getIntegrantes())),
				new Propiedad(ADMINS, ""),
				new Propiedad(LISTAMSG, "")
				)));
		
		return eGrupo;
	}
	
	@Override
	public void create(Grupo grupo) {
		Entidad eGrupo = this.grupoToEntidad(grupo);
		eGrupo = servPersistencia.registrarEntidad(eGrupo);
		grupo.setDBID(eGrupo.getId());
	}

	@Override
	public boolean delete(Grupo grupo) {
		Entidad eGrupo;
		eGrupo = servPersistencia.recuperarEntidad(grupo.getDBID());
		MensajeDAO adaptadorMensaje = MensajeDAO.getUnicaInstancia();
		for(ModelMessage msg  : obtenerListaMsg(grupo.getDBID())) adaptadorMensaje.delete(msg);
		return servPersistencia.borrarEntidad(eGrupo);
	}

	@Override
	public void update(Grupo grupo) {
		Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getDBID());
		
		for(Propiedad prop : eGrupo.getPropiedades()) {
			if(prop.getNombre().equals(GRUPOID)) {
				prop.setValor(Long.toString(grupo.getID()));
			}else if(prop.getNombre().equals(NOMBRE)) {
				prop.setValor(grupo.getNombre());
			}else if (prop.getNombre().equals(ICONURL)) {
				prop.setValor(grupo.getIconUrl());
			}else if(prop.getNombre().equals(MIEMBROS)) {
				String miembros = obtenerIDsMiembros(grupo.getIntegrantes());
				prop.setValor(miembros);
			}
			servPersistencia.modificarPropiedad(prop);
		}
		
	}

	@Override
	public Grupo get(int id) {
		Entidad eGrupo = servPersistencia.recuperarEntidad(id);
		
		if(eGrupo == null) return null;
		else {
			Grupo grupo = entidadToGrupo(eGrupo);
			grupo.setDBID(eGrupo.getId());
			return grupo;
		}
	}
	
	// ### lista miembros
	
	public void addMiembro(int id, EntidadComunicable miembro) {
		String listMiembros= servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(id), MIEMBROS);
		
		if(listMiembros.isBlank()||listMiembros == null) {
			listMiembros += "" + miembro.getId();
		}else {
			listMiembros += " " + miembro.getId();
		}
		
		System.out.println("[DEBUG]" + "GrupoDAO" + " db lista miembros : " + listMiembros);
		
		for(Propiedad prop : servPersistencia.recuperarEntidad(id).getPropiedades()) {
			if(prop.getNombre().equals(MIEMBROS)) {
				prop.setValor(listMiembros);
			}
			servPersistencia.modificarPropiedad(prop);
		}
		System.out.println("[DEBUG]" + "GrupoDAO" + " comprobación lista de miembros : "+servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(id), MIEMBROS));
	}
	
	public void eliminarMiembro(int id, EntidadComunicable miembro) {
		String listaContactos = servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(id), MIEMBROS);
		List<EntidadComunicable> list = obtenerMiembrosFomIDs(listaContactos);
		EntidadComunicable miembrotAux = list.stream().filter(e -> e.getId() == miembro.getId()).findFirst().get() ;
		list.remove(miembrotAux);
		listaContactos = obtenerIDsMiembros(list);
		Entidad eUser = servPersistencia.recuperarEntidad(id);
		for(Propiedad prop : eUser.getPropiedades()) {
			if(prop.getNombre().equals(MIEMBROS)) {
				prop.setValor(listaContactos);
			}
			servPersistencia.modificarPropiedad(prop);
		}
	}
	
	public List<EntidadComunicable> obtenerListaMiembros(int id){
		return obtenerMiembrosFomIDs(servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(id), MIEMBROS));
	}
	
	public boolean isMiembro(int numeroUser, int groupID) {	
		List<EntidadComunicable> lista = obtenerListaMiembros(groupID);
		return lista.stream().anyMatch(e -> e.getNumero() == numeroUser);
	}
	
	// ### lista admins
	
	public void addAdmin(int id, int admin) {
		String listMiembros= servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(id), ADMINS);
		if(listMiembros.isBlank()||listMiembros == null) {
			listMiembros += "" + admin;
		}else {
			listMiembros += " " + admin;
		}
		
		System.out.println("[DEBUG]" + "GrupoDAO" + " db lista admins : " + listMiembros);
		
		for(Propiedad prop : servPersistencia.recuperarEntidad(id).getPropiedades()) {
			if(prop.getNombre().equals(ADMINS)) {
				prop.setValor(listMiembros);
			}
			servPersistencia.modificarPropiedad(prop);
		}
		System.out.println("[DEBUG]" + "GrupoDAO" + " comprobación lista de admins : "+servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(id), ADMINS));
	}
	
	public void eliminarAdmin(int id, int admin) {
		String listaContactos = servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(id), ADMINS);
		List<Integer> list = obtenerAdminsFomIDs(listaContactos);
		int miembrotAux = list.stream().filter(e -> e == admin).findFirst().get() ;
		list.remove(miembrotAux);
		listaContactos = obtenerIDsAdmins(list);
		Entidad eUser = servPersistencia.recuperarEntidad(id);
		for(Propiedad prop : eUser.getPropiedades()) {
			if(prop.getNombre().equals(ADMINS)) {
				prop.setValor(listaContactos);
			}
			servPersistencia.modificarPropiedad(prop);
		}
	}
	
	public List<Integer> obtenerListaAdmins(int id){
		return obtenerAdminsFomIDs(servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(id), ADMINS));
	}
	
	public boolean isAdmin(int numeroUser, int groupID) {
		List<Integer> lista = obtenerListaAdmins(groupID);
		return lista.stream().anyMatch(e -> e == numeroUser);
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
		//System.out.println("[DEBUG]" + "GrupoDAO" + " db lista mensajes : " + msgs);
		
		for(Propiedad prop : servPersistencia.recuperarEntidad(id).getPropiedades()) {
			if(prop.getNombre().equals(LISTAMSG)) {
				prop.setValor(msgs);
			}
			servPersistencia.modificarPropiedad(prop);
		}
		//System.out.println("[DEBUG]" + "GrupoDAO" + " comprobación lista de mensajes : "+servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(id), LISTAMSG));
	}
	
	public List<ModelMessage> obtenerListaMsg(int id){
		return obtenerListaMsgFromIDs(servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(id), LISTAMSG));
	}
	
	public List<ModelMessage> obtenerLoteMsg(int id,  int batchSize, int origin){
		List<ModelMessage> lista = new ArrayList<ModelMessage>();
		String msgs = servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(id), LISTAMSG);
		//System.out.println("[DEBUG]" + " GrupoDAO" + " mensaje a cargar : " + '\n' + msgs);
		//System.out.println("[DEBUG]" + " GrupoDAO" + " grupo : " + id + " lista de IDs : " + msgs);
		
		MensajeDAO adaptadorMsg = MensajeDAO.getUnicaInstancia();
		
		List<Integer> lote = obtenerLoteIDsMsg(msgs, batchSize, origin); //mensajes de más reciente a más antiguo (en ese orden)
		//for(Integer msg : lote) System.out.println("[DEBUG]" + " GrupoDAO" + " mensaje a cargar : " + '\n' + msg);
		//System.out.println("[DEBUG]" + " GrupoDAO" + " se han recuperado : " + lote.size() + " IDs");
		
		for(int i = 0; i < lote.size(); i++) {
			lista.add(adaptadorMsg.get(lote.get(i)));
		}
		//for(ModelMessage msg : lista) System.out.println("[DEBUG]" + " GrupoDAO" + " mensaje a cargar : " + '\n' + msg.toString());
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
	            System.err.println("[ERROR]" + " GrupoDAO " + "eliminarMsgs : " + "Token inválido en LISTAMSG -> " + token);
	        }
			
		}
		
	}
	
	// ### funciónes auxiliares
	
	private String obtenerIDsMiembros(List<EntidadComunicable> lista) {
		String miembros = "";
		for (EntidadComunicable miembro : lista) {
			miembros += miembro.getId() + " ";
		}
		return miembros.trim();
	}
	
	public List<Integer> obtenerAdminsFomIDs(String lista) {
		List<Integer> adminList = new LinkedList<Integer>();
		if(lista.isBlank())return adminList;
		StringTokenizer strTok = new StringTokenizer(lista, " ");
		//ContactoDAO adaptadorContacto = ContactoDAO.getUnicaInstancia();
		while(strTok.hasMoreTokens()) {
			int adminAux = Integer.valueOf((String)strTok.nextElement());
			adminList.add(adminAux);
		}
		return adminList;
	}
	
	private String obtenerIDsAdmins(List<Integer> lista) {
		String admins = "";
		for (int admin : lista) {
			admins += admin + " ";
		}
		return admins.trim();
	}
	
	public List<EntidadComunicable> obtenerMiembrosFomIDs(String lista) { 
		List<EntidadComunicable> memberList = new LinkedList<EntidadComunicable>();
		if(lista.isBlank())return memberList;
		StringTokenizer strTok = new StringTokenizer(lista, " ");
		ContactoDAO adaptadorContacto = ContactoDAO.getUnicaInstancia();
		while(strTok.hasMoreTokens()) {
			EntidadComunicable miembroAux = adaptadorContacto.get(Integer.valueOf((String)strTok.nextElement()));
			memberList.add(new EntidadComunicable(miembroAux.getNumero(), miembroAux.getNombre(), miembroAux.getIconUrl()));
		}
		return memberList;
	}
	
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
