package com.driver;

public class AlbumDoesNotExistException extends RuntimeException{
    public AlbumDoesNotExistException(){
        super("Album does not exist") ;
    }
}
