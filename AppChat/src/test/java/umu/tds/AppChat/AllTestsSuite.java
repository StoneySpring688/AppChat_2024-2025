package umu.tds.AppChat;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import umu.tds.AppChat.backend.ChatServiceTest;
import umu.tds.AppChat.backend.LRUCacheTest;
import umu.tds.AppChat.devtools.LoggerTest;

@Suite
@IncludeEngines("junit-jupiter")
@SelectClasses({
    MainControllerAuthTest.class,
    MainControllerGroupTest.class,
    MainControllerMessagingTest.class,
    MainControllerSearchTest.class,
    ChatServiceTest.class,
    LRUCacheTest.class,
    LoggerTest.class
})
public class AllTestsSuite {
}
