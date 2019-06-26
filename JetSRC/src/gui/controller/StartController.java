package gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import gui.model.GuiModel;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import utils.GenericSingletonFactory;

public class StartController implements Initializable{
	
	@FXML 
	public BorderPane main;
	public ProgressIndicator progressIndicator;
	public Label errorLabel;
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		progressIndicator.setProgress(0);
		Task<Parent> loadTask = new Task<Parent>() {
	        @Override
	        public Parent call() throws IOException, InterruptedException {

	            // prepare the aircraft model for loading:
	        	GuiModel dataGuiModel = GenericSingletonFactory.getInstance(GuiModel.class);
	        	progressIndicator.setProgress(0.2); 
	        	dataGuiModel.setup();
	        	progressIndicator.setProgress(0.5);
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/main.fxml"));
	            progressIndicator.setProgress(0.7);
	            Parent root = loader.load();
	            return root ;
	        }
	    };

	    loadTask.setOnSucceeded(e -> {
	        Scene scene = new Scene(loadTask.getValue());
	        Stage stage = new Stage();
	        progressIndicator.setProgress(1);
	        stage.setScene(scene);
	        final Stage oldStage = (Stage) main.getScene().getWindow();
	        oldStage.close();
	        stage.show();
	    });

	    loadTask.setOnFailed(e -> {
	    	loadTask.getException().printStackTrace();
	    	errorLabel.setText("Error loading configuration files, please check the error logs.");
	    	});

	    Thread thread = new Thread(loadTask);
	    thread.start();
	}
    
}
