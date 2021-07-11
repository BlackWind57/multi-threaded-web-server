package com.darryl.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.darryl.main.beans.HttpRequest;

public class WebServer {

	private final static int port = 6789;
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;

	public static void main(String[] args) {
		
		try {
			serverSocket = new ServerSocket ( port );
		}
		catch ( IOException e ) {
			System.err.println(e);
		}
		
		while ( true ) {
				try {
					clientSocket = serverSocket.accept();
					System.out.println("Client connection accepted");
					
					// Construct an object to process the Http request message.
					HttpRequest request = new HttpRequest ( clientSocket );
					
					// Create a new thread to process the request
					Thread thread = new Thread ( request );
					
					// Start the thread
					thread.start();
				} 
				
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			
			
		}
		
	}

}
