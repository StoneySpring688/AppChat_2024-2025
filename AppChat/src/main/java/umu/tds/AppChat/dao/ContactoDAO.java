package umu.tds.AppChat.dao;

import java.util.ArrayList;
import java.util.Arrays;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.controllers.DAOController;

public class ContactoDAO implements InterfaceContactoDAO {

		// entidad
		private static final String CONTACTO = "Contacto";
		
		// propiedades
		private static final String NUMERO = "numero";
		private static final String NOMBRE = "nombre";
		
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
				new Propiedad(NOMBRE, contact.getNombre())
				)));
		
		return eContact;
	}
	
	@Override
	public void create(EntidadComunicable contacto) {
		Entidad eContact = this.contactToEntidad(contacto);
		eContact = servPersistencia.registrarEntidad(eContact);
		contacto.setId(eContact.getId());
		
		System.out.println("[DEBUG]" + "ContactoDAO" + "  entidad id : "+eContact.getId() +" parametro id : "+contacto.getId());

	}

	@Override
	public boolean delete(EntidadComunicable contacto) {
		Entidad eContact;
		eContact = servPersistencia.recuperarEntidad(contacto.getId());
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

}
