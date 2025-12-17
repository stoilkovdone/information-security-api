package mk.ukim.finki.informationsecurityapi.service.helper;

import jakarta.servlet.http.Cookie;

public abstract class RequestHelper {

    public static String getToken(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
