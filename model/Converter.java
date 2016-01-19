package model;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Converter {

	public ObservableList<File> toObservableList(List<java.io.File> fileList)
	{
		ObservableList<File> observableList = FXCollections.observableArrayList();
		for (java.io.File file : fileList)
		{
			observableList.add(new File(file.getName(), file.getAbsolutePath()) );
		}
		
		return observableList;
		
	}
	
	public File toModelFile(java.io.File file)
	{
		File modelFile = new File(file.getName(), file.getAbsolutePath());
		modelFile.setFileStatus("Received");
		return modelFile;
	}
}
