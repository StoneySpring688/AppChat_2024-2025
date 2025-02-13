package umu.tds.AppChat.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import beans.Entidad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;
import umu.tds.AppChat.backend.utils.Usuario;

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
	
	private Grupo entidadToContacto(Entidad eGroup) {
		String grupoId = servPersistencia.recuperarPropiedadEntidad(eGroup, GRUPOID);
		String nombre = servPersistencia.recuperarPropiedadEntidad(eGroup, NOMBRE);
		String iconUrl = servPersistencia.recuperarPropiedadEntidad(eGroup, ICONURL);
		String miembros = servPersistencia.recuperarPropiedadEntidad(eGroup, MIEMBROS);
		
		long gId = Long.parseLong(grupoId);
		Grupo grupo = new Grupo(gId, nombre, nombre, obtenerMiembrosFomIDs(miembros));
		
		return grupo;
	}
	
	@Override
	public void create(Grupo grupo) {
		
		
	}

	@Override
	public boolean delete(Grupo grupo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(Grupo grupo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Grupo get(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	// ### funciónes auxiliares
	private String obtenerIDsMiembros(List<EntidadComunicable> lista) {
		String contactos = "";
		for (EntidadComunicable contacto : lista) {
			contactos += contacto.getId() + " ";
		}
		return contactos.trim();
	}
	
	public List<EntidadComunicable> obtenerMiembrosFomIDs(String lista) { 
		List<EntidadComunicable> memberList = new LinkedList<EntidadComunicable>();
		if(lista.isBlank())return memberList;
		StringTokenizer strTok = new StringTokenizer(lista, " ");
		UsuarioDAO adaptadorUsuario = UsuarioDAO.getUnicaInstancia();
		while(strTok.hasMoreTokens()) {
			Usuario userAux = adaptadorUsuario.get(Integer.valueOf((String)strTok.nextElement()));
			memberList.add(new EntidadComunicable(userAux.getNumero(), userAux.getNombre(), userAux.getIconUrl()));
		}
		return memberList;
	}
	
}
