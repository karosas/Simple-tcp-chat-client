package com.chatroom.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerConn implements Runnable {
	private BufferedReader in = null;
	
	public ServerConn(Socket server) throws IOException {
		in = new BufferedReader(new InputStreamReader(
				server.getInputStream()));
	}
	
	public void run() {
		String msg;
		try {
			while((msg = in.readLine()) != null) {
				System.out.println(msg);
			}
		} catch(IOException e ) {
			System.err.println(e);
		}
	}
}
