package org.visallo.web;

import org.slf4j.MDC;
import org.visallo.core.user.User;
import org.visallo.web.auth.AuthToken;
import org.visallo.web.util.RemoteAddressUtil;

import javax.servlet.http.HttpServletRequest;

public class CurrentUser {
    public static final String CURRENT_USER_REQ_ATTR_NAME = "user.current";
    public static final String AUTH_TOKEN_REQ_ATTR_NAME = "user.auth_token";
    private static final String MDC_USER_ID = "userId";
    private static final String MDC_USER_NAME = "userName";
    private static final String MDC_CLIENT_IP_ADDRESS = "clientIpAddress";

    public static void unset(HttpServletRequest request) {
        request.removeAttribute(CURRENT_USER_REQ_ATTR_NAME);
        clearUserFromLogMappedDiagnosticContexts();
    }

    public static void set(HttpServletRequest request, User user) {
        request.setAttribute(CURRENT_USER_REQ_ATTR_NAME, user);
        setUserInLogMappedDiagnosticContexts(request);
    }

    public static void set(HttpServletRequest request, User user, AuthToken authToken) {
        set(request, user);
        request.setAttribute(AUTH_TOKEN_REQ_ATTR_NAME, authToken);
    }

    public static User get(HttpServletRequest request) {
        return (User) request.getAttribute(CURRENT_USER_REQ_ATTR_NAME);
    }

    public static AuthToken getAuthToken(HttpServletRequest request) {
        return (AuthToken) request.getAttribute(AUTH_TOKEN_REQ_ATTR_NAME);
    }

    public static void clearUserFromLogMappedDiagnosticContexts() {
        MDC.remove(MDC_USER_ID);
        MDC.remove(MDC_USER_NAME);
        MDC.remove(MDC_CLIENT_IP_ADDRESS);
    }

    public static void setUserInLogMappedDiagnosticContexts(HttpServletRequest request) {
        User currentUser = CurrentUser.get(request);

        if (currentUser != null) {
            String userId = currentUser.getUserId();
            if (userId != null) {
                MDC.put(MDC_USER_ID, userId);
            }
            String userName = currentUser.getUsername();
            if (userName != null) {
                MDC.put(MDC_USER_NAME, userName);
            }
        }

        MDC.put(MDC_CLIENT_IP_ADDRESS, RemoteAddressUtil.getClientIpAddr(request));
    }
}
