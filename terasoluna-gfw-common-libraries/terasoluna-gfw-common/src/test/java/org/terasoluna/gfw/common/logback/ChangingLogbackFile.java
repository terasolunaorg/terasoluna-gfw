package org.terasoluna.gfw.common.logback;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.Context;

public abstract class ChangingLogbackFile {

    private final String LOGBACK_UNIT_TEST_FILE_PATH = "src/test/resources/logback-unittest.xml";

    private final String LOGBACK_DEFAULT_FILE_PATH = "src/test/resources/logback.xml";

    private String logbackUnitTestFilePath = LOGBACK_UNIT_TEST_FILE_PATH;

    private String logbackDefaultFilePath = LOGBACK_DEFAULT_FILE_PATH;

    protected Logger logger;

    private Context context;

    private JoranConfigurator configurator;

    public void setLogger(Class<?> clazz) {

        logger = (Logger) LoggerFactory.getLogger(clazz);
    }

    public void before() throws Exception {
        // set up
        context = logger.getLoggerContext();
        configurator = new JoranConfigurator();

        configurator.setContext(context);

        // Changing to logback-high-loglevel.xml
        ((LoggerContext) context).reset();
        configurator.doConfigure(logbackUnitTestFilePath);
    }

    public void after() throws Exception {
        // Changing to logback.xml
        ((LoggerContext) context).reset();
        configurator.doConfigure(logbackDefaultFilePath);
    }

}
