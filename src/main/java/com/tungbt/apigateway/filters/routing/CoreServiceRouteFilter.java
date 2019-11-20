package com.tungbt.apigateway.filters.routing;

import com.netflix.zuul.context.RequestContext;
import com.tungbt.apigateway.configurations.ApplicationConfiguration;
import com.tungbt.apigateway.filters.template.BasicRouteFilterTemplate;

public class CoreServiceRouteFilter extends BasicRouteFilterTemplate {
    private ApplicationConfiguration applicationConfiguration;

    public CoreServiceRouteFilter(ApplicationConfiguration config, String MAP_KEY, Integer order) {
        super(config, MAP_KEY, order);
        this.applicationConfiguration = config;
    }

    @Override
    protected void prepareHeader(){
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader("TMN-X-ACCESS-KEY",(String)applicationConfiguration.getCoreservice().get("CORE_SERVICE_X_ACCESS_KEY"));
        ctx.addZuulRequestHeader("Content-Type","application/json");
    }
}
