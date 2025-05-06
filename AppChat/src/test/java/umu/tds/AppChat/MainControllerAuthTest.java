package umu.tds.AppChat;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import umu.tds.AppChat.controllers.MainController;

public class MainControllerAuthTest {

    private static MainController controller;

    @BeforeAll
    public static void setup() {
        controller = MainController.getUnicaInstancia();
        controller.startApp();
    }

    @Test
    public void testLoginSuccess() {
        controller.doLogin(695201515, "stoney123");
        assertEquals(Byte.valueOf((byte)1), controller.getEstadoActual(), "Login debería ser exitoso");
        controller.doLogout();
    }

    @Test
    public void testLoginFailureWrongPassword() {
        controller.doLogin(695201515, "wrongpassword");
        assertNotEquals(Byte.valueOf((byte)1), controller.getEstadoActual(), "Login no debería ser exitoso");
        controller.doLogout();
    }

    @Test
    public void testLoginNonexistentUser() {
        controller.doLogin(123456789, "irrelevante");
        assertNotEquals(Byte.valueOf((byte)1), controller.getEstadoActual(), "Login no debería ser exitoso");
        controller.doLogout();
    }
}
