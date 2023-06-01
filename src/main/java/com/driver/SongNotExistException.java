package com.driver;

public class SongNotExistException extends RuntimeException {

    public SongDoesNotExistException(){
        super("Song does not exist") ;
    }

}
