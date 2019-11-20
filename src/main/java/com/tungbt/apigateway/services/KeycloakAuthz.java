package com.tungbt.apigateway.services;

import com.ascendcorp.logger.log4j.ACNLogger;
import com.tungbt.apigateway.configurations.ApplicationConfiguration;
import com.tungbt.apigateway.configurations.KeycloakClientConfiguration;
import com.tungbt.apigateway.filters.pre.AuthorizationFilter;
import org.apache.http.client.HttpResponseException;
import org.apache.http.impl.client.HttpClientBuilder;
import org.keycloak.authorization.client.AuthorizationDeniedException;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.representation.TokenIntrospectionResponse;
import org.keycloak.authorization.client.resource.ProtectionResource;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.keycloak.representations.idm.authorization.Permission;
import org.keycloak.representations.idm.authorization.PermissionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class KeycloakAuthz {

  @Autowired
  KeycloakClientConfiguration keycloakConfig;

  @Autowired
  ApplicationConfiguration applicationConfiguration;

  private Map<String, AuthzClient> authzClientMap;
  private static final ACNLogger log = ACNLogger.create(AuthorizationFilter.class);

  /**
   * Initiate AuthzClient for each client.
   */
  @PostConstruct
  public void init() {
    authzClientMap = new HashMap<>();
    for (Map.Entry<String, ApplicationConfiguration.Routes> route : applicationConfiguration
        .getRoutes().entrySet()) {
      if (route.getValue().getClientId() != null) {
        Configuration configuration = new Configuration(keycloakConfig.getAuthServerUrl(),
            keycloakConfig.getRealm(), route.getValue().getClientId(),
            route.getValue().getCredentials(), HttpClientBuilder.create().build());

        authzClientMap.put(route.getKey(), AuthzClient.create(configuration));
      }
    }

  }

  /**
   * Send entitlement request to obtain rpt with all permissions granted to user.
   * 
   * @param path
   * @param token
   * @return
   */
  public AuthorizationResponse entitlementGetALL(String path, String token) {
    String temp = applicationConfiguration.getRoutes().get(path).getClientId();
    AuthzClient c = authzClientMap.get(path);
    try {
      AuthorizationResponse s = authzClientMap.get(path).authorization(token).authorize();
      return s;
    } catch (RuntimeException e) {
      log.error(e);
      if (e.getMessage().contains("401")) {
        throw new AuthorizationDeniedException(e);
      } else {
        throw e;
      }
    }
  }

  /**
   * Validate permission of calling path
   * 
   * @param path
   * @param rpt
   * @return
   */
  public TokenIntrospectionResponse introspectRequestingPartyToken(String path, String rpt) {
    AuthzClient c = authzClientMap.get(path);
    ProtectionResource x = c.protection(rpt);
    TokenIntrospectionResponse s = x.introspectRequestingPartyToken(rpt);
    return s;
  }

  public static boolean isPremitGranted(String pathAfter, Permission permission,
      PermissionRequest request) {
    return permission.getScopes().contains(request.getScopes().toArray()[0].toString())
        && !permission.getScopes().isEmpty() && !request.getScopes().isEmpty()
        && compareResource(pathAfter, permission.getResourceName());
  }

  private static boolean compareResource(String requestPath, String permissionPath) {
    String compile = permissionPath.replaceAll("(?<!/)\\*\\*", "(\\$|\\/.\\*)")
        .replaceAll("/(\\*)(?<!/)$", "\\/((\\\\w|\\\\*)*)[^\\/]\\$")
        .replaceAll("/(\\*)/", "\\/((\\\\w|\\\\*)*)\\/");
    Pattern pattern = Pattern.compile(compile);
    Matcher matcher = pattern.matcher(requestPath);
    return matcher.matches();
  }

}
