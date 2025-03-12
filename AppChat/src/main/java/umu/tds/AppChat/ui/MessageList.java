package umu.tds.AppChat.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.MatteBorder;

import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.controllers.UIController;

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
		
		/*
		for(int i=0 ;i<20 ;i++) {
			//messages.addElement(new ElementoMessage("mensaje de prueba", "User1", "User2"));
			ElementoMessage emsg =  new ElementoMessage(new ModelMessage(new ImageIcon(getClass().getResource("/assets/ProfilePic.png")), "USER_DESCONOCIDO "+i, "dd / MM / yyyy", 123456789+i , 1234567890 , Optional.of("Mensaje de contacto"), Optional.empty()));
			messages.addElement(emsg);
		}
		*/
		
		this.lista = new JList<>(messages);
		this.lista.setCellRenderer(new ElementoMessageRender(this.lista));
		this.lista.setBackground(this.darkPorDefecto);
		this.lista.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                // Obtener el índice del elemento clickeado
                int index = lista.locationToIndex(evt.getPoint());
                if (index >= 0) {
                    // Obtener el elemento clickeado
                    ModelMessage elemento = new ModelMessage(messages.getElementAt(index).getMsg());
                    //System.out.println("[DEBUG] " + '\n' + elemento);
                    UIController.previewMessage(elemento);
                }
            }
        });
		
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
	
	public void reset() {
	    this.messages.clear();
	    this.lista.setModel(this.messages);
	    this.lista.revalidate();
	    this.lista.repaint();
	}


}
