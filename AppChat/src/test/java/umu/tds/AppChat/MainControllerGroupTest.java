package umu.tds.AppChat;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Arrays;

import umu.tds.AppChat.controllers.MainController;

public class MainControllerGroupTest {

    private static MainController controller;

    @BeforeAll
    public static void setup() {
        controller = MainController.getUnicaInstancia();
        controller.startApp();
        controller.doLogin(695201515, "stoney123");
    }

    @Test
    public void testCreateGroupWithMembers() {
        List<Integer> members = Arrays.asList(635951958, 624840847);
        boolean result = controller.makeGroup("TestGroup", "http://default.url/icon.png", members);
        assertTrue(result, "El grupo debería crearse correctamente");
    }

    @Test
    public void testCreateGroupWithoutName() {
        List<Integer> members = Arrays.asList(635951958);
        boolean result = controller.makeGroup("", "http://default.url/icon.png", members);
        assertFalse(result, "No se debería permitir crear un grupo sin nombre");
    }

    @Test
    public void testCreateGroupWithoutMembers() {
        boolean result = controller.makeGroup("GroupWithoutMembers", "http://default.url/icon.png", List.of());
        assertFalse(result, "No se debería permitir crear un grupo sin miembros");
    }
}
