package umu.tds.AppChat.ui.chatInterface;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import umu.tds.AppChat.ui.Background;
import umu.tds.AppChat.ui.PanelGrande;

public class ChatPanel extends PanelGrande {
	
	private static final long serialVersionUID = 1L;
	
	private Background fondo;
	private ChatArea chat;

	public ChatPanel() {
		this.fondo = new Background();
		this.fondo.setBounds(0, 60, 920, 660);
		this.add(fondo);
		
		this.chat = new ChatArea();
		this.chat.setBounds(5, 5, 915, 655);
		this.fondo.add(chat);
		
		SimpleDateFormat fechaAux = new SimpleDateFormat("dd/MM/yyyy, hh:mmaa");
		
		this.chat.addChatEvent(new ChatEvent() {
			
			@Override
			public void mousePressedSendButton(ActionEvent evt) {
				//ImageIcon icono = new ImageIcon(getClass().getResource("/assets/ProfilePic.png"));
				Icon icono = new ImageIcon(getClass().getResource("/assets/ProfilePic.png"));
				String nombre = "UserPrueba1";
				String fecha =fechaAux.format(new Date());
				String mensaje = chat.getText().trim();
				chat.addChatBox(new ModelMessage(icono, nombre, fecha, mensaje), ChatBox.BoxType.RIGHT);
				chat.resetText();
				
				
			}
			
			@Override
			public void mousePressedEmojiButton(ActionEvent evt) {
				chat.showHideEmojiPanel();
				
			}
			
			@Override
			public void keyTyped(KeyEvent evt) {
				
			}
		});
		
		
		
	}

}
