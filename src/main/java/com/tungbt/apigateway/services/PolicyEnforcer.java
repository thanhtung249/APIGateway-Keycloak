package com.tungbt.apigateway.services;


import com.ascendcorp.logger.log4j.ACNLogger;
import com.tungbt.apigateway.configurations.KeycloakClientConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class PolicyEnforcer implements InitializingBean{
    private static final ACNLogger log = ACNLogger.create(PolicyEnforcer.class);

    @Autowired
    KeycloakClientConfiguration keycloakConfig;

    @Autowired
    KeycloakAuthz keycloakAuthz;

    public PolicyEnforcer(){
        log.info("PolicyEnforcer Constructor");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("PolicyEnforcer Initializing Bean");
    }

    @PostConstruct
    public void postConstruct() {
        log.info("PolicyEnforcer PostConstruct");
    }

    public void init() {
/*        log.info("PolicyEnforcer initial enforcement");
        List<PolicyEnforcerConfig.PathConfig> resources = keycloakConfig.getPolicyEnforcer().getPaths();
        for(PolicyEnforcerConfig.PathConfig resource : resources){
            for(PolicyEnforcerConfig.MethodConfig method : resource.getMethods()){
                //createOnRemote(resource,method);
            }
        }*/

    }



}
