package com.ascendcorp.acmbackofficeapigateway.filters.routing;

import com.netflix.zuul.context.RequestContext;
import com.tungbt.apigateway.configurations.ApplicationConfiguration;
import com.tungbt.apigateway.filters.template.BasicRouteFilterTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BasicRouteFilterTest {

    @Autowired
    ApplicationConfiguration applicationConfiguration;

    BasicRouteFilterTemplate basicRouteFilter;

    @Before
    public void initTest(){
        basicRouteFilter = new BasicRouteFilterTemplate(applicationConfiguration, "acm-merchant-api", 1);
    }

    @Test
    public void test_route_with_merchant_api(){

        applicationConfiguration.getRoutes().get("acm-merchant-api").getRouteInfos().forEach((httpMethod,list) -> {
            list.forEach(m -> {
            createSendForwardFilter(new MockHttpServletRequest(httpMethod.toString(), ""), "/acm-merchant-api" + m.get("path"));

            assertTrue(basicRouteFilter.shouldFilter());

            RequestContext ctx = (RequestContext) basicRouteFilter.run();

            for(String key : m.keySet()) {
                assertEquals(1, basicRouteFilter.filterOrder());
                //assertEquals(m.get(key).replaceFirst("#PATH#", key), ctx.get("requestURI"));
            }
            });
        });
    }

    private void createSendForwardFilter(HttpServletRequest request,String requestURI) {
        RequestContext context = new RequestContext();
        context.setRequest(request);
        context.setResponse(new MockHttpServletResponse());
        context.set("requestURI", requestURI);
        RequestContext.testSetCurrentContext(context);
    }

}