package com.izen;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = {"/clean_schema.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class BaseIntegrationTest {
}
