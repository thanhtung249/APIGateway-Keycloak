package com.ascendcorp.acmbackofficeapigateway.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.tungbt.apigateway.controllers.ErrorControllerImp;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(ErrorControllerImp.class)
@RunWith(SpringRunner.class)
public class ErrorControllerImpTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testErrorControllerWithRequestAttr() throws Exception {
        mockMvc.perform(get("/error")
                .requestAttr("javax.servlet.error.status_code",404)
                .requestAttr("javax.servlet.error.exception","hello"))
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("hello")));
    }

    @Test
    public void testErrorControllerWithRequestAttr_unAuthorized() throws Exception {
        mockMvc.perform(get("/error")
                .requestAttr("javax.servlet.error.status_code",HttpStatus.UNAUTHORIZED.value())
                .requestAttr("javax.servlet.error.exception",HttpStatus.UNAUTHORIZED.getReasonPhrase()))
                .andExpect(jsonPath("$.code", is(401)))
                .andExpect(jsonPath("$.message", is("Unauthorized")));
    }

    @Test
    public void testErrorController() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(jsonPath("$.code", is(HttpStatus.INTERNAL_SERVER_ERROR.value())))
                .andExpect(jsonPath("$.message", is("Unexpected error occurred")));;
    }

}