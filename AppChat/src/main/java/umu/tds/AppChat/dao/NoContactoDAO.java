package umu.tds.AppChat.dao;

import java.util.ArrayList;
import java.util.Arrays;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Usuario;
import umu.tds.AppChat.controllers.DAOController;

public class NoContactoDAO implements InterfaceNoContactoDAO {
	
	// entidad
	private static final String NOCONTACTO = "noContacto";
	
	// propiedades
	private static final String NUMERO = "numero";
	
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
		return noContacto;
	}
	
	private Entidad noContactToEntidad(EntidadComunicable noContact) {
		Entidad eNoContact = new Entidad();
		eNoContact.setNombre(NOCONTACTO);
		
		eNoContact.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(
				new Propiedad(NUMERO, String.valueOf(noContact.getNumero()))
				)));
		
		return eNoContact;
	}
	
	@Override
	public void create(EntidadComunicable noContacto) {
		Entidad eNoContact = this.noContactToEntidad(noContacto);
		eNoContact = servPersistencia.registrarEntidad(eNoContact);
		noContacto.setId(eNoContact.getId());
		
		System.out.println("[DEBUG]" + "noContactoDAO" + " entidad id : "+eNoContact.getId() +" parametro id : "+noContacto.getId());

	}

	@Override
	public boolean delete(EntidadComunicable noContacto) {
		Entidad eNoContact;
		eNoContact = servPersistencia.recuperarEntidad(noContacto.getId());
		return servPersistencia.borrarEntidad(eNoContact);
	}

	@Override
	public void update(EntidadComunicable noContacto) {
		// se deja para seguir la misma estructura, pero el número es la referencia(clave ajena), y este no cambia nunca
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

}
