package com.driver;

public class UserDoesNotExistException extends RuntimeException {

    public UserDoesNotExistException(){
        super("User does not exist") ;
    }

}
