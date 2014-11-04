package org.terasoluna.gfw.web.mvc.support;

import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

class ProcessActionInvocationHelper {
    private final Method processActionMethod;
    private final boolean isOldProcessAction;

    public ProcessActionInvocationHelper() {
        // Check Spring4's signature
        Method targetProcessActionMethod = ReflectionUtils.findMethod(RequestDataValueProcessor.class, "processAction",
                HttpServletRequest.class, String.class, String.class);
        boolean isOld = false;

        if (targetProcessActionMethod == null) {
            // Check Spring3's signature
            targetProcessActionMethod = ReflectionUtils.findMethod(RequestDataValueProcessor.class, "processAction",
                    HttpServletRequest.class, String.class);
            isOld = true;
        }
        if (targetProcessActionMethod == null) {
            throw new IllegalStateException("'processActionMethod' is not found. Should never get here!");
        }
        this.processActionMethod = targetProcessActionMethod;
        this.isOldProcessAction = isOld;
    }

    public String invokeProcessAction(RequestDataValueProcessor requestDataValueProcessor, HttpServletRequest request, String action, String method) {
        if (isOldProcessAction) {
            return (String) ReflectionUtils.invokeMethod(this.processActionMethod, requestDataValueProcessor, request, action);
        } else {
            return (String) ReflectionUtils.invokeMethod(this.processActionMethod, requestDataValueProcessor, request, action, method);
        }
    }
}
