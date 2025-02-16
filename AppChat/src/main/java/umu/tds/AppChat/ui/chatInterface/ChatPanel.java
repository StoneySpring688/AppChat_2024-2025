package umu.tds.AppChat.ui.chatInterface;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.ImageIcon;

import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.controllers.UIController;
import umu.tds.AppChat.ui.Background;
import umu.tds.AppChat.ui.ElementoChatOGrupo;
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
		
		//this.emojiPanel = this.emojiPanel==null ? new EmojiPanel() : this.emojiPanel;
		
		this.emojiPanel = new EmojiPanel();
		
		this.chat = new ChatArea(this.emojiPanel);
		this.chat.setBounds(5, 5, 915, 655);
		this.fondo.add(chat);
		
		this.emojiPanel.addEmojiClickListener(new EmojiClickListener() {
			
			@Override
			public void emojiClicked(ImageIcon emoji, int id) {

				 if (chat == null) {
		                System.out.println("[ERROR] No hay chat activo en este ChatPanel");
		                return;
		            }
				
				System.out.println("[DEBUG]" + " ChatPanel " + "enviando emoji a : " + chat.getCurrentChatID());
				
				UIController.sendMessage(chat.getCurrentChatID() ,Optional.empty(), Optional.of(id));
			}
		});

		this.chat.addChatEvent(new ChatEvent() {
			
			@Override
			public void mousePressedSendButton(ActionEvent evt) {
				
				System.out.println("[DEBUG]" + " ChatPanel " + "enviando mensaje a : " + chat.getCurrentChatID());
				
				String mensaje = chat.getText().trim();
				UIController.sendMessage(chat.getCurrentChatID() ,Optional.of(mensaje), Optional.empty());
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
	
	public void addMessage(List<ModelMessage> msgs, int userNumber) {
		for(ModelMessage msg : msgs) {
			this.chat.addChatBox(msg, msg.getSender() == userNumber ? BoxType.RIGHT : BoxType.LEFT);
		}
	}
	
	public void addMessageAtTop(List<ModelMessage> msgs, int userNumber) {
	    int batchSize = 5;
	    List<ModelMessage> batch = new ArrayList<>();

	    for (int i = msgs.size() - 1; i >= 0; i--) {
	        ModelMessage msg = msgs.get(i);
	        batch.add(msg);

	        
	        if (batch.size() == batchSize || i == 0) { // renderizar en lotes de máximo 5
	            List<ModelMessage> finalBatch = new ArrayList<>(batch); // Copia para evitar modificaciones concurrentes
	            
	            this.chat.addChatBoxAtTop(finalBatch, userNumber);

	            batch.clear(); // Limpiar lote para los siguientes mensajes
	        }
	    }
	}

	public void resetText() {
		chat.resetText();
	}
	
	public void changeChat(ElementoChatOGrupo chat) {
		
		System.out.println("[DEBUG]" + " ChatPanel " + "cambiando chat a : " + chat.getNumero());
		
			this.chat.clearChatBox();
			this.chat.setCurrentChat(chat);
		
	}

}
