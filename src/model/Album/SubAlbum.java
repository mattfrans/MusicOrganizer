package model.Album;

import model.SoundClip;

public class SubAlbum extends Album{

    // instance variables
    private Album parentAlbum; // is not final because maybe we want to add some features that change parent album?

    public SubAlbum(final String albumName, Album parent) {
        // constructor for the SubAlbum class, calls super, and assigns rootAlbum as parent if no parent is assigned
        super(albumName);
        if (parent == null){
            System.out.println("Sub-album '" + this + "' has no parent assigned, assigning root album as parent...");
            this.parentAlbum = RootAlbum.get();
        } else {
            this.parentAlbum = parent;
        }
        // add this as subAlbum to parentAlbum
        this.parentAlbum.add(this);
        // check invariant for both parent and this
        assert this.parentAlbum.invariant() && invariant();
    }

    @Override
    public boolean add(final SoundClip clip) {
        // adds the song to this album and recursively adds the song to all parentAlbums that doesn't contain it
        // if parent album doesn't contain song, call this method on parent album,
        // recursion stops at rootAlbum since that method is the base "add" method defined in "Album" class
        // Recursion functionality moved to command (AddSoundClipCommand)
        super.add(clip);  // adds the sound clip to this album
        return true;  // for testing purposes
    }

    @Override
    public Album getParentAlbum(){
        // returns the parent album
        return this.parentAlbum;
    }

    @Override
    public boolean invariant() {
        // checks that the instance variables are viable
        return super.invariant() && this.parentAlbum != null;
    }

    @Override
    public boolean isRootAlbum() {
        // returns false because this is not the root album
        return false;
    }
}
