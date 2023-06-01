package com.driver;

public class SongDoesNotExistException extends RuntimeException {

    public SongDoesNotExistException(){
        super("Song does not exist") ;
    }

}
