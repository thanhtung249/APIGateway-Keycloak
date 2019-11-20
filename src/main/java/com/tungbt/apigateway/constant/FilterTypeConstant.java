package com.tungbt.apigateway.constant;

public final class FilterTypeConstant {
    public static final String PRE_ROUTE = "pre";
    public static final String ROUTING = "route";
    public static final String POST_ROUTE = "post";

    public static final String SEND_ERROR_FILTER_RAN = "sendErrorFilter.ran";
    public static final String JAVAX_SERVLET_ERROR_STATUS_CODE = "javax.servlet.error.status_code";
    public static final String JAVAX_SERVLET_ERROR_EXCEPTION = "javax.servlet.error.exception";
    public static final String ERROR_EXCEPTION = "error.exception";
    public static final String ERROR_STATUS_CODE = "error.status_code";
    public static final String DEFAULT_ERROR_MESSAGE = "Unexpected error occurred";

    private FilterTypeConstant() {
        throw new UnsupportedOperationException();
    }
}
