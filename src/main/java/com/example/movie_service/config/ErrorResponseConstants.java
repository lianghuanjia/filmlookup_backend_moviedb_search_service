package com.example.movie_service.config;

public class ErrorResponseConstants {
    public static class InvalidYear {
        public static final int CODE = 40001;
        public static final String MESSAGE = "Invalid year";
    }

    public static class InvalidLimit {
        public static final int CODE = 40002;
        public static final String MESSAGE = "Invalid limit";
    }

    public static class InvalidPage {
        public static final int CODE = 40003;
        public static final String MESSAGE = "Invalid page";
    }

    public static class InvalidOrderBy {
        public static final int CODE = 40004;
        public static final String MESSAGE = "Invalid orderBy";
    }

    public static class InvalidDirection {
        public static final int CODE = 40005;
        public static final String MESSAGE = "Invalid direction";
    }

    public static class MissingTitle {
        public static final int CODE = 40006;
        public static final String MESSAGE = "Missing title";
    }

    public static class Unauthorized {
        public static final int CODE = 40101;
        public static final String MESSAGE = "Unauthorized request";
    }

    public static class InvalidCredential {
        public static final int CODE = 40102;
        public static final String MESSAGE = "Invalid credential";
    }

    public static class FailedAuthorization {
        public static final int CODE = 40103;
        public static final String MESSAGE = "Authorization failed";
    }

    public static class NoPermission {
        public static final int CODE = 40301;
        public static final String MESSAGE = "No permission to access this endpoint";
    }

    public static class PersonIdNotFound {
        public static final int CODE = 404;
        public static final String MESSAGE = "PersonId not found";
    }
}