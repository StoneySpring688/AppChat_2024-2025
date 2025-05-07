package umu.tds.AppChat;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Arrays;

import umu.tds.AppChat.controllers.MainController;
import umu.tds.AppChat.backend.utils.Grupo;

public class MainControllerGroupTest {

    private static MainController controller;
    private final static String defaultProfileImage = "/assets/ProfilePic.png";
    private Set<Long> gruposAntes;

    @BeforeAll
    public static void setup() {
        controller = MainController.getUnicaInstancia();
        controller.startApp();
        controller.doLogin(695201515, "stoney123");
    }

    @BeforeEach
    public void registrarGruposIniciales() {
        gruposAntes = controller.getGrupos()
                                .stream()
                                .map(Grupo::getID)
                                .collect(Collectors.toSet());
    }

    @AfterEach
    public void eliminarGruposNuevos() {
        List<Grupo> gruposActuales = controller.getGrupos();
        for (Grupo g : gruposActuales) {
            if (!gruposAntes.contains(g.getID())) {
                controller.removeGroup(g);
            }
        }
    }

    @Test
    public void testCreateGroupWithMembers() {
        List<Integer> members = Arrays.asList(635951958, 624840847);
        boolean result = controller.makeGroup("TestGroup", defaultProfileImage, members);
        assertTrue(result, "El grupo debería crearse correctamente");
    }

    @Test
    public void testCreateGroupWithoutName() {
        List<Integer> members = Arrays.asList(635951958);
        boolean result = controller.makeGroup("", defaultProfileImage, members);
        assertFalse(result, "No se debería permitir crear un grupo sin nombre");
    }

    @Test
    public void testCreateGroupWithoutMembers() {
        boolean result = controller.makeGroup("GroupWithoutMembers", defaultProfileImage, List.of());
        assertFalse(result, "No se debería permitir crear un grupo sin miembros");
    }
}
