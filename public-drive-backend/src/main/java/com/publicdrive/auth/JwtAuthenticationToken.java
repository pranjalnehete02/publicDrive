package com.publicdrive.auth;


import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String username;
    private Object credentials;

    public JwtAuthenticationToken(String username, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.username = username;
        this.credentials = credentials;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }
}
