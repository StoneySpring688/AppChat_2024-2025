package umu.tds.AppChat.dao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.AppChat.backend.utils.Usuario;

public class UsuarioDAO implements InterfaceUsuarioDAO{
	
	private static UsuarioDAO unicaInstancia = null;

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
	
	// utils
	private ServicioPersistencia servPersistencia;
	private DateTimeFormatter dateFormat;
	
	public static UsuarioDAO getUnicaInstancia() { // patron singleton
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
		
		Usuario user = new Usuario(nombre, Integer.parseInt(numero), passwd, LocalDate.parse(fechNacim, dateFormat), profileUrl, signature, Boolean.parseBoolean(premium), LocalDate.parse(endPremiumDate, dateFormat));
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
				new Propiedad(ENDPREMIUMDATE, user.getEndPremiumDate().get().format(dateFormat).toString())
				)));
		
		return eUser;
	}
	
	@Override
	public void create(Usuario usuario) {
		Entidad eUser = this.userToEntidad(usuario);
		eUser = servPersistencia.registrarEntidad(eUser);
		eUser.setId(usuario.getNumero()); // el número es el id, en los grupos no es posible porque usan long, por paralelismo con los grupos se mantiene la propiedad
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
			}
			servPersistencia.modificarPropiedad(prop);
		}
		
	}

	@Override
	public Usuario get(int id) {
		Entidad eUser = servPersistencia.recuperarEntidad(id);
		return entidadToUser(eUser);
	}

}
