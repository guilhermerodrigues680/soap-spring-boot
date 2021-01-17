package com.example.producingwebservice.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Filter extends GenericFilterBean {

    Logger logger = LoggerFactory.getLogger(Filter.class);
    final String momento;

    public Filter(String momento) {
        super();
        this.momento = momento;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        //Authentication authentication = TokenAuthenticationService.getAuthentication((HttpServletRequest) request);
        //SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.debug("Passei aqui: {}", momento);

        HttpServletResponse res = (HttpServletResponse) response;
        res.addHeader("Access-Control-Allow-Origin", "*");

        if (((HttpServletRequest) request).getMethod().equals("OPTIONS")) {
            res.addHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
            res.addHeader("Access-Control-Allow-Headers", "*");
            return;
        }

        filterChain.doFilter(request, response);
    }

}
