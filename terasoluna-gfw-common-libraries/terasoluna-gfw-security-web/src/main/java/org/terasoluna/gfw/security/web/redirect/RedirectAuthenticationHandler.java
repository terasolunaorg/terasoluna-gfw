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
package org.terasoluna.gfw.security.web.redirect;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

/**
 * Default transition destination after a successful login is specified by {@code default-target-url} attribute. This class
 * changes the default transition destination to a path received as a request parameter. <br>
 * <p>
 * This functionality is to be used when there is a need to transition to a different location rather than the one specified in
 * {@code default-target-url}<br>
 * To use this functionality, transition destination path must be added in the request parameter of the screen from which
 * transition to login screen is done, as well as the login screen.<br>
 * Using the transition destination path received from the login screen, redirect is executed.
 * <p>
 * Attribute name of request parameter is set to {@code redirectTo} by default. However, it can be changed by explicitly it in
 * the {@code targetUrlParameter}.<br>
 * <p>
 * Since, this functionality does a {@code javax.servlet.http.HttpServletResponse#sendRedirect(String)} after successful login.
 * {@code sendRedirect()} creates a new {@code HttServletRequest} of type {@code GET} is created. Hence, transition destination
 * path should correspond to a {@code GET} request handler. <br>
 * .
 * <p>
 * This functionality can be used in a situation where search screen -&gt; detail screen is possible without login. but login is
 * required for any update process on the detail screen. In this case, it should transition back to the detail screen after
 * login <br>
 * Usage Example is as below:
 * </p>
 * <h3>Example of Spring Security Configuration</h3>
 * 
 * <pre>
 * &lt;http auto-config="true"&gt;
 *                 ....
 *     &lt;form-login login-page="/login" default-target-url="/"
 *         always-use-default-target="false"
 *         <strong style=color:red>authentication-failure-handler-ref="redirectErrorHandler"
 *         authentication-success-handler-ref="redirectHandler"</strong> /&gt;
 *                 ....
 * &lt;/http&gt;
 * 
 * &lt;beans:bean id="redirectHandler"
 *     class="org.terasoluna.gfw.security.web.redirect.RedirectAuthenticationHandler"/&gt;
 * 
 * &lt;beans:bean id="redirectErrorHandler"
 *     class="org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler"&gt;
 *     &lt;beans:property name="defaultFailureUrl" value="/login?error=true"/&gt;
 *     <strong style=color:red>&lt;beans:property name="useForward" value="true"/&gt;</strong>
 * &lt;/beans:bean&gt;
 * </pre>
 * 
 * Specify {@code org.terasoluna.gfw.web.security.RedirectAuthenticationHandler} as the handler class at the time of successful
 * authentication in {@code authentication-success-handler-ref}<br>
 * Specify {@code org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler} as the handler class at
 * the time of failed authentication in {@code authentication-failure-handler-ref}<br>
 * At the time of failed authentication, redirect is done by default. In case of a redirect, a new {@code HttpServletRequest} is
 * created and the existing one is lost. Hence in order to change this behavior from redirect to forward for preserving the
 * existing request, a handler is needed to be specified for failed authentication also.<br>
 * In the bean definition for {@code org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler},
 * {@code true} needs to be specified for {@code useForward} attribute. By doing this, redirect gets changed to forward<br>
 * Further, path of login screen must be specified in {@code defaultFailureUrl}<br>
 * <h3>Example of Transition Source Screen</h3>
 * 
 * <pre>
 * &lt;form:form action="${pageContext.request.contextPath}/login" method="get" cssClass="inline" &gt;
 *     &lt;input type="submit" value="login"&gt;
 *     <strong style=color:red>&lt;input type="hidden" name="redirectTo"
 *     value="${pageContext.request.contextPath}/user/read?userCode=${f:h(param.userCode)}" /&gt;</strong>
 * &lt;/form:form&gt;
 * </pre>
 * 
 * In order to go to login page, specify the path of login screen as a hidden input field. <h3>Example of Login Screen</h3>
 * 
 * <pre>
 * &lt;input type="submit" value="login" /&gt;
 * &lt;input type="hidden" name="redirectTo" value="${f:h(param.redirectTo)}" /&gt;
 * </pre>
 * 
 * The value received from transition source screen is again set to {@code hidden} field<br>
 * In this example, redirect is done to following path {@code /user/read?userCode=$ f:h(param.userCode)}
 * @deprecated This feature is expected to replace the alternative functions that will be provided by Spring Security. Until the
 *             function is provided , Use
 *             {@link org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler} .
 */
@Deprecated
public class RedirectAuthenticationHandler extends
                                           SavedRequestAwareAuthenticationSuccessHandler
                                           implements InitializingBean {
    /**
     * RedirectStrategy to use if redirect path is specified as a request parameter. (default:"redirectTo")
     */
    private RedirectStrategy targetUrlParameterRedirectStrategy;

    /**
     * Constructor. <br>
     */
    public RedirectAuthenticationHandler() {
        super.setTargetUrlParameter("redirectTo");
    }

    /**
     * set RedirectStrategy to use if redirect path is specified as a request parameter. (default:"redirectTo")<br>
     * @param redirectToRedirectStrategy RedirectStrategy to use if redirect path is specified as a request parameter
     */
    public void setRedirectToRedirectStrategy(
            RedirectStrategy redirectToRedirectStrategy) {
        this.targetUrlParameterRedirectStrategy = redirectToRedirectStrategy;
    }

    /**
     * Performs redirect<br>
     * <p>
     * If there is no redirect path specified as a request parameter, redirects to default path
     * @param request current HTTP request
     * @param response current HTTP response
     * @param authentication Authentication information of spring security
     * @see org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler#onAuthenticationSuccess
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String targetUrlParameter = getTargetUrlParameter();

        if (targetUrlParameter != null
                && targetUrlParameterRedirectStrategy != null) {
            String redirectUrl = request.getParameter(targetUrlParameter);
            if (StringUtils.hasText(redirectUrl)) {
                clearAuthenticationAttributes(request);
                targetUrlParameterRedirectStrategy.sendRedirect(request,
                        response, redirectUrl);
                return;
            }
        }

        super.onAuthenticationSuccess(request, response, authentication);

    }

    /**
     * If {@link RedirectStrategy} is not externally set, {@link DefaultRedirectStrategy} is used.
     * <p>
     * {@code contextRelative} property of DefaultRedirectStrategy is set to true.<br>
     * In order to set it to {@code false}, {@code targetUrlParameterRedirectStrategy} property <br>
     * must be set in the bean definition <br>
     * </p>
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() {
        if (targetUrlParameterRedirectStrategy == null) {
            // set contextRelative=true as default RedirectStrategy
            targetUrlParameterRedirectStrategy = new DefaultRedirectStrategy();
            ((DefaultRedirectStrategy) targetUrlParameterRedirectStrategy)
                    .setContextRelative(true);
        }
    }
}
