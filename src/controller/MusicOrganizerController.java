package controller;

import java.util.List;
import java.util.Set;

import model.*;
import model.Album.Album;
import model.Album.FlaggedSoundClipsAlbum;
import model.Album.GreatSoundClipsAlbum;
import model.Album.RootAlbum;
import model.Commands.*;
import view.MusicOrganizerWindow;

public class MusicOrganizerController {

	private MusicOrganizerWindow view;
	private SoundClipBlockingQueue queue;
	
	public MusicOrganizerController() {
		
		// Create the blocking queue
		queue = new SoundClipBlockingQueue();
				
		// Create a separate thread for the sound clip player and start it
		
		(new Thread(new SoundClipPlayer(queue))).start();
	}
	
	/**
	 * Load the sound clips found in all subfolders of a path on disk. If path is not
	 * an actual folder on disk, has no effect.
	 */
	public Set<SoundClip> loadSoundClips(String path) {
		// TODO: Add the loaded sound clips to the root album

		Set<SoundClip> clips = SoundClipLoader.loadSoundClips(path);

		for (SoundClip clip: clips){
			System.out.println("Adding clip: " + clip.toString());
			RootAlbum.get().add(clip);
		}

		// for testing purposes, print nr of sound clips loaded to root
		System.out.println("Root album has " + RootAlbum.get().getSoundClips().size() + " sound clips");
		return clips;
	}
	
	public void registerView(MusicOrganizerWindow view) {
		this.view = view;
	}
	
	/**
	 * Returns the root album
	 */
	public Album getRootAlbum(){
		// returns the RootAlbum singleton
		return RootAlbum.get();
	}

	public Album getFlagAlbum() {
		// returns the FlagAlbum singleton
		return FlaggedSoundClipsAlbum.getInstance();
	}

	public Album getRatingAlbum() {
		// returns the RatingAlbum singleton
		return GreatSoundClipsAlbum.getInstance();
	}
	
	/**
	 * Adds an album to the Music Organizer
	 */
	public void addNewAlbum(){
		// the currently selected album as parent,
		// if none selected => parent album is null => root album is parent (defined in SubAlbum constructor)
		// albumName comes from the input prompt

		String albumName = view.promptForAlbumName();
		AddAlbumCommand command = new AddAlbumCommand(view.getSelectedAlbum(), albumName, this.view);
		CommandController.get().addNewCommand(command);

		this.view.updateCommandButtonsActive();
	}
	
	/**
	 * Removes an album from the Music Organizer
	 */
	public void deleteAlbum(){
		// delete currently selected album (and all it's subAlbums?)
		if (this.view.getSelectedAlbum() != RootAlbum.get() || this.view.getSelectedAlbum() != null){
			RemoveAlbumCommand command = new RemoveAlbumCommand(this.view.getSelectedAlbum(), this.view);
			CommandController.get().addNewCommand(command);
			this.view.updateCommandButtonsActive();
		} else {
			this.view.displayMessage("Cannot remove root album!");
		}
	}
	
	/**
	 * Adds sound clips to an album
	 */
	public void addSoundClips(){
		// add sound clip to currently selected album (which automatically adds the clip to all parents)
		// if no album is selected => add to root
		AddSoundClipCommand command = new AddSoundClipCommand(view.getSelectedAlbum(), this.view.getSelectedSoundClips(), this.view);
		CommandController.get().addNewCommand(command);

		this.view.updateCommandButtonsActive();
	}
	
	/**
	 * Removes sound clips from an album
	 */
	public void removeSoundClips(){
		// remove from currently selected album currently selected soundclip(s)
		RemoveSoundClipCommand command = new RemoveSoundClipCommand(this.view.getSelectedSoundClips(), this.view.getSelectedAlbum(), this.view);
		CommandController.get().addNewCommand(command);

		this.view.updateCommandButtonsActive();
	}

	public void toggleSoundClipsFlag(){
		// make the command and add it to the command stack in command controller, it gets executed there
		ToggleFlagCommand command = new ToggleFlagCommand(this.view.getSelectedAlbum(), this.view, this.view.getSelectedSoundClips());
		CommandController.get().addNewCommand(command);

		this.view.updateCommandButtonsActive();
	}

	public void setSoundClipsRating(){
		// make the command and add it to the command stack in command controller, it gets executed there
		int rating = this.view.promptForSoundClipRating();
		SetRatingCommand command = new SetRatingCommand(this.view.getSelectedAlbum(), this.view, this.view.getSelectedSoundClips(), rating);
		CommandController.get().addNewCommand(command);

		this.view.updateCommandButtonsActive();
	}
	
	/**
	 * Puts the selected sound clips on the queue and lets
	 * the sound clip player thread play them. Essentially, when
	 * this method is called, the selected sound clips in the 
	 * SoundClipTable are played.
	 */

	public void playSoundClips(){
		List<SoundClip> l = view.getSelectedSoundClips();
		queue.enqueue(l);
		for (SoundClip soundClip : l) {
			view.displayMessage("Playing " + soundClip);
		}
	}

	public void undoCommand() {
		// uses CommandController singleton to execute undo command
		CommandController.get().undoLastCommand();
		this.view.updateCommandButtonsActive();
	}

	public void redoCommand() {
		// uses CommandController singleton to execute redo command
		CommandController.get().redoLastUndo();
		this.view.updateCommandButtonsActive();
	}
}
