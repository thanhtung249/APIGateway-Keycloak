package com.tungbt.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ConfigurableApplicationContext;
import com.tungbt.apigateway.configurations.ApplicationConfiguration;
import com.tungbt.apigateway.configurations.KeycloakClientConfiguration;
import com.tungbt.apigateway.services.PolicyEnforcer;

@EnableZuulProxy
@SpringBootApplication
@EnableConfigurationProperties({ ApplicationConfiguration.class , KeycloakClientConfiguration.class})
public class AcmBackofficeApiGatewayApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(AcmBackofficeApiGatewayApplication.class, args);
		context.getBean(PolicyEnforcer.class).init();
	}

}
