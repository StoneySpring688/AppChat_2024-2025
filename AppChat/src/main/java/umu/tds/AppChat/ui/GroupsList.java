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

public class GroupsList extends JPanel {

	private static final long serialVersionUID = 1L;
	DefaultListModel<ElementoChatOGrupo> groups;
	JList<ElementoChatOGrupo> lista;
	JScrollPane scroll;
	private final Color darkPorDefecto = new Color(54, 57, 63);
	
	/**
	 * Create the panel.
	 */
	public GroupsList() {
		setBackground(this.darkPorDefecto);
		this.setLayout(new BorderLayout());
		this.setBounds(120, 0, 240, 660);
		this.groups = new DefaultListModel<>();
		
		//Para probar
		
		groups.addElement(new ElementoChatOGrupo("Group1", 648163506, new ImageIcon(getClass().getResource("/assets/ProfilePic.png")),Optional.of(true)));
		groups.addElement(new ElementoChatOGrupo("Group2", 648564523, new ImageIcon(getClass().getResource("/assets/ProfilePic.png")),Optional.of(true)));
		for(int i=0 ;i<10 ;i++) {
			groups.addElement(new ElementoChatOGrupo("Group", 648564523, new ImageIcon(getClass().getResource("/assets/ProfilePic.png")),Optional.of(true)));
		}
		
		
		this.lista = new JList<>(groups);
		this.lista.setCellRenderer(new ElementoChatOGrupoRender(this.lista));
		this.lista.setBackground(this.darkPorDefecto);
		
		
		this.scroll = new JScrollPane(lista);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.scroll.setVerticalScrollBar(new ScrollBar());
		this.scroll.setBorder(BorderFactory.createEmptyBorder());
		this.scroll.setBackground(this.darkPorDefecto);
		
		this.add(this.scroll, BorderLayout.CENTER);
	}

}
