package com.ascendcorp.acmbackofficeapigateway.constant;

import org.junit.Test;
import com.tungbt.apigateway.constant.FilterTypeConstant;
import com.tungbt.apigateway.constant.RequestContextConstant;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorConstantTest {

    @Test(expected = InvocationTargetException.class)
    public void testFilterTypePrivateConstructor() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Constructor<FilterTypeConstant> c = FilterTypeConstant.class.getDeclaredConstructor();
        c.setAccessible(true);
        c.newInstance();
    }

    @Test(expected = InvocationTargetException.class)
    public void testRequestContextPrivateConstructor() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Constructor<RequestContextConstant> c = RequestContextConstant.class.getDeclaredConstructor();
        c.setAccessible(true);
        c.newInstance();
    }

}