package umu.tds.AppChat.ui;

import java.awt.Color;
import java.util.Optional;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;

public class ElementoChatOGrupo extends JPanel {
    private static final long serialVersionUID = 1L;
    private ImageIcon imagen;
    private int numero;
    private String nombre;
    private JPanel panelInfo;
    boolean isGrupo;
    
    private final Color darkPorDefecto = new Color(54, 57, 63);
    
    public ElementoChatOGrupo(String nom, int num, ImageIcon img, Optional<Boolean> isGrupo) {
        setBackground(this.darkPorDefecto);
        this.nombre = nom;
        this.numero = num;
        this.imagen = img;
        this.isGrupo = isGrupo.orElse(false);
        
        // Configuración de MigLayout
        setLayout(new MigLayout("wrap 2, insets 5", "[55px][grow,fill]", "[grow,fill][grow,fill]"));
        setBorder(new LineBorder(this.darkPorDefecto, 1));
        setSize(240, 65);
        
        // Imagen de perfil
        ImageAvatar avatar = new ImageAvatar();
        avatar.setBackground(this.darkPorDefecto);
        avatar.setImage(new ImageIcon(getClass().getResource("/assets/ProfilePic.png")));
        avatar.setBorderSize(1);
        avatar.setBorderSpace(1);
        add(avatar, "cell 0 0, spany 2, height 50, width 50");
        
        if (!this.isGrupo) {
            panelInfo = new JPanel(new MigLayout("fill, insets 0, gap 0"));
            panelInfo.setBackground(this.darkPorDefecto);
            
            JLabel lblNombre = new JLabel(nombre);
            lblNombre.setForeground(Color.WHITE);
            panelInfo.add(lblNombre, "wrap");
            
            JLabel lblNumero = new JLabel("" + numero);
            lblNumero.setForeground(Color.WHITE);
            panelInfo.add(lblNumero);
            
            add(panelInfo, "cell 1 0, span 1 2, grow");
        } else {
            panelInfo = new JPanel(new MigLayout("fill, insets 0"));
            panelInfo.setBackground(this.darkPorDefecto);
            
            JLabel lblNombre = new JLabel(nombre);
            lblNombre.setForeground(Color.WHITE);
            panelInfo.add(lblNombre);
            
            add(panelInfo, "cell 1 0, span 1 2, grow");
        }
        
        this.addMouseListener(null);
    }
    
    public void cambio_color(Color color) {
        this.panelInfo.setBackground(color);
        this.setBackground(color);
    }
}
