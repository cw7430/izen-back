package com.izen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.izen.auth.AuthTestUtil;
import com.izen.module.auth.dto.request.LoginRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public abstract class BaseIntegrationTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected AuthTestUtil authTestUtil;
    protected final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    protected static final String BASE_URL = "/api/v1";
    protected static final LoginRequestDto defaultLoginData = new LoginRequestDto(
            "EMP003",
            "EMP003",
            false
    );

    private RequestBuilder request(HttpMethod method, String url) {
        return new RequestBuilder(method, url);
    }

    protected RequestBuilder get(String url) {
        return request(HttpMethod.GET, url);
    }

    protected RequestBuilder post(String url) {
        return request(HttpMethod.POST, url);
    }

    protected RequestBuilder put(String url) {
        return request(HttpMethod.PUT, url);
    }

    protected RequestBuilder patch(String url) {
        return request(HttpMethod.PATCH, url);
    }

    protected RequestBuilder delete(String url) {
        return request(HttpMethod.DELETE, url);
    }

    public class RequestBuilder {
        private final MockHttpServletRequestBuilder requestBuilder;

        public RequestBuilder(HttpMethod method, String url) {
            this.requestBuilder = MockMvcRequestBuilders.request(method, url)
                    .contentType(MediaType.APPLICATION_JSON);
        }

        public RequestBuilder key() {
            this.requestBuilder.header("X-API-Key", authTestUtil.getTestApiKey());
            return this;
        }

        public RequestBuilder auth(String token) {
            if (token != null) {
                this.requestBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            }
            return this;
        }

        public RequestBuilder body(Object body) throws Exception {
            if (body != null) {
                this.requestBuilder.content(objectMapper.writeValueAsString(body));
            }
            return this;
        }

        public ResultActions send() throws Exception {
            return mockMvc.perform(this.requestBuilder).andDo(print());
        }
    }
}
