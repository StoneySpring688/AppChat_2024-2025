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

public class GrupoDAO implements InterfaceGrupoDAO{
	
	// entidad
	private static final String GRUPO = "Grupo";
	
	// propiedades
	private static final String GRUPOID = "GrupoId";
	private static final String NOMBRE = "nombre";
	private static final String ICONURL = "iconUrl";
	private static final String MIEMBROS = "miembros";
	
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
		
		long gId = Long.parseLong(grupoId);
		Grupo grupo = new Grupo(gId, nombre, iconUrl, obtenerMiembrosFomIDs(miembros));
		
		return grupo;
	}
	
	private Entidad grupoToEntidad(Grupo grupo) {
		Entidad eGrupo = new Entidad();
		eGrupo.setNombre(GRUPO);
		
		eGrupo.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(
				new Propiedad(GRUPOID, Long.toString(grupo.getID())),
				new Propiedad(NOMBRE, grupo.getNombre()),
				new Propiedad(ICONURL, grupo.getIconUrl()),
				new Propiedad(MIEMBROS, obtenerIDsMiembros(grupo.getIntegrantes()))
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
		//System.out.println("[DEBUG] lista antes : "+ listaContactos);
		//System.out.println("[DEBUG se debería anyadir : "+contact.getId());
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
		list.remove(miembro);
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

	// ### funciónes auxiliares
	private String obtenerIDsMiembros(List<EntidadComunicable> lista) {
		String miembros = "";
		for (EntidadComunicable miembro : lista) {
			miembros += miembro.getId() + " ";
		}
		return miembros.trim();
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
	
}
