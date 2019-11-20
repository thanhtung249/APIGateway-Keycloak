package com.tungbt.apigateway.utils;

import com.netflix.zuul.context.RequestContext;

import java.util.UUID;

public class GeneratorUtility {

    private GeneratorUtility(){ throw new UnsupportedOperationException();}

    public static String generateRequestTarget(RequestContext ctx) {
    	return ctx.getRequest().getMethod() + ": " + ctx.getRequest().getRequestURI();
    }

	public static String generateTransactionId() {
			return convertString(UUID.randomUUID().toString());
	}

	private static String convertString(String transactionId){

        return "bigw-"+ transactionId.replace("-", "");
	}

}
