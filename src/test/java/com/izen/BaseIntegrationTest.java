package com.izen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.izen.auth.AuthTestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public abstract class BaseIntegrationTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected AuthTestUtil authTestUtil;
    protected final ObjectMapper objectMapper = new ObjectMapper();

}
