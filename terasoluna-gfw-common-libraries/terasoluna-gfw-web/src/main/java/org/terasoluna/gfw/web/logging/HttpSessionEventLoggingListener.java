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
package org.terasoluna.gfw.web.logging;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener for session logging.
 * <p>
 * Listener for logging creation and destruction of session, activation and deactivation, session object bind <br>
 * and unbind, session attribute operation.
 * </p>
 */
public class HttpSessionEventLoggingListener implements HttpSessionListener,
                                             HttpSessionAttributeListener,
                                             HttpSessionActivationListener {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(
            HttpSessionEventLoggingListener.class);

    /**
     * Logging when object bind to the session is activated.
     * <p>
     * Session ID and the object that generated the event must be logged.
     * </p>
     * @see javax.servlet.http.HttpSessionActivationListener#sessionWillPassivate(javax.servlet.http.HttpSessionEvent)
     */
    @Override
    public void sessionWillPassivate(HttpSessionEvent se) {
        if (logger.isDebugEnabled()) {
            HttpSession session = se.getSession();
            logger.debug("SESSIONID#{} sessionWillPassivate : {}", session
                    .getId(), se.getSource());
        }
    }

    /**
     * Logging when object bind to the session is deactivated.
     * <p>
     * Session ID and the object that generated the event must be logged.
     * </p>
     * @see javax.servlet.http.HttpSessionActivationListener#sessionDidActivate(javax.servlet.http.HttpSessionEvent)
     */
    @Override
    public void sessionDidActivate(HttpSessionEvent se) {
        if (logger.isDebugEnabled()) {
            HttpSession session = se.getSession();
            logger.debug("SESSIONID#{} sessionDidActivate : {}", session
                    .getId(), se.getSource());
        }
    }

    /**
     * Logging when some attribute gets added to the session.
     * <p>
     * Session ID, name of the object in which attribute is added and value of the object gets logged.
     * </p>
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeAdded(javax.servlet.http.HttpSessionBindingEvent)
     */
    @Override
    public void attributeAdded(HttpSessionBindingEvent se) {
        if (logger.isDebugEnabled()) {
            HttpSession session = se.getSession();
            logger.debug("SESSIONID#{} attributeAdded : {}={}", session.getId(),
                    se.getName(), se.getValue());
        }
    }

    /**
     * Logging when some attribute gets deleted from the session.
     * <p>
     * Session ID, name of the object in which attribute is deleted from and value of the object gets logged.
     * </p>
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeRemoved(javax.servlet.http.HttpSessionBindingEvent)
     */
    @Override
    public void attributeRemoved(HttpSessionBindingEvent se) {
        if (logger.isDebugEnabled()) {
            HttpSession session = se.getSession();
            logger.debug("SESSIONID#{} attributeRemoved : {}={}", session
                    .getId(), se.getName(), se.getValue());
        }
    }

    /**
     * Logging when some attribute added to session gets changed.
     * <p>
     * Session ID, name of the object in which attribute is replaced and value of the object gets logged.
     * </p>
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeReplaced(javax.servlet.http.HttpSessionBindingEvent)
     */
    @Override
    public void attributeReplaced(HttpSessionBindingEvent se) {
        if (logger.isTraceEnabled()) {
            HttpSession session = se.getSession();
            logger.trace("SESSIONID#{} attributeReplaced : {}={}", session
                    .getId(), se.getName(), se.getValue());
        }
    }

    /**
     * Logging when session is created
     * <p>
     * Session ID and implementation class of {@link HttpSession} is logged.
     * </p>
     * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        if (logger.isDebugEnabled()) {
            HttpSession session = se.getSession();
            logger.debug("SESSIONID#{} sessionCreated : {}", session.getId(), se
                    .getSource());
        }
    }

    /**
     * Logging when session is being invalidated.
     * <p>
     * Session ID and implementation class of {@link HttpSession} is logged.
     * </p>
     * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        if (logger.isDebugEnabled()) {
            HttpSession session = se.getSession();
            logger.debug("SESSIONID#{} sessionDestroyed : {}", session.getId(),
                    se.getSource());
        }
    }

}
