package umu.tds.AppChat.controllers;

import java.util.List;
import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.backend.services.ChatService;

public class BackendController {
    private ChatService chatService;

    public BackendController() {
        this.chatService = new ChatService(15);
    }

    public void nuevoMensaje(int chatID, ModelMessage mensaje) {
        chatService.addMessage(chatID, mensaje);
    }

    public List<ModelMessage> obtenerMensajesChat() {
        return chatService.getMsgChatActual();
    }
}

