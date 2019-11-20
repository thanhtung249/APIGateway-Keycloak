package com.tungbt.apigateway.filters.template;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.tungbt.apigateway.configurations.ApplicationConfiguration;
import com.tungbt.apigateway.exception.PathNotFoundException;
import com.tungbt.apigateway.utils.PathMapUtility;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import static com.tungbt.apigateway.constant.FilterTypeConstant.ROUTING;
import static com.tungbt.apigateway.constant.RequestContextConstant.REQUEST_URI;
import java.util.List;
import java.util.Map;

@ConditionalOnMissingBean
public class BasicRouteFilterTemplate extends ZuulFilter {

    private String MAP_KEY;
    private Map<HttpMethod,List<Map<String, String>>>  configApiPath;
    private Integer order;

    public BasicRouteFilterTemplate(ApplicationConfiguration config, String MAP_KEY, Integer order){
        this.order = order;
        this.MAP_KEY = MAP_KEY;
        this.configApiPath = config.getRoutes().get(MAP_KEY).getRouteInfos();
    }

    @Override
    public String filterType() {
        return ROUTING;
    }

    @Override
    public int filterOrder() {
        return order;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String path = (String) ctx.get(REQUEST_URI);

        return path.startsWith("/" + MAP_KEY);
    }

    @Override
    public Object run() {

        prepareHeader();
        preparePath();

        return RequestContext.getCurrentContext();
    }

    protected void prepareHeader(){

    }

    protected void preparePath(){
        RequestContext ctx = RequestContext.getCurrentContext();
        try {

            String path = ((String) ctx.get(REQUEST_URI)).substring(("/" + MAP_KEY).length());
            String method = ctx.getRequest().getMethod();

            ctx.set(REQUEST_URI, convertPath(PathMapUtility.findMatchedPath(configApiPath, method, path)));

        } catch(PathNotFoundException e){
            prepareNotFoundResponse();
            ctx.setSendZuulResponse(false);
        }

    }

    protected void prepareNotFoundResponse() {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.set("error.status_code", HttpStatus.NOT_FOUND.value());
        ctx.set("error.exception", HttpStatus.NOT_FOUND.getReasonPhrase());

    }

    private String convertPath(String path){
        if(path != null) {
            return path.trim().replaceFirst("^/","").replaceAll("(/){2,}","/");
        }
        return null;
    }



}
