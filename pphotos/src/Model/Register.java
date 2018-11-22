package Model;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import controller.CBase;
import controller.ControllerInterface;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Register {
	static Register register=null;

	  String ADMIN_USERNAME = "admin";

	public String getADMIN_USERNAME() {
		return ADMIN_USERNAME;
	}

	public void setADMIN_USERNAME(String aDMIN_USERNAME) {
		ADMIN_USERNAME = aDMIN_USERNAME;
	}

	String ALBUM_NAME_SEARCH_BY_TAG	= " Tag Search Result";
	String ALBUM_NAME_SEARCH_BY_DATE	= " Date Search Result";

	public String getALBUM_NAME_SEARCH_BY_TAG() {
		return ALBUM_NAME_SEARCH_BY_TAG;
	}

	public String getALBUM_NAME_SEARCH_BY_DATE() {
		return ALBUM_NAME_SEARCH_BY_DATE;
	}

	public static Register getRegister()
	{
		if(register==null)
		{
			register=new Register();
		}
		return register;
	}
	public String epochToLocalTime(long time) {
		LocalDateTime datetime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
		//
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		//
		return datetime.format(formatter);
	}
	public  <T,U> List<T> filter(Collection<T> tCollection, U u, BiPredicate<T,U> biPredicate) {
		return tCollection.stream().filter(t -> biPredicate.test(t, u)).collect(Collectors.toList());
	}
	public  <T extends Comparable<T>> boolean search(List<T> source, List<T> condition, BiPredicate<T,T> bp) {
		boolean isAllMatch = true;
		//
		for (T c : condition) {
			boolean isMatch = source.stream().anyMatch(s -> bp.test(s, c));
			if (!isMatch) {
				isAllMatch = false;
				break;
			}
		}
		return isAllMatch;
	}
	private  PhotosHandler model;
	public String DATA_FILE_PATH 	= "photos.dat";
	public PhotosHandler getModel() {
		if (model == null) {
			try {
				FileInputStream fileIn = new FileInputStream(DATA_FILE_PATH);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				model = (PhotosHandler)in.readObject();
				model.save(false);
				in.close();
				fileIn.close();
			}
			catch(IOException | ClassNotFoundException i) {
				model = null;
				//i.printStackTrace();
			}
			//
			if (model==null) {
				model = new PhotosHandler();
				//
				model.addUser("admin");
				model.addUser("stock");
				model.setCurrentUser(model.getUser("stock"));
				//
				User user = model.getCurrentUser();
				//
				Album album1 = user.addAlbum("ocean");
				album1.addToAlbum(Photo.createPhoto("stockphotos/ocean1.jpg", null));
				album1.addToAlbum(Photo.createPhoto("stockphotos/ocean2.jpg", null));
				album1.addToAlbum(Photo.createPhoto("stockphotos/ocean3.jpg", null));
				album1.addToAlbum(Photo.createPhoto("stockphotos/ocean4.jpg", null));
				album1.addToAlbum(Photo.createPhoto("stockphotos/ocean5.jpg", null));
				//
				Album album2 = user.addAlbum("snake");
				album2.addToAlbum(Photo.createPhoto("stockphotos/snake1.jpg", null));
				album2.addToAlbum(Photo.createPhoto("stockphotos/snake2.jpg", null));
				album2.addToAlbum(Photo.createPhoto("stockphotos/snake3.jpg", null));
				album2.addToAlbum(Photo.createPhoto("stockphotos/snake4.jpg", null));
				album2.addToAlbum(Photo.createPhoto("stockphotos/snake5.jpg", null));
			}
		}
		//
		return model;
	}

	public void gotoLogin() {
		loginStage.setScene(sceneLogin);
		//
		controllerLogin.callBeforeShow();
		loginStage.setTitle("Welcome To Photos App!");
		loginStage.show();
	}


	String ADMIN_HELP_FXML 	= "/view/AdminHelp.fxml";
	String PHOTOS_HELP_FXML = "/view/PhotosHelp.fxml";
	Scene sceneLogin 			= null;
	Scene sceneAdmin 				= null;
	Scene sceneUser 				= null;
	Scene sceneAlbum 				= null;
	ControllerInterface controllerLogin 	= null;
	ControllerInterface controllerAdmin 	= null;
	ControllerInterface controllerUser 	= null;
	ControllerInterface controllerAlbum 	= null;
	Stage loginStage				= null;
	public String getDATA_FILE_PATH() {
		return DATA_FILE_PATH;
	}

	public void setDATA_FILE_PATH(String dATA_FILE_PATH) {
		DATA_FILE_PATH = dATA_FILE_PATH;
	}

	public String getADMIN_HELP_FXML() {
		return ADMIN_HELP_FXML;
	}

	public void setADMIN_HELP_FXML(String aDMIN_HELP_FXML) {
		ADMIN_HELP_FXML = aDMIN_HELP_FXML;
	}

	public String getPHOTOS_HELP_FXML() {
		return PHOTOS_HELP_FXML;
	}

	public void setPHOTOS_HELP_FXML(String pHOTOS_HELP_FXML) {
		PHOTOS_HELP_FXML = pHOTOS_HELP_FXML;
	}

	public Scene getSceneLogin() {
		return sceneLogin;
	}

	public void setSceneLogin(Scene sceneLogin) {
		this.sceneLogin = sceneLogin;
	}

	public Scene getSceneAdmin() {
		return sceneAdmin;
	}

	public void setSceneAdmin(Scene sceneAdmin) {
		this.sceneAdmin = sceneAdmin;
	}

	public Scene getSceneUser() {
		return sceneUser;
	}

	public void setSceneUser(Scene sceneUser) {
		this.sceneUser = sceneUser;
	}

	public Scene getSceneAlbum() {
		return sceneAlbum;
	}

	public void setSceneAlbum(Scene sceneAlbum) {
		this.sceneAlbum = sceneAlbum;
	}

	public ControllerInterface getControllerLogin() {
		return controllerLogin;
	}

	public void setControllerLogin(ControllerInterface controllerLogin) {
		this.controllerLogin = controllerLogin;
	}

	public ControllerInterface getControllerAdmin() {
		return controllerAdmin;
	}

	public void setControllerAdmin(ControllerInterface controllerAdmin) {
		this.controllerAdmin = controllerAdmin;
	}

	public ControllerInterface getControllerUser() {
		return controllerUser;
	}

	public void setControllerUser(ControllerInterface controllerUser) {
		this.controllerUser = controllerUser;
	}

	public ControllerInterface getControllerAlbum() {
		return controllerAlbum;
	}

	public void setControllerAlbum(ControllerInterface controllerAlbum) {
		this.controllerAlbum = controllerAlbum;
	}

	public Stage getLoginStage() {
		return loginStage;
	}

	public void setLoginStage(Stage loginStage) {
		this.loginStage = loginStage;
	}

	public Stage getAlbumStage() {
		return albumStage;
	}

	public void setAlbumStage(Stage albumStage) {
		this.albumStage = albumStage;
	}

	public static void setRegister(Register register) {
		Register.register = register;
	}

	public void setALBUM_NAME_SEARCH_BY_TAG(String aLBUM_NAME_SEARCH_BY_TAG) {
		ALBUM_NAME_SEARCH_BY_TAG = aLBUM_NAME_SEARCH_BY_TAG;
	}

	public void setALBUM_NAME_SEARCH_BY_DATE(String aLBUM_NAME_SEARCH_BY_DATE) {
		ALBUM_NAME_SEARCH_BY_DATE = aLBUM_NAME_SEARCH_BY_DATE;
	}

	public void setModel(PhotosHandler model) {
		this.model = model;
	}

	Stage albumStage				= null;


	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(CBase.class.getResource("/view/Login.fxml"));
		Parent root = loader.load();
		sceneLogin = new Scene(root);
		controllerLogin = loader.getController();
		loader = new FXMLLoader();
		loader.setLocation(CBase.class.getResource("/view/Admin.fxml"));
		root = loader.load();
		sceneAdmin = new Scene(root);
		controllerAdmin = loader.getController();
		loader = new FXMLLoader();
		loader.setLocation(CBase.class.getResource("/view/User.fxml"));
		root = loader.load();
		sceneUser = new Scene(root);
		controllerUser = loader.getController();
		loader = new FXMLLoader();
		loader.setLocation(CBase.class.getResource("/view/Album.fxml"));
		root = loader.load();
		sceneAlbum = new Scene(root);
		controllerAlbum = loader.getController();
		primaryStage.setTitle("Not yet");
		primaryStage.setResizable(false);
		loginStage = primaryStage;
		albumStage = new Stage();
		primaryStage.setTitle("Not yet");
		albumStage.setResizable(false);
		gotoLogin();
	}

	public void gotoUser() {
		PhotosHandler model = Register.getRegister().getModel();
		albumStage.setScene(sceneUser);

		controllerUser.callBeforeShow();
		albumStage.setTitle("Welcome " + model.getCurrentUser().getUsername() + "!");
		albumStage.show();
	}
	public  void gotoAdminFromLogin() {
		loginStage.setScene(sceneAdmin);
		controllerAdmin.callBeforeShow();
		loginStage.setTitle("Welcome to the Admin Tool!");
		loginStage.show();
	}
	public void gotoAlbumFromUser() {
		PhotosHandler model = Register.getRegister().getModel();
		albumStage.setScene(sceneAlbum);
		controllerAlbum.callBeforeShow();
		albumStage.setTitle("Album " + model.getCurrentUser().getCurrentAlbum().getAlbumName());
		albumStage.show();
	}
	public void Help(String fml) {
		Parent root;
		try {
			root = FXMLLoader.load(CBase.class.getResource(fml));
			//
			Stage window = new Stage();
			//
			window.initModality(Modality.APPLICATION_MODAL);
			// Create scene
			Scene scene = new Scene(root);
			window.setScene(scene);
			//
			// Set properties of stage
			window.setTitle("Help");
			window.setResizable(false);
			// Render it
			window.show();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
