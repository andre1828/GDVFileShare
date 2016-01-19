package model;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import javafx.collections.ObservableList;

public class Client {

	private String ip;
	private int port;
	private Socket client;
	private ObservableList<model.File> observableList;
	
	
	public void start(String ip, int port) throws UnknownHostException, IOException
	{
		try {
			client = new Socket(ip, port);
		} catch (UnknownHostException e) {
			throw new UnknownHostException();
		} catch (IOException e) {
			throw new IOException();
		} 
	}

	public void sendFiles(List<File> files) throws UnknownHostException, IOException
	{
		try {

			BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());

			DataOutputStream dos = new DataOutputStream(bos);

			dos.writeInt(files.size());

			for (File file : files) 
			{
				dos.writeLong(file.length());

				dos.writeUTF(file.getName());

				BufferedInputStream bis =  new BufferedInputStream(new FileInputStream(file));

				int count = 0;

				while ( (count = bis.read()) !=  -1 ) 
				{
					bos.write(count);
				}

				bis.close();	
			}

			dos.close();

		} catch (IOException e) {
			throw new IOException();			
		}

	}

	public void sendFileName(String fileName)
	{
		try {
			OutputStream temporaryOs = client.getOutputStream();
			//Sends the file name
			temporaryOs.write( (fileName + "\n").getBytes() );

			//Disposes the stream to clean any residues
			temporaryOs.flush();

			temporaryOs = null;

		} catch (IOException e) {
			System.out.println("IOException " + e.getMessage());
		}

	}

	public void sendNumberOfFiles(int number)
	{
		try {
			OutputStream temporaryOs = client.getOutputStream();

			byte[] numberOfFiles = (String.valueOf(number) + "\n").getBytes();

			//Send the number of files that will be sent to server
			temporaryOs.write( numberOfFiles );

			temporaryOs = null;

		} catch (IOException e) {
			System.out.println("IOException " + e.getMessage());
		}


	}

	public void setObservableList(ObservableList<model.File> observableList)
	{
		this.observableList = observableList;
	}
}

