package umu.tds.AppChat.devtools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    private LoggerUtil() {
        // Clase de utilidad, no instanciable
    }

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
