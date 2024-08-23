package com.example.movie_service.integration.generator;

import com.example.movie_service.entity.Movie;
import com.example.movie_service.entity.Person;
import com.example.movie_service.junitExtension.MySQLTestContainerExtension;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.example.movie_service.constant.MovieConstant.MOVIE_ID_PREFIX;
import static com.example.movie_service.constant.MovieConstant.PERSON_ID_PREFIX;
import static com.example.movie_service.constants.TestConstant.MOVIE_1_TITLE;
import static com.example.movie_service.constants.TestConstant.MOVIE_2_TITLE;
import static com.example.movie_service.constants.TestConstant.MOVIE_3_TITLE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test") // Explicitly specify to use the configuration in the application-test.properties
@ExtendWith(MySQLTestContainerExtension.class)
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomIdGeneratorTest {

    @PersistenceContext
    private EntityManager entityManager;

    // @Transactional annotation in tests will automatically roll back the transaction at the end of the test method.
    // This means that any data persisted during the test will not be committed to the database and will be discarded
    // when the test finishes.
    @Transactional
    @Test
    void testMovieIdStartsWithPrefix_tt() {
        Movie movie = new Movie();
        movie.setTitle(MOVIE_1_TITLE);
        entityManager.persist(movie);

        // Make sure the id starts with tt
        assertTrue(movie.getId().startsWith(MOVIE_ID_PREFIX));

        // Assert the numeric part is 1 because there was no Movie entity in the table before.
        String numericPart = movie.getId().substring(2);
        assertDoesNotThrow(()-> Integer.parseInt(numericPart));
        Integer number = Integer.parseInt(numericPart);
        assertEquals(1, number);
    }

    @Transactional
    @Test
    void testMovieIdHaveAscendingNumeric() {
        Movie movie1 = new Movie();
        movie1.setTitle(MOVIE_1_TITLE);
        entityManager.persist(movie1);

        Movie movie2 = new Movie();
        movie2.setTitle(MOVIE_2_TITLE);
        entityManager.persist(movie2);

        Movie movie3 = new Movie();
        movie3.setTitle(MOVIE_3_TITLE);
        entityManager.persist(movie3);

        // Make sure the ids all start with tt
        assertTrue(movie1.getId().startsWith(MOVIE_ID_PREFIX));
        assertTrue(movie2.getId().startsWith(MOVIE_ID_PREFIX));
        assertTrue(movie3.getId().startsWith(MOVIE_ID_PREFIX));

        // Assert the movie1's numeric part is 1 because there was no Movie entity in the table before.
        String numericPart = movie1.getId().substring(2);
        assertDoesNotThrow(()-> Integer.parseInt(numericPart));
        Integer number = Integer.parseInt(numericPart);
        assertEquals(1, number);

        // Assert the movie2's numeric part is 2 because there is 1 Movie entity in the table already.
        String numericPart2 = movie2.getId().substring(2);
        assertDoesNotThrow(()-> Integer.parseInt(numericPart2));
        Integer number2 = Integer.parseInt(numericPart2);
        assertEquals(2, number2);

        // Assert the movie1's numeric part is 3 because there are 2 Movie entities in the table already,
        // and the biggest numeric number is 2.
        String numericPart3 = movie3.getId().substring(2);
        assertDoesNotThrow(()-> Integer.parseInt(numericPart3));
        Integer number3 = Integer.parseInt(numericPart3);
        assertEquals(3, number3);
    }

    @Transactional
    @Test
    void testPersonIdStartsWithPrefix_nm() {
        Person person = new Person();
        entityManager.persist(person);

        // Make sure the id starts with PERSON_ID_PREFIX
        assertTrue(person.getId().startsWith(PERSON_ID_PREFIX));

        // Assert the numeric part is 1 because there was no Person entity in the table before.
        String numericPart = person.getId().substring(2);
        assertDoesNotThrow(()-> Integer.parseInt(numericPart));
        Integer number = Integer.parseInt(numericPart);
        assertEquals(1, number);
    }

    @Transactional
    @Test
    void testPersonIdNumericPartIsAscending() {
        Person person1 = new Person();
        entityManager.persist(person1);

        // Make sure the id starts with PERSON_ID_PREFIX
        assertTrue(person1.getId().startsWith(PERSON_ID_PREFIX));

        // Assert the numeric part is 1 because there was no Person entity in the table before.
        String numericPart = person1.getId().substring(2);
        assertDoesNotThrow(()-> Integer.parseInt(numericPart));
        Integer number = Integer.parseInt(numericPart);
        assertEquals(1, number);

        // Generate a 2nd Person
        Person person2 = new Person();
        entityManager.persist(person2);

        // Make sure the id starts with PERSON_ID_PREFIX
        assertTrue(person2.getId().startsWith(PERSON_ID_PREFIX));

        // Assert the numeric part is 2 because there is one Person in the table and its id numeric part is 1, which is
        // also the biggest one in the table.
        String numericPart2 = person2.getId().substring(2);
        assertDoesNotThrow(()-> Integer.parseInt(numericPart2));
        Integer number2 = Integer.parseInt(numericPart2);
        assertEquals(2, number2);

        // Generate a 3rd Person
        Person person3 = new Person();
        entityManager.persist(person3);

        // Make sure the id starts with PERSON_ID_PREFIX
        assertTrue(person3.getId().startsWith(PERSON_ID_PREFIX));

        // Assert the numeric part is 3 because there are 2 Person in the table already,
        // and the biggest numeric part is 2, so the new Person's ID's numeric part will be 2+1 = 3
        String numericPart3 = person3.getId().substring(2);
        assertDoesNotThrow(()-> Integer.parseInt(numericPart3));
        Integer number3 = Integer.parseInt(numericPart3);
        assertEquals(3, number3);
    }
}
