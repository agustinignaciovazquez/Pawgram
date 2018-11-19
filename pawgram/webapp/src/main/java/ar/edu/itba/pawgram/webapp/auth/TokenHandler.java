package ar.edu.itba.pawgram.webapp.auth;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Service
public class TokenHandler {

    @Autowired
    private String tokenSigningKey;

    @Autowired
    private UserDetailsService userDetailsService;

    public UserDetails parseUserFromToken(final String token) {
        try {
            final String username = Jwts.parser()
                    .setSigningKey(tokenSigningKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return userDetailsService.loadUserByUsername(username);
        } catch (SignatureException e) {
            return null;
        }
    }

    public String createTokenForUser(final String username) {
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS512, tokenSigningKey)
                .compact();
    }
}

