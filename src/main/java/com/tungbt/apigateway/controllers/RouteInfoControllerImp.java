package com.tungbt.apigateway.controllers;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.tungbt.apigateway.domains.RouteInfoResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Profile("non-prod")
@RestController
public class RouteInfoControllerImp{

    @RequestMapping(path = "/route-info")
    public @ResponseBody ResponseEntity getRouteInfo(HttpServletRequest request, HttpServletResponse a) throws IOException {

        RouteInfoResponse response = new RouteInfoResponse();

        response.setHttpMethod(request.getMethod());
        response.setRequestHeader(getHeadersInfo(request));
        response.setRequestBody(getRequestBody(request));
        response.setForwardHost(request.getAttribute("forwardHost").toString());
        response.setForwardPath((String) request.getAttribute("forwardPath"));
        response.setForwardHeader((HashMap<String, String>) request.getAttribute("forwardHeader"));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private String getRequestBody(HttpServletRequest request) {
        try {
            return request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } catch(Exception e){
            return null;
        }
    }

    private Map<String, String> getHeadersInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }
}