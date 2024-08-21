package com.example.movie_service.constant;

// This is helpful because when you write test, you can use those reference too.
public final class MovieConstant {

    // Define a private constructor to hide the public one
    private MovieConstant() {
    }

    // ERROR CODES AND MESSAGES:
    public static final int INVALID_YEAR_CODE = 40001;
    public static final String INVALID_YEAR_MESSAGE = "Invalid year";
    public static final int INVALID_LIMIT_CODE = 40002;
    public static final String INVALID_LIMIT_MESSAGE = "Invalid limit";
    public static final int INVALID_PAGE_CODE = 40003;
    public static final String INVALID_PAGE_MESSAGE = "Invalid page";
    public static final int INVALID_ORDER_BY_CODE = 40004;
    public static final String INVALID_ORDER_BY_MESSAGE = "Invalid order by";
    public static final int INVALID_DIRECTION_CODE = 40005;
    public static final String INVALID_DIRECTION_MESSAGE = "Invalid direction";
    public static final int MISSING_TITLE_CODE = 40006;
    public static final String MISSING_TITLE_MESSAGE = "Missing title";
    public static final int UNAUTHORIZED_CODE = 40101;
    public static final String UNAUTHORIZED_MESSAGE = "Unauthorized request";
    public static final int INVALID_CREDENTIAL_CODE = 40102;
    public static final String INVALID_CREDENTIAL_MESSAGE = "Invalid credential";
    public static final int FAILED_AUTHORIZATION_CODE = 40103;
    public static final String FAILED_AUTHORIZATION_MESSAGE = "Authorization failed";
    public static final int NO_PERMISSION_CODE = 40301;
    public static final String NO_PERMISSION_MESSAGE = "No permission to access this endpoint";
    public static final int PERSON_ID_NOT_FOUND_CODE = 40401;
    public static final String PERSON_ID_NOT_FOUND_MESSAGE = "PersonId not found";

    public static final int INTERNAL_SERVICE_ERROR_CODE = 500;

    // SUCCESS CODE AND MESSAGE
    public static final int MOVIE_FOUND_CODE = 20001;
    public static final String MOVIE_FOUND_MESSAGE = "Movie(s) found";
    public static final String MOVIE_FOUND = "MoviesFound";

    public static final int MOVIE_NOT_FOUND_CODE = 20002;
    public static final String MOVIE_NOT_FOUND_MESSAGE = "No movies found with the given search parameters";

    public static final int MOVIE_FOUND_WITH_PERSON_ID_CODE = 20003;
    public static final String MOVIE_FOUND_WITH_PERSON_ID_MESSAGE = "Movie(s) found with personId";
    public static final int MOVIE_NOT_FOUND_WITH_PERSON_ID_CODE = 20004;
    public static final String MOVIE_NOT_FOUND_WITH_PERSON_ID_MESSAGE = "No movies found with the given personId";

    public static final String MOVIE_SEARCH_RESULT_DTO_MAPPING = "MovieSearchResultDTOMapping";

    // MISS PARAMETER MAP
    public static final String TITLE = "title";
    public static final int MISSING_REQUIRED_PARAMETER_CODE = 40000;
    public static final String MISSING_REQUIRED_PARAMETER_MESSAGE_PREFIX = "Missing required parameter: ";
}
