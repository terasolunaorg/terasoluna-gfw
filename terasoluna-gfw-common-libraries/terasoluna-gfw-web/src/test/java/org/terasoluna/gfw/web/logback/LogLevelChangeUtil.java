package org.terasoluna.gfw.web.logback;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

public class LogLevelChangeUtil {

    private static final String LOG_LEVEL_SYS_PROP_NAME = "ut.log.level";

    private static final String LOGBACK_FILE_PATH = "src/test/resources/logback.xml";

    /**
     * set log level system property, configure log settings
     */
    public static void setLogLevel(LogLevel logLevel) throws Exception {
        System.setProperty(LOG_LEVEL_SYS_PROP_NAME, logLevel.getName());
        configureLogSettings();
    }

    /**
     * clear log level system property, configure log settings
     */
    public static void clearProperty() throws Exception {
        System.clearProperty(LOG_LEVEL_SYS_PROP_NAME);
        configureLogSettings();
    }

    /**
     * configure log settings
     */
    private static void configureLogSettings() throws Exception {
        LoggerContext context = (LoggerContext) LoggerFactory
                .getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);
        context.reset();
        configurator.doConfigure(LOGBACK_FILE_PATH);
    }

    /**
     * log level
     */
    public enum LogLevel {

        TRACE("trace"), DEBUG("debug"), INFO("info"), WARN("warn"), ERROR(
                "error");

        private String name;

        LogLevel(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

}