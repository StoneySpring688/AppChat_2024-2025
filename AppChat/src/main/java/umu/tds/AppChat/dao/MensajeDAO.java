package umu.tds.AppChat.dao;

import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.ImageIcon;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.controllers.DAOController;
import umu.tds.AppChat.ui.RegisterPanel;

public class MensajeDAO implements InterfaceMensajeDAO {
	
	// entidad
	private static final String MSG = "Msg";
	
	// propiedades
	private static final String ICONURL = "IconUrl";
	private static final String SENDER = "Sender";
	private static final String RECIVER = "Reciver";
	private static final String SENDERNAME = "SenderName";
	private static final String SENDDATE = "SendDate";
	private static final String MESSAGE = "Message"; // "" vacio si optional.empty()
	private static final String EMOJI = "Emoji"; // "" vacio si optional.empty()
	
	// utils
	private ServicioPersistencia servPersistencia;
	private final static String defaultProfileImage = "/assets/ProfilePic.png";
	
	// singleton
	private static MensajeDAO unicaInstancia = null;
	
	public static MensajeDAO getUnicaInstancia() {
		if (unicaInstancia == null)
			return new MensajeDAO();
		else
			return unicaInstancia;
	}
	
	public MensajeDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	private ModelMessage entidadToMsg(Entidad eMsg) {
		String iconUrl = servPersistencia.recuperarPropiedadEntidad(eMsg, ICONURL);
		String sender = servPersistencia.recuperarPropiedadEntidad(eMsg, SENDER);
		String reciver = servPersistencia.recuperarPropiedadEntidad(eMsg, RECIVER);
		String senderName = servPersistencia.recuperarPropiedadEntidad(eMsg, SENDERNAME);
		String sendDate = servPersistencia.recuperarPropiedadEntidad(eMsg, SENDDATE);
		String message = servPersistencia.recuperarPropiedadEntidad(eMsg, MESSAGE);
		String emoji = servPersistencia.recuperarPropiedadEntidad(eMsg, EMOJI);
		
		ImageIcon icon = null;
		try {
			icon = actualizarImagen(iconUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Optional<String> messageMsg = message.isBlank() ? Optional.empty() : Optional.of(message);
		Optional<Integer> emoojiMSG = emoji.isBlank() ? Optional.empty() : Optional.of(Integer.parseInt(emoji));
		
		ModelMessage msg = new ModelMessage(icon, senderName, sendDate, Integer.parseInt(sender), Integer.parseInt(reciver), messageMsg, emoojiMSG);
		return msg;
	}
	
	private Entidad msgToEntidad(ModelMessage msg) {
		Entidad eUser = new Entidad();
		eUser.setNombre(MSG);
		EntidadComunicable sender = DAOController.recuperarUser(msg.getSender()).get();
		
		eUser.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(
				new Propiedad(ICONURL, sender.getIconUrl()),
				new Propiedad(SENDERNAME, sender.getNombre()),
				new Propiedad(SENDER, Integer.toString(msg.getSender())),
				new Propiedad(RECIVER, Long.toString(msg.getReciver())),
				new Propiedad(SENDDATE, msg.getDate()),
				new Propiedad(MESSAGE, msg.getMessage().isEmpty() ? "" : msg.getMessage().get()),
				new Propiedad(EMOJI, msg.getEmoji().isEmpty() ? "" : Integer.toString(msg.getEmoji().get()))
				)));
		return eUser;
	}
	
	@Override
	public void create(ModelMessage msg) {
		Entidad eMsg = this.msgToEntidad(msg);
		eMsg = servPersistencia.registrarEntidad(eMsg);
		msg.setBDID(eMsg.getId());
	}

	@Override
	public boolean delete(ModelMessage msg) {
		Entidad eMsg;
		eMsg = servPersistencia.recuperarEntidad(msg.getBDID());
		return servPersistencia.borrarEntidad(eMsg);
	}

	@Override
	public ModelMessage get(int id) {
		Entidad eMsg = servPersistencia.recuperarEntidad(id);
		if(eMsg == null)return null;
		else {
			ModelMessage msg = entidadToMsg(eMsg);
			msg.setBDID(eMsg.getId());
			return msg;
		}
	}
	
	// ### utils
	
    private ImageIcon actualizarImagen(String urlString) throws MalformedURLException {
        URL url;
        
		try {
			url = new URL(urlString);
			ImageIcon icono = new ImageIcon(url);
			
	        if (icono.getIconWidth() > 0 && icono.getIconHeight() > 0) { // Verifica que la imagen sea válida
	            Image imagenEscalada = icono.getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH);
	            //image.setImage(imagenEscalada);
	            ImageIcon image =new ImageIcon(imagenEscalada);
	            return image;
	            //System.out.println("Imagen cargada exitosamente.");
	            
	        } else {
	            //System.out.println("Texto ingresado no contiene una imagen válida.");
	            ImageIcon image = new ImageIcon( new ImageIcon(RegisterPanel.class.getResource(defaultProfileImage)).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH) );
	            return image;
	        }
	        
		} catch (MalformedURLException e) {
            ImageIcon image = new ImageIcon( new ImageIcon(RegisterPanel.class.getResource(defaultProfileImage)).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH) );
            return image;
		}catch (NullPointerException e){
            ImageIcon image = new ImageIcon( new ImageIcon(RegisterPanel.class.getResource(defaultProfileImage)).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH) );
            return image;
		} catch (Exception e) {
            ImageIcon image = new ImageIcon( new ImageIcon(RegisterPanel.class.getResource(defaultProfileImage)).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH) );
            return image;
		}
    }

}
