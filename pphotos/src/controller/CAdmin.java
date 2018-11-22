package controller;


import Model.PhotosHandler;
import Model.Register;
import Model.User;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;


/**
 * This class helps with the functionality of an admin account.
 * @author AlexMandez
 */
public class CAdmin extends CBase implements ControllerInterface, ChangeListener<User> {
	@FXML
	ListView<User> listView;
	@FXML
	TextField name;
	public CAdmin() {}
	public void callBeforeShow() {
		name.clear();
	}
	public void initialize() {
		PhotosHandler model = Register.getRegister().getModel();
		listView.setItems(model.getListUsers());
		listView.getSelectionModel().selectedItemProperty().addListener(this);
		if (model.getListUsers().size() > 0) {
			listView.getSelectionModel().select(0);
		}
	}
	public void doDelete() {
		PhotosHandler model = Register.getRegister().getModel();
		int index = listView.getSelectionModel().getSelectedIndex();
		if (index>=0) {
			model.deleteUser(index);
			listView.refresh();
		}
	}
	public void doAdd() {
		String username = name.getText().trim();
		PhotosHandler model = Register.getRegister().getModel();
		if (!username.isEmpty()) {
			User user = model.getUser(username);
			if (user!=null) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Name Already exists");
				alert.setContentText("Another user with this name exists already.");
				alert.showAndWait();
			}
			else {
				model.addUser(username);
				name.clear();
			}
		}
		else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Missing Username");
			alert.setContentText("Username should not be empty");
			alert.showAndWait();
		}
	}
	public void gotoAlbumList() {
		gotoUserFromAdmin();
	}
	public void doLogoff() {
		gotoLoginFromAdmin();
	}
	public void doExit() {
		System.exit(0);
	}
	public void doHelp() {
		Register.getRegister().Help(Register.getRegister().getADMIN_HELP_FXML());
	}

	@Override
	public void changed(ObservableValue<? extends User> arg0, User arg1, User arg2) {
	}
}
