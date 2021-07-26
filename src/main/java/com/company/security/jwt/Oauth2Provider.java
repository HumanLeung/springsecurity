package com.company.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


//@Component
public class Oauth2Provider implements AuthenticationProvider {

    private Oauth2Token oauth2Token;

    private Oauth2Repository repository;


    public Oauth2Provider(Oauth2Repository oauth2Repository) {
        this.repository = oauth2Repository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
