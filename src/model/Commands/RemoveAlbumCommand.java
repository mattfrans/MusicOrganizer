package model.Commands;

import model.Album.Album;
import view.MusicOrganizerWindow;

public class RemoveAlbumCommand extends Command {
    // command for removing albums from album tree
    private final Album parentAlbum;

    public RemoveAlbumCommand(final Album album, final MusicOrganizerWindow view){
        super(album, view);
        // constructor
        this.parentAlbum = album.getParentAlbum();

        assert this.parentAlbum != null && !this.album.isRootAlbum() && this.view != null;
        // design by contract stuff
    }

    @Override
    public void execute() {
        this.parentAlbum.remove(this.album);  // remove self from parent list of sub album
        // no need to store all subAlbums of removed Album?
        this.view.onAlbumRemoved();  // update view
    }

    @Override
    public void undo() {
        this.parentAlbum.add(this.album);  // add the removed album back to parent
        // add back all stored subAlbums of removed Album?
        // NO! removed album is never actually deleted so all sub-albums are still there!
        this.view.onAlbumAdded(this.album);  // update view
    }

    @Override
    public void redo() {
        execute();
    }
}
