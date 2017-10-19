/*
 * Copyright (C) 2013-2017 NTT DATA Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.terasoluna.gfw.common.exception;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * Class that outputs the exception information to the log.
 * <p>
 * Creates a log message using specified exception and uses {@link org.slf4j.Logger} to output the log.<br>
 * Actual destination of log output varies depending on the log definition of log output library.
 * </p>
 * <p>
 * <strong>[Default Log format]</strong><br>
 * 
 * <pre>
 *   Application Log :
 *       [{exceptionCode}] {exceptionMessage}
 *       {StackTrace}
 *    
 *   Monitoring Log :
 *       [{exceptionCode}] {exceptionMessage}
 * </pre>
 * 
 * <strong>[Output example]</strong>
 * 
 * <pre>
 * throw new SystemException(&quot;e.xx.xx.0001&quot;, &quot;system error.&quot;, e);
 * </pre>
 * 
 * <strong>Application Log :</strong>
 * 
 * <pre>
 *       [e.xx.xx.0001] system error.
 *       org.terasoluna.gfw.common.exception.SystemException: system error.
 *           at org.xxxx ...
 *           at org.xxxx ...
 *           at org.xxxx ...
 *           at org.xxxx ...
 *       Caused by: java.io.IOException: ..
 *           at org.xxxx ...
 *           at org.xxxx ...
 * </pre>
 * 
 * <strong>Monitoring Log :</strong>
 * 
 * <pre>
 *   Monitoring Log :
 *       [e.xx.xx.0001] system error.
 * </pre>
 */
public class ExceptionLogger implements InitializingBean {

    /**
     * Logger suffix of monitoring log.
     */
    private static final String MONITORING_LOG_LOGGER_SUFFIX = ".Monitoring";

    /**
     * Logger for application log output.
     */
    private final Logger applicationLogger;

    /**
     * Logger for monitoring log output.
     */
    private final Logger monitoringLogger;

    /**
     * Logger for each log level.
     */
    private final Map<ExceptionLevel, LogLevelWrappingLogger> exceptionLevelLoggers = new ConcurrentHashMap<ExceptionLevel, LogLevelWrappingLogger>();

    /**
     * Logger that outputs log at INFO log level.
     */
    private final InfoLogger infoLogger;

    /**
     * Logger that outputs log at WARN log level.
     */
    private final WarnLogger warnLogger;

    /**
     * Logger that outputs log at ERROR log level.
     */
    private final ErrorLogger errorLogger;

    /**
     * Object that resolves exception code.
     */
    private ExceptionCodeResolver exceptionCodeResolver = new SimpleMappingExceptionCodeResolver();

    /**
     * Object that resolves exception level.
     */
    private ExceptionLevelResolver exceptionLevelResolver;

    /**
     * placeholder for exception code of log formatter.
     */
    private String PLACEHOLDER_OF_EXCEPTION_CODE = "{0}";

    /**
     * placeholder for exception message of log formatter.
     */
    private String PLACEHOLDER_OF_EXCEPTION_MESSAGE = "{1}";

    /**
     * Message formatter for log output.
     */
    private String logMessageFormat = String.format("[%s] %s",
            PLACEHOLDER_OF_EXCEPTION_CODE, PLACEHOLDER_OF_EXCEPTION_MESSAGE);

    /**
     * Default exception code in case it is not specified.
     */
    private String defaultCode = "UNDEFINED-CODE";

    /**
     * Default exception message in case it is not specified.
     */
    private String defaultMessage = "UNDEFINED-MESSAGE";

    /**
     * Log message trim flag.
     */
    private boolean trimLogMessage = true;

    /**
     * Default constructor.
     * <p>
     * {@link #ExceptionLogger(String)} is called with FQCN of this class as parameter.
     * </p>
     */
    public ExceptionLogger() {
        this(ExceptionLogger.class.getName());
    }

