package umu.tds.AppChat.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class ChatsList extends JPanel {

	private static final long serialVersionUID = 1L;
	DefaultListModel<ElementoChatOGrupo> chats;
	JList<ElementoChatOGrupo> lista;
	JScrollPane scroll;
	
	/**
	 * Create the panel.
	 */
	public ChatsList() {
		setBackground(Color.GRAY);
		this.setLayout(new BorderLayout());
		this.setBounds(120, 0, 240, 660);
		this.chats = new DefaultListModel<>();
		
		//Para probar
		
		chats.addElement(new ElementoChatOGrupo("User1", 648163506, new ImageIcon(getClass().getResource("/assets/ProfilePic.png")),Optional.empty()));
		chats.addElement(new ElementoChatOGrupo("User2", 648564523, new ImageIcon(getClass().getResource("/assets/ProfilePic.png")),Optional.empty()));
		for(int i=0 ;i<10 ;i++) {
			chats.addElement(new ElementoChatOGrupo("User", 648564523, new ImageIcon(getClass().getResource("/assets/ProfilePic.png")),Optional.empty()));
		}
		
		
		this.lista = new JList<>(chats);
		this.lista.setCellRenderer(new ElementoChatOGrupoRender(this.lista));
		this.lista.setBackground(Color.GRAY);
		
		
		this.scroll = new JScrollPane(lista);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.scroll.setVerticalScrollBar(new ScrollBar());
		this.scroll.setBorder(BorderFactory.createEmptyBorder());
		this.scroll.setBackground(Color.GRAY);
		
		this.add(this.scroll, BorderLayout.CENTER);
		
	}

}
