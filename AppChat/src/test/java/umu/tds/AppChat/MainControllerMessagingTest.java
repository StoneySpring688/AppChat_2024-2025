package umu.tds.AppChat;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.controllers.MainController;

public class MainControllerMessagingTest {

    private static MainController controller;

    private static final String USER1_NUM = "699999993";
    private static final String USER2_NUM = "699999994";
    private static final String USER1_NAME = "MsgTest1";
    private static final String USER2_NAME = "MsgTest2";
    private static final String PASS = "test123";
    private static final String BIRTH = "01/01/2000";

    @BeforeAll
    public static void setup() {
        controller = MainController.getUnicaInstancia();
        controller.startApp();

        // Registrar ambos usuarios (ignorar fallo si ya existen)
        controller.doRegister(USER1_NAME, USER1_NUM, PASS, BIRTH, "", "Firma 1");
        controller.doRegister(USER2_NAME, USER2_NUM, PASS, BIRTH, "", "Firma 2");

        // Login como USER1
        controller.doLogin(Integer.parseInt(USER1_NUM), PASS);
        if (!Byte.valueOf((byte)1).equals(controller.getEstadoActual())) {
            fail("Login de USER1 fallido, test no puede continuar.");
        }

        controller.anyadirContacto(USER2_NUM, USER2_NAME); // puede fallar si ya está

        controller.doLogout();

        // Login como USER2
        controller.doLogin(Integer.parseInt(USER2_NUM), PASS);
        if (!Byte.valueOf((byte)1).equals(controller.getEstadoActual())) {
            fail("Login de USER2 fallido, test no puede continuar.");
        }

        controller.anyadirContacto(USER1_NUM, USER1_NAME); // puede fallar si ya está
    }

    @Test
    public void testUserToUserMessage() {
        controller.doLogin(Integer.parseInt(USER1_NUM), PASS);
        controller.sendMessage(new ModelMessage(null, USER1_NAME, "ahora", Integer.parseInt(USER1_NUM), Integer.parseInt(USER2_NUM), Optional.of("Hola desde el test"), Optional.empty()));

        List<ModelMessage> mensajes = controller.doSearch(Integer.parseInt(USER2_NUM), null, "Hola desde el test");
        assertFalse(mensajes.isEmpty(), "El mensaje enviado debería poder encontrarse");
    }

    @AfterAll
    public static void cleanup() {
        // Opcional: eliminar contactos, pero no lanzar error si ya no existen
        controller.doLogin(Integer.parseInt(USER1_NUM), PASS);
        controller.removeContact(USER2_NUM);

        controller.doLogin(Integer.parseInt(USER2_NUM), PASS);
        controller.removeContact(USER1_NUM);
    }
}
