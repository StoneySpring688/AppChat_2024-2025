package umu.tds.AppChat.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ElementoChatOGrupo extends JPanel {
	private static final long serialVersionUID = 1L;
	private ImageIcon imagen;
	private int numero;
	private String nombre;
	JPanel panelInfo;
	Color colorElemento;
	boolean isGrupo;
	
	private final Color darkPorDefecto = new Color(54, 57, 63);
	
	public ElementoChatOGrupo(String nom, int num, ImageIcon img, Optional<Boolean> isGrupo) {
		this.colorElemento = this.darkPorDefecto;
		setBackground(this.darkPorDefecto);
		this.nombre = nom;
		this.numero = num;
		this.imagen = img;
		this.isGrupo = isGrupo.orElse(false);
		
		this.setLayout(new BorderLayout());
		this.setBorder(new LineBorder(this.darkPorDefecto, 1));
		this.setSize(240,65);
		
		JLabel lblImagen = new JLabel(this.imagen);
		lblImagen.setBackground(this.darkPorDefecto);
		lblImagen.setIcon(new ImageIcon(getClass().getResource("/assets/ProfilePic.png")));
		lblImagen.setBounds(0,5,55,55);
		this.add(lblImagen, BorderLayout.WEST);
		
		if (! this.isGrupo) {
			panelInfo = new JPanel();
			panelInfo.setBackground(this.darkPorDefecto);
	        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
	        /*
	        JLabel lblNombre = new JLabel("Nombre: " + nombre);
	        JLabel lblNumero = new JLabel("Número: " + numero);
	        */
	        JLabel lblNombre = new JLabel(nombre);
	        lblNombre.setForeground(Color.WHITE);
	        JLabel lblNumero = new JLabel("" + numero);
	        lblNumero.setForeground(Color.WHITE);

	        panelInfo.add(lblNombre);
	        panelInfo.add(lblNumero);
	        this.add(panelInfo, BorderLayout.CENTER);
		}
		else if(this.isGrupo) {
			panelInfo = new JPanel();
			panelInfo.setBackground(this.darkPorDefecto);
			panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.X_AXIS));
			/*
			JLabel lblNombre = new JLabel("Nombre: " + nombre);
			*/
			JLabel lblNombre = new JLabel(nombre);
			lblNombre.setForeground(Color.WHITE);
			
			panelInfo.add(lblNombre);
			this.add(panelInfo, BorderLayout.CENTER);
		}
        
	}
	
	public void cambio_color(Color color) {
		this.panelInfo.setBackground(color);
		this.setBackground(color);
		
		
	}
}
