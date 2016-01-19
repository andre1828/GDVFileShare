package view;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import main.mainApp;
import model.Client;
import model.Converter;
import model.File;
import model.Server;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;


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
	private TableColumn<File, String> fileColumnClient;

	@FXML 
	private TableColumn<File, String> statusColumnClient;

	@FXML
	private TableColumn<File, String> fileColumnServer;

	@FXML
	private TableColumn<File, String> statusColumnServer;

	@FXML
	private TextField portServer;

	@FXML
	private Button startServer;

	@FXML
	private Button saveIn;

	@FXML
	private Label destinationFolderLabel;

	@FXML 
	private Label serverStatus;

	@FXML
	private TableView<File> fileTableServer;

	private List<java.io.File> selectedFiles;
	
	private ObservableList<File> observableSelectedFiles;

	private List<java.io.File> receivedFiles;

	private java.io.File destinationFolder;

	public void setMainApp(mainApp mainApp)
	{
		this.mainapp = mainApp;

		fileTableClient.setItems(mainapp.getFileList());
		fileTableServer.setItems(mainApp.getReceivedFilesList());
	}


	@FXML
	public void selectFilesBtnAction()
	{	
		Converter converter = new Converter();

		//Save the selected files in a list
		selectedFiles = new FileChooser().showOpenMultipleDialog(null);

		//Converts the list of selected files into a ObservableList<model.File> so it can feed the TableView in the client tab
		observableSelectedFiles = FXCollections.observableArrayList(  converter.toObservableList(selectedFiles) );

		//Transfers the ObservableList to the list in mainApp
		mainapp.getFileList().addAll(observableSelectedFiles);

		//Populates the file name collumn
		fileColumnClient.setCellValueFactory( file -> file.getValue().getFileNameProperty());

		//Populates the file status column
		statusColumnClient.setCellValueFactory(file -> file.getValue().getFileStatus());

		//Populates table with ObservableList
		fileTableClient.setItems(mainapp.getFileList());

	}

	@FXML
	public void sendFiles()
	{
		
		
		Thread clientThread = new Thread(() -> 
		{
			
			try {

				Client client = new Client();

				client.start(destinationIP.getText(), Integer.parseInt(destinationPort.getText()));

				client.setObservableList(observableSelectedFiles);
				
				client.sendFiles(selectedFiles);

				
			} catch (UnknownHostException e) {

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erro");
				alert.setContentText("UnkonwnHostException \n could not connect to server ");
				alert.showAndWait();

			} catch (NumberFormatException e) {

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erro");
				alert.setContentText("NumberFormatException \n input on Port textbox is invalid ");
				alert.showAndWait();

			} catch (IOException e) {
				
			}

		});
		
		clientThread.setDaemon(true);
		clientThread.start();
		
	}

	@FXML 
	public void chooseDestinationFolder()
	{
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Where to save the received files");
		destinationFolder = directoryChooser.showDialog(null);
		destinationFolderLabel.setText(destinationFolder.toString());
	}


	@FXML
	public void startServer()
	{			
		
		Thread serverThread = new Thread( () ->  
		{

			try {
				Server server = new Server();

				server.setDestinationFolder(destinationFolder); 
				
				server.start(Integer.parseInt(portServer.getText()));

				receivedFiles = new ArrayList<>();

				ObservableList<File> observableReceivedFiles = FXCollections.observableArrayList( new Converter().toObservableList(receivedFiles) );

				fileColumnServer.setCellValueFactory(file -> file.getValue().getFileNameProperty());
				
				statusColumnServer.setCellValueFactory(file -> file.getValue().getFileStatus());

				fileTableServer.setItems(observableReceivedFiles); 

				server.setObservableList(observableReceivedFiles);
				
				server.receiveFiles();

			} catch (Exception e) {
				e.printStackTrace();
			}

			
			
		});

		if ( destinationFolder == null ) 
		{
			Alert noDestinationFolderAlert = new Alert(AlertType.WARNING);
			noDestinationFolderAlert.setContentText("Please select a folder to save the files before starting the server");

			noDestinationFolderAlert.showAndWait();
			
		}
		else if (portServer.getText() == null   || portServer.getText() == ""  ||  !portServer.getText().matches("[0-9]+"))
		{
			Alert invalidPortAlert = new Alert(AlertType.WARNING);
			invalidPortAlert.setTitle("Warning");
			invalidPortAlert.setHeaderText("Invalid port number");
			invalidPortAlert.setContentText("Please enter a valid number in the \"Port\" text field ");
			invalidPortAlert.showAndWait();
		}else 
		{
			serverThread.setDaemon(true);
			serverStatus.setText("Server listening on : " + portServer.getText());
			serverThread.start();
			
			TrayNotification serverOnNotification = new TrayNotification("Server on!", "The server is ready and listening on port " + portServer.getText(), NotificationType.SUCCESS);
			serverOnNotification.setAnimationType(AnimationType.POPUP);
			serverOnNotification.showAndDismiss(new Duration(3000));
		}


	}

}
