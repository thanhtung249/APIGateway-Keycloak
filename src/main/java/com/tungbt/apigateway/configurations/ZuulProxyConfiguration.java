package com.tungbt.apigateway.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.tungbt.apigateway.filters.post.PostRouteInfoFilter;
import com.tungbt.apigateway.filters.post.SendErrorFilter;
import com.tungbt.apigateway.filters.pre.AuthorizationFilter;
import com.tungbt.apigateway.filters.pre.CorrelationIDFilter;
import com.tungbt.apigateway.filters.pre.PreRouteInfoFilter;
import com.tungbt.apigateway.filters.routing.AcquiringRouteFilter;
import com.tungbt.apigateway.filters.routing.CoreServiceRouteFilter;
import com.tungbt.apigateway.filters.routing.PayAtRetailRouteFilter;
import com.tungbt.apigateway.filters.template.BasicRouteFilterTemplate;

//import com.ascendcorp.acmbackofficeapigateway.filters.routing.RequestTokenRouteFilter;

@Configuration
public class ZuulProxyConfiguration {

    @Autowired
    ApplicationConfiguration applicationConfiguration;

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new org.springframework.web.filter.CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

    //Pre
    @Bean
    public AuthorizationFilter authenticationRouteFilter() { return new AuthorizationFilter();}

    @Bean
    public CorrelationIDFilter correlationIDFilter() { return new CorrelationIDFilter();}

    @Bean
    @Profile("non-prod")
    public PreRouteInfoFilter preRouteInfoFilter(){
        return new PreRouteInfoFilter();
    }


//    //Route
//    @Bean
//    public BasicRouteFilterTemplate merchantRouteFilter() {
//        return new BasicRouteFilterTemplate(applicationConfiguration, "acm-merchant-api", 1);
//    }
//
//    @Bean
//    public BasicRouteFilterTemplate alipayRouteFilter() {
//        return new BasicRouteFilterTemplate(applicationConfiguration, "acm-alipay-api", 1);
//    }
//
//    @Bean
//    public PayAtRetailRouteFilter payAtRetailRouteFilter() {
//        return new PayAtRetailRouteFilter(applicationConfiguration, "payment",1);
//    }
//
//    @Bean
//    public BasicRouteFilterTemplate merchantDaemonRouteFilter() {
//        return new BasicRouteFilterTemplate(applicationConfiguration, "acm-merchant-daemon", 1);
//    }
//
//    @Bean
//    public BasicRouteFilterTemplate dataServiceRouteFilter() {
//        return new BasicRouteFilterTemplate(applicationConfiguration, "tmn-data-service", 1);
//    }
//
//    @Bean
//    public BasicRouteFilterTemplate notificationRouteFilter() {
//        return new BasicRouteFilterTemplate(applicationConfiguration, "notifications-api", 1);
//    }
//
//    @Bean
//    public BasicRouteFilterTemplate financialRouterFilter() {
//        return new BasicRouteFilterTemplate(applicationConfiguration, "financial-data-service", 1);
//    }
//
//    @Bean
//    public AcquiringRouteFilter acquiringRouteFilter() {
//        return new AcquiringRouteFilter(applicationConfiguration, "acquiring", 1);
//    }
//
//    @Bean
//    public CoreServiceRouteFilter coreServiceRouteFilter() {
//        return new CoreServiceRouteFilter(applicationConfiguration, "core-service-web", 1);
//    }

    @Bean
    public BasicRouteFilterTemplate amlGlobalBlacklistApiRouteFilter() {
        return new BasicRouteFilterTemplate(applicationConfiguration, "aml-global-blacklist-scheduler", 1);
    }
    
    @Bean
    public BasicRouteFilterTemplate amlLocalBlacklistApiRouteFilter() {
        return new BasicRouteFilterTemplate(applicationConfiguration, "aml-local-blacklist-scheduler", 1);
    }

    /*@Bean
    public PayAtRetailRouteFilter payAtRetailRouteFilter() { return new PayAtRetailRouteFilter(); }*/
/*
    @Bean
    public MerchantDaemonRouteFilter merchantDaemonRouteFilter() { return new MerchantDaemonRouteFilter();}

    @Bean
    public AuthorizationFilter authenticationRouteFilter() { return new AuthorizationFilter();}

    @Bean
    public SendErrorFilter sendErrorRouteFilter() {
        return new SendErrorFilter();
    }

    @Bean
    public AlipayRouteFilter alipayRouteFilter() {
        return new AlipayRouteFilter();
    }

    @Bean
    public DataServiceRouteFilter dataServiceRouteFilter() {
        return new DataServiceRouteFilter();
    }

    @Bean
    public NotificationRouteFilter notificationRouteFilter() { return new NotificationRouteFilter(); }*/


    //Post
    @Bean
    public SendErrorFilter sendErrorRouteFilter() {
        return new SendErrorFilter();
    }

    @Bean
    @Profile("non-prod")
    public PostRouteInfoFilter postRouteInfoFilter(){
        return new PostRouteInfoFilter();
    }
}
