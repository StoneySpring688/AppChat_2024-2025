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

import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.controllers.UIController;

public class ChatsList extends JPanel {

	private static final long serialVersionUID = 1L;
	DefaultListModel<ElementoChatOGrupo> chats;
	JList<ElementoChatOGrupo> lista;
	JScrollPane scroll;
	private final Color darkPorDefecto = new Color(54, 57, 63);
	
	public ChatsList(UIController uiController) {
		setBackground(this.darkPorDefecto);
		//this.setLayout(new BorderLayout());
		this.setLayout(null);
		this.setBounds(120, 0, 240, 660);
		this.chats = new DefaultListModel<>();
		
		/*
		chats.addElement(new ElementoChatOGrupo("User1", 648163506, new ImageIcon(getClass().getResource("/assets/ProfilePic.png")),Optional.empty()));
		chats.addElement(new ElementoChatOGrupo("User2", 648564523, new ImageIcon(getClass().getResource("/assets/ProfilePic.png")),Optional.empty()));
		chats.addElement(new ElementoChatOGrupo("User3", 648564523, new ImageIcon(getClass().getResource("/assets/ProfilePic.png")),Optional.empty()));
		for(int i=0 ;i<25 ;i++) {
			chats.addElement(new ElementoChatOGrupo("User", 648564523, new ImageIcon(getClass().getResource("/assets/ProfilePic.png")),Optional.empty()));
		}
		*/
		
		// lista
		this.lista = new JList<>(chats);
		this.lista.setCellRenderer(new ElementoChatOGrupoRender(this.lista));
		this.lista.setBackground(this.darkPorDefecto);
		
		this.scroll = new JScrollPane(lista);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.scroll.setVerticalScrollBar(new ScrollBar());
		this.scroll.setBorder(BorderFactory.createEmptyBorder());
		this.scroll.setBackground(this.darkPorDefecto);
		//this.add(this.scroll, BorderLayout.CENTER);
		this.scroll.setBounds(0, 65, 240, 595);
		this.add(this.scroll);
		
		//boton añadir usuario
		JPanel buttonAnyadirUser = new JPanel();
		JLabel iconAnyadirUser = new JLabel();
		JLabel lblAnyadirUser = new JLabel("Add Contact");
		ImageIcon icono = new ImageIcon(getClass().getResource("/assets/ui-talent-s-mika-03.png"));
		iconAnyadirUser.setIcon(new ImageIcon(icono.getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH)));
		iconAnyadirUser.setBounds(13, 12, 44, 44);
		lblAnyadirUser.setBounds(61, 23, 80, 30);
		lblAnyadirUser.setForeground(Color.WHITE);
		buttonAnyadirUser.add(iconAnyadirUser);
		buttonAnyadirUser.add(lblAnyadirUser);
		buttonAnyadirUser.setLayout(null);
		buttonAnyadirUser.setBackground(this.darkPorDefecto);
		buttonAnyadirUser.setBounds(0, 0, 240, 60);
		buttonAnyadirUser.setBorder(new MatteBorder(0, 0, 1, 0, new Color(40, 40, 40)));
		this.add(buttonAnyadirUser);
		
		buttonAnyadirUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				uiController.anyadirContacto();
				}
			
			@Override
            public void mouseEntered(MouseEvent e) {
		        lblAnyadirUser.setForeground(new Color(173, 216, 230)); // azul claro
            }
			
			@Override
            public void mouseExited(MouseEvent e) {
		        lblAnyadirUser.setForeground(Color.WHITE);
            }
			
			});
		
		this.lista.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                // Obtener el índice del elemento clickeado
                int index = lista.locationToIndex(evt.getPoint());
                if (index >= 0) {
                    // Obtener el elemento clickeado
                    ElementoChatOGrupo elemento = chats.getElementAt(index);
                    System.out.println("Elemento clickeado: " + elemento.getNombre()); //TODO
                }
            }
        });
		
	}
	
	public void addChat(EntidadComunicable contacto) {
		this.chats.addElement(new ElementoChatOGrupo(contacto.getNombre(), contacto.getNumero(), contacto.actualizarImagenFromUrl(), Optional.empty()));
	}

}
