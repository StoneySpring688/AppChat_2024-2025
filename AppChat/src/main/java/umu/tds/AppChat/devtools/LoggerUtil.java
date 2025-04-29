package umu.tds.AppChat.devtools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

public class LoggerUtil {

    private LoggerUtil() {
        // Clase de utilidad, no instanciable
    }

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static void setLogLevel(Level level) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("umu.tds.AppChat").setLevel(level);
    }

    public static void setLogLevel(String levelStr) {
        Level level;
        switch (levelStr.trim().toUpperCase()) {
            case "DEBUG":
                level = Level.DEBUG;
                break;
            case "INFO":
                level = Level.INFO;
                break;
            case "WARN":
                level = Level.WARN;
                break;
            case "ERROR":
                level = Level.ERROR;
                break;
            case "OFF":
                level = Level.OFF;
                break;
            default:
                System.err.println("Nivel desconocido '" + levelStr + "'. Usando OFF por defecto.");
                level = Level.OFF;
        }
        setLogLevel(level);
    }
}
