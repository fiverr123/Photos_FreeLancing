package controller;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Model.PhotosHandler;
import Model.Register;


/**
 * This class is base of all controllers.
 * @author AlexMandez
 */
public class CBase {
	public static void storeModelToFile() {
		PhotosHandler model = Register.getRegister().getModel();
		if (model!=null) {
			model.save(true);
			//
			try {
				FileOutputStream fileOut = new FileOutputStream(Register.getRegister().DATA_FILE_PATH);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(model);
				out.close();
				fileOut.close();
			}
			catch (IOException i) {
				i.printStackTrace();
			}
		}
	}
	public static void gotoLoginFromAdmin() {
		Register.getRegister().gotoLogin();
	}
	public static void gotoLoginFromAlbum() {
		Register.getRegister().getAlbumStage().hide();
		Register.getRegister().gotoLogin();
	}
	public static void gotoLoginFromUser() {
		Register.getRegister().getAlbumStage().hide();
		Register.getRegister().gotoLogin();
	}
	public static void gotoUserFromLogin() {
		Register.getRegister().getLoginStage().hide();
		Register.getRegister().gotoUser();;
	}
	public static void gotoUserFromAdmin() {
		Register.getRegister().getLoginStage().hide();
		Register.getRegister().gotoUser();
	}
	public static void gotoUserFromAlbum() {
		Register.getRegister().gotoUser();
	}	
}
