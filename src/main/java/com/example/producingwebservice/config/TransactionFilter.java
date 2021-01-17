package com.example.producingwebservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class TransactionFilter implements Filter {

    final Logger LOG = LoggerFactory.getLogger(TransactionFilter.class);

    @Override
    public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        LOG.info("Starting a transaction for req : {}", parseRequestToString(req));

        res.addHeader("Access-Control-Allow-Origin", "*");

        // Todas RequisiçõesHTTP OPTIONS são finalizadas aqui
        if (req.getMethod().equalsIgnoreCase("OPTIONS")) {

            res.addHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
            res.addHeader("Access-Control-Allow-Headers", "*");
            res.setStatus(HttpServletResponse.SC_OK);
        } else {
            // Somente requisições != HTTP OPTIONS são processadas
            chain.doFilter(request, response);
        }

        LOG.info("Committing a transaction for req (status: {}): {}", res.getStatus(), parseRequestToString(req));
    }

    private String parseRequestToString(HttpServletRequest req) {
        String remoteAddr = req.getRemoteAddr();
        String method = req.getMethod();

        String uri = req.getScheme() + "://" +
                req.getServerName() + ":" +
                req.getServerPort() +
                req.getRequestURI() +
                (req.getQueryString() != null ? "?" + req.getQueryString(): "");

        String xForwardedFor = req.getHeader("X-Forwarded-For");
        String xForwardedProto = req.getHeader("X-Forwarded-Proto");

        return String.format("%s - %s %s - X-Forwarded-For: %s X-Forwarded-Proto: %s", remoteAddr, method, uri, xForwardedFor, xForwardedProto);
    }

}
