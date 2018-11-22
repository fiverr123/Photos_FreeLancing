package Model;


import controller.CBase;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This class is used to launch the application and store data upon stop.
 * @author AlexMandez
 */
public class Driver extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		Register.getRegister().start(primaryStage);
	}
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void stop(){
        CBase.storeModelToFile();
	}
}