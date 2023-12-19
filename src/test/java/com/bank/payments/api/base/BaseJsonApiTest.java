package com.bank.payments.api.base;

import com.bank.payments.api.thirdparty.exception.BankRepositoryException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@WebAppConfiguration
public class BaseJsonApiTest {

  protected MockMvc mvc;
  @Autowired
  private WebApplicationContext webApplicationContext;

  @BeforeEach
  public void setUp() throws BankRepositoryException {
    mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  protected <T> T makeApiCall(final String jsonRequest, String endpoint, Class<T> clazz)
      throws Exception {
    final MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(endpoint)
        .contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonRequest)).andReturn();

    final String jsonResponse = mvcResult.getResponse().getContentAsString();
    return mapFromJson(jsonResponse, clazz);
  }

  protected String mapToJson(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(obj);
  }

  protected <T> T mapFromJson(String json, Class<T> clazz)
      throws JsonParseException, JsonMappingException, IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(json, clazz);
  }

}
