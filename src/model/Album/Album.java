package model.Album;

import model.SoundClip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Album implements AlbumInterface{

    // TODO: preconditions!!!

    // instance variables for this class
    String albumName;
    List<Album> subAlbums;
    List<SoundClip> songs;

    public Album(final String albumName){
        assert albumName != null; // precondition
        // constructor
        this.albumName = albumName;
        this.subAlbums = new ArrayList<Album>() {};
        this.songs = new ArrayList<SoundClip>();
    }

    public boolean isSearchBasedAlbum(){
        return false;
    }

    //  methods that all albums should have below:
    @Override
    public boolean add(final SoundClip clip){
        // adds a song to this album if it doesn't contain it
        assert clip != null;  // precondition
        if (!contains(clip)){
            this.songs.add(clip);
        }
        assert invariant();
        // returns true for testing purposes
        return true;
    }
    @Override
    public boolean add(Album album){
        // adds an album to the list of sub-albums, returns boolean for testing purposes
        assert album != null;  // precondition
        if (!contains(album)){
            this.subAlbums.add(album);
            assert invariant();
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(final SoundClip clip){
        // removes a song from this album and all sub-albums, returns boolean for testing purposes
        assert clip != null;  // precondition
        if (contains(clip)){
            this.songs.remove(clip);
            for (Album album: this.subAlbums){
                album.remove(clip);
            }
            assert invariant();
            return true;
        }
        return false;
    }
    @Override
    public boolean remove(final Album album){
        // removes an album for list of sub-albums, returns boolean for testing purposes
        assert album != null;  // precondition
        if (contains(album)){
            this.subAlbums.remove(album);
            assert invariant();
            return true;
        }
        return false;
    }
    @Override
    public boolean contains(final Album album){
        // checks if this objects contains an album
        assert album != null;  // precondition
        return this.subAlbums.contains(album);
    }

    @Override
    public boolean contains(final SoundClip clip){
        // checks if this objects contains a song
        assert clip != null;  // precondition
        return this.songs.contains(clip);
    }

    public void setAlbumName(final String albumName){
        // sets a new album name
        assert albumName != null;  // precondition

        this.albumName = albumName;

        assert invariant();
    }

    public SoundClip getSong(final int i){
        assert i >= 0;  // precondition, don't allow negative numbers
        // returns song at index i
        return this.songs.get(i);
    }

    public Album getSubAlbum(final int i){
        assert i >= 0;  // precondition, don't allow negative numbers
        // returns album at index i
        return this.subAlbums.get(i);
    }

    public List<Album> getSubAlbums(){
        // returns an unmodifiable list of this object's sub-albums
        return this.subAlbums;
    }

    public List<SoundClip> getSoundClips(){
        // returns an unmodifiable list of this object's songs
        return this.songs;
    }

    public boolean invariant(){  // invariant method for album
        for (Album album: this.subAlbums){
            if (!album.invariant()){
                return false;
            }
        }
        return this.albumName != null;
    }

    public abstract boolean isRootAlbum();  // method for checking if subclass is a root album

    public abstract Album getParentAlbum(); // method for getting albums parent album

    @Override
    public boolean equals(final Object other){
        // overriding object's equals method, which can be useful in some cases like sorting
        if (this.getClass() != other.getClass()){
            return false;
        }
        return this.hashCode() == other.hashCode();
    }

    @Override
    public int hashCode(){
        // returns unique hashCode based on instance variables
        assert invariant();
        int hash = 7;
        int prime = 31;
        hash = prime * hash + this.albumName.hashCode();
        hash = prime * hash + this.subAlbums.size();
        hash = prime * hash + this.songs.size();
        return hash;
    }

    @Override
    public String toString(){
        // overrides the toString method which can be useful for debugging purposes,
        // and also displaying the album name without needing to call "getAlbumName" method.
        return this.albumName;
    }
}
