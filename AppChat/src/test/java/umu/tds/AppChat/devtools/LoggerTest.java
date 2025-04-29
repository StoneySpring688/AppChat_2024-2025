package umu.tds.AppChat.devtools;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

public class LoggerTest {

    private static final Logger logger = LoggerUtil.getLogger(LoggerTest.class);

    @Test
    public void testLoggerOutputs() {
        logger.info("Este es un mensaje de INFO");
        logger.warn("Este es un mensaje de WARNING");
        logger.error("Este es un mensaje de ERROR");
        logger.debug("Este es un mensaje de DEBUG");
    }
}
