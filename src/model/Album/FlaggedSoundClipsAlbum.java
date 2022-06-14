package model.Album;

import model.SoundClip;

import java.util.ArrayList;
import java.util.List;

public class FlaggedSoundClipsAlbum extends SearchBasedAlbum{

    private static FlaggedSoundClipsAlbum instance;

    private FlaggedSoundClipsAlbum(String albumName) {
        super(albumName);
    }

    @Override
    public List<SoundClip> getSoundClips() {
        ArrayList<SoundClip> clips = new ArrayList<>();
        for (SoundClip clip: RootAlbum.get().getSoundClips()){
            if (clip.isFlagged()){
                clips.add(clip);
            }
        }
        return clips;
    }

    public static FlaggedSoundClipsAlbum getInstance(){
        if (instance == null){
            instance = new FlaggedSoundClipsAlbum("Flagged Sound Clips");
        }
        return instance;
    }

}
