package umu.tds.AppChat.ui;

import java.awt.Color;

import javax.swing.JPanel;

public class OptionsPane extends PanelGrande {

	private static final long serialVersionUID = 1L;

	private Background fondo;
	private JPanel contactsPreview;
	private JPanel groupsPreview;
	
	private final Color Gray = new Color(64, 68, 75);
	
	public OptionsPane() {

		this.fondo = new Background();
		this.fondo.setBounds(0, 60, 920, 660);
		this.add(fondo);
		
	}

}
