package umu.tds.AppChat.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ChatsList extends JPanel {

	private static final long serialVersionUID = 1L;
	DefaultListModel<ElementoChatOGrupo> chats;
	JList<ElementoChatOGrupo> lista;
	JScrollPane scroll;
	
	/**
	 * Create the panel.
	 */
	public ChatsList() {
		this.setLayout(new BorderLayout());
		this.setBounds(120, 0, 240, 660);
		this.chats = new DefaultListModel<>();
		
		//Para probar
		/*
		chats.addElement(new ElementoChatOGrupo("User1", 648163506, new ImageIcon(getClass().getResource("/assets/ProfilePic.png"))));
		chats.addElement(new ElementoChatOGrupo("User2", 648564523, new ImageIcon(getClass().getResource("/assets/ProfilePic.png"))));
		for(int i=0 ;i<25 ;i++) {
			chats.addElement(new ElementoChatOGrupo("User", 648564523, new ImageIcon(getClass().getResource("/assets/ProfilePic.png"))));
		}
		*/
		
		this.lista = new JList<>(chats);
		this.lista.setCellRenderer(new ElementoChatOGrupoRender());
		this.lista.setBackground(Color.GRAY);
		
		
		this.scroll = new JScrollPane(lista);
		this.scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.scroll.setBackground(Color.GRAY);
		
		this.add(this.scroll, BorderLayout.CENTER);
		
	}

}
