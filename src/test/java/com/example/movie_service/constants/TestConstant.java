package com.example.movie_service.constants;

public class TestConstant {
    // Test container's MySQL version
    public static final String SQL_VERSION = "mysql:8.0.39";

    public static final String EXISTED_MOVIE_TITLE_LOWER_CASE = "movie";
    public static final String EXISTED_MOVIE_TITLE_CAPITALIZED = "Movie";
    public static final String EXISTED_MOVIE_TITLE_UPPER_CASE = "MOVIE";
    public static final String NON_EXISTED_MOVIE_TITLE = "Miss Jerry";
    public static final String MOVIE_TITLE_FOR_RETURN_RESULTS_NOT_INCLUDING_POSTER_PATH = "poster_path_testing_title";
    public static final String ORDER_BY_TITLE = "title";
    public static final String RELEASED_YEAR = "releasedYear";
    public static final String ORDER_BY = "orderBy";
    public static final String RELEASE_TIME = "releaseTime";
    public static final String DIRECTION = "direction";
    public static final String RATING = "rating";
    public static final String DESC = "desc";
    public static final String ASC = "asc";
    public static final String PAGE = "page";
    public static final String THE_DARK_KNIGHT = "The Dark Knight";
    public static final String THE_DARK_KNIGHT_OVERVIEW = "The Dark Knight's Overview";
    public static final String THE_DARK_KNIGHT_RELEASE_TIME = "2008-05-08";
    public static final String THE_DARK_KNIGHT_RISES = "The Dark Knight Rises";
    public static final String THE_DARK_KNIGHT_RISES_RELEASE_TIME = "2012-03-09";
    public static final String THE_DARK_KNIGHT_RISES_AGAIN = "The Dark Knight Rises Again";
    public static final String THE_DARK_KNIGHT_RISES_AGAIN_RELEASE_TIME = "2012-08-09";
    public static final String YEAR_2012 = "2012";
    public static final String INVALID_YEAR_2030 = "2030";
    public static final String DIRECTOR_NOLAN = "Christopher Nolan";
    public static final String DIRECTOR_NOLAN_PROFILE_PATH = "NolanProfilePath";
    public static final String EMPTY_STRING = "";
    public static final String DIRECTOR_ROLE = "director";
    public static final String BACKDROP_PATH = "backdrop_path";
    public static final String POSTER_PATH = "poster_path";

    // Constants to set up test container database

    // Movie titles
    public static final String TITLE_STARTS_WITH_MOVIE = "Movie";
    public static final String MOVIE_1_TITLE = "Movie1";
    public static final String MOVIE_2_TITLE = "Movie2";
    public static final String MOVIE_3_TITLE = "Movie3";
    public static final String MOVIE_4_TITLE = "Movie4";
    public static final String MOVIE_5_TITLE = "Movie5";
    public static final String MOVIE_6_TITLE = "Movie6";
    public static final String MOVIE_7_TITLE = "Movie7";
    public static final String MOVIE_8_TITLE = "Movie8";
    public static final String MOVIE_9_TITLE = "Movie9";
    public static final String MOVIE_10_TITLE = "Movie10";
    public static final String MOVIE_11_TITLE = "Movie11";
    public static final String MOVIE_12_TITLE = "Movie12";
    public static final String MOVIE_13_TITLE = "Movie13";
    public static final String MOVIE_WITH_TITLE_ONLY = "Title_only";

    // Movie releaseTime
    public static final String MOVIE_1_RELEASE_TIME = "2023-10-08";
    public static final String MOVIE_2_RELEASE_TIME = "2023-09-08";
    public static final String MOVIE_3_RELEASE_TIME = "2023-09-08";
    public static final String MOVIE_4_RELEASE_TIME = "2023-07-08";
    public static final String MOVIE_5_RELEASE_TIME = "2023";
    public static final String MOVIE_6_RELEASE_TIME = "2022-01-08";
    public static final String MOVIE_7_RELEASE_TIME = "2021";
    public static final String MOVIE_8_RELEASE_TIME = "2020";
    public static final String MOVIE_9_RELEASE_TIME = "2019";
    public static final String MOVIE_10_RELEASE_TIME = "2018";
    public static final String MOVIE_11_RELEASE_TIME = "2017";
    public static final String MOVIE_12_RELEASE_TIME = null;
    public static final String MOVIE_13_RELEASE_TIME = null;

