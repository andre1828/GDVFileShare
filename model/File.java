package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class File {

	private StringProperty fileName;
	private StringProperty filePath;
	private StringProperty fileStatus;

	public File(String fileName, String filePath)
	{
		this.fileName = new SimpleStringProperty(fileName);
		this.fileStatus = new SimpleStringProperty("Ready to send");
	}

	public String getFileName() {
		return fileName.get();
	}

	public StringProperty getFileNameProperty() {
		return fileName;
	}
	
	public StringProperty getFileStatus() {
		return fileStatus;
	}

	public StringProperty getFileStatusProperty() {
		return fileStatus;
	}
	
	public void setFileStatus(String fileStatus) {
		this.fileStatus.set(fileStatus);
	}

	public StringProperty getFilePath() {
		return filePath;
	}

}
