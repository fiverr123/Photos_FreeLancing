package controller;


import Model.Register;
import Model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert;



/**
 * This class represents a log-in controller.
 * @author AlexMandez
 */
public class CLogin extends CBase implements ControllerInterface {
	@FXML 
	TextField userID;
	public void callBeforeShow() {
		Register.getRegister().getModel().setCurrentUser(null);
		userID.clear();
	}
	public void userIDKeyPressed(KeyEvent keyEvent) {
		if (keyEvent.getCode() == KeyCode.ENTER ||keyEvent.getCode() == KeyCode.SPACE) {
			doLogin();
		}
	}
	public void doLogin() {
		String user_id = userID.getText().trim();
		User user = Register.getRegister().getModel().getUser(user_id);
		if (user!=null) {
			Register.getRegister().getModel().setCurrentUser(user);
			if (user_id.equalsIgnoreCase("admin")) {
				Register.getRegister().gotoAdminFromLogin();;
			}
			else {
				gotoUserFromLogin();
			}
		}
		else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Not registered User");
			alert.setContentText("Username is not found");
			alert.showAndWait();
			userID.setText("");
		}
	}
	public void doExit() {
		Platform.exit();
	}
	public void doHelp() {
		Register.getRegister().Help(Register.getRegister().getPHOTOS_HELP_FXML());
	}
}