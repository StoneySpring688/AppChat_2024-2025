package umu.tds.AppChat;

import java.awt.EventQueue;

import umu.tds.AppChat.controllers.MainController;
import umu.tds.AppChat.devtools.LoggerUtil;

/**
 * @author StoneySpring688
 */
public class AppChat {

    public static void main(String[] args) {

        // Nivel por defecto: OFF (no mostrar logs si no se pasa argumento)
        if (args.length > 0) {
            LoggerUtil.setLogLevel(args[0]);
        } else {
            LoggerUtil.setLogLevel("OFF");
        }

        EventQueue.invokeLater(() -> {
            try {
                MainController.getUnicaInstancia().startApp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
