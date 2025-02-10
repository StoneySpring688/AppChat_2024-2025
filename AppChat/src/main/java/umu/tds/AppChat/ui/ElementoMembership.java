package umu.tds.AppChat.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import umu.tds.AppChat.backend.utils.Membership;

public class ElementoMembership extends JPanel {

	private static final long serialVersionUID = 1L;
	private Membership mship;
	private JPanel panelInfo;
	
	private final Color darkPorDefecto = new Color(54, 57, 63);
	
	public ElementoMembership(Membership mship) {
		this.mship = mship;
		
		this.setBackground(this.darkPorDefecto);
		this.setBorder(new MatteBorder(0, 0, 1, 0, new Color(40, 40, 40)));
		
		this.setLayout(new BorderLayout());
		this.setBorder(new LineBorder(this.darkPorDefecto, 1));
		this.setSize(240,65);
		
		panelInfo = new JPanel();
		panelInfo.setBackground(this.darkPorDefecto);
		panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
		
		JLabel lblName = new JLabel(this.mship.getName());
		lblName.setForeground(Color.WHITE);
		JLabel lblPrice = new JLabel("Price : " + this.mship.getPrice());
		lblPrice.setForeground(Color.WHITE);
		
		panelInfo.add(lblName);
		panelInfo.add(lblPrice);
		
		this.add(panelInfo, BorderLayout.CENTER);
		this.add(new JSeparator(), BorderLayout.SOUTH);
	}
	
	public Membership getMship() {
		return new Membership(mship);
	}
	
	public void cambio_color(Color color) {
		this.panelInfo.setBackground(color);
		this.setBackground(color);
	}
	
}
