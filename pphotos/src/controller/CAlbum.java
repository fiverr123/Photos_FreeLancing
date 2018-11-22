package controller;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

import Model.Album;
import Model.Photo;
import Model.PhotosHandler;
import Model.Register;
import Model.Tag;
import Model.User;



/**
 * This class represents all the functionality an album can have.
 * @author AlexMandez
 */
public class CAlbum extends CBase implements ControllerInterface, EventHandler<MouseEvent>, ChangeListener<Tab> {
	@FXML
	TabPane tab;
	@FXML
	TilePane tile;
	@FXML ListView<Tag> listTag;
	@FXML Pagination pagination;
	@FXML TextField tagName;
	@FXML TextField tagValue;
	ObservableList<Node> list;
	final ContextMenu cm = new ContextMenu();
	Menu cmCopy = new Menu("Copy");
	Menu cmMove = new Menu("Move");
	final ContextMenu cm_tile = new ContextMenu();
	public void callBeforeShow() {
		tab.getSelectionModel().select(0);
		User user = Register.getRegister().getModel().getCurrentUser();
		Album album = user.getCurrentAlbum();
		ObservableList<Album> albums =user.getListAlbums();
		list.clear();
		List<Photo> photoList = album.getPhotoList();
		for (Photo onePhoto : photoList) {
			BorderPane viewWrapper = onePhoto.getThumbnailImageView(this, "-fx-border-color: white");
			list.add(viewWrapper);
		}
		int index = album.getCurrentIndex();
		setupCurrentPhoto(index, true);
	}
	public CAlbum() {
		CAlbum controller = this;
		{	MenuItem cmItemAdd = new MenuItem("Add");
		cmItemAdd.setOnAction(e -> {
			User user = Register.getRegister().getModel().getCurrentUser();
			Album album = user.getCurrentAlbum();
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Open Photo File");
			File file = chooser.showOpenDialog(new Stage());
			if (file!=null) {
				Photo photo = Photo.createPhoto(file.getAbsolutePath(), file);
				int index = album.addPhotoToAlbum(null, photo);
				BorderPane viewWrapper = null;
				if (photo != null) {
					viewWrapper = photo.getThumbnailImageView(controller, "-fx-border-color: white");
				}				list.add(index, viewWrapper);
				setupCurrentPhoto(index, true);
			}
		});
		cm_tile.getItems().add(cmItemAdd);
		}
		MenuItem cmItemAdd = new MenuItem("Add");
		cmItemAdd.setOnAction(e -> {
			Photo onePhoto = (Photo)cm.getUserData();			User user = Register.getRegister().getModel().getCurrentUser();
			Album album = user.getCurrentAlbum();
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Open File");
			File file = chooser.showOpenDialog(new Stage());
			if (file!=null) {
				Photo photo = Photo.createPhoto(file.getAbsolutePath(), file);
				int oldcurrIndex = album.getCurrentIndex();
				int index = album.addPhotoToAlbum(onePhoto, photo);
				BorderPane viewWrapper = null;
				if (photo != null) {
					viewWrapper = photo.getThumbnailImageView(controller, "-fx-border-color: white");
				}
				list.add(index, viewWrapper);
				if (index<=oldcurrIndex) {
					BorderPane vw = (BorderPane)list.get(oldcurrIndex+1);
					vw.setStyle("-fx-border-color: white");
				}
				setupCurrentPhoto(index, true);
			}
		});
		cm.getItems().add(cmItemAdd);
		cm.getItems().add(cmCopy);
		cm.getItems().add(cmMove);
		MenuItem cmItemDelete = new MenuItem("Delete");
		cmItemDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Photo onePhoto = (Photo)cm.getUserData();
				User user = Register.getRegister().getModel().getCurrentUser();
				Album album = user.getCurrentAlbum();
				int index = album.deletePhotoFromAlbum(onePhoto);
				list.remove(index);
				int indexCurr = album.getCurrentIndex();
				setupCurrentPhoto(indexCurr, true);
			}
		});
		cm.getItems().add(cmItemDelete);
	}
	@FXML
	public void initialize() {
		list = tile.getChildren();
		tile.setOnMouseClicked(this);
		tab.getSelectionModel().selectedItemProperty().addListener(this);
		tab.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		pagination.setPageFactory(pageIndex -> {
			User user = Register.getRegister().getModel().getCurrentUser();
			Album album = user.getCurrentAlbum();
			if (album.getSize() > 0) {
				CAlbum.this.setupCurrentPhoto(pageIndex, false);
				Photo currPhoto = album.getCurrentPhoto();
				return currPhoto.getImgNode(arg0 -> tab.getSelectionModel().select(0));
			}
			else {
				return null;
			}
		});
	}
	public void setupCurrentPhoto(Photo photo) {
		User user = Register.getRegister().getModel().getCurrentUser();
		Album album = user.getCurrentAlbum();
		ObservableList<Album> albums =user.getListAlbums();
		int index = album.getPhotoIndex(photo);
		setupCurrentPhoto(index, true);
	}
	public void setupCurrentPhoto(int newIndex, boolean isFromAlbum) {
		User user = Register.getRegister().getModel().getCurrentUser();
		Album album = user.getCurrentAlbum();
		ObservableList<Album> albums =user.getListAlbums();
		int oldIndex = album.getCurrentIndex();
		newIndex = album.setCurrentPhotoIndex(newIndex);
		Photo newPhoto = album.getCurrentPhoto();
		if (newIndex > -1) {
			if (list.size()>0) {
				if (oldIndex==-1) {
					BorderPane currViewWrapper = (BorderPane)list.get(newIndex);
					currViewWrapper.setStyle("-fx-border-color: white;");
				}
				else if (oldIndex!=newIndex) {
					BorderPane oldViewWrapper = (BorderPane)list.get(oldIndex);
					BorderPane currViewWrapper = (BorderPane)list.get(newIndex);
					oldViewWrapper.setStyle("-fx-border-color: white");
					currViewWrapper.setStyle("-fx-border-color: white;");
				}
				else {
					BorderPane currViewWrapper = (BorderPane)list.get(newIndex);
					currViewWrapper.setStyle("-fx-border-color: white;");
				}
			}
			listTag.setItems(newPhoto.getTags());
			if (newPhoto.getTags().size() > 0) {
				listTag.getSelectionModel().select(0);
			}
			if (isFromAlbum) {
				pagination.setPageCount(album.getSize());
				pagination.setCurrentPageIndex(newIndex);
			}
		}
	}
	@Override
	public void handle(MouseEvent event) {
		Object obj = event.getSource();
		if (obj instanceof TilePane) {
			if (event.getButton() == MouseButton.SECONDARY) {
				cm_tile.show(tile, event.getScreenX(), event.getScreenY());
			}
			event.consume();
		}
		else if (obj instanceof ImageView) {
			ImageView view = (ImageView)obj;
			if (event.getButton().equals(MouseButton.PRIMARY)) {
				if (event.getClickCount() == 1) {
					Object obj1 = view.getUserData();
					Photo onePhoto = (Photo)obj1;
					setupCurrentPhoto(onePhoto);
				}
				else {
					Object obj1 = view.getUserData();
					Photo onePhoto = (Photo)obj1;
					setupCurrentPhoto(onePhoto);
					tab.getSelectionModel().select(1);
				}
			}
			else if (event.getButton() == MouseButton.SECONDARY) {
				Object obj1 = view.getUserData();
				Photo onePhoto = (Photo)obj1;
				PhotosHandler model = Register.getRegister().getModel();
				User user = model.getCurrentUser();
				ObservableList<Album> albums =user.getListAlbums();
				Album album = user.getCurrentAlbum();
				List<Album> albumList = Register.getRegister().filter(albums, album, (t,u)->t!=u&&(!t.getAlbumName().equalsIgnoreCase(Register.getRegister().getALBUM_NAME_SEARCH_BY_DATE()) && !t.getAlbumName().equalsIgnoreCase(Register.getRegister().getALBUM_NAME_SEARCH_BY_TAG())));
				cmCopy.getItems().clear();
				for (Album a : albumList) {
					MenuItem cmItemAlbum = new MenuItem(a.getAlbumName());
					cmItemAlbum.setOnAction(e -> {
						MenuItem mi =  (MenuItem)e.getSource();
						User user1 = Register.getRegister().getModel().getCurrentUser();
						Photo onePhoto1 = (Photo)cm.getUserData();
						String albumTarget = mi.getText();
						user1.copyPhotoToNewAlbum(onePhoto1, albumTarget);
					});
					cmCopy.getItems().add(cmItemAlbum);
				}
				cmMove.getItems().clear();
				for (Album a : albumList) {
					MenuItem cmItemAlbum = new MenuItem(a.getAlbumName());
					cmItemAlbum.setOnAction(e -> {
						MenuItem mi =  (MenuItem)e.getSource();
						Photo onePhoto12 = (Photo)cm.getUserData();
						String albumTarget = mi.getText();
						User user12 = Register.getRegister().getModel().getCurrentUser();
						Album album1 = user12.getCurrentAlbum();
						int index = album1.getPhotoIndex(onePhoto12);
						list.remove(index);
						user12.movePhotoToNewAlbum(onePhoto12, albumTarget);
						int iCurr = album1.getCurrentIndex();
						setupCurrentPhoto(iCurr, true);
					});
					cmMove.getItems().add(cmItemAlbum);
				}
				cm.setUserData(onePhoto);
				cm.show(view, event.getScreenX(), event.getScreenY());
			}
			event.consume();
		}
	}
	public void doLogoff() {
		gotoLoginFromAlbum();
	}
	public void doExit() {
		Platform.exit();
	}
	@Override
	public void changed(ObservableValue<? extends Tab> arg0, Tab arg1, Tab arg2) {
		if (arg2.getText().equals("Photo")) {
			User user = Register.getRegister().getModel().getCurrentUser();
			Album album = user.getCurrentAlbum();
			Photo currPhoto = album.getCurrentPhoto();
			//
			if (currPhoto!=null) {
				pagination.setVisible(true);
				int index = album.getPhotoIndex(currPhoto);
				pagination.setPageCount(album.getSize());
				pagination.setCurrentPageIndex(index);
			}
			else {
				pagination.setVisible(false);
			}
		}
	}
	public void doDeleteTag() {
		User user = Register.getRegister().getModel().getCurrentUser();
		Album album = user.getCurrentAlbum();
		Photo currPhoto = album.getCurrentPhoto();
		int index = listTag.getSelectionModel().getSelectedIndex();
		currPhoto.deleteTag(index);
		listTag.refresh();
	}
	public void doAddTag() {
		String name = tagName.getText().trim();
		String value = tagValue.getText().trim();
		if (name.length()>0 && value.length()>0) {
			User user = Register.getRegister().getModel().getCurrentUser();
			Album album = user.getCurrentAlbum();
			Photo currPhoto = album.getCurrentPhoto();
			boolean ret = currPhoto.addTag(name, value);
			if (ret) {
				listTag.refresh();
				tagName.setText("");
				tagValue.setText("");
			}
			else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Not complete");
				alert.setContentText("Duplicate Name and Value Pair. Try again.");
				alert.showAndWait();
			}
		}
		else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Not complete");
			alert.setContentText("Name and Value Fields are empty. Enter again");
			alert.showAndWait();
		}

	}
	public void gotoAlbumList() {
		gotoUserFromAlbum();
	}
	public void doHelp() {
		Register.getRegister().Help(Register.getRegister().getPHOTOS_HELP_FXML());
	}
}