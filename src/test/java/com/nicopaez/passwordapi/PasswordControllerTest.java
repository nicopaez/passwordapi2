package com.nicopaez.passwordapi;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PasswordControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getPassword() throws Exception {
        MvcResult result =mvc.perform(MockMvcRequestBuilders.get("/password")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String generatedPassword = JsonPath.read(result.getResponse().getContentAsString(), "$.password");
        Assert.assertThat(generatedPassword.length(), is(20));
    }

    @Test
    public void getHash() throws Exception {
        MvcResult result =mvc.perform(MockMvcRequestBuilders.get("/hash?password=myPlainPassword!1")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String hashedPassword = JsonPath.read(result.getResponse().getContentAsString(), "$.hash");
        Assert.assertThat(hashedPassword.length(), is(60));
    }

    @Test
    public void getValid() throws Exception {
        MvcResult result =mvc.perform(MockMvcRequestBuilders.get("/valid?password=myPlainPassword!1")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        boolean isValid = JsonPath.read(result.getResponse().getContentAsString(), "$.isValid");
        Assert.assertTrue(isValid);
    }

    @Test
    public void getCheckMatch() throws Exception {
        MvcResult result =mvc.perform(MockMvcRequestBuilders.get("/check-match?password=myPlainPassword!1&hash=$2a$12$WQJJ357DDAeokBgOTfOiT.jKmV.TLziIYAyRl6Ndfpm2tsObs7ORC")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        boolean matched = JsonPath.read(result.getResponse().getContentAsString(), "$.matched");
        Assert.assertTrue(matched);
    }

}
