package model.Commands;

import model.Album.Album;
import model.Album.RootAlbum;
import model.Album.SubAlbum;
import view.MusicOrganizerWindow;

public class AddAlbumCommand extends Command {
    // command class for undo-able and redo-able add album action
    private String newAlbumName;  // name for the new album
    private Album newAlbum;  // the actual album

    public AddAlbumCommand(final Album parent, String newAlbumName, final MusicOrganizerWindow view){
        // constructor
        super(parent, view);
        this.newAlbumName = newAlbumName;
        if (this.newAlbumName == null){
            this.newAlbumName = "New Album";
        }
    }

    @Override
    public void execute() {
        this.newAlbum = new SubAlbum(this.newAlbumName, this.album);  // create the new album
        // parent album gets this new album assigned as sub album in constructor
        this.view.onAlbumAdded(this.newAlbum);  // call method to update album tree
    }

    @Override
    public void undo() {
        // removes this new album from the parent album
        this.album.remove(this.newAlbum);
        this.view.onAlbumRemoved();  // call method to update album tree
    }

    @Override
    public void redo() {
        execute();
    }
}
