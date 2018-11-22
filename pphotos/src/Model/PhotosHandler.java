package Model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents all the data for this application.
 * @author AlexMandez
 */
public class PhotosHandler implements Serializable {
	private static final long serialVersionUID = 1L;
	public void save(boolean isStore) {
		if (isStore) {
			listOfUsersToKeep = new ArrayList<>(listUsers);
			listUsers = null;
			for (User u : listOfUsersToKeep) {
				u.writeToFile(true);
			}
		}
		else {
			listUsers = FXCollections.observableList(listOfUsersToKeep);
			listOfUsersToKeep = null;
			//
			for (User u : listUsers) {
				u.writeToFile(false);
			}
		}
	}

	private ObservableList<User> listUsers;
	private ArrayList<User> listOfUsersToKeep;
	private User currentUser;
	public PhotosHandler() {
		listUsers = FXCollections.observableArrayList();
		listOfUsersToKeep = null;
		currentUser = null;
	}
	public ObservableList<User> getListUsers() {
		return listUsers;
	}
	public User getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	public void addUser(String userName) {
		User user = new User(userName);
		if(currentUser==null)
			currentUser=user;
		if(listUsers== null)
		{
			listUsers= FXCollections.observableArrayList();
		}
		listUsers.add(user);
	}
	public void deleteUser(int i) {
		if (i>=0 && i<listUsers.size()) {
			User u=listUsers.get(i);
			if (!u.getUsername().equalsIgnoreCase(Register.getRegister().getADMIN_USERNAME())) {
				listUsers.remove(i);
			}
			else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Trying to Deleting Admin.");
				alert.setContentText("Admin cannot be deleted!!");
				alert.showAndWait();
			}
		}
	}
	public User getUser(String userName) {

		for(User u:listUsers)
		{
			if(u.getUsername().equalsIgnoreCase(userName))
				return u;
		}
		return null;
	}
}