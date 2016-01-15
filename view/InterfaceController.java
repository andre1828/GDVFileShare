package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import main.mainApp;
import model.Converter;
import model.File;

public class InterfaceController {
	
	private mainApp mainapp;
	
	@FXML
	private Button selectFiles;
	
	@FXML
	private TextField destinationIP;
	
	@FXML 
	private TextField destinationPort;
	
	@FXML 
	private Button sendFiles;
	
	@FXML
	private TableView<File> fileTableClient;
	
	@FXML 
	private TableColumn<File, String> fileColumn;
	
	@FXML 
	private TableColumn<File, String> statusColumn;
	
	@FXML
	private TextField portServer;
	
	@FXML
	private Button startServer;
	
	@FXML 
	private Label serverStatus;
	
	@FXML
	private TableView<File> fileTableServer;
	
	public void setMainApp(mainApp mainApp)
	{
		this.mainapp = mainApp;
		
		fileTableClient.setItems(mainapp.getFileList());
		fileTableServer.setItems(mainApp.getReceivedFilesList());
	}
	
	
	@FXML
	public void selectFilesBtnAction()
	{
		FileChooser fileChooser = new FileChooser();
		
		Converter converter = new Converter();
		
		ObservableList<File> selectedFiles = FXCollections.observableArrayList(  converter.toObservableFile(fileChooser.showOpenMultipleDialog(null)) );
		
		mainapp.getFileList().addAll(selectedFiles);
	
		fileColumn.setCellValueFactory( file -> file.getValue().getFileNameProperty());
		
		statusColumn.setCellValueFactory(file -> file.getValue().getFileStatus());
		
		fileTableClient.setItems(mainapp.getFileList());
		
	}

}
