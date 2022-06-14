package model.Commands;

import model.Album.Album;
import model.SoundClip;
import view.MusicOrganizerWindow;

import java.util.HashMap;
import java.util.List;

public class SetRatingCommand extends Command{

    private final List<SoundClip> soundClips;  // clips to set rating on
    private final HashMap<SoundClip, Integer> previousSoundClipRatings;  // keep track of what the ratings were for undo
    private final int rating;  // rating to give the sound clips

    public SetRatingCommand(Album album, MusicOrganizerWindow view, List<SoundClip> soundClips, int rating) {
        // constructor
        super(album, view);
        this.soundClips = soundClips;
        this.rating = rating;
        this.previousSoundClipRatings = new HashMap<>();
    }

    @Override
    public void execute() {
        for (SoundClip clip: this.soundClips){
            // for each selected sound clip, store the previous rating (for being able to undo action)
            this.previousSoundClipRatings.put(clip, clip.getRating());
            // ...then set the rating
            clip.setRating(this.rating);
        }
        this.view.onClipsUpdated();  // update clips view to indicate the new rating
    }

    @Override
    public void undo() {
        for (SoundClip clip: this.soundClips){
            // for each sound clip, get the previous rating from the hashmap
            clip.setRating(this.previousSoundClipRatings.get(clip));
        }
        this.view.onClipsUpdated();  // update view
    }

    @Override
    public void redo() {
        execute();
    }
}
