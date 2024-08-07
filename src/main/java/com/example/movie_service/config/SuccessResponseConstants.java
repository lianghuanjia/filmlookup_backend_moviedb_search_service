package com.example.movie_service.config;

public class SuccessResponseConstants {
    public static class MovieFound {
        public static final int CODE = 20001;
        public static final String MESSAGE = "Movie(s) found";
    }

    public static class MovieNotFound {
        public static final int CODE = 20002;
        public static final String MESSAGE = "Movie(s) not found";
    }

    public static class MovieFoundWithPersonId {
        public static final int CODE = 20003;
        public static final String MESSAGE = "Movie(s) found with personId";
    }

    public static class MovieNotFoundWithPersonId {
        public static final int CODE = 20004;
        public static final String MESSAGE = "Movie(s) not found with personId";
    }
}
