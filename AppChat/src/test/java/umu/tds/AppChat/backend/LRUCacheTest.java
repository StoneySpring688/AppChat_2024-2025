package umu.tds.AppChat.backend;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import umu.tds.AppChat.backend.utils.LRUCache;

import java.util.List;

public class LRUCacheTest {

    private LRUCache<Integer, String> chatCache;

    @BeforeEach
    void setUp() {
        // Crear caché con capacidad máxima de 3 chats
        chatCache = new LRUCache<>(3, 10, 0.75f, true);
    }

    @Test
    public void testAddMessagesToChat() {
        // Agregar mensajes a diferentes chats
        chatCache.addMessageToChat(1, "Hola");
        chatCache.addMessageToChat(1, "¿Cómo estás?");
        chatCache.addMessageToChat(2, "Buen día");
        chatCache.addMessageToChat(3, "¿Qué tal?");

        // Verificar que los mensajes se almacenan correctamente
        assertEquals(List.of("Hola", "¿Cómo estás?"), chatCache.getMessages(1), "Mensajes incorrectos en chat 1");
        assertEquals(List.of("Buen día"), chatCache.getMessages(2), "Mensajes incorrectos en chat 2");
        assertEquals(List.of("¿Qué tal?"), chatCache.getMessages(3), "Mensajes incorrectos en chat 3");
    }

    @Test
    public void testEvictionPolicy() {
        // Llenar la caché con 3 chats
        chatCache.addMessageToChat(1, "Hola");
        chatCache.addMessageToChat(2, "Buen día");
        chatCache.addMessageToChat(3, "¿Qué tal?");

        // Agregar un nuevo chat (4) y verificar que el chat menos usado (1) se elimina
        chatCache.addMessageToChat(4, "Nuevo chat");

        // Verificar que el chat 1 ha sido eliminado
        assertTrue(chatCache.getMessages(1).isEmpty(), "El chat 1 debería haber sido eliminado");
        assertFalse(chatCache.getMessages(2).isEmpty(), "El chat 2 debería seguir existiendo");
        assertFalse(chatCache.getMessages(3).isEmpty(), "El chat 3 debería seguir existiendo");
        assertFalse(chatCache.getMessages(4).isEmpty(), "El chat 4 debería haber sido agregado");
    }

    @Test
    public void testGetMessagesFromEmptyChat() {
        // Verificar que un chat sin mensajes devuelve una lista vacía
        assertTrue(chatCache.getMessages(99).isEmpty(), "Un chat vacío debería devolver una lista vacía");
    }
}
