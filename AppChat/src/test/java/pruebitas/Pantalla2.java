package pruebitas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.BevelBorder;

public class Pantalla2 extends JPanel{
	private JLabel labelImegen;
	private ImageIcon oscuro1,oscuro2;
	
	public Pantalla2() {
		oscuro1=new ImageIcon(getClass().getResource("/resources/oscuro-1.gif"));
		oscuro2=new ImageIcon(getClass().getResource("/resources/oscuro-2.gif"));
		
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		this.setMaximumSize(new Dimension(690,470));
		this.setMinimumSize(new Dimension(690,470));
		this.setPreferredSize(new Dimension(690,470));
		this.setBackground(Color.DARK_GRAY);
		JButton boton=new JButton();
		boton.setIcon(new ImageIcon(getClass().getResource("/resources/iconoboton.png")));
		boton.setMinimumSize(new Dimension(78,78));
		boton.setMaximumSize(new Dimension(78,78));
		boton.setPreferredSize(new Dimension(78,78));
		boton.setAlignmentY(JComponent.TOP_ALIGNMENT);
		boton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				labelImegen.setIcon(oscuro2);
			}
			
		});
		labelImegen =new JLabel();
		labelImegen.setMinimumSize(new Dimension(470,470));
		labelImegen.setMaximumSize(new Dimension(470,470));
		labelImegen.setPreferredSize(new Dimension(470,470));
		labelImegen.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		labelImegen.setIcon(new ImageIcon(Pantalla2.class.getResource("/resources/oscuro-1.gif")));
		this.add(Box.createHorizontalStrut(50));
		this.add(boton);
		this.add(Box.createHorizontalStrut(84));
		this.add(labelImegen);
	}
}
