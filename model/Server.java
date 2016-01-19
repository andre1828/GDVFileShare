package model;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.collections.ObservableList;

public class Server {

	private Socket client;
	private ServerSocket server;
	private java.io.File destinationFolder;
	private ObservableList<File> observableList;

	public void start(int port)
	{
		try {
			server = new ServerSocket(port);

			client = server.accept();


		} catch (IOException e) {
			System.out.println("IOException " + e.getMessage());
		}
	}

	public void receiveFiles() throws IOException
	{	

		BufferedInputStream bis = new BufferedInputStream( client.getInputStream() );

		DataInputStream dis = new DataInputStream(bis);

		int numberOfFiles = dis.readInt();

		int loops = 0;
		while (loops < numberOfFiles) 
		{
			try {

				long fileLength = dis.readLong();

				String fileName = dis.readUTF();

				FileOutputStream fos = new FileOutputStream(destinationFolder + "\\" + fileName);

				System.out.println(destinationFolder + "\\" + fileName);
				
				BufferedOutputStream bos = new BufferedOutputStream(fos);

				for(int j = 0; j < fileLength; j++) 
				{
					bos.write(bis.read());
				}

				bos.close();
				
				observableList.add( new Converter().toModelFile( new java.io.File(destinationFolder + "\\" + fileName) ) );
				
			} catch (IOException e) {
				System.out.println("IOException " + e.getMessage());
				System.out.println("Metodo receiveFiles");
			} 

			loops++;
		}

		dis.close();
	}

	public String receiveFileName()
	{

		try {
			InputStream temporaryIs = client.getInputStream();

			InputStreamReader isr = new InputStreamReader(temporaryIs);

			BufferedReader br = new BufferedReader(isr);

			String fileName = br.readLine();

			temporaryIs = null;
			isr = null;
			br = null;

			return fileName;

		} catch (IOException e) 
		{
			System.out.println("IOException " + e.getMessage());
			System.out.println("Metodo receiveFileName");
			return null;
		}

	}

	

	public int getServerPort()
	{
		return server.getLocalPort();
	}

	public void setDestinationFolder(java.io.File destinationFolder)
	{
		this.destinationFolder = destinationFolder;
	}

	public void setObservableList(ObservableList<File> observableList)
	{
		this.observableList = observableList;
	}
}



