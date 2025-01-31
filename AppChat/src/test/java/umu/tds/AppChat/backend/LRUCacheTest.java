package umu.tds.AppChat.backend;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import umu.tds.AppChat.backend.utils.LRUCache;

import java.util.List;

/**
 * Unit test for LRUCache.
 */
public class LRUCacheTest {

    /**
     * Test the LRU cache behavior.
     */
    @Test
    public void testLRUCacheBehavior() {
        System.out.println("Inicio de la prueba de LRUCache...");
        
        // Crear caché con capacidad máxima de 3 chats
        LRUCache<Integer, String> chatCache = new LRUCache<>(3, 10, 0.75f, true);

        // Agregar mensajes
        chatCache.addMessageToChat(1, "Hola");
        chatCache.addMessageToChat(1, "¿Cómo estás?");
        chatCache.addMessageToChat(2, "Buen día");
        chatCache.addMessageToChat(3, "¿Qué tal?");
        
        System.out.println("Se han insertado mensajes en los chats 1, 2 y 3.");

        // Verificar inserción correcta
        assertEquals(List.of("Hola", "¿Cómo estás?"), chatCache.getMessages(1));
        assertEquals(List.of("Buen día"), chatCache.getMessages(2));
        assertEquals(List.of("¿Qué tal?"), chatCache.getMessages(3));

        // Insertar nuevo chat y verificar eliminación del menos usado
        chatCache.addMessageToChat(4, "Nuevo chat");
        System.out.println("Se ha agregado un mensaje en el chat 4, el chat 1 debería haber sido eliminado.");
        
        assertTrue(chatCache.getMessages(1).isEmpty(), "[ERROR] El chat 1 debería haber sido eliminado");
        assertFalse(chatCache.getMessages(2).isEmpty(), "[ERROR] El chat 2 debería seguir existiendo");
        assertFalse(chatCache.getMessages(3).isEmpty(), "[ERROR] El chat 3 debería seguir existiendo");
        assertFalse(chatCache.getMessages(4).isEmpty(), "[ERROR] El chat 4 debería haber sido agregado");
        
        System.out.println("Todas las verificaciones pasaron correctamente.");
        System.out.println("Prueba de LRUCache exitosa.");
    }
}
