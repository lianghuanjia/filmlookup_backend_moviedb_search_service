package com.example.movie_service.integrationTest.mainApplication;

import com.example.movie_service.MovieServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = MovieServiceApplication.class)
class MovieServiceApplicationIntegrationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void testSpringApplicationRun() {
        {
            MovieServiceApplication.main(new String[]{
                    "--spring.main.web-environment=false",
            });
            assertNotNull(context);
        }
    }
}
