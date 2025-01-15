package umu.tds.AppChat.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.MatteBorder;

public class MessageList extends JPanel {

	private static final long serialVersionUID = 1L;
	private DefaultListModel<ElementoMessage> messages;
	private JList<ElementoMessage> lista;
	private JScrollPane scroll;
	private final Color darkPorDefecto = new Color(54, 57, 63);
	
	public MessageList() {
		setBackground(this.darkPorDefecto);
		//this.setLayout(new BorderLayout());
		this.setLayout(null);
		this.setBounds(120, 0, 240, 660);
		this.messages = new DefaultListModel<>();
		
		//Para probar
		for(int i=0 ;i<20 ;i++) {
			messages.addElement(new ElementoMessage("mensaje de prueba", "User1", "User2"));
		}
		
		
		this.lista = new JList<>(messages);
		this.lista.setCellRenderer(new ElementoMessageRender(this.lista));
		this.lista.setBackground(this.darkPorDefecto);
		
		
		this.scroll = new JScrollPane(lista);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.scroll.setVerticalScrollBar(new ScrollBar());
		this.scroll.setBorder(BorderFactory.createEmptyBorder());
		this.scroll.setBackground(this.darkPorDefecto);
		//this.add(this.scroll, BorderLayout.CENTER);
		this.scroll.setBounds(0, 65, 240, 595);
		this.add(this.scroll, BorderLayout.CENTER);
		
		//panel de padding
		JPanel buttonAnyadirUser = new JPanel();
		buttonAnyadirUser.setLayout(null);
		buttonAnyadirUser.setBackground(this.darkPorDefecto);
		buttonAnyadirUser.setBounds(0, 0, 240, 60);
		buttonAnyadirUser.setBorder(new MatteBorder(0, 0, 1, 0, new Color(40, 40, 40)));
		this.add(buttonAnyadirUser);
	}

}
