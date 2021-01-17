package com.example.producingwebservice;

import com.example.contrato.GetCountryRequest;
import com.example.contrato.GetCountryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Endpoint
public class CountryEndpoint {
    private static final String NAMESPACE_URI = "http://example.com/contrato";

    private CountryRepository countryRepository;
    private final HttpServletRequest servletRequest;
    private final HttpServletResponse servletResponse;

    @Autowired
    public CountryEndpoint(CountryRepository countryRepository, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        this.countryRepository = countryRepository;
        this.servletRequest = servletRequest;
        this.servletResponse = servletResponse;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
    @ResponsePayload
    public GetCountryResponse getCountry(@RequestPayload GetCountryRequest request) {
        GetCountryResponse response = new GetCountryResponse();
        response.setCountry(countryRepository.findCountry(request.getName()));

        return response;
    }
}
