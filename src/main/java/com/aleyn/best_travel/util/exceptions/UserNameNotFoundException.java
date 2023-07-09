package com.aleyn.best_travel.util.exceptions;

public class UserNameNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "User no exist in %s";

    public UserNameNotFoundException(String tableName) {
        super(String.format(ERROR_MESSAGE, tableName));
    }

}
