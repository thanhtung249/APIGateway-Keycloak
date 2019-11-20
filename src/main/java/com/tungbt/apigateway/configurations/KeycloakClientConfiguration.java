package com.tungbt.apigateway.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "keycloak", ignoreUnknownFields = false)
public class KeycloakClientConfiguration {
     String realm;
     String authServerUrl;
     String sslRequired;
     //PolicyEnforcerConfig policyEnforcer;
     Scopes scope;
     List<String> authorized;

     @Data
     public static class Scopes {
          String get;
          String post;
          String put;
          String delete;
     }
}
