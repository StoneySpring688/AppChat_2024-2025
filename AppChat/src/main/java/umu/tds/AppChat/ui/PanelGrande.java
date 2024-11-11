package umu.tds.AppChat.ui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class PanelGrande extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel cabecera;
	private JLabel closeButton;
	
	private final Color Gray = new Color(64, 68, 75);

	public PanelGrande() {
		setBackground(this.Gray);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(null);
		
		//cabecera
		this.cabecera = new JPanel();
		cabecera.setBorder(new MatteBorder(0, 0, 1, 0, new Color(40, 40, 40)));
		this.cabecera.setLayout(null);
		this.cabecera.setBounds(0, 0, 920, 60);
		this.cabecera.setBackground(this.Gray);
		this.add(cabecera);

		//boton de cierre
		closeButton = new JLabel("");
		closeButton.setIcon(new ImageIcon(getClass().getResource("/assets/UI_Exit.png")));
		closeButton.setBounds(885, 2, 33, 33);
		this.cabecera.add(closeButton);
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0); // Cierra la aplicación
				}
			});
		
	}

}
