package model.Album;

import model.SoundClip;

public interface AlbumInterface {
    // interface for making sure that the album class contains all the necessary methods
    boolean add(final SoundClip clip);
    boolean remove(final SoundClip clip);
    boolean add(Album album);
    boolean remove(final Album album);
    boolean contains(final Album album);
    boolean contains(final SoundClip clip);
}
