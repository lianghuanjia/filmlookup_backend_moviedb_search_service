package com.example.movie_service.moviesearch.integration.mainApplication;

import com.example.movie_service.SearchServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = SearchServiceApplication.class)
class SearchServiceApplicationIntegrationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void testSpringApplicationRun() {
        {
            SearchServiceApplication.main(new String[]{
                    "--spring.main.web-environment=false",
            });
            assertNotNull(context);
        }
    }
}
