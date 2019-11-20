package com.ascendcorp.acmbackofficeapigateway.filters.post;


import com.netflix.zuul.context.RequestContext;
import com.tungbt.apigateway.filters.post.SendErrorFilter;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SendErrorFilterTest {

    @After
    public void reset() {
        RequestContext.testSetCurrentContext(null);
    }

    @Value("${server.error.path}")
    private String errorPath;

    @Test
    public void testFilterOrder(){
        SendErrorFilter filter = createSendErrorFilter(new MockHttpServletRequest());
        assertEquals(-1, filter.filterOrder());
    }

    @Test
    public void runsNormally() {
        SendErrorFilter filter = createSendErrorFilter(new MockHttpServletRequest());
        assertTrue("shouldFilter returned false", filter.shouldFilter());
        filter.run();
    }

    private SendErrorFilter createSendErrorFilter(HttpServletRequest request) {
        RequestContext context = new RequestContext();
        context.setRequest(request);
        context.setResponse(new MockHttpServletResponse());
        context.set("error.status_code", HttpStatus.NOT_FOUND.value());
        context.set("error.exception", "test message exception");
        RequestContext.testSetCurrentContext(context);
        SendErrorFilter filter = new SendErrorFilter();
        filter.setErrorPath(errorPath);
        return filter;
    }

    private SendErrorFilter createSendErrorFilterWithSendErrorFilterVar(HttpServletRequest request) {
        RequestContext context = new RequestContext();
        context.setRequest(request);
        context.setResponse(new MockHttpServletResponse());
        context.set("sendErrorFilter.ran", true);
        RequestContext.testSetCurrentContext(context);
        SendErrorFilter filter = new SendErrorFilter();
        filter.setErrorPath(errorPath);
        return filter;
    }

    private SendErrorFilter createSendErrorFilterWithNotSetErrorPath(HttpServletRequest request) {
        RequestContext context = new RequestContext();
        context.setRequest(request);
        context.setResponse(new MockHttpServletResponse());
        context.set("sendErrorFilter.ran", true);
        RequestContext.testSetCurrentContext(context);
        SendErrorFilter filter = new SendErrorFilter();
        return filter;
    }

    @Test
    public void noRequestDispatcher() {
        SendErrorFilter filter = createSendErrorFilter(mock(HttpServletRequest.class));
        assertTrue("shouldFilter returned false", filter.shouldFilter());
        filter.run();
    }

    @Test
    public void doesNotRunTwice() {
        SendErrorFilter filter = createSendErrorFilter(new MockHttpServletRequest());
        assertTrue("shouldFilter returned false", filter.shouldFilter());
        filter.run();
        assertFalse("shouldFilter returned true", filter.shouldFilter());
    }

    @Test
    public void testShouldFilterWithSendErrorFilterRanVar(){
        SendErrorFilter filter = createSendErrorFilterWithSendErrorFilterVar(new MockHttpServletRequest());
        filter.run();
        assertFalse("shouldFilter returned false", filter.shouldFilter());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShouldFilterWithError(){
        SendErrorFilter filter = createSendErrorFilterWithNotSetErrorPath(new MockHttpServletRequest());
        filter.run();
        assertFalse("shouldFilter returned false", filter.shouldFilter());

    }
}