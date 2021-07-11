package com.darryl.main.beans;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

public class HttpRequest implements Runnable {

	final static String CRLF = "\r\n";
	Socket socket;	
	
	
	public HttpRequest( Socket socket ) throws Exception {
		
		this.socket = socket;
	}

	public void run() {
		
		try {
			processRequest();
		}
		catch ( Exception e ) {
			System.out.println(e);
		}
		
	}
	
	private void processRequest() throws Exception {
		InputStream is = socket.getInputStream();
		DataOutputStream os = new DataOutputStream ( socket.getOutputStream() );
		
		BufferedReader br = new BufferedReader ( new InputStreamReader ( is ) );
		
		String requestLine = br.readLine();
		System.out.println();
		System.out.println( "REQUEST : " + requestLine );
		
		System.out.println("===================================================");
		
		// get and display header lines
		String headerLine = br.readLine();
		
		while ( headerLine != null && headerLine.length() != 0 ) {
			System.out.println(headerLine);
			headerLine = br.readLine();
		}
		
		// Extract the filename from the request line
		StringTokenizer tokens = new StringTokenizer ( requestLine );
		tokens.nextToken(); // skip over the method, which should be "Get"
		String fileName = tokens.nextToken();
		
		// prepend a "." so that file request is within the current directory
		fileName = "." + fileName;
		
		// Open the requested file
		FileInputStream fis = null;
		boolean fileExists = true;
		
		try {
			fis = new FileInputStream ( fileName );
		}
		catch ( FileNotFoundException e ) {
			fileExists = false;
		}
		
		// Construct the response message 
		String statusLine = null;
		String contentTypeLine = null;
		String entityBody = null;
		
		if ( fileExists ) {
			statusLine = "200 File is found";
			contentTypeLine = "Content=type: " + 
					contentType ( fileName ) + CRLF;
		}
		else {
			statusLine = "404 File not found";
			contentTypeLine = "File doesn't exists";
			entityBody = "<HTML>"
					+ "<HEAD><TITLE>Not Found</TITLE></HEAD>"
					+ "<BODY>Not Found</BODY></HTML>";
		}
		
		// Send the status line.
		//os.writeBytes(statusLine);
		
		//os.writeBytes(CRLF);
		
		// Send the content type line.
		//os.writeBytes(contentTypeLine);

		// Send a blank line to indicate the end of the header lines.
		//os.writeBytes(CRLF);
		
		// Send the entity body.
		if ( fileExists ) {
			sendBytes ( fis, os );
			fis.close();
		}
		else {
			os.writeBytes(entityBody);
		}
		os.writeBytes(CRLF);
		
		// close streams and socket
		os.close();
		br.close();
		socket.close();
		System.out.println("Socket is closed now");
	}
	
	private static void sendBytes ( FileInputStream fis, OutputStream os ) 
			throws Exception{
		
		byte[] buffer = new byte[1024];
		int bytes = 0;
		
		// Copy requested file into the socket's output stream
		while ( (bytes = fis.read(buffer)) != -1 ) {
			os.write(buffer, 0, bytes);
		}
	}

	private String contentType(String fileName) {
		if ( fileName.endsWith(".html") || fileName.endsWith(".html") ) {
			return "text/html";
		}
		if ( fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ) {
			return "image/jpeg";
		}
		if ( fileName.endsWith(".gif") ) {
			return "image/gif";
		}
		return "application/octet-stream";
	}

}
