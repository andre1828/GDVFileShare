package controller;

import java.io.File;
import java.util.List;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;

public class Controller {
	
	@FXML
	public List<File> selectFilesBtnAction()
	{
		return new FileChooser().showOpenMultipleDialog(null);
	
	}
}
