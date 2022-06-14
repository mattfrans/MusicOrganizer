package view;
	
import java.util.List;
import java.util.Optional;

import controller.CommandController;
import controller.MusicOrganizerController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Album.Album;
import model.Album.RootAlbum;
import model.SoundClip;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;



public class MusicOrganizerWindow extends Application {
	
	private BorderPane bord;
	private static MusicOrganizerController controller;
	private TreeView<Album> tree;
	private ButtonPaneHBox buttons;
	private SoundClipListView soundClipTable;
	private TextArea messages;
	
	
	public static void main(String[] args) {
		controller = new MusicOrganizerController();
		if (args.length == 0) {
			// path is for the moment absolute and only works on Jesper's computer
			String path = "C:\\Users\\jespe\\Downloads\\OOD_Task2_InitialFiles\\OOD_Task2_initialFiles_RENAME_PROJECT";
			// change the above path to get code to work on your computer
			controller.loadSoundClips(path + "\\src\\sample-sound");
		} else if (args.length == 1) {
			controller.loadSoundClips(args[0]);
		} else {
			System.err.println("too many command-line arguments");
			System.exit(0);
		}
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
				
		try {
			controller.registerView(this);
			primaryStage.setTitle("Music Organizer");
			
			bord = new BorderPane();
			
			// Create buttons in the top of the GUI
			buttons = new ButtonPaneHBox(controller, this);
			updateCommandButtonsActive();
			bord.setTop(buttons);

			// Create the tree in the left of the GUI
			tree = createTreeView();
			tree.setShowRoot(false);
			bord.setLeft(tree);
			
			// Create the list in the right of the GUI
			soundClipTable = createSoundClipListView();
			bord.setCenter(soundClipTable);
						
			// Create the text area in the bottom of the GUI
			bord.setBottom(createBottomTextArea());
			
			Scene scene = new Scene(bord);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.sizeToScene();
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent arg0) {
					Platform.exit();
					System.exit(0);
					
				}
				
			});

			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private TreeView<Album> createTreeView(){
		final TreeItem<Album> flagNode = new TreeItem<>(controller.getFlagAlbum());
		final TreeItem<Album> ratingNode = new TreeItem<>(controller.getRatingAlbum());
		final TreeItem<Album> rootAlbumNode = new TreeItem<>(controller.getRootAlbum());
		final TreeItem<Album> dummyNode = new TreeItem<Album>();
		dummyNode.getChildren().addAll(flagNode, ratingNode, rootAlbumNode);

		TreeView<Album> v = new TreeView<>(dummyNode);
		
		v.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				if(e.getClickCount()==2) {
					// This code gets invoked whenever the user double clicks in the TreeView
					// TODO: ADD YOUR CODE HERE
					//  - show sound clip in album in sound clip view
					displayMessage("Currently selected album: " + getSelectedAlbum().toString());
					getSelectedAlbum();
					onClipsUpdated();  // update clip view
				}
			}
		});
		return v;
	}
	
	private SoundClipListView createSoundClipListView() {
		SoundClipListView v = new SoundClipListView();
		v.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		v.display(controller.getRootAlbum());
		
		v.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				if (e.getClickCount() == 2) {
					// This code gets invoked whenever the user double clicks in the sound clip table
					// TODO: ADD YOUR CODE HERE
					//  - Play the sound clip
					displayMessage("Playing selected sound clip(s)");
					controller.playSoundClips();
				}
				
			}
			
		});
		
		return v;
	}
	
	private ScrollPane createBottomTextArea() {
		messages = new TextArea();
		messages.setPrefRowCount(3);
		messages.setWrapText(true);
		messages.prefWidthProperty().bind(bord.widthProperty());
		messages.setEditable(false); // don't allow user to edit this area
		
		// Wrap the TextArea in a ScrollPane, so that the user can scroll the 
		// text area up and down
		ScrollPane sp = new ScrollPane(messages);
		sp.setHbarPolicy(ScrollBarPolicy.NEVER);
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
		return sp;
	}
	
	/**
	 * Displays the message in the text area at the bottom of the GUI
	 * @param message the message to display
	 */
	public void displayMessage(String message) {
		messages.appendText(message + "\n");
	}
	
	public Album getSelectedAlbum() {
		TreeItem<Album> selectedItem = getSelectedTreeItem();
		if (selectedItem == null){
			return null;
		}

		onAlbumSelectedDisableButtons(selectedItem.getValue().isSearchBasedAlbum());
		// if the selected album is search based, call a method that disables the add/remove album/soundclip buttons,
		// since those actions cannot be done with that type of album

		return selectedItem.getValue();
	}
	
	private TreeItem<Album> getSelectedTreeItem(){
		return tree.getSelectionModel().getSelectedItem();
	}
	
	
	
	/**
	 * Pop up a dialog box prompting the user for a name for a new album.
	 * Returns the name, or null if the user pressed Cancel
	 */
	public String promptForAlbumName() {
		TextInputDialog dialog = new TextInputDialog();
		
		dialog.setTitle("Enter album name");
		dialog.setHeaderText(null);
		dialog.setContentText("Please enter the name for the album");
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			return result.get();
		} else {
			return null;
		}
	}

	public int promptForSoundClipRating(){
		// this method prompts the user for inputting a rating for sound clips
		TextInputDialog dialog = new TextInputDialog();

		dialog.setTitle("Enter sound clip(s) rating"); // title
		dialog.setHeaderText(null);
		dialog.setContentText("Please enter integer rating 0-5 for selected sound clip(s)"); // prompt
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			return Integer.parseInt(result.get());  // returns the int value user inputted
		} else {
			return 0;
		}
	}
	
	/**
	 * Return all the sound clips currently selected in the clip table.
	 */
	public List<SoundClip> getSelectedSoundClips(){
		return soundClipTable.getSelectedClips();
	}
	
	
	
	/**
	 * *****************************************************************
	 * Methods to be called in response to events in the Music Organizer
	 * *****************************************************************
	 */	
	
	
	
	/**
	 * Updates the album hierarchy with a new album
	 * @param newAlbum
	 */
	public void onAlbumAdded(Album newAlbum){
		TreeItem<Album> parentItem = getSelectedTreeItem();
		TreeItem<Album> newItem = new TreeItem<>(newAlbum);
		parentItem.getChildren().add(newItem);
		parentItem.setExpanded(true); // automatically expand the parent node in the tree
	}

	public void onAlbumSelectedDisableButtons(boolean searchBased){
		// if a search based album is selected, disable the buttons that adds / removes albums / sound clips
		if (searchBased) {
			buttons.disableButtonsOnSearchBasedAlbumSelected();
		} else {
			buttons.activateButtonsOnNonSearchBasedAlbumSelected();
		}
	}

	public void updateCommandButtonsActive(){
		// this method updates the undo / redo buttons to indicate when the user can undo an action or redo an action
		buttons.updateUndoButton(CommandController.get().hasCommands());
		buttons.updateRedoButton(CommandController.get().hasRedoableCommands());
	}
	
	/**
	 * Updates the album hierarchy by removing an album from it
	 */
	public void onAlbumRemoved(){
		TreeItem<Album> toRemove = getSelectedTreeItem();
		TreeItem<Album> parent = toRemove.getParent();
		parent.getChildren().remove(toRemove);
	}
	
	/**
	 * Refreshes the clipTable in response to the event that clips have
	 * been modified in an album
	 */
	public void onClipsUpdated(){
		Album a = getSelectedAlbum();
		if (a == null){
			a = RootAlbum.get();
		}
		soundClipTable.display(a);
	}
	
}
