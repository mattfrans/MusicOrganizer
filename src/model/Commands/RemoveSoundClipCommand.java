package model.Commands;

import model.Album.Album;
import model.Album.RootAlbum;
import model.SoundClip;
import view.MusicOrganizerWindow;

import java.util.ArrayList;
import java.util.List;

public class RemoveSoundClipCommand extends Command {
    // command class for removing clips from album
    private final List<SoundClip> clips;  // the inputted clips to remove
    private final List<SoundClip> removedClips;  // keep track of which clips were actually removed
    // (user can maybe try to remove clips that aren't in the selected album somehow maybe possibly?!?)

    public RemoveSoundClipCommand(final List<SoundClip> clips, final Album album, final MusicOrganizerWindow view){
        super(album, view);
        // constructor
        this.clips = clips;
        this.removedClips = new ArrayList<SoundClip>();

        assert invariant();
    }

    private boolean invariant() {
        // Can you remove clips from root album?
        return this.album != null && this.view != null;
    }

    @Override
    public void execute() {
        for (SoundClip clip: this.clips){
            if (this.album.remove(clip)){  // remove the selected clips from selected album
                this.removedClips.add(clip);  // keep track of the clips that were actually removed
            }
        }
        this.view.onClipsUpdated();  // method call for updating GUI
    }

    @Override
    public void undo() {
        for (SoundClip clip: this.removedClips){  // add back actually removed sound clips
            this.album.add(clip);
        }
        this.view.onClipsUpdated();  // method call for updating GUI
    }

    @Override
    public void redo() {
        execute();
    }
}
