package umu.tds.AppChat.ui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import umu.tds.AppChat.backend.utils.Membership.MembershipType;
import umu.tds.AppChat.controllers.UIController;

import java.awt.Color;

public class PremiumShopPanel extends PanelGrande {

	private static final long serialVersionUID = 1L;
	
	private Background fondo;
	private JLabel banner;
	private JLabel price;
	private JButton buyButton;
	
	private final String standar = "/assets/premium_membership_Standar_Reescalada.png";
	private final String special = "/assets/premium_membership_Discount_Especial_Reescalada.png";
	private final String celebration = "/assets/premium_membership_Discount_Celebration_Reescalada.png";
	
	public PremiumShopPanel() {
		
		this.fondo = new Background();
		this.fondo.setBounds(0, 60, 920, 660);
		this.add(fondo);
		
		this.price = new JLabel();
		this.price.setForeground(Color.DARK_GRAY);
		this.price.setSize(50, 20);
		this.price.setLocation(490, 276);
		this.price.setOpaque(false);
		this.fondo.add(price);
		
		this.buyButton = new JButton("Buy Premium");
		this.buyButton.setBounds(395, 550, 130, 35);
		this.buyButton.setForeground(Color.WHITE);
		this.buyButton.setBackground(new Color(241, 57, 83));
		this.buyButton.addActionListener(e -> UIController.getUnicaInstancia().buyPremium());
		this.fondo.add(buyButton);
		
		this.banner = new JLabel();
		this.banner.setOpaque(false);
		this.banner.setBounds(5, 40, 910, 518);
		//this.banner.setIcon(new ImageIcon(new ImageIcon(getClass().getResource(this.standar)).getImage().getScaledInstance(853, 812, Image.SCALE_SMOOTH)));
		//this.banner.setIcon(new ImageIcon(getClass().getResource(standar)));
		this.fondo.add(banner);
	}
	
	public void showShop(double precio, MembershipType type) {
		switch (type) {
		case STANDAR : {
			this.banner.setIcon(new ImageIcon(getClass().getResource(standar)));
			break;
		}
		case SPECIAL : {
			this.banner.setIcon(new ImageIcon(getClass().getResource(special)));
			break;
		}
		case CELEBRATION : {
			this.banner.setIcon(new ImageIcon(getClass().getResource(celebration)));
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
		
		this.price.setText(precio + " $");
		
		this.banner.repaint();
		this.price.repaint();
		this.banner.revalidate();
		this.price.revalidate();
	}

}
