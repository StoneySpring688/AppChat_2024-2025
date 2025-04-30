package umu.tds.AppChat.devtools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

/**
 * Clase de utilidad para obtener y configurar loggers en la aplicación.
 * 
 * <p>Permite:
 * <ul>
 *   <li>Obtener un logger específico por clase.</li>
 *   <li>Cambiar dinámicamente el nivel de logging en tiempo de ejecución.</li>
 * </ul>
 * 
 * <p>Niveles permitidos: {@code TRACE}, {@code DEBUG}, {@code INFO}, {@code WARN}, {@code ERROR}, {@code OFF}.
 * 
 * <p>Ejemplo de uso:
 * <pre>{@code
 *   Logger logger = LoggerUtil.getLogger(MiClase.class);
 *   logger.info("Mensaje de log");
 *   LoggerUtil.setLogLevel("DEBUG");
 * }</pre>
 * 
 * @author StoneySpring
 */
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
            case "TRACE":
				level = Level.TRACE;
				break;
            default:
                System.err.println("Nivel desconocido '" + levelStr + "'. Usando OFF por defecto.");
                level = Level.OFF;
        }
        setLogLevel(level);
    }
}
