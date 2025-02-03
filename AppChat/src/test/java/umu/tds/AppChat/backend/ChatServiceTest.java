package umu.tds.AppChat.backend;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import umu.tds.AppChat.backend.services.ChatService;
import umu.tds.AppChat.backend.utils.ModelMessage;

class ChatServiceTest {
    private ChatService service;
    private long idGrupo;
    private long idContacto;

    @BeforeEach
    void setUp() {
        service = new ChatService(10);
        idGrupo = 1234567890L;  // Grupo con ID de 10 dígitos
        idContacto = 123456789L;  // Contacto con ID de 9 dígitos
    }

    @Test
    void testAddAndRetrieveMessages() {
        ModelMessage msgContacto = new ModelMessage(null, null, null, Optional.of("Mensaje de contacto"), Optional.empty());
        ModelMessage msgGrupo = new ModelMessage(null, null, null, Optional.of("Mensaje de grupo"), Optional.empty());

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
}
