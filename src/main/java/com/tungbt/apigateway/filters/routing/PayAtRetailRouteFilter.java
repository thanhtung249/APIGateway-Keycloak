package com.tungbt.apigateway.filters.routing;

import com.netflix.zuul.context.RequestContext;
import com.tungbt.apigateway.configurations.ApplicationConfiguration;
import com.tungbt.apigateway.filters.template.BasicRouteFilterTemplate;


public class PayAtRetailRouteFilter extends BasicRouteFilterTemplate {

    private ApplicationConfiguration applicationConfiguration;

    public PayAtRetailRouteFilter(ApplicationConfiguration config, String MAP_KEY, Integer order) {
        super(config, MAP_KEY, order);
        this.applicationConfiguration = config;
    }
    @Override
    protected void prepareHeader(){
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader("X-API-Version",(String)applicationConfiguration.getPayment().get("PAYMENT_PLATFORM_X_API_VERSION"));
        ctx.addZuulRequestHeader("ISV",(String)applicationConfiguration.getPayment().get("PAYMENT_PLATFORM_ISV"));
        ctx.addZuulRequestHeader("Content-Type","application/json");
    }

}
