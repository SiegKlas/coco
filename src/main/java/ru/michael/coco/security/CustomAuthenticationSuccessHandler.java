package ru.michael.coco.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String redirectUrl = null;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_OVERSEER")) {
                redirectUrl = "/overseer";
                break;
            } else if (authority.getAuthority().equals("ROLE_TEACHER")) {
                redirectUrl = "/teacher";
                break;
            } else if (authority.getAuthority().equals("ROLE_STUDENT")) {
                redirectUrl = "/";
                break;
            }
        }

        if (redirectUrl == null) {
            throw new IllegalStateException();
        }

        response.sendRedirect(redirectUrl);
    }
}
