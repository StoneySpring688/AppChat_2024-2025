package umu.tds.AppChat.dao;

/**
 * Factoria abstracta DAO.
 */

public abstract class AbstractFactoriaDAO {
	
	public static final String DAO_TDS = "umu.tds.AppChat.dao.FactoriaDAO";

	private static AbstractFactoriaDAO unicaInstancia;
	
	/** 
	 * Crea un tipo de factoria DAO.
	 * Solo existe el tipo TDSFactoriaDAO
	 */
	public static AbstractFactoriaDAO getInstancia(String tipo) throws DAOException{
		if (unicaInstancia == null) {
			try { 
				unicaInstancia=(AbstractFactoriaDAO) Class.forName(tipo).newInstance();
			} catch (Exception e) {	
				throw new DAOException(e.getMessage());
			} 
		}
			
		return unicaInstancia;
	}
	

	public static AbstractFactoriaDAO getInstancia() throws DAOException{
		if(unicaInstancia == null) return getInstancia(AbstractFactoriaDAO.DAO_TDS);
		else return unicaInstancia;
	}

	protected AbstractFactoriaDAO (){}
	
	// Metodos factoria para obtener adaptadores
	
	public abstract InterfaceUsuarioDAO getUsuarioDAO();
	public abstract InterfaceContactoDAO getContactoDAO();
	public abstract InterfaceGrupoDAO getGrupoDAO();
	public abstract InterfaceNoContactoDAO getNoContactoDAO();
	public abstract InterfaceMensajeDAO getMensajeDAO();
}
