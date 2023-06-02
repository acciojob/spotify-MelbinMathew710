package com.driver;

public class ArtistNotFoundException extends RuntimeException{

    public ArtistNotFoundException(){
        super("Artist does not Exist");
    }

}