    /**
     * Constructor
     * <p>
     * Based on the name specified in the parameters, logger for application log and <br>
     * logger for monitoring log is fetched.
     * </p>
     * <p>
     * Logger Name<br>
     * <ul>
     * <li>logger for output of application log: {name}</li>
     * <li>logger for output of monitoring log: {name} + ".Monitoring"</li>
     * </ul>
     * @param name name of logger
     */
    public ExceptionLogger(String name) {
        this.applicationLogger = LoggerFactory.getLogger(name);
        this.monitoringLogger = LoggerFactory.getLogger(name
                + MONITORING_LOG_LOGGER_SUFFIX);
        this.infoLogger = new InfoLogger();
        this.warnLogger = new WarnLogger();
        this.errorLogger = new ErrorLogger();
    }

    /**
     * Set the resolver object for exception code.
     * <p>
     * If the exception code resolution object is not set, exception code is not output to the log.
     * </p>
     * @param exceptionCodeResolver exception code resolution object
     */
    public void setExceptionCodeResolver(
            ExceptionCodeResolver exceptionCodeResolver) {
        this.exceptionCodeResolver = exceptionCodeResolver;
    }

    /**
     * set the resolution object for exception level.
     * <p>
     * If the exception level resolution object is not set, exception level is not output to the log.
     * </p>
     * @param exceptionLevelResolver exception level resolution object.
     */
    public void setExceptionLevelResolver(
            ExceptionLevelResolver exceptionLevelResolver) {
        this.exceptionLevelResolver = exceptionLevelResolver;
    }

    /**
     * set the log format.
     * <p>
     * It is possible to specify the output position of the exception message and exception code in log format. <br>
     * The position can specified using "{0}" for exception code and "{1}" for exception message.<br>
     * "{0}" and "{1}" is must be specified. if changed validation rule of logMessageFormat, please override
     * {@link #validateLogMessageFormat(String)} method.
     * </p>
     * @param logMessageFormat log format.
     */
    public void setLogMessageFormat(String logMessageFormat) {
        this.logMessageFormat = logMessageFormat;
    }

    /**
     * Set the trim flag of log message.
     * <p>
     * Default is <code>true</code>
     * </p>
     * @param trimLogMessage set <code>true</code> for trimming
     */
    public void setTrimLogMessage(boolean trimLogMessage) {
        this.trimLogMessage = trimLogMessage;
    }

    /**
     * Set default exception code.
     * @param defaultCode default exception code.
     */
    public void setDefaultCode(String defaultCode) {
        this.defaultCode = defaultCode;
    }

    /**
     * Set default exception message.
     * @param defaultMessage default exception message.
     */
    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    /**
     * Initializes the exception logger.
     * <p>
     * If exception resolution object is not set, use {@link org.terasoluna.gfw.common.exception.DefaultExceptionLevelResolver}.
     * </p>
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() {

        validateLogMessageFormat(logMessageFormat);

        if (exceptionLevelResolver == null) {
            exceptionLevelResolver = new DefaultExceptionLevelResolver(exceptionCodeResolver);
        }
        registerExceptionLevelLoggers(ExceptionLevel.INFO, infoLogger);
        registerExceptionLevelLoggers(ExceptionLevel.WARN, warnLogger);
        registerExceptionLevelLoggers(ExceptionLevel.ERROR, errorLogger);
    }

    /**
     * Output the log related to exception level.
     * @param ex Exception
     */
    public void log(Exception ex) {
        ExceptionLevel level = exceptionLevelResolver.resolveExceptionLevel(ex);
        LogLevelWrappingLogger logger = null;
        if (level != null) {
            logger = exceptionLevelLoggers.get(level);
        }
        if (logger == null) {
            logger = errorLogger;
        }
        log(ex, logger);
    }

    /**
     * Output the information log.
     * <p>
     * Outputs INFO level log to application log and monitoring log.
     * </p>
     * @param ex Exception
     */
    public void info(Exception ex) {
        log(ex, infoLogger);
    }

