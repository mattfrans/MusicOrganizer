package model.Commands;

import model.Album.Album;
import model.Album.RootAlbum;
import model.SoundClip;
import view.MusicOrganizerWindow;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddSoundClipCommand extends Command {

    private final List<SoundClip> soundClips;  // clips to be added
    private final HashMap<Album, List<SoundClip>> addedClipsPerAlbum;  // keeps track of which clips were added to which album,
    // used for the undo method to not accidentally remove clips that were initially already in the album

    public AddSoundClipCommand(final Album selectedAlbum, final List<SoundClip> soundClips, final MusicOrganizerWindow view){
        // constructor
        super(selectedAlbum, view);

        this.soundClips = soundClips;
        this.addedClipsPerAlbum = new HashMap<>();

        assert invariant();  // design by contract stuff
    }

    private boolean invariant() {
        return this.album != null && this.view != null;
    }

    @Override
    public void execute() {
        ArrayList<SoundClip> addedClips = new ArrayList<>();  // temporary list of actually added clips
        boolean addToParent = false;  // boolean for if we need to check parent album
        for (SoundClip clip: this.soundClips) {
            // foreach clip in selected sound clips, check if the selected album contain it,
            // if so, add it to the album and add it to the temporary list of actually added sound clips
            if (!this.album.contains(clip)) {
                this.album.add(clip);
                addedClips.add(clip);
                addToParent = true;  // we should probably check if we should add this clip to the parent album aswell
            }
        }
        this.addedClipsPerAlbum.put(this.album, addedClips);
        // store the actually added sound clips in a hashmap were the key is the selected album

        // below is a while loop were we call a method that return false when:
        // no clips are added to the currently "selected" album,
        // or the album is null => AKA the root album's parent,
        // or no clips are left to add

        // the currentAlbum variable is updated whenever the method returns true, so that we are working on the album's parent
        Album currentAlbum = this.album;
        while (addToParent){
            addToParent = setParentAddedClips(currentAlbum.getParentAlbum(), this.addedClipsPerAlbum.get(currentAlbum));
            // call the method with the next generation of album as Album parameter,
            // and use this album's added sound clips to check to add as List<SoundClip> parameter
            currentAlbum = currentAlbum.getParentAlbum();  // update method parameter
        }

        this.view.onClipsUpdated();  // method call to update clips tree
    }

    private boolean setParentAddedClips(final Album album, final List<SoundClip> clips){
        // helper method for the execute method, for storing information of which albums added which clips
        if (clips.size() == 0 || album == null){
            return false;  // if no clips are to be added, or album is null AKA root album's parent,
            // then return false, since we don't need to do anything, and the while loop in the execute method stops
        }
        System.out.println("Checking to add clip(s) to parent album...");
        System.out.println("Clips to add: " + clips);
        System.out.println("Album to add to: " + album);
        ArrayList<SoundClip> addedClips = new ArrayList<>();  // temporary list of which clips were added to this album
        for (SoundClip clip: clips){
            if (!album.contains(clip)){
                // if this album doesn't contain the clip,
                // add it to the temporary list and to the album
                addedClips.add(clip);
                album.add(clip);
            }
        }
        if (addedClips.size() == 0){
            // if clips were added to this album, store the information in the class variable addedClipsPerAlbum
            this.addedClipsPerAlbum.put(album, addedClips);
            return true;  // return true, since we now know to check to add at least one clip in this album's parent
        }
        return false;  // no clips were added to this album, so we don't need to check this albums parent,
        // since we know that all clips are already there
    }

    @Override
    public void undo() {
        // go through each album that was updated in the execute method
        for (Album album: this.addedClipsPerAlbum.keySet()){
            assert album != null;  // something has gone wrong if album is null
            for (SoundClip clip: this.addedClipsPerAlbum.get(album)){  // for each clip that was added to this album
                assert clip != null;  // check that the clip isn't null
                album.remove(clip);  // remove the clip
            }
        }
        this.view.onClipsUpdated();  // method call to update clips tree
    }

    @Override
    public void redo() {
        // redo does the same as execute in most cases
        execute();
    }
}
