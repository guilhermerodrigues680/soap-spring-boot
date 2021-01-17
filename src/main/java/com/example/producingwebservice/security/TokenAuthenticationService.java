package com.example.producingwebservice.security;


import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.producingwebservice.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenAuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationService.class);
    private static final String ORIGINAL_SECRET = "teste#1234"; // Mesmo SECRET da Api Pública do ÓtimoAPP
    private static final String SECRET = Base64.getEncoder().encodeToString(ORIGINAL_SECRET.getBytes());
    private static final String HEADER_NAME = "Authorization";

    static void addAuthentication(HttpServletResponse response, String username) {
        String JWT = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setIssuer("Java SOAP example")
                .claim("env", ApplicationConfig.getENVIRONMENT())
                .claim("usuario", username)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .setHeaderParam("typ", "JWT")
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 1000))
                .compact();

        Cookie cookie = new Cookie("__Host-jwt", JWT);
        cookie.setHttpOnly(true);
        //cookie.setSecure(true);
        cookie.setMaxAge(60*60);

        response.addCookie(cookie);
        response.addHeader(HEADER_NAME, "Bearer " + JWT);

        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            ObjectMapper objectMapper = new ObjectMapper();
            JWTResponseDTO dto = new JWTResponseDTO(JWT);
            String dtoStr = objectMapper.writeValueAsString(dto);
            response.getWriter().write(dtoStr);
        } catch (IOException e) {
            logger.error("{}", e);
        }
    }

    static Authentication getAuthentication(HttpServletRequest request) {

        String token = request.getHeader(HEADER_NAME);

        if(token != null) {

            try {
                String user = Jwts.parser()
                        .setSigningKey(SECRET)
                        .parseClaimsJws(token)
                        .getBody()
                        //.getSubject();
                        .get("usuario", String.class);

                String envToken = Jwts.parser()
                        .setSigningKey(SECRET)
                        .parseClaimsJws(token)
                        .getBody()
                        .get("env", String.class);

                logger.debug("AMBIENTE ATUAL: {} , AMBIENTE DO TOKEN: {}", ApplicationConfig.getENVIRONMENT(), envToken);

                if(ApplicationConfig.getENVIRONMENT().equals(envToken)) {
                    if (user != null) {
                        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
                    }

                } else if(user.equals("appotimo")) { // Captura o token do ÓtimoAPP (Ele não possui ambiente)
                    return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
                } else {
                    logger.error("Token invalido para o ambiente. AMBIENTE ATUAL: {} != AMBIENTE DO TOKEN: {}", ApplicationConfig.getENVIRONMENT(), envToken);
                }

            } catch (Exception e) {
                logger.error("ERRO NA AUTENTICACAO: {}", e);
            }

        }
        return null;
    }
}

