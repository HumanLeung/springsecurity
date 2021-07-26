package com.company.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class Oauth2Token extends UsernamePasswordAuthenticationToken {

    public Oauth2Token(Object principal, Object credentials) {
        super(principal, credentials);
    }
    public Oauth2Token(Object principal, Object credentials,Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

}
