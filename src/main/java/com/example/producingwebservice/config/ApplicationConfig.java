package com.example.producingwebservice.config;

public class ApplicationConfig {
    private static final String ENVIRONMENT = "development"; // production = Produção, development = Homologação

    public static String getENVIRONMENT() {
        return ENVIRONMENT;
    }

}

