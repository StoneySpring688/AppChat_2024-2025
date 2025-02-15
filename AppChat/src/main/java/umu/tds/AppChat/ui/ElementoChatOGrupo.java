package umu.tds.AppChat.ui;

import java.awt.Color;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.net.URL;

import net.miginfocom.swing.MigLayout;
import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;

public class ElementoChatOGrupo extends JPanel {
    private static final long serialVersionUID = 1L;
    private ImageIcon imagen;
    private Optional<EntidadComunicable> contacto;
    private Optional<Grupo> grupo;
    private boolean isGrupo;
    private JPanel panelInfo;
    
    private final int MAX_CARACTERES_DISPLAY = 12;
    private final Color darkPorDefecto = new Color(54, 57, 63);
    
    public ElementoChatOGrupo(Optional<EntidadComunicable> contacto, Optional<Grupo> grupo) {
        setBackground(this.darkPorDefecto);
        try {
        	if(contacto.isPresent()) {
            	this.contacto = contacto;
            	this.imagen = new ImageIcon(new URL(this.contacto.get().getIconUrl()));
            	this.isGrupo = false;
            }else {
            	this.grupo = grupo;
            	this.imagen = new ImageIcon(new URL(this.grupo.get().getIconUrl()));
            	this.isGrupo = true;
            }
		} catch (Exception e) {
			this.imagen = new ImageIcon(getClass().getResource("/assets/ProfilePic.png"));
			if(contacto.isPresent()) {
            	this.contacto = contacto;
            	this.isGrupo = false;
            }else {
            	this.grupo = grupo;
            	this.isGrupo = true;
            }
		}
        
        
        // Configuración de MigLayout
        setLayout(new MigLayout("wrap 2, insets 5", "[55px][grow,fill]", "[grow,fill][grow,fill]"));
        setBorder(new LineBorder(this.darkPorDefecto, 1));
        setSize(240, 65);
        
        // Imagen de perfil
        ImageAvatar avatar = new ImageAvatar();
        avatar.setBackground(this.darkPorDefecto);
        avatar.setImage(this.imagen);
        avatar.setBorderSize(1);
        avatar.setBorderSpace(1);
        add(avatar, "cell 0 0, spany 2, height 50, width 50");
        
        if (!this.isGrupo) {
            panelInfo = new JPanel(new MigLayout("fill, insets 0, gap 0"));
            panelInfo.setBackground(this.darkPorDefecto);
            
            String nombre = getNombre().length() > MAX_CARACTERES_DISPLAY ? getNombre().substring(0, MAX_CARACTERES_DISPLAY) + "…" : getNombre(); //limita a 12 caracteres
            JLabel lblNombre = new JLabel(nombre);
            lblNombre.setForeground(Color.WHITE);
            panelInfo.add(lblNombre, "wrap");
            
            JLabel lblNumero = new JLabel("" + getNumero());
            lblNumero.setForeground(Color.WHITE);
            panelInfo.add(lblNumero);
            
            add(panelInfo, "cell 1 0, span 1 2, grow");
        } else {
            panelInfo = new JPanel(new MigLayout("fill, insets 0"));
            panelInfo.setBackground(this.darkPorDefecto);
            
            String nombre = getNombre().length() > MAX_CARACTERES_DISPLAY ? getNombre().substring(0, MAX_CARACTERES_DISPLAY) + "…" : getNombre(); //limita a 12 caracteres
            JLabel lblNombre = new JLabel(nombre);
            lblNombre.setForeground(Color.WHITE);
            panelInfo.add(lblNombre);
            
            add(panelInfo, "cell 1 0, span 1 2, grow");
        }
        

    }
    
    public Optional<EntidadComunicable> getContacto(){
    	return this.contacto == null || this.contacto.isEmpty() ? Optional.empty() : this.contacto;
    }
    
    public Optional<Grupo> getGrupo() {
    	return this.grupo == null || this.grupo.isEmpty() ? Optional.empty() : this.grupo;
    }
    
    public String getNombre() {
        return !this.isGrupo ? this.contacto.get().getNombre() : this.grupo.get().getNombre();
    }
    
    public int getNumero() {
        return !this.isGrupo ? this.contacto.get().getNumero() : null;
    }
    
    public long getGroupID() {
    	return this.isGrupo ? this.grupo.get().getID() : null;
    }
    
    public boolean isGrupo() {
    	return this.isGrupo;
    }
    
    public ImageIcon getProfilePic(){
    	return new ImageIcon(this.imagen.getImage());
    }
    
    public void cambio_color(Color color) {
        this.panelInfo.setBackground(color);
        this.setBackground(color);
    }
}
