package com.ascendcorp.acmbackofficeapigateway.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.tungbt.apigateway.configurations.ApplicationConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PathMapUtilityTest {

    @Autowired
    ApplicationConfiguration applicationConfiguration;

    @Test
    public void create_pathmap_with_not_exist_key_in_in_config(){
        //no throw exception
        //new PathMapUtility(applicationConfiguration, "Nope_key");

    }
}