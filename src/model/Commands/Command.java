package model.Commands;

import model.Album.Album;
import model.Album.RootAlbum;
import view.MusicOrganizerWindow;

public abstract class Command {

    Album album; // all commands need an album to work with
    MusicOrganizerWindow view;  // all commands need to be able to update the view

    public Command(final Album album, final MusicOrganizerWindow view){
        // super constructor
        this.album = album;
        this.view = view;

        if (this.album == null){
            // select root album by default if album is null
            this.album = RootAlbum.get();
        }

        assert this.album != null && this.view != null;  // design by contract stuff
    }

    public abstract void execute();
    public abstract void undo();
    public abstract void redo();
}
