package com.tungbt.apigateway.filters.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.tungbt.apigateway.constant.Constants;
import com.tungbt.apigateway.utils.GeneratorUtility;
import lombok.extern.log4j.Log4j2;
import static com.tungbt.apigateway.constant.FilterTypeConstant.PRE_ROUTE;
import org.apache.logging.log4j.ThreadContext;

@Log4j2
public class CorrelationIDFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return PRE_ROUTE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        String xCorId = RequestContext.getCurrentContext().getRequest().getHeader(Constants.X_CORRELATION_ID_HEADER);
        if(xCorId != null){
            ThreadContext.put(Constants.THREAD_CONTEXT_CORRELATIONID_ID, xCorId);
            return false;
        }
        return true;
    }

    @Override
    public Object run() {
        String correlationId = GeneratorUtility.generateTransactionId();
        RequestContext.getCurrentContext().addZuulRequestHeader(Constants.X_CORRELATION_ID_HEADER, correlationId);
        ThreadContext.put(Constants.THREAD_CONTEXT_CORRELATIONID_ID, correlationId);
        log.info("Gateway generate X-Correlation-ID '" + correlationId + "' for " + GeneratorUtility.generateRequestTarget(RequestContext.getCurrentContext()));
        return true;
    }

}
