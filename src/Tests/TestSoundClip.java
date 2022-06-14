package Tests;

import model.Album.RootAlbum;
import model.Album.SubAlbum;
import model.SoundClip;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

public class TestSoundClip {
    @Test
    public void testSong(){
        // tests the song class
        SoundClip song = new SoundClip(new File("src/SongFiles/shrek.wav"));
        RootAlbum root = RootAlbum.get();
        SubAlbum shrekAlbum = new SubAlbum("ShrekAlbum", null);
        assertTrue(shrekAlbum.add(song));  // add song to shrekAlbum, which should get passed up to root
        assertTrue(root.contains(song));
        // test removing the song from root, song should get removed from all subAlbums
        assertTrue(root.remove(song));
        assertFalse(root.remove(song)); // cannot remove twice
        assertFalse(shrekAlbum.contains(song));

        System.out.println(song.getFile().getPath());
    }
}
