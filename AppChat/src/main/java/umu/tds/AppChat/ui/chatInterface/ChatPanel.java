package umu.tds.AppChat.ui.chatInterface;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import umu.tds.AppChat.controllers.UIController;
import umu.tds.AppChat.ui.Background;
import umu.tds.AppChat.ui.PanelGrande;
import umu.tds.AppChat.ui.chatInterface.ChatBox.BoxType;

public class ChatPanel extends PanelGrande {
	
	private static final long serialVersionUID = 1L;
	
	private Background fondo;
	private ChatArea chat;
	private EmojiPanel emojiPanel;

	public ChatPanel() {
		this.fondo = new Background();
		this.fondo.setBounds(0, 60, 920, 660);
		this.add(fondo);
		
		this.emojiPanel = this.emojiPanel==null ? new EmojiPanel() : this.emojiPanel;
		this.emojiPanel.addEmojiClickListener(new EmojiClickListener() {
			
			@Override
			public void emojiClicked(ImageIcon emoji) {
				SimpleDateFormat fechaAux = new SimpleDateFormat("dd/MM/yyyy, hh:mmaa");
				Icon icono = new ImageIcon(getClass().getResource("/assets/ProfilePic.png"));
				String nombre = "UserPrueba1";
				String fecha =fechaAux.format(new Date());
				ModelMessage MMsg = new ModelMessage(icono, nombre, fecha, Optional.empty(), Optional.of(emoji));
				UIController.addMessage(chat, MMsg, BoxType.RIGHT);
				System.out.println("TODO hacer que funcione");
				
			}
		});
		
		this.chat = new ChatArea(this.emojiPanel);
		this.chat.setBounds(5, 5, 915, 655);
		this.fondo.add(chat);
		
		
		
		this.chat.addChatEvent(new ChatEvent() {
			
			@Override
			public void mousePressedSendButton(ActionEvent evt) {
				//ImageIcon icono = new ImageIcon(getClass().getResource("/assets/ProfilePic.png"));
				SimpleDateFormat fechaAux = new SimpleDateFormat("dd/MM/yyyy, hh:mmaa");
				Icon icono = new ImageIcon(getClass().getResource("/assets/ProfilePic.png"));
				String nombre = "UserPrueba1";
				String fecha =fechaAux.format(new Date());
				String mensaje = chat.getText().trim();
				ModelMessage MMsg = new ModelMessage(icono, nombre, fecha, Optional.of(mensaje), Optional.empty());
				UIController.addMessage(chat, MMsg, BoxType.RIGHT);
				
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
	
	public void addEmojiMessage(ImageIcon emoji) {
		
	}

}