    /**
     * Output WARN level log
     * <p>
     * Outputs WARN level log to application log and monitoring log.
     * </p>
     * @param ex Exception
     */
    public void warn(Exception ex) {
        log(ex, warnLogger);
    }

    /**
     * Ouputs ERROR log.
     * <p>
     * Outputs ERROR level log to application log and monitoring log.
     * </p>
     * @param ex Exception
     */
    public void error(Exception ex) {
        log(ex, errorLogger);
    }

    /**
     * validate a logMessageFormat.
     * <p>
     * logMessageFormat must have placeholder("{0}" and "{1}"). "{0}" is replaced with exception code. "{1}" is replaced with
     * exception message.
     * </p>
     * @param logMessageFormat Message formatter for log output.
     */
    protected void validateLogMessageFormat(String logMessageFormat) {
        if (logMessageFormat == null || !logMessageFormat.contains(
                PLACEHOLDER_OF_EXCEPTION_CODE) || !logMessageFormat.contains(
                        PLACEHOLDER_OF_EXCEPTION_MESSAGE)) {
            String message = "logMessageFormat must have placeholder({0} and {1})."
                    + " {0} is replaced with exception code."
                    + " {1} is replaced with exception message. current logMessageFormat is \""
                    + logMessageFormat + "\".";
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Resolved exception code.
     * <p>
     * Fetches exception code from the occured exception.
     * </p>
     * @param ex Exception
     * @return Exception code
     */
    protected String resolveExceptionCode(Exception ex) {
        String exceptionCode = null;
        if (exceptionCodeResolver != null) {
            exceptionCode = exceptionCodeResolver.resolveExceptionCode(ex);
        }
        return exceptionCode;
    }

    /**
     * Creats log message.
     * @param ex Exception
     * @return log message
     */
    protected String makeLogMessage(Exception ex) {
        String exceptionCode = resolveExceptionCode(ex);
        return formatLogMessage(exceptionCode, ex.getMessage());
    }

    /**
     * Formats the message for log output.
     * <p>
     * Formats exception code and exception message and creates message for log output. <br>
     * </p>
     * <p>
     * [Default format of message for log output]<br>
     * <ul>
     * <li>If exception code is resolved: "[${exceptionCode}] ${exceptionMessage}" format</li>
     * <li>If exception code is not resolved: "${exceptionMessage}" format</li>
     * </ul>
     * @param exceptionCode exception code
     * @param exceptionMessage exception message
     * @return message ready for log output
     */
    protected String formatLogMessage(String exceptionCode,
            String exceptionMessage) {

        String bindingExceptionCode = exceptionCode;
        String bindingExceptionMessage = exceptionMessage;
        if (StringUtils.isEmpty(bindingExceptionCode)) {
            bindingExceptionCode = defaultCode;
        }
        if (StringUtils.isEmpty(bindingExceptionMessage)) {
            bindingExceptionMessage = defaultMessage;
        }

        String message = MessageFormat.format(logMessageFormat,
                bindingExceptionCode, bindingExceptionMessage);
        if (trimLogMessage) {
            message = message.trim();
        }
        return message;
    }

    /**
     * Registers the logger corresponding to the level of specified exception.
     * <p>
     * If a logger of same level has already been registered, it will be overwritten.
     * </p>
     * @param level exception level
     * @param logger delegating logger
     */
    protected void registerExceptionLevelLoggers(ExceptionLevel level,
            LogLevelWrappingLogger logger) {
        this.exceptionLevelLoggers.put(level, logger);
    }

    /**
     * Returns logger for output of application log.
     * @return logger for output of application log
     */
    protected Logger getApplicationLogger() {
        return applicationLogger;
    }

    /**
     * Returns logger for output of monitoring log.
     * @return logger for output of monitoring log.
     */
    protected Logger getMonitoringLogger() {
        return monitoringLogger;
    }

    /**
     * Outputs the log using specified logger.
     * @param ex Exception
     * @param logger delegating logger
     */
    private void log(Exception ex, LogLevelWrappingLogger logger) {
        if (!logger.isEnabled()) {
            return;
        }
        String logMessage = makeLogMessage(ex);
        logger.log(logMessage, ex);
    }

    /**
     * Logger instance that wraps the log level.
     */
    protected interface LogLevelWrappingLogger {

        /**
         * Determines if the logger is valid.
         * @return Returns <code>true</code> if valid.
         */
        boolean isEnabled();

        /**
         * Outputs the log.
         * @param logMessage log message
         * @param ex Exception
         */
        void log(String logMessage, Exception ex);

    }

    /**
     * Wrapper logger for output of INFO level of log.
     */
    private final class InfoLogger implements LogLevelWrappingLogger {

        /**
         * Checks whether Info logging is enabled in either monitoring log or application log
         * @see org.terasoluna.gfw.common.exception.ExceptionLogger.LogLevelWrappingLogger#isEnabled()
         */
        @Override
        public boolean isEnabled() {
            return monitoringLogger.isInfoEnabled() || applicationLogger
                    .isInfoEnabled();
        }

        /**
         * Logs messages of Info level.
         * <p>
         * Logs messages of Info level in Monitoring log and Application log if Info logging in these loggers are enabled.
         * </p>
         * @see org.terasoluna.gfw.common.exception.ExceptionLogger.LogLevelWrappingLogger#log(java.lang.String,
         *      java.lang.Exception)
         */
        @Override
        public void log(String logMessage, Exception ex) {
            if (monitoringLogger.isInfoEnabled()) {
                monitoringLogger.info(logMessage);
            }
            if (applicationLogger.isInfoEnabled()) {
                applicationLogger.info(logMessage, ex);
            }
        }
    }

    /**
     * Wrapper logger for output of WARN level of log.
     */
    private final class WarnLogger implements LogLevelWrappingLogger {

        /**
         * Checks whether Warn logging is enabled in either monitoring log or application log
         * @see org.terasoluna.gfw.common.exception.ExceptionLogger.LogLevelWrappingLogger#isEnabled()
         */
        @Override
        public boolean isEnabled() {
            return monitoringLogger.isWarnEnabled() || applicationLogger
                    .isWarnEnabled();
        }

        /**
         * Logs messages of Warn level.
         * <p>
         * Logs messages of Warn level in Monitoring log and Application log if Warn logging in these loggers are enabled.
         * </p>
         * @see org.terasoluna.gfw.common.exception.ExceptionLogger.LogLevelWrappingLogger#log(java.lang.String,
         *      java.lang.Exception)
         */
        @Override
        public void log(String logMessage, Exception ex) {
            if (monitoringLogger.isWarnEnabled()) {
                monitoringLogger.warn(logMessage);
            }
            if (applicationLogger.isWarnEnabled()) {
                applicationLogger.warn(logMessage, ex);
            }
        }
    }

    /**
     * Wrapper logger for output of ERROR level of log.
     */
    private final class ErrorLogger implements LogLevelWrappingLogger {

        /**
         * Checks whether Error logging is enabled in either monitoring log or application log
         * @see org.terasoluna.gfw.common.exception.ExceptionLogger.LogLevelWrappingLogger#isEnabled()
         */
        @Override
        public boolean isEnabled() {
            return monitoringLogger.isErrorEnabled() || applicationLogger
                    .isErrorEnabled();
        }

        /**
         * Logs messages of Error level.
         * <p>
         * Logs messages of Error level in Monitoring log and Application log if error logging in these loggers are enabled.
         * </p>
         * @see org.terasoluna.gfw.common.exception.ExceptionLogger.LogLevelWrappingLogger#log(java.lang.String,
         *      java.lang.Exception)
         */
        @Override
        public void log(String logMessage, Exception ex) {
            if (monitoringLogger.isErrorEnabled()) {
                monitoringLogger.error(logMessage);
            }
            if (applicationLogger.isErrorEnabled()) {
                applicationLogger.error(logMessage, ex);
            }
        }
    }

}
