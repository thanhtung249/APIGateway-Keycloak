package com.tungbt.apigateway.domains;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteInfoResponse {

    String httpMethod;
    String forwardHost;
    String forwardPath;
    Map<String,String> forwardHeader;
    Map<String,String> RequestHeader;
    String RequestBody;

}
