package com.driver;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class SpotifyService {

    //Auto-wire will not work in this case, no need to change this and add autowire

    SpotifyRepository spotifyRepository = new SpotifyRepository();

    public static boolean isAlbum(String albumName) {
        Album album = new Album(albumName) ;
        boolean ans = SpotifyRepository.isAlbum(album) ;
        return ans ;
    }

    public User createUser(String name, String mobile){
        User user = spotifyRepository.createUser(name, mobile) ;
        return user ;
    }

    public Artist createArtist(String name) {
        Artist artist = spotifyRepository.createArtist(name) ;
        return artist ;
    }

    public Album createAlbum(String title, String artistName) {
        Artist artist = new Artist(artistName) ;
        if(!spotifyRepository.artists.contains(artist)){
            spotifyRepository.artists.add(artist) ;
        }
        Album album = spotifyRepository.createAlbum(title) ;
        return album ;
    }

    public Song createSong(String title, String albumName, int length) throws Exception {

        Album album = new Album(albumName) ;
        if(!SpotifyRepository.isAlbum(album)){
            throw new AlbumDoesNotExistException() ;
        }

        Song song = spotifyRepository.createSong(title, albumName, length) ;
        return song ;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {

        Playlist playlist = new Playlist(title) ;

        List<Song> songs = spotifyRepository.songs ;
        HashMap<Playlist, List<Song>> songList = spotifyRepository.playlistSongMap ;

        for(Song song : songs){
            if(song.getLength() == length){
                if(!songList.containsKey(playlist)){
                    songList.put(playlist, new ArrayList<>()) ;
                }
                songList.get(playlist).add(song) ;
            }
        }

        User res = findUser(mobile) ;

        if(res == null){
            throw new UserDoesNotExistException() ;
        }
        spotifyRepository.creatorPlaylistMap.put(res,playlist) ;

        List<User> user2 = new ArrayList<>() ;
        user2.add(res) ;
        spotifyRepository.playlistListenerMap.put(playlist,user2) ;

        return playlist ;

    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {

        Playlist playlist = new Playlist(title) ;

        List<Song> songs = spotifyRepository.songs ;
        HashMap<Playlist, List<Song>> songList = spotifyRepository.playlistSongMap ;

        for(Song song : songs){
            if(song.getTitle().equals(title)){
                if(!songList.containsKey(playlist)){
                    songList.put(playlist, new ArrayList<>()) ;
                }
                songList.get(playlist).add(song) ;
            }
        }

        User res = findUser(mobile) ;

        if(res == null){
            throw new UserDoesNotExistException() ;
        }
        spotifyRepository.creatorPlaylistMap.put(res,playlist) ;

        List<User> user2 = new ArrayList<>() ;
        user2.add(res) ;
        spotifyRepository.playlistListenerMap.put(playlist,user2) ;

        return playlist ;

    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {

        User res = findUser(mobile) ;
        boolean flag1 = false ;

        // checking users
        for( Playlist p : spotifyRepository.playlistListenerMap.keySet() ){
            for(User u : spotifyRepository.playlistListenerMap.get(p) ){
                if(u.getMobile().equals(mobile)){
                    flag1  = true ;
                    break ;
                }
            }
        }


        // checking creator
        boolean flag2 = false ;
        Playlist p1 = null ;
        for(Playlist p : spotifyRepository.playlistListenerMap.keySet()){
            if(p.getTitle().equals(playlistTitle)) {
                p1 = p ;
                flag2 = true ;
                break ;
            }
        }

        if(!checkPlayLists(playlistTitle) ){
            throw new PlayListDoesNotExistException() ;
        }

        if(!flag1 && !flag2 && !spotifyRepository.creatorPlaylistMap.containsKey(res)){
            spotifyRepository.playlistListenerMap.get(p1).add(res) ;
        }

        return p1 ;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {

        User u = findUser(mobile) ;
        Song s = checkSongs(songTitle) ;

        if(!spotifyRepository.songLikeMap.containsKey(s)){
            spotifyRepository.songLikeMap.put(s,new ArrayList<>()) ;
        }

        if(!spotifyRepository.songLikeMap.get(s).contains(u)){
            spotifyRepository.songLikeMap.get(s).add(u) ;
        }


//        for(User u1 : spotifyRepository.creatorPlaylistMap.keySet() ){
//            for(Song s1 : spotifyRepository.creatorPlaylistMap.get(u1)){
//
//            }
//        }

        return s ;
    }

    public String mostPopularArtist() {
        return "" ;
    }

    public String mostPopularSong() {
        int max = 0 ;
        Song result = null ;
        for (Song s : spotifyRepository.songLikeMap.keySet()) {
            if (spotifyRepository.songLikeMap.get(s).size() > max) {
                max = spotifyRepository.songLikeMap.get(s).size();
                result = s;
            }
        }
        return "most popuar song is:" + result ;
    }

    public boolean hasArtist(String artistName) {
        Artist artist = new Artist(artistName) ;
        boolean ans = spotifyRepository.hasArtist(artist) ;
        return ans ;
    }

    public User findUser(String mobile){

            User creator = null ;
            boolean flag = false ;
            for(User user : spotifyRepository.users){
                if(user.getMobile().equals(mobile)){
                    creator = user ;
                    flag = true ;
                    return user ;
                }
            }
            throw new UserDoesNotExistException() ;

        }

    public boolean checkPlayLists(String playlistTitle) {

        for(Playlist p : spotifyRepository.playlists){
            if(p.getTitle().equals(playlistTitle)) return true ;
        }
        return false ;
    }

    public Song checkSongs(String songTitle) {

        for(Song s : spotifyRepository.songs){
            if(s.getTitle().equals(songTitle)) return s ;
        }
        throw new SongDoesNotExistException() ;
    }


}
