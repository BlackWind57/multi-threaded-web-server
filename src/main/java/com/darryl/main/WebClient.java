package com.darryl.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;

public class WebClient {

	public static void main(String[] args) {
		
		try {
			Socket s = new Socket ("127.0.0.1", 6789);
			System.out.println("Connected to server successfully");
			PrintStream ps = new PrintStream ( s.getOutputStream() );
			InputStream is = s.getInputStream();
			
			// send request
			ps.println("GET /friday/index.html HTTP/1.1\r\n\r\n");
			ps.flush();
			
			System.out.println(ps);
			
			// Close the socket
			s.close();
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}

	}

}
