package model.Commands;

import model.Album.Album;
import model.SoundClip;
import view.MusicOrganizerWindow;

import java.util.List;

public class ToggleFlagCommand extends Command{

    private final List<SoundClip> soundClips;  // clips to toggle flag on

    public ToggleFlagCommand(Album album, MusicOrganizerWindow view, List<SoundClip> soundClips) {
        super(album, view);
        this.soundClips = soundClips;
    }

    @Override
    public void execute() {
        for (SoundClip clip: this.soundClips){
            clip.toggleFlag();  // for each sound clip, toggle flag
        }
        this.view.onClipsUpdated();  // update the view
    }

    @Override
    public void undo() {
        execute();  // since we're only toggling booleans, doing the same thing twice will undo the action
    }

    @Override
    public void redo() {
        execute();
    }
}
