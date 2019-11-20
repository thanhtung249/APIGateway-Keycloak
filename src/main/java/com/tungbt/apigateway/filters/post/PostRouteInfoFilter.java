package com.tungbt.apigateway.filters.post;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ReflectionUtils;
import static com.tungbt.apigateway.constant.FilterTypeConstant.POST_ROUTE;
import javax.servlet.RequestDispatcher;

public class PostRouteInfoFilter extends ZuulFilter {

    protected static final String SEND_ERROR_FILTER_RAN = "sendErrorFilter.ran";

    @Override
    public String filterType() { return POST_ROUTE; }

    @Override
    public int filterOrder() { return 1; }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return StringUtils.equals("true", ctx.getRequest().getHeader("get-route"));
    }

    @Override
    public Object run() {
        try {
            RequestContext ctx = RequestContext.getCurrentContext();
            ctx.getRequest().setAttribute("forwardHost",ctx.getRouteHost());
            ctx.getRequest().setAttribute("forwardPath",ctx.get("requestURI"));
            ctx.getRequest().setAttribute("forwardHeader",ctx.get("zuulRequestHeaders"));
            RequestDispatcher dispatcher = ctx.getRequest().getRequestDispatcher("/route-info");
            if (dispatcher != null) {
                if (!ctx.getResponse().isCommitted()) {
                    dispatcher.forward(ctx.getRequest(), ctx.getResponse());
                }
            }

        }
        catch (Exception ex) {
            ReflectionUtils.rethrowRuntimeException(ex);
        }
        return null;
    }

}