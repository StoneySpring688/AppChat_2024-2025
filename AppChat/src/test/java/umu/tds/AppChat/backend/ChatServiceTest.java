package umu.tds.AppChat.backend;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;
import javax.swing.ImageIcon;
import java.util.List;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.JUnitException;
import umu.tds.AppChat.backend.services.ChatService;
import umu.tds.AppChat.backend.utils.ModelMessage;

@SuppressWarnings("unused")
class ChatServiceTest {
    private static final int STRESS_TEST = 0; // Cambia a 0 para ejecutar la prueba normal
    private static final int CACHE_SIZE = (STRESS_TEST == 1) ? 500 : 10;
    private ChatService service;
    private long idGrupo;
    private long idContacto;

    @BeforeEach
    void setUp() {
        System.out.println("[TEST] STRESS_TEST = " + STRESS_TEST);
        service = new ChatService(CACHE_SIZE); // Tamaño de caché depende del tipo de prueba
        idGrupo = 1234567890L;
        idContacto = 123456789L;
    }

    @Test
    void testChatService() {
        switch (STRESS_TEST) {
            case 0:
                testAddAndRetrieveMessages();
                break;
            case 1:
                testStressPerformance();
                break;
            default:
                System.err.println("[ERROR] Unknown STRESS_CODE");
                break;
        }
    }

    void testAddAndRetrieveMessages() {
        ModelMessage msgContacto = new ModelMessage(new ImageIcon(getClass().getResource("/assets/ProfilePic.png")), "USER_DESCONOCIDO", "dd / MM / yyyy", 123456789 , 1234567890 , Optional.of("Mensaje de contacto"), Optional.empty());
        ModelMessage msgGrupo = new ModelMessage(new ImageIcon(getClass().getResource("/assets/ProfilePic.png")), "USER_DESCONOCIDO", "dd / MM / yyyy", 123456789 , 1234567890 ,  Optional.of("Mensaje de grupo"), Optional.empty());

        service.addMessage(idContacto, msgContacto);
        service.addMessage(idGrupo, msgGrupo);

        assertTrue(service.isInLRU(idContacto), "El mensaje del contacto no se encuentra en la caché");
        assertTrue(service.isInLRU(idGrupo), "El mensaje del grupo no se encuentra en la caché");

        List<ModelMessage> mensajesContacto = service.getLRUChat(idContacto);
        List<ModelMessage> mensajesGrupo = service.getLRUChat(idGrupo);

        assertEquals(1, mensajesContacto.size(), "Número incorrecto de mensajes en contacto");
        assertEquals(1, mensajesGrupo.size(), "Número incorrecto de mensajes en grupo");

        assertEquals("Mensaje de contacto", mensajesContacto.get(0).getMessage().get(), "Mensaje incorrecto en contacto");
        assertEquals("Mensaje de grupo", mensajesGrupo.get(0).getMessage().get(), "Mensaje incorrecto en grupo");
    }

    void testStressPerformance() {
        int numChats = 10000;
        int numMensajesPorChat = 90;
        int numEmojisPorChat = 10;

        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // Solicitar recolección de basura antes de la medición
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long memoriaAntes = memoryBean.getHeapMemoryUsage().getUsed();
        long tiempoInicio = System.currentTimeMillis();

        for (long chatId = 1; chatId <= numChats; chatId++) {
            for (int i = 0; i < numEmojisPorChat; i++) {
                service.addMessage(chatId, new ModelMessage(new ImageIcon(getClass().getResource("/assets/ProfilePic.png")), "USUARIO_" + chatId, "dd / MM / yyyy", 123456789 , 1234567890 , Optional.empty(), Optional.of(1)));
            }
            
            for (int i = 0; i < numMensajesPorChat; i++) {
                service.addMessage(chatId, new ModelMessage(new ImageIcon(getClass().getResource("/assets/ProfilePic.png")), "USUARIO_" + chatId, "dd / MM / yyyy", 123456789 , 1234567890 ,  Optional.of("Mensaje " + i), Optional.empty()));
            }
        }
        
        long tiempoFin = System.currentTimeMillis();
        long tiempoTotalSegundos = (tiempoFin - tiempoInicio) / 1000;
        long memoriaDespues = memoryBean.getHeapMemoryUsage().getUsed();
        long memoriaConsumidaMB = (memoriaDespues - memoriaAntes) / (1024 * 1024);
        
        System.out.println("[TEST] Num Chats en cache: " + CACHE_SIZE);
        System.out.println("[TEST] Num Chats: " + numChats + " | " + "Num Mensajes por chat: " + numMensajesPorChat + " | " + "Num Emojis por chat: " + numEmojisPorChat);
        System.out.println("[TEST] Memoria consumida en STRESS_TEST: " + memoriaConsumidaMB + " MB");
        System.out.println("[TEST] Tiempo total en STRESS_TEST: " + tiempoTotalSegundos + " segundos");

        for (long chatId = numChats - CACHE_SIZE + 1; chatId <= numChats; chatId++) {
            assertTrue(service.isInLRU(chatId), "El chat " + chatId + " debería estar en caché");
        }
    }
}
