package umu.tds.AppChat;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.controllers.MainController;

public class MainControllerSearchTest {

    private static MainController controller;

    @BeforeAll
    static void setup() {
        controller = MainController.getUnicaInstancia();

        controller.startApp(); // en lugar de acceder directamente a dao/backend

        controller.doLogin(695201515, "stoney123");

        assertEquals(Byte.valueOf((byte)1), controller.getEstadoActual(), "Login fallido");
    }

    @Test
    @DisplayName("Buscar solo por número")
    void buscarSoloNumero() {
        List<ModelMessage> mensajes = controller.doSearch(635951958, null, null);
        assertFalse(mensajes.isEmpty(), "No se encontraron mensajes al buscar solo por número");
    }

    @Test
    @DisplayName("Buscar solo por contacto")
    void buscarSoloContacto() {
        List<ModelMessage> mensajes = controller.doSearch(0, "Pikachu", null);
        assertFalse(mensajes.isEmpty(), "No se encontraron mensajes al buscar solo por contacto");
    }

    @Test
    @DisplayName("Buscar solo por mensaje")
    void buscarSoloMensaje() {
        List<ModelMessage> mensajes = controller.doSearch(0, null, "emojis");
        assertFalse(mensajes.isEmpty(), "No se encontraron mensajes al buscar solo por mensaje");
    }

    @Test
    @DisplayName("Buscar por número y contacto")
    void buscarNumeroYContacto() {
        List<ModelMessage> mensajes = controller.doSearch(635951958, "Guilliman", null);
        assertFalse(mensajes.isEmpty(), "No se encontraron mensajes al buscar por número y contacto");
    }

    @Test
    @DisplayName("Buscar por número y mensaje")
    void buscarNumeroYMensaje() {
        List<ModelMessage> mensajes = controller.doSearch(635951958, null, "No");
        assertFalse(mensajes.isEmpty(), "No se encontraron mensajes al buscar por número y mensaje");
    }

    @Test
    @DisplayName("Buscar por contacto y mensaje")
    void buscarContactoYMensaje() {
        List<ModelMessage> mensajes = controller.doSearch(0, "Pikachu", "emojis");
        assertFalse(mensajes.isEmpty(), "No se encontraron mensajes al buscar por contacto y mensaje");
    }

    @Test
    @DisplayName("Buscar por número, contacto y mensaje")
    void buscarTodo() {
        List<ModelMessage> mensajes = controller.doSearch(635951958, "Guilliman", "eso");
        assertFalse(mensajes.isEmpty(), "No se encontraron mensajes al usar todos los filtros");
    }

    @Test
    @DisplayName("Buscar sin filtros (espera vacío)")
    void buscarSinFiltros() {
        List<ModelMessage> mensajes = controller.doSearch(0, null, null);
        assertTrue(mensajes.isEmpty(), "Se encontraron mensajes en una búsqueda vacía");
    }

    @Test
    @DisplayName("Buscar con contacto inexistente")
    void buscarContactoInexistente() {
        List<ModelMessage> mensajes = controller.doSearch(0, "NoExiste", null);
        assertTrue(mensajes.isEmpty(), "Se encontraron mensajes con un contacto inexistente");
    }
    
    @Test
    @DisplayName("Buscar mensaje en grupo por número del remitente")
    void buscarEnGrupoPorNumero() {
        List<ModelMessage> mensajes = controller.doSearch(695201515, null, "conversaciones grupales");
        assertFalse(mensajes.isEmpty(), "No se encontró el mensaje en grupo enviado por Stoney");
    }

}
