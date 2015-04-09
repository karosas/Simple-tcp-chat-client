package com.chatroom.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
	private static int port =9764;
	private static String host = "localhost";
	
	private static String nick;
	
	private static BufferedReader stdIn;
	private Socket server;
	private PrintWriter out;
	private BufferedReader in;
	ServerConn sc=null;
	
	
	public ChatClient() throws UnknownHostException, IOException {
		
		server = new Socket(host,port);
		out = new PrintWriter(server.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(
							server.getInputStream()));
		stdIn = new BufferedReader(new InputStreamReader(System.in));
		sc = new ServerConn(server);
	}
	
	public boolean login(String nick) throws IOException {
		out.println("NICK " + nick);
		String serverResponse = in.readLine();
		if(serverResponse.equals("SERVER: OK")) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getNick() {
		return nick;
	}
	
	public Socket getSocket() {
		return server;
	}
	
	public void sendMsg(String msg) throws IOException {
		if(msg.length() > 0) {
			out.println(msg);
		}
	}
	
	public void logout() throws IOException {
		out.println("LOGOUT");
		server.close();
	}
	

}
