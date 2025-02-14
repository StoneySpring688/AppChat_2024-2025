package umu.tds.AppChat.dao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import umu.tds.AppChat.backend.utils.Usuario;
import umu.tds.AppChat.controllers.BackendController;

public class UsuarioDAO implements InterfaceUsuarioDAO{
	


	// entidad
	private static final String USUARIO = "Usuario";
	
	// propiedades
	private static final String NUMERO = "numero";
	private static final String NOMBRE = "nombre";
	private static final String PROFILEURL = "profileUrl";
	private static final String PASSWORD = "password";
	private static final String SIGNATURE = "signature";
	private static final String FECHA_NACIMIENTO = "fechaNacimiento";
	private static final String PREMIUM = "premium";
	private static final String ENDPREMIUMDATE = "endPremiumDate";
	private static final String LISTACONTACTOS = "listaContactos";
	private static final String LISTAGRUPOS = "listaGrupos";
	
	// utils
	private ServicioPersistencia servPersistencia;
	private DateTimeFormatter dateFormat;
	
	// singleton
	private static UsuarioDAO unicaInstancia = null;
	
	public static UsuarioDAO getUnicaInstancia() {
		if (unicaInstancia == null)
			return new UsuarioDAO();
		else
			return unicaInstancia;
	}
	
