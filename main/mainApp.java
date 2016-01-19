package main;

import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.File;
import view.InterfaceController;

public class mainApp extends Application {

	private Stage stage;
	private AnchorPane layout;
	
	private ObservableList<File> fileList = FXCollections.observableArrayList();
	private ObservableList<File> receivedFilesList = FXCollections.observableArrayList();
	
	@Override
	public void start(Stage primaryStage) {
		this.stage = primaryStage;
		this.stage.setTitle("GDVFileShare");
		
		try {
			
			
			FXMLLoader fxmlLoader = new FXMLLoader();
			
			fxmlLoader.setLocation(mainApp.class.getResource("/view/Interface.fxml"));
			
			layout = (AnchorPane) fxmlLoader.load();
			
			Scene scene = new Scene(layout);
			
			stage.setScene(scene);
			
			stage.show();
			
			InterfaceController interfaceController = fxmlLoader.getController();
			
			interfaceController.setMainApp(this);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		launch(args);
	}

	public ObservableList<File> getFileList() {
		return fileList;
	}

	public void setFileList(ObservableList<File> fileList) {
		this.fileList = fileList;
	}
	
	public ObservableList<File> getReceivedFilesList()
	{
		return receivedFilesList;
	}
}
