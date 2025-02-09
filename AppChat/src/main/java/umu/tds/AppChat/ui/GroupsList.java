package umu.tds.AppChat.ui;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.MatteBorder;

import umu.tds.AppChat.backend.utils.Grupo;
import umu.tds.AppChat.controllers.UIController;

public class GroupsList extends JPanel {

	private static final long serialVersionUID = 1L;
	private DefaultListModel<ElementoChatOGrupo> groups;
	private JList<ElementoChatOGrupo> lista;
	private JScrollPane scroll;
	
	//private final static String defaultGroupUrl = "https://github.com/StoneySpring688/AppChat_2024-2025/blob/main/AppChat/src/main/resources/assets/ProfilePic.png?raw=true";
	
	private final Color darkPorDefecto = new Color(54, 57, 63);

	public GroupsList() {
		setBackground(this.darkPorDefecto);
		this.setLayout(null);
		this.setBounds(120, 0, 240, 660);
		this.groups = new DefaultListModel<>();
		
		/*
		//Para probar
		
		for(int i=0 ;i<10 ;i++) {
			groups.addElement(new ElementoChatOGrupo(Optional.empty() , Optional.of(new Grupo(6485645230L+i, "Group "+i, defaultGroupUrl))));
		}
		*/
		
		this.lista = new JList<>(groups);
		this.lista.setCellRenderer(new ElementoChatOGrupoRender(this.lista));
		this.lista.setBackground(this.darkPorDefecto);
		
		
		this.scroll = new JScrollPane(lista);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.scroll.setVerticalScrollBar(new ScrollBar());
		this.scroll.setBorder(BorderFactory.createEmptyBorder());
		this.scroll.setBackground(this.darkPorDefecto);
		
		this.scroll.setBounds(0, 65, 240, 595);
		this.add(this.scroll);
		
		//boton crear grupo
		JPanel buttonAnyadirGrupo = new JPanel();
		JLabel iconAnyadirGrupo = new JLabel();
		JLabel lblAnyadirGrupo = new JLabel("Create Group");
		ImageIcon icono = new ImageIcon(getClass().getResource("/assets/ui-talent-s-mika-03.png"));
		iconAnyadirGrupo.setIcon(new ImageIcon(icono.getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH)));
		iconAnyadirGrupo.setBounds(13, 12, 44, 44);
		lblAnyadirGrupo.setBounds(61, 23, 90, 30);
		lblAnyadirGrupo.setForeground(Color.WHITE);
		buttonAnyadirGrupo.add(iconAnyadirGrupo);
		buttonAnyadirGrupo.add(lblAnyadirGrupo);
		buttonAnyadirGrupo.setLayout(null);
		buttonAnyadirGrupo.setBackground(this.darkPorDefecto);
		buttonAnyadirGrupo.setBounds(0,  0, 240, 60);
		buttonAnyadirGrupo.setBorder(new MatteBorder(0, 0, 1, 0, new Color(40, 40, 40)));
		this.add(buttonAnyadirGrupo);

		buttonAnyadirGrupo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UIController.showPanelCrearGrupo();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblAnyadirGrupo.setForeground(new Color(173, 216, 230)); // azul claro
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblAnyadirGrupo.setForeground(Color.WHITE);
			}

		});

		this.lista.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				// Obtener el índice del elemento clickeado
				int index = lista.locationToIndex(evt.getPoint());
				if (index >= 0) {
					// Obtener el elemento clickeado
					ElementoChatOGrupo elemento = groups.getElementAt(index);
					UIController.changeChat(elemento);
					//System.out.println("Elemento clickeado: " + elemento.getGroupID()); ///TODO
				}
			}
		});
				
	}
	
	public void addGroup(Grupo grupo) {
		this.groups.addElement(new ElementoChatOGrupo(Optional.empty(), Optional.of(grupo)));
	}

}
