package Tests;

import model.Album.RootAlbum;
import model.Album.SubAlbum;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAlbum {

    // Tests all the classes under the package "Album"

    @Test
    public void testAlbum(){

        // check root album
        RootAlbum root = RootAlbum.get();
        assertEquals("All Sound Clips", root.toString());

        // add new subAlbums to root
        // checking addAlbum method
        SubAlbum rockAlbum = new SubAlbum("Rock", root);
        assertTrue(root.contains(rockAlbum)); // rockAlbum gets added as subAlbum to root in rockAlbum's constructor
        SubAlbum classicalAlbum = new SubAlbum("Classical", null);
        assertTrue(root.contains(classicalAlbum));

        // check getAlbum methods and values
        assertEquals(2, root.getSubAlbums().size());
        assertEquals("Rock", root.getSubAlbum(0).toString());
        assertEquals(0, rockAlbum.getSoundClips().size());

        // checking contains method
        assertTrue(root.contains(rockAlbum));
        assertEquals("Classical", root.getSubAlbum(1).toString());

        // checking getParent method
        assertEquals(root, classicalAlbum.getParentAlbum());

        // checking remove method
        assertTrue(root.remove(classicalAlbum));
        assertEquals(1, root.getSubAlbums().size());

        // adding sub-albums to sub-albums
        SubAlbum metalAlbum = new SubAlbum("Metal", rockAlbum);
        assertFalse(rockAlbum.add(metalAlbum));
        assertTrue(rockAlbum.contains(metalAlbum));
        assertEquals(metalAlbum, root.getSubAlbum(0).getSubAlbum(0));
        assertEquals(root, metalAlbum.getParentAlbum().getParentAlbum());
        assertNull(root.getParentAlbum());

        // checking isRootAlbum method
        assertFalse(rockAlbum.isRootAlbum());
        assertTrue(root.isRootAlbum());
    }
}
