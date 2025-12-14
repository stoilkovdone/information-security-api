package mk.ukim.finki.informationsecurityapi.service.helper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import mk.ukim.finki.informationsecurityapi.domain.Session;
import mk.ukim.finki.informationsecurityapi.exception.UnauthorizedException;

import java.time.LocalDateTime;
import java.util.Map;

public abstract class AuthenticationHelper {

    @Getter
    private static Map<String, Session> activeSessions;

    public static void setActiveSessions(Map<String, Session> activeSessions) {
        AuthenticationHelper.activeSessions = activeSessions;
    }

    public static void addSession(String token, Session session) {
        activeSessions.put(token, session);
    }

    public static void removeSession(String token) {
        activeSessions.remove(token);
    }

    public static void authenticateRequest(HttpServletRequest request) {
        String sessionId = getSessionId(request.getCookies());
        validateSession(sessionId);
    }

    public static String getSessionId(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("session_id")) {
                return cookie.getValue();
            }
        }

        return null;
    }

    private static void validateSession(String sessionId) {
        if (sessionId == null) {
            throw new UnauthorizedException("Unauthorized: session invalid or expired");
        }

        Session session = activeSessions.get(sessionId);
        if (session == null || LocalDateTime.now().isAfter(session.getExpirationDate())) {
            activeSessions.remove(sessionId);
            throw new UnauthorizedException("Unauthorized: session invalid or expired");
        }
    }

}
