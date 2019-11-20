package com.tungbt.apigateway.filters.post;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.tungbt.apigateway.constant.FilterTypeConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ReflectionUtils;
import static com.tungbt.apigateway.constant.FilterTypeConstant.POST_ROUTE;
import javax.servlet.RequestDispatcher;

public class SendErrorFilter extends ZuulFilter {


    @Value("${server.error.path}")
    private String errorPath;

    @Override
    public String filterType() {
        return POST_ROUTE;
    }

    @Override
    public int filterOrder() {
        // Filter order for org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter is 0
        return -1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        // only forward to errorPath if it hasn't been forwarded to already
        return ctx.containsKey(FilterTypeConstant.ERROR_STATUS_CODE)
                && !ctx.getBoolean(FilterTypeConstant.SEND_ERROR_FILTER_RAN, false);
    }

    @Override
    public Object run() {
        try {
            RequestContext ctx = RequestContext.getCurrentContext();

            // In case of unchecked exception in upstream services;
            // the 'error.status_code' can be null.
            final int statusCode = (ctx.get(FilterTypeConstant.ERROR_STATUS_CODE) == null)? ctx.getResponseStatusCode()
                    : (Integer) ctx.get(FilterTypeConstant.ERROR_STATUS_CODE);

            if (ctx.containsKey(FilterTypeConstant.ERROR_EXCEPTION)) {
                final Object exceptionObject = ctx.get(FilterTypeConstant.ERROR_EXCEPTION);
                ctx.getRequest().setAttribute(FilterTypeConstant.JAVAX_SERVLET_ERROR_EXCEPTION, getThrowable(exceptionObject));
            }
            ctx.getRequest().setAttribute(FilterTypeConstant.JAVAX_SERVLET_ERROR_STATUS_CODE, statusCode);
            RequestDispatcher dispatcher = ctx.getRequest().getRequestDispatcher(this.errorPath);
            if (dispatcher != null) {
                ctx.set(FilterTypeConstant.SEND_ERROR_FILTER_RAN, true);
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

    private static Throwable getThrowable(final Object exceptionObject) {
        if(exceptionObject instanceof String){
            return new Throwable((String)exceptionObject);
        }
        if(exceptionObject instanceof Throwable) {
            return (Throwable)exceptionObject;
        }
        return new Throwable(FilterTypeConstant.DEFAULT_ERROR_MESSAGE);
    }

    public void setErrorPath(String errorPath) {
        this.errorPath = errorPath;
    }


}