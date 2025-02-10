package umu.tds.AppChat.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.MatteBorder;
import javax.swing.text.DateFormatter;

import umu.tds.AppChat.backend.utils.Membership;
import umu.tds.AppChat.backend.utils.Membership.MembershipType;
import umu.tds.AppChat.controllers.BackendController;

public class MembershipList extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private DefaultListModel<ElementoMembership> mships;
	private JList<ElementoMembership> lista;
	private JScrollPane scroll;
	
	private final Color darkPorDefecto = new Color(54, 57, 63);
	
	public MembershipList() {
		this.setBackground(this.darkPorDefecto);
		this.setLayout(null);
		this.setBounds(120, 0, 240, 660);
		this.mships = new DefaultListModel<>();
		
		// TODO Para probar
		for(int i=0 ;i<20 ;i++) {
			ElementoMembership emship = new ElementoMembership(new Membership(MembershipType.SPECIAL, "test "+i, 1.99+i));
			mships.addElement(emship);
		}
		
		this.lista = new JList<>(mships);
		this.lista.setCellRenderer(new ElementoMembershipRender(this.lista));
		this.lista.setBackground(this.darkPorDefecto);
		this.lista.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                // Obtener el índice del elemento clickeado
                int index = lista.locationToIndex(evt.getPoint());
                if (index >= 0) {
                   // TODO cambio panel al de esta oferta o al estandar
                }
            }
        });
		
		this.scroll = new JScrollPane(lista);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.scroll.setVerticalScrollBar(new ScrollBar());
		this.scroll.setBorder(BorderFactory.createEmptyBorder());
		this.scroll.setBackground(this.darkPorDefecto);
		this.scroll.setBounds(0, 65, 240, 595);
		this.add(this.scroll, BorderLayout.CENTER);
		
		//panel duración premium
		JPanel expireDate = new JPanel();
		expireDate.setLayout(null);
		expireDate.setBackground(this.darkPorDefecto);
		expireDate.setBounds(0, 0, 240, 60);
		expireDate.setBorder(new MatteBorder(0, 0, 1, 0, new Color(40, 40, 40)));
		this.add(expireDate);
		
		JLabel expiresMsg = new JLabel("premium expires : ");
		expiresMsg.setForeground(Color.WHITE);
		expiresMsg.setBounds(12, 12, 126, 18);
		expireDate.add(expiresMsg);
		
		JLabel premiumExpireDate = new JLabel();
		Optional<LocalDate> expireLocalDate = BackendController.getEndPremium();
		premiumExpireDate.setText(expireLocalDate.isPresent() ? expireLocalDate.get().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString() : "not premium user");
		premiumExpireDate.setForeground(Color.WHITE);
		premiumExpireDate.setBounds(89, 30, 139, 18);
		expireDate.add(premiumExpireDate);
		
		
		
	}
	
}
