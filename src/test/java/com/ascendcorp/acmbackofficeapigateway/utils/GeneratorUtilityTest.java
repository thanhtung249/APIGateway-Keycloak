package com.ascendcorp.acmbackofficeapigateway.utils;

import org.junit.Test;
import com.tungbt.apigateway.utils.GeneratorUtility;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class GeneratorUtilityTest {

    @Test(expected = InvocationTargetException.class)
    public void testPrivateConstructor() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Constructor<GeneratorUtility> c = GeneratorUtility.class.getDeclaredConstructor();
        c.setAccessible(true);
        c.newInstance();
    }

}