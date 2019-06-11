// for explanation see main();

package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Jet_SRC extends Application {

	public static void main(String[] args) {
		// begin UI
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent child = FXMLLoader.load(getClass().getResource("../gui/view/start.fxml"));
		Scene scene = new Scene(child, 900, 600);
        stage.setScene(scene);
        stage.setTitle("JetSRC");
        stage.show();	
	}

}
