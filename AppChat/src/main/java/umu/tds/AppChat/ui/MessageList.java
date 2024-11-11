package umu.tds.AppChat.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class MessageList extends JPanel {

	private static final long serialVersionUID = 1L;
	private DefaultListModel<ElementoMessage> messages;
	private JList<ElementoMessage> lista;
	private JScrollPane scroll;
	private final Color darkPorDefecto = new Color(54, 57, 63);
	
	public MessageList() {
		setBackground(this.darkPorDefecto);
		this.setLayout(new BorderLayout());
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
		
		this.add(this.scroll, BorderLayout.CENTER);
	}

}
