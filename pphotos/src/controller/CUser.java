package controller;


import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.util.List;

import Model.Album;
import Model.Photo;
import Model.PhotosHandler;
import Model.Register;
import Model.Tag;
import Model.TagSearchCondition;
import Model.User;


/**
 * This class helps to control user functionality.
 * @author AlexMandez
 */
public class CUser extends CBase implements EventHandler<MouseEvent>, ControllerInterface, ChangeListener<Album> {

	@FXML 
	TableView<Album> 	tab;
	@FXML 
	TableColumn 	calbumName;
	@FXML 
	TableColumn 	cphotoCount;
	@FXML 
	TableColumn 	cstartTime;
	@FXML 
	TableColumn 	cendTime;
	@FXML
	ListView<Tag> listOfTag;
	@FXML 
	TextField tagName;
	@FXML 
	TextField tagValue;
	@FXML 
	TextField newAlbumName;
	@FXML 
	DatePicker sDate;
	@FXML 
	DatePicker eDate;
	public void callBeforeShow() {
		User user = Register.getRegister().getModel().getCurrentUser();
		ObservableList<Album> albumList = user.getListAlbums();
		listOfTag.setItems(user.getTags().getTags());
		if (user.getTags().getTags().size() > 0) {
			listOfTag.getSelectionModel().select(0);
		}
		for (Album a : albumList) {
			a.setCounterDatetime();
		}
		tab.setItems(albumList);
		if (user.getListAlbums().size() > 0) {
			tab.getSelectionModel().select(0);
		}
		tab.refresh();
	}
	public CUser() {
	}
	@FXML
	public void initialize() {
		tab.setEditable(true);
		calbumName.setCellFactory(TextFieldTableCell.<Album>forTableColumn());
		calbumName.setOnEditCommit(
				new EventHandler<CellEditEvent<Album, String>>() {
					@Override
					public void handle(CellEditEvent<Album, String> t) {
						String newAlbumName = t.getNewValue().trim();
						//
						if (newAlbumName.length()>0) {
							User user = Register.getRegister().getModel().getCurrentUser();
							//
							int i = tab.getSelectionModel().getSelectedIndex();
							//
							user.updateAlbumName(i, newAlbumName);
							//((Album) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAlbumName(new_albumname);
						}
						//
						tab.refresh();
					}
				}
				);
		tab.setRowFactory(tableView -> {
			final TableRow<Album> row = new TableRow<>();
			final ContextMenu contextMenu = new ContextMenu();
			final MenuItem removeMenuItem = new MenuItem("Remove");
			removeMenuItem.setOnAction(event -> tab.getItems().remove(row.getItem()));
			contextMenu.getItems().add(removeMenuItem);
			// Set context menu on row, but use a binding to make it only show for non-empty rows:
			row.contextMenuProperty().bind(
					Bindings.when(row.emptyProperty())
					.then((ContextMenu)null)
					.otherwise(contextMenu)
					);
			return row ;
		});
		
		eDate.setValue(LocalDate.now());
		sDate.setValue(eDate.getValue().minusDays(30));
		tab.getSelectionModel().selectedItemProperty().addListener(this);
		//
		tab.setOnMouseClicked(event -> {
			if (!event.getButton().equals(MouseButton.PRIMARY) || event.getClickCount() != 1) {
				if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
					Register.getRegister().gotoAlbumFromUser();
				}
			}
		});
	}
	@Override
	public void handle(MouseEvent arg0) {
	}
	public void doLogoff() {
		gotoLoginFromUser();
	}
	public void doExit() {
		Platform.exit();
	}
	public void doAddUserTag() {
		String name = tagName.getText().trim();
		String value = tagValue.getText().trim();
		User user = Register.getRegister().getModel().getCurrentUser();
		if (name.length()>0 && value.length()>0) {
			boolean ret = user.getTags().addTag(name, value);
			if (ret) {
				listOfTag.refresh();
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
	public void doDeleteUserTag() {
		User user = Register.getRegister().getModel().getCurrentUser();
		int index = listOfTag.getSelectionModel().getSelectedIndex();
		user.deleteTagSearchConditionTag(index);
		listOfTag.refresh();
	}
	public void doSearchDate() {
		User user = Register.getRegister().getModel().getCurrentUser();
		if (sDate.getValue()==null || eDate.getValue()==null) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please set search condition (dates) first.", ButtonType.YES/*, ButtonType.NO, ButtonType.CANCEL*/);
			alert.showAndWait();
		}
		else {
			user.getDates().setSDate(sDate.getValue());
			user.getDates().setEDate(eDate.getValue());
			List<Photo> lst = user.searchByDate();
			String message;
			if (lst.size()>0) {
				Album newOne = new Album(Register.getRegister().getALBUM_NAME_SEARCH_BY_DATE(), lst);
				newOne.setCounterDatetime();
				user.addOrOverwriteAlbum(newOne);
				message = "Album '" + Register.getRegister().getALBUM_NAME_SEARCH_BY_DATE() + "' is created for search results. It will be replaced in next search. To keep it, simply change its name.";
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES/*, ButtonType.NO, ButtonType.CANCEL*/);
				alert.showAndWait();
				user.setCurrentAlbum(newOne);
				Register.getRegister().gotoAlbumFromUser();
			}
			else {
				message = "No photo matched";
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES/*, ButtonType.NO, ButtonType.CANCEL*/);
				alert.showAndWait();
			}
		}
	}
	public void doSearchTag() {
		User user = Register.getRegister().getModel().getCurrentUser();
		TagSearchCondition tagCondition = user.getTags();
		if (tagCondition.getTags().size()==0) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please set search condition (tags) first.", ButtonType.YES/*, ButtonType.NO, ButtonType.CANCEL*/);
			alert.showAndWait();
		}
		else {
			List<Photo> lst = user.searchByTag();
			String message;
			if (lst.size()>0) {
				Album newOne = new Album(Register.getRegister().getALBUM_NAME_SEARCH_BY_TAG(), lst);
				newOne.setCounterDatetime();
				user.addOrOverwriteAlbum(newOne);
				message = "Album '" + Register.getRegister().getALBUM_NAME_SEARCH_BY_TAG() + "' is created for search results. It will be replaced in next search. To keep it, simply change its name.";
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES/*, ButtonType.NO, ButtonType.CANCEL*/);
				alert.showAndWait();
				user.setCurrentAlbum(newOne);
				Register.getRegister().gotoAlbumFromUser();
			}
			else {
				message = "No photo matched";
				//
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES/*, ButtonType.NO, ButtonType.CANCEL*/);
				alert.showAndWait();
			}
		}
	}
	public void doAddNewAlbum() {
		User user = Register.getRegister().getModel().getCurrentUser();
		//
		String album_name = newAlbumName.getText().trim();
		if (album_name.length()>0) {
			user.addAlbum(album_name);
			tab.refresh();
			//
			newAlbumName.setText("");
		}
	}
	@Override
	public void changed(ObservableValue<? extends Album> observable, Album oldValue, Album newValue) {
		PhotosHandler model = Register.getRegister().getModel();
		User user = model.getCurrentUser();
		if (newValue!=null) {
			user.setCurrentAlbum(newValue);
		}
	}
	public void doHelp() {
		Register.getRegister().Help(Register.getRegister().getPHOTOS_HELP_FXML());
	}
}