package com.ascendcorp.acmbackofficeapigateway.filters.pre;

import com.netflix.zuul.context.RequestContext;
import com.tungbt.apigateway.filters.pre.AuthorizationFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.zuul.filters.route.SendForwardFilter;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AuthorizationFilterTest {

    @Autowired
    AuthorizationFilter authenticationFilter;

    @Test
    public void test_authentication_filter(){
        createSendForwardFilter(new MockHttpServletRequest(HttpMethod.PUT.name(),""), "/acm-merchant-api/merchant/1100800808012341/tmn-keys");

//        authenticationFilter.run();
        assertFalse(authenticationFilter.shouldFilter());
        assertEquals(1, authenticationFilter.filterOrder());

    }

    private SendForwardFilter createSendForwardFilter(HttpServletRequest request, String requestURI) {
        RequestContext context = new RequestContext();
        context.setRequest(request);
        context.setResponse(new MockHttpServletResponse());
        context.set("requestURI", requestURI);
        RequestContext.testSetCurrentContext(context);
        SendForwardFilter filter = new SendForwardFilter();
        return filter;
    }

}