	public UsuarioDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	}
	
	private Usuario entidadToUser(Entidad eUser) {
		String numero = servPersistencia.recuperarPropiedadEntidad(eUser, NUMERO);
		String nombre = servPersistencia.recuperarPropiedadEntidad(eUser, NOMBRE);
		String profileUrl = servPersistencia.recuperarPropiedadEntidad(eUser, PROFILEURL);
		String passwd = servPersistencia.recuperarPropiedadEntidad(eUser, PASSWORD);
		String signature = servPersistencia.recuperarPropiedadEntidad(eUser, SIGNATURE);
		String fechNacim = servPersistencia.recuperarPropiedadEntidad(eUser, FECHA_NACIMIENTO);
		String premium = servPersistencia.recuperarPropiedadEntidad(eUser, PREMIUM);
		String endPremiumDate = servPersistencia.recuperarPropiedadEntidad(eUser, ENDPREMIUMDATE);
		
		Usuario user = new Usuario(nombre, Integer.parseInt(numero), passwd, LocalDate.parse(fechNacim, dateFormat), profileUrl, signature, Boolean.parseBoolean(premium), (endPremiumDate.isBlank() ? null : LocalDate.parse(endPremiumDate, dateFormat)) );
		return user;
	}
	
	private Entidad userToEntidad(Usuario user) {
		Entidad eUser = new Entidad();
		eUser.setNombre(USUARIO);
		
		eUser.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(
				new Propiedad(NUMERO, String.valueOf(user.getNumero())),
				new Propiedad(NOMBRE, user.getNombre()),
				new Propiedad(PROFILEURL, user.getIconUrl()),
				new Propiedad(PASSWORD, user.getPasswd()),
				new Propiedad(SIGNATURE, user.getSignature()),
				new Propiedad(FECHA_NACIMIENTO, user.getBirthDate().format(dateFormat).toString()),
				new Propiedad(PREMIUM, String.valueOf(user.isPremium())),
				new Propiedad(ENDPREMIUMDATE, user.getEndPremiumDate().isPresent() ? user.getEndPremiumDate().get().format(dateFormat).toString() : ""),
				new Propiedad(LISTACONTACTOS, ""),
				new Propiedad(LISTAGRUPOS, "")
				)));
		
		return eUser;
	}
	
	@Override
	public void create(Usuario usuario) {
		Entidad eUser = this.userToEntidad(usuario);
		eUser.setId(usuario.getNumero());
		eUser = servPersistencia.registrarEntidad(eUser);
		// el número es el id, en los grupos no es posible porque usan long, por paralelismo con los grupos se mantiene la propiedad
		// usuario.setId(eUsuario.getId());
	}

	@Override
	public boolean delete(Usuario usuario) {
		Entidad eUser;
		eUser = servPersistencia.recuperarEntidad(usuario.getNumero());
		
		return servPersistencia.borrarEntidad(eUser);
	}

	@Override
	public void update(Usuario usuario) {
		Entidad eUser = servPersistencia.recuperarEntidad(usuario.getNumero());
		
		for(Propiedad prop : eUser.getPropiedades()) { //el numero no se puede actualizar una vez registrado
			if(prop.getNombre().equals(NOMBRE)) {
				prop.setValor(usuario.getNombre());
			} else if(prop.getNombre().equals(PROFILEURL)) {
				prop.setValor(usuario.getIconUrl());
			}else if(prop.getNombre().equals(PASSWORD)) {
				prop.setValor(usuario.getPasswd());
			}else if(prop.getNombre().equals(SIGNATURE)) {
				prop.setValor(usuario.getSignature());
			}else if(prop.getNombre().equals(FECHA_NACIMIENTO)) {
				prop.setValor(usuario.getBirthDate().format(dateFormat).toString());
			}else if(prop.getNombre().equals(PREMIUM)) {
				prop.setValor(String.valueOf(usuario.isPremium()));
			}else if(prop.getNombre().equals(ENDPREMIUMDATE)) {
				prop.setValor(usuario.getEndPremiumDate().get().format(dateFormat).toString());
			}else if(prop.getNombre().equals(LISTACONTACTOS)) {
				String contactList = obtenerIDsContactos(BackendController.getListaContactos());
				prop.setValor(contactList);
			}
			servPersistencia.modificarPropiedad(prop);
		}
		
	}

	@Override
	public Usuario get(int id) {
		Entidad eUser = servPersistencia.recuperarEntidad(id);
		
		//System.out.println(eUser);
		
		if(eUser == null) return null;
		else return entidadToUser(eUser);
	}
	
	// ### login
	
	public boolean checkLogin(int id, String passwd) {
		
		Usuario user = get(id);
		if(user != null && user.getPasswd().equals(passwd)) return true;
		else return false;
	}
	
	// ### lista contactos
	
	public void addContacto(int number, EntidadComunicable contact) {
		String listaContactos = servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(number), LISTACONTACTOS);
		//System.out.println("[DEBUG] lista antes : "+ listaContactos);
		//System.out.println("[DEBUG se debería anyadir : "+contact.getId());
		if(listaContactos.isBlank()||listaContactos == null) {
			listaContactos += "" + contact.getId();
		}else {
			listaContactos += " " + contact.getId();
		}
		
		System.out.println("[DEBUG]" + "UsuarioDAO" + " db lista contactos : " + listaContactos);
		
		for(Propiedad prop : servPersistencia.recuperarEntidad(number).getPropiedades()) {
			if(prop.getNombre().equals(LISTACONTACTOS)) {
				prop.setValor(listaContactos);
			}
			servPersistencia.modificarPropiedad(prop);
		}
		System.out.println("[DEBUG]" + "UsuarioDAO" + " comprobación lista de contactos : "+servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(number), LISTACONTACTOS));
	}
	
	public void eliminarContacto(int number, EntidadComunicable contact) {
		String listaContactos = servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(number), LISTACONTACTOS);
		List<EntidadComunicable> list = obtenerListaContactosFomIDs(listaContactos);
		list.remove(contact);
		listaContactos = obtenerIDsContactos(list);
		Entidad eUser = servPersistencia.recuperarEntidad(number);
		for(Propiedad prop : eUser.getPropiedades()) {
			if(prop.getNombre().equals(LISTACONTACTOS)) {
				prop.setValor(listaContactos);
			}
			servPersistencia.modificarPropiedad(prop);
		}
	}
	
	public List<EntidadComunicable> obtenerListaContactos(int numero){
		return obtenerListaContactosFomIDs(servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(numero), LISTACONTACTOS));
	}
	
	// ### lista grupos
	
	public void addGrupoToUser(int number, Grupo grupo) {
		String listaGrupos = servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(number), LISTAGRUPOS);
		//System.out.println("[DEBUG] lista antes : "+ listaContactos);
		//System.out.println("[DEBUG se debería anyadir : "+contact.getId());
		if(listaGrupos.isBlank()||listaGrupos == null) {
			listaGrupos += "" + grupo.getDBID();
		}else {
			listaGrupos += " " + grupo.getDBID();
		}
		
		System.out.println("[DEBUG]" + "UsuarioDAO" + " db lista grupos del usuario : " + listaGrupos);
		
		for(Propiedad prop : servPersistencia.recuperarEntidad(number).getPropiedades()) {
			if(prop.getNombre().equals(LISTAGRUPOS)) {
				prop.setValor(listaGrupos);
			}
			servPersistencia.modificarPropiedad(prop);
		}
		System.out.println("[DEBUG]" + "UsuarioDAO" + " comprobación lista de grupos del usuario : "+servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(number), LISTAGRUPOS));
	}
	
	public void eliminarGrupoFromUser(int number, Grupo grupo) {
		String listaGrupos = servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(number), LISTAGRUPOS);
		List<Grupo> list = obtenerListaGruposFromIDs(listaGrupos);
		list.remove(grupo);
		listaGrupos = obtenerIDsGrupos(list);
		Entidad eUser = servPersistencia.recuperarEntidad(number);
		for(Propiedad prop : eUser.getPropiedades()) {
			if(prop.getNombre().equals(LISTAGRUPOS)) {
				prop.setValor(listaGrupos);
			}
			servPersistencia.modificarPropiedad(prop);
		}
	}
	
	public List<Grupo> obtenerListaGruposFromUser(int numero){
		return obtenerListaGruposFromIDs(servPersistencia.recuperarPropiedadEntidad(servPersistencia.recuperarEntidad(numero), LISTAGRUPOS));
	}
	
	// ### funciónes auxiliares
	private String obtenerIDsContactos(List<EntidadComunicable> lista) {
		String contactos = "";
		for (EntidadComunicable contacto : lista) {
			contactos += contacto.getId() + " ";
		}
		return contactos.trim();
	}
	
	public List<EntidadComunicable> obtenerListaContactosFomIDs(String lista) { 
		List<EntidadComunicable> contactList = new LinkedList<EntidadComunicable>();
		if(lista.isBlank())return contactList;
		StringTokenizer strTok = new StringTokenizer(lista, " ");
		ContactoDAO adaptadorContacto = ContactoDAO.getUnicaInstancia();
		while(strTok.hasMoreTokens()) {
			contactList.add(adaptadorContacto.get(Integer.valueOf((String)strTok.nextElement())));
		}
		return contactList;
	}
	
	private String obtenerIDsGrupos(List<Grupo> lista) {
		String grupos = "";
		for (Grupo grupo : lista) {
			grupos += grupo.getDBID() + " ";
		}
		return grupos.trim();
	}
	
	public List<Grupo> obtenerListaGruposFromIDs(String lista){
		List<Grupo> groupList = new LinkedList<Grupo>();
		if(lista.isBlank()) return groupList;
		StringTokenizer strTok = new StringTokenizer(lista, " ");
		GrupoDAO adaptadorGrupo = GrupoDAO.getUnicaInstancia();
		while(strTok.hasMoreTokens()) {
			groupList.add(adaptadorGrupo.get(Integer.valueOf((String)strTok.nextElement())));
		}
		return groupList;
	}

}
