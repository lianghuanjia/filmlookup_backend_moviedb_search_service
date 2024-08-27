package com.example.movie_service.constant;

/**
 * Put all constants that are used in the movie_service (exclude the test folder) in this file <br>
 *
 * Why using constants:
 * 1. Creating String is expensive. We can create one String here and reuse them elsewhere.
 * 2. Constants are helpful because when we write test, we can use those reference as expected values.
 */
public final class MovieConstant {

    // Define a private constructor to hide the public one
    private MovieConstant() {
    }

    // ID PREFIX:
    public static final String MOVIE_ID_PREFIX = "tt";
    public static final String PERSON_ID_PREFIX = "nm";

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
    public static final int INVALID_TITLE_CODE = 40006;
    public static final String INVALID_TITLE_MESSAGE = "Invalid title. Title cannot be null or an empty string";
    public static final int INVALID_MOVIE_ID_CODE = 40007;
    public static final String INVALID_MOVIE_ID_MESSAGE = "Invalid movie id. Movie id cannot be null or an empty string";
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

    // SQL REQUEST DTO MAPPING
    public static final String MOVIE_SEARCH_RESULT_DTO_MAPPING = "MovieSearchResultDTOMapping";
    public static final String SINGLE_MOVIE_BASIC_DETAILS_DTO_MAPPING = "SingleMovieBasicDetailsDTOMapping";
    public static final String SINGLE_MOVIE_CREW_MEMBER_DTO_MAPPING = "SingleMovieCrewMemberDTOMapping";

    // MISS PARAMETER MAP
    public static final String TITLE = "title";
    public static final int MISSING_REQUIRED_PARAMETER_CODE = 40000;
    public static final String MISSING_REQUIRED_PARAMETER_MESSAGE_PREFIX = "Missing required parameter: ";
}
