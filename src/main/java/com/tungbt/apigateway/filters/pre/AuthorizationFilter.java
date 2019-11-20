package com.tungbt.apigateway.filters.pre;

import com.ascendcorp.logger.log4j.ACNLogger;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.tungbt.apigateway.configurations.ApplicationConfiguration;
import com.tungbt.apigateway.configurations.KeycloakClientConfiguration;
import com.tungbt.apigateway.services.KeycloakAuthz;
import com.tungbt.apigateway.utils.PathMapUtility;
import org.keycloak.authorization.client.AuthorizationDeniedException;
import org.keycloak.authorization.client.representation.TokenIntrospectionResponse;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.keycloak.representations.idm.authorization.Permission;
import org.keycloak.representations.idm.authorization.PermissionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.annotation.PostConstruct;
import static com.tungbt.apigateway.constant.FilterTypeConstant.PRE_ROUTE;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthorizationFilter extends ZuulFilter {
    private static final ACNLogger log = ACNLogger.create(AuthorizationFilter.class);

    @Autowired
    ApplicationConfiguration applicationConfiguration;

    @Autowired
    KeycloakClientConfiguration keycloakConfig;

    @Autowired
    KeycloakAuthz keycloakAuthz;

    private Pattern pattern;
    private Matcher matcher;
    private String bearerHead = "Bearer ";
    private String group = "(\\/".concat(".").concat("\\*)");

    @Override
    public String filterType() {
        return PRE_ROUTE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String path = ctx.getRequest().getRequestURI();

        return filterShouldAuthorized(path,ctx);
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpMethod httpMethod = HttpMethod.valueOf(ctx.getRequest().getMethod());
        String apiURL = ctx.getRequest().getRequestURI().replaceAll(ctx.getRequest().getContextPath(),""); // remove /acm-backoffice-api-gateway
        String apiPath =  apiURL.split("/")[1];
        String eat = ctx.getRequest().getHeader("Authorization")!=null?ctx.getRequest().getHeader("Authorization").substring(bearerHead.length()):null;

        Optional<String> requestResouceName = Optional.ofNullable(findMatch(apiURL,apiPath,httpMethod));
        AuthorizationRequest request;
        PermissionRequest permission;

        boolean isAuthorized = Boolean.FALSE;

        if(requestResouceName.isPresent()){
            //Found matcher then request to grant permission
            Set<String> scope = new HashSet<>(Collections.singletonList(mapToScopeRepresentation(httpMethod.name().toLowerCase())));
            request = new AuthorizationRequest();

            permission = new PermissionRequest();
            permission.setResourceId(requestResouceName.get());
            permission.setScopes(scope);
            request.addPermission(requestResouceName.get(), new ArrayList<>(scope));

            try{
                AuthorizationResponse response = keycloakAuthz.entitlementGetALL(apiPath, eat);
                String rpt = response.getToken();

                TokenIntrospectionResponse requestingPartyToken = keycloakAuthz.introspectRequestingPartyToken(apiPath, rpt);
                for(Permission permissionRegistered : requestingPartyToken.getPermissions()){
                    if(KeycloakAuthz.isPremitGranted(requestResouceName.get(), permissionRegistered, permission)){
                        isAuthorized = true;
                        log.debug("granted "+permission.getResourceId() + " with scope "+permission.getScopes().toArray()[0].toString());
                        break;
                    }
                }

            }catch (AuthorizationDeniedException e){
                log.debug("AuthorizationDeniedException :"+e.getMessage());
                setFailedRequest("You don't have a permission or authorized service "+apiURL, HttpStatus.UNAUTHORIZED.value());
                return null;
            }

            if(!isAuthorized)
            {
                log.debug("Denied on "+requestResouceName.get()+" from "+apiURL);
                setFailedRequest("You don't have a permission or authorized service "+apiURL, HttpStatus.FORBIDDEN.value());
                return null;
            }else{
                log.debug("Permit granted on "+requestResouceName.get()+" from "+apiURL);
            }
        }

        return true;
    }

    private void setFailedRequest(String body, int code) {
        RequestContext ctx = RequestContext.getCurrentContext();

        ctx.setSendZuulResponse(false);
        ctx.set("error.status_code", code);
        ctx.set("error.exception", body);
    }

    private String mapToScopeRepresentation(String method){
        switch (method) {
            case "get":
                return keycloakConfig.getScope().getGet();
            case "post":
                return keycloakConfig.getScope().getPost();
            case "put":
                return keycloakConfig.getScope().getPut();
            case "delete":
                return keycloakConfig.getScope().getDelete();
            default:
                return "";
        }
    }

    private boolean filterShouldAuthorized(String path ,RequestContext ctx ){
        boolean result = Boolean.FALSE;
        for(String startPathModule : keycloakConfig.getAuthorized())
        {
            if(path.startsWith(ctx.getRequest().getContextPath().concat("/").concat(startPathModule)))
            {
                result = true;
                break;
            }
        }
        return result;
    }

    private String findMatch(String apiURL, String apiPath, HttpMethod httpMethod){
        if(applicationConfiguration.getRoutes().containsKey(apiPath)) {
            for(Map<String, String> map :  applicationConfiguration.getRoutes().get(apiPath).getRouteInfos().get(httpMethod)){
                for(String k : map.keySet()){
                    String compile = k.replaceAll("\\/\\*",group);
                    pattern = Pattern.compile(compile);
                    matcher = pattern.matcher(apiURL.substring(apiPath.length() + 2));
                    if(matcher.matches()){
                        return k;
                    }
                }
            }
        }
        return null;
    }

}
