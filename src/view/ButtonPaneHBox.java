package view;


import controller.CommandController;
import controller.MusicOrganizerController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

public class ButtonPaneHBox extends HBox {

	private MusicOrganizerController controller;
	private MusicOrganizerWindow view;
	
	private Button newAlbumButton;
	private Button deleteAlbumButton;
	private Button addSoundClipsButton;
	private Button removeSoundClipsButton;	
	private Button playButton;
	private Button undoButton;
	private Button redoButton;
	private Button flagButton;
	private Button ratingButton;
	public static final int BUTTON_MIN_WIDTH = 150;


	public ButtonPaneHBox(MusicOrganizerController contr, MusicOrganizerWindow view) {
		super();
		this.controller = contr;
		this.view = view;
		
		newAlbumButton = createNewAlbumButton();
		this.getChildren().add(newAlbumButton);

		deleteAlbumButton = createDeleteAlbumButton();
		this.getChildren().add(deleteAlbumButton);
		
		addSoundClipsButton = createAddSoundClipsButton();
		this.getChildren().add(addSoundClipsButton);
		
		removeSoundClipsButton = createRemoveSoundClipsButton();
		this.getChildren().add(removeSoundClipsButton);
		
		playButton = createPlaySoundClipsButton();
		this.getChildren().add(playButton);

		undoButton = createUndoButton();
		this.getChildren().add(undoButton);

		redoButton = createRedoButton();
		this.getChildren().add(redoButton);
		
		flagButton = createFlagButton();
		this.getChildren().add(flagButton);

		ratingButton = createRatingButton();
		this.getChildren().add(ratingButton);
	}

	public void disableButtonsOnSearchBasedAlbumSelected(){
		newAlbumButton.setDisable(true);
		deleteAlbumButton.setDisable(true);
		addSoundClipsButton.setDisable(true);
		removeSoundClipsButton.setDisable(true);
	}

	public void activateButtonsOnNonSearchBasedAlbumSelected(){
		newAlbumButton.setDisable(false);
		deleteAlbumButton.setDisable(false);
		addSoundClipsButton.setDisable(false);
		removeSoundClipsButton.setDisable(false);
	}

	/*
	 * Each method below creates a single button. The buttons are also linked
	 * with event handlers, so that they react to the user clicking on the buttons
	 * in the user interface
	 */

	private Button createNewAlbumButton() {
		Button button = new Button("New Album");
		button.setTooltip(new Tooltip("Create new sub-album to selected album"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
			
				controller.addNewAlbum();
			}
			
		});
		return button;
	}
	
	private Button createDeleteAlbumButton() {
		Button button = new Button("Remove Album");
		button.setTooltip(new Tooltip("Remove selected album"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				
				controller.deleteAlbum();
			}
			
		});
		return button;
	}
	
	private Button createAddSoundClipsButton() {
		Button button = new Button("Add Sound Clips");
		button.setTooltip(new Tooltip("Add selected sound clips to selected album"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				
				controller.addSoundClips();
			}
			
		});
		return button;
	}
	
	private Button createRemoveSoundClipsButton() {
		Button button = new Button("Remove Sound Clips");
		button.setTooltip(new Tooltip("Remove selected sound clips from selected album"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				
				controller.removeSoundClips();
			}
			
		});
		return button;
	}
	
	private Button createPlaySoundClipsButton() {
		Button button = new Button("Play Sound Clips");
		button.setTooltip(new Tooltip("Play selected sound clips"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				
				controller.playSoundClips();
			}
			
		});
		return button;
	}

	private Button createUndoButton() {
		// creates the undo button
		Button button = new Button("Undo action");  // creates button with text
		button.setTooltip(new Tooltip("Undo last actions up to 10"));  // tooltip for button
		button.setMinWidth(BUTTON_MIN_WIDTH);  // sets minimum width of button
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (CommandController.get().hasCommands()) {
					// on button press: check if command controller has commands to undo,
					// if so, undo the command
					controller.undoCommand();
				}
			}

		});
		return button;
	}

	private Button createRedoButton() {
		// creates the redo button
		Button button = new Button("Redo action");
		button.setTooltip(new Tooltip("Redo last undo action"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (CommandController.get().hasRedoableCommands()) {
					// on button click: check if command controller has redo-able commands
					// if so, redo last undid command
					controller.redoCommand();
				}
			}

		});
		return button;
	}

	private Button createFlagButton(){
		// creates the flag button
		Button button = new Button("Toggle flag");
		button.setTooltip(new Tooltip("Toggle flag on the selected album"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// call method in controller on button press
				controller.toggleSoundClipsFlag();
			}

		});
		return button;
	}

	private Button createRatingButton(){
		// creates the flag button
		Button button = new Button("Set rating");
		button.setTooltip(new Tooltip("Set a rating for the selected sound clip(s)"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// call method in controller on button press
				controller.setSoundClipsRating();
			}

		});
		return button;
	}

	public void updateUndoButton(boolean hasCommands) {
		// helper methods for updating button active / disable
		this.undoButton.setDisable(!hasCommands);
	}

	public void updateRedoButton(boolean hasRedoableCommands) {
		// helper methods for updating button active / disable
		this.redoButton.setDisable(!hasRedoableCommands);
	}
}
