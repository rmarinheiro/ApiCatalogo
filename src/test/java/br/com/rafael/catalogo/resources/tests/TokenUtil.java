package br.com.rafael.catalogo.resources.tests;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class TokenUtil {

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    @Value("${operatorUsername:alex@gmail.com}")
    private String operatorUsername;
    @Value("${operatorPassword:123456}")
    private String operatorPassword;
    @Value("${adminUsername:maria@gmail.com}")
    private String adminUsername;
    @Value("${operatorPassword:123456}")
    private String adminPassword;

    public String obtainAccessToken(MockMvc mockMvc, String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", clientId);
        params.add("username", username);
        params.add("password", password);

        ResultActions result = mockMvc
                .perform(post("/oauth/token")
                        .params(params)
                        .with(httpBasic(clientId, clientSecret))
                        .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }

    public MockHttpServletRequestBuilder authenticatedRequestAdmin(MockMvc mocMVC, MockHttpServletRequestBuilder request) throws Exception {
        return authenticatedRequest(mocMVC, adminUsername, adminPassword, request );
    }

    public MockHttpServletRequestBuilder authenticatedRequestOperator(MockMvc mocMVC, MockHttpServletRequestBuilder request) throws Exception {
        return authenticatedRequest(mocMVC, operatorUsername, operatorPassword, request );
    }
    public MockHttpServletRequestBuilder authenticatedRequest(MockMvc mocMVC, MockHttpServletRequestBuilder request) throws Exception {

        return authenticatedRequestAdmin(mocMVC,request);

    }
    public MockHttpServletRequestBuilder authenticatedRequest(MockMvc mocMVC, String operatorUsername, String operatorPassword, MockHttpServletRequestBuilder request) throws Exception {
        String accessToken = obtainAccessToken(mocMVC, operatorUsername, operatorPassword);
        System.out.println(accessToken);
        return request
                .header("Authorization", "Bearer " + accessToken);
    }
}
