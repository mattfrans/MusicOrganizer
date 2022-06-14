package model.Album;

import model.SoundClip;

import java.util.ArrayList;
import java.util.List;

public abstract class SearchBasedAlbum extends Album{

    public SearchBasedAlbum(String albumName) {
        super(albumName);
    }

    public abstract List<SoundClip> getSoundClips();  // template method

    @Override
    public boolean isSearchBasedAlbum(){
        return true;
    }

    @Override
    public boolean add(Album album){
        return false;
    }

    @Override
    public boolean add(final SoundClip clip){
        return false;
    }

    @Override
    public boolean remove(Album album){
        return false;
    }

    @Override
    public boolean remove(final SoundClip clip){
        return false;
    }

    @Override
    public boolean isRootAlbum() {
        return false;
    }

    @Override
    public Album getParentAlbum() {
        return null;
    }
}
