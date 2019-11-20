package com.tungbt.apigateway.filters.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import static com.tungbt.apigateway.constant.FilterTypeConstant.PRE_ROUTE;
import org.apache.commons.lang.StringUtils;

public class PreRouteInfoFilter extends ZuulFilter {

    @Override
    public String filterType() { return PRE_ROUTE; }

    @Override
    public int filterOrder() { return 1; }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return StringUtils.equals("true", ctx.getRequest().getHeader("get-route"));
    }

    @Override
    public Object run() {
        RequestContext.getCurrentContext().setSendZuulResponse(false);
        return null;
    }

}