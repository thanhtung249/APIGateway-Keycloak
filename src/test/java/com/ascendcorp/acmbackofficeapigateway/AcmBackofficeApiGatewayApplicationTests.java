package com.ascendcorp.acmbackofficeapigateway;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.tungbt.apigateway.AcmBackofficeApiGatewayApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcmBackofficeApiGatewayApplicationTests {

	@Test
	@Ignore
	public void contextLoads() {
		AcmBackofficeApiGatewayApplication.main(new String[] {"arg1"});
	}

}
