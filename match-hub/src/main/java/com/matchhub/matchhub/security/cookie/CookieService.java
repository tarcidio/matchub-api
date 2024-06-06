package com.matchhub.matchhub.security.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private Cookie createCookie(String refreshToken){
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);// Cookie can't be managed in client side
        refreshCookie.setSecure(true); // Cookie can be used only secure https connection
        refreshCookie.setPath("/");// Cookie will send to all domain request
        refreshCookie.setMaxAge( (int) refreshExpiration);
        return refreshCookie;
    }

    public void addCookie(HttpServletResponse response, String refreshToken){
        response.addCookie(createCookie(refreshToken));
    }

    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null; // Retorna null se o cookie não for encontrado
    }

}
