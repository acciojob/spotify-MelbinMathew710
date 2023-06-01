package com.driver;

public class PlayListDoesNotExistException extends RuntimeException{

    public PlayListDoesNotExistException(){
        super("PlayList does not exist") ;
    }

}
