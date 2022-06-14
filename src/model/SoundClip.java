package model;
import java.io.File;
import java.util.Objects;

/**
 * SoundClip is a class representing a digital
 * sound clip file on disk.
 */
public class SoundClip {

	private final File file;
	private boolean isFlagged;
	private int rating;
	
	/**
	 * Make a SoundClip from a file.
	 * Requires file != null.
	 */
	public SoundClip(File file) {
		assert file != null;
		this.file = file;
		this.isFlagged = false;
		this.rating = 0;
	}

	/**
	 * @return the file containing this sound clip.
	 */
	public File getFile() {
		return file;
	}
	
	public String toString(){
		String flag = this.isFlagged ? " (F)" : "";
		String rating = ", rating: " + this.rating;
		return file.getName() + flag + rating;
	}

	public boolean isFlagged(){
		return this.isFlagged;
	}

	public void toggleFlag(){
		this.isFlagged = !this.isFlagged;
	}

	public int getRating(){
		return this.rating;
	}

	public void setRating(int rating){
		if (rating > 5){
			rating = 5;
		} else if (rating < 0){
			rating = 0;
		}
		this.rating = rating;
	}
	
	@Override
	public boolean equals(Object obj) {
		return 
			obj instanceof SoundClip
			&& ((SoundClip)obj).file.equals(file);
	}
	
	@Override
	public int hashCode() {
		return file.hashCode();
	}
}
