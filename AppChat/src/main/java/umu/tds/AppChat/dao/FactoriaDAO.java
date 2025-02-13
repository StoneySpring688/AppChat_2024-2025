package umu.tds.AppChat.dao;

/** 
 * Factoria concreta DAO para el Servidor de Persistencia de la asignatura TDS.
 * 
 */

public class FactoriaDAO extends AbstractFactoriaDAO {
	
	public FactoriaDAO() {	}
	
	@Override
	public UsuarioDAO getUsuarioDAO() {
		return UsuarioDAO.getUnicaInstancia(); 
	}

	@Override
	public InterfaceContactoDAO getContactoDAO() {
		return ContactoDAO.getUnicaInstancia();
	}

	@Override
	public InterfaceGrupoDAO getGrupoDAO() {
		return GrupoDAO.getUnicaInstancia();
	}
	
}