    // Movie Genres
    public static final String ACTION_GENRE = "Action";
    public static final String CRIME_GENRE = "Crime";
    public static final String LOVE_GENRE = "Love";

    // Directors
    public static final String DIRECTOR_1 = "Director1";
    public static final String DIRECTOR_2 = "Director2";
    public static final String DIRECTOR_3 = "Director3";
    public static final String DIRECTOR_4 = "Director4";

    // Jobs
    public static final String ACTOR = "actor";
    public static final String ACTRESS = "actress";
    public static final String COMPOSER = "composer";

    // Actor1
    public static final String ACTOR_1_NAME = "Actor1";
    public static final String ACTOR_1_PROFILE_PATH = "Actor1ProfilePath";
    // Actress
    public static final String ACTRESS_1_NAME = "Actress1";
    public static final String ACTRESS_1_PROFILE_PATH = "Actress1ProfilePath";

    // Composer
    public static final String COMPOSER_1_NAME = "Composer1";
    public static final String COMPOSER_1_PROFILE_PATH = "Composer1ProfilePath";


    // Ratings
    public static final Double MOVIE_1_RATING = 0.1;
    public static final Double MOVIE_2_RATING = 0.2;
    public static final Double MOVIE_3_RATING = 0.2;
    public static final Double MOVIE_4_RATING = 0.4;
    public static final Double MOVIE_5_RATING = 0.5;
    public static final Double MOVIE_6_RATING = 0.6;
    public static final Double MOVIE_7_RATING = 0.7;
    public static final Double MOVIE_8_RATING = 0.8;
    public static final Double MOVIE_9_RATING = 0.9;
    public static final Double MOVIE_10_RATING = 1.0;
    public static final Double MOVIE_11_RATING = 1.1;
    public static final Double MOVIE_12_RATING = 1.2;
    public static final Double MOVIE_13_RATING = 1.3;

    // Number of votes
    public static final Integer NUM_OF_VOTES_10 = 10;
    public static final Integer NUM_OF_VOTES_15 = 15;

    public static final String MOVIE_MATERIALIZED_VIEW_QUERY_STRING = "CREATE TABLE movie_materialized_view AS "
            + "SELECT m.movie_id AS movie_id, "
            + "m.primaryTitle AS primaryTitle, "
            + "m.releaseTime AS releaseTime, "
            + "GROUP_CONCAT(DISTINCT p.name ORDER BY p.name SEPARATOR ', ') AS directors, "
            + "m.backdrop_path AS backdrop_path, "
            + "m.poster_path AS poster_path, "
            + "mr.averageRating AS averageRating, "
            + "m.overview AS overview, "
            + "GROUP_CONCAT(DISTINCT g.name ORDER BY g.name SEPARATOR ', ') AS genres "
            + "FROM movie m "
            + "LEFT JOIN movie_crew mc ON m.movie_id = mc.movie_id "
            + "LEFT JOIN person p ON mc.person_id = p.person_id AND mc.job = 'director' "
            + "LEFT JOIN movie_genres mg ON m.movie_id = mg.movie_id "
            + "LEFT JOIN genre g ON mg.genre_id = g.id "
            + "LEFT JOIN movie_rating mr ON m.movie_id = mr.movie_id "
            + "GROUP BY m.movie_id, m.primaryTitle, m.releaseTime, m.backdrop_path, m.poster_path, mr.averageRating, m.overview;";

    public static final String DROP_MOVIE_MATERIALIZED_VIEW_QUERY_STRING = "DROP TABLE IF EXISTS movie_materialized_view;";
}
