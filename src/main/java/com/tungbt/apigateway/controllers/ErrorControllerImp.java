package com.tungbt.apigateway.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.tungbt.apigateway.constant.FilterTypeConstant;
import com.tungbt.apigateway.domains.ErrorResponse;
import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class ErrorControllerImp implements ErrorController{

    @Value("${server.error.path}")
    private String errorPath;

    @Override
    public String getErrorPath(){
        return errorPath;
    }

    @RequestMapping(value = "${server.error.path}")
    public @ResponseBody
    ResponseEntity error(HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse();

        final int status = getErrorStatus(request);
        final String errorMessage = getErrorMessage(request);

        errorResponse.setCode(status);
        errorResponse.setMessage(errorMessage);

        return ResponseEntity.status(status).body(errorResponse);
    }

    private int getErrorStatus(HttpServletRequest request) {
        final Integer statusCode = (Integer)request.getAttribute(FilterTypeConstant.JAVAX_SERVLET_ERROR_STATUS_CODE);
        return statusCode != null ? statusCode : HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    private String getErrorMessage(HttpServletRequest request) {
        final Object exceptionType = request.getAttribute(FilterTypeConstant.JAVAX_SERVLET_ERROR_EXCEPTION);
        String message = FilterTypeConstant.DEFAULT_ERROR_MESSAGE;

        if(exceptionType instanceof Throwable) {
            message = ((Throwable) exceptionType).getLocalizedMessage();
        }
        if(exceptionType instanceof String) {
            message = (String) exceptionType;
        }
        log.debug("Error message: {}", message);
        return message;
    }
}