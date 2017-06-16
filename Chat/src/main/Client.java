package main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable{
	private Socket client;

	public final static String empty = "@@";

	private String messageRecived = empty;

	private PrintWriter out;
	private BufferedReader in;

	private boolean running = true;
	
	private String name;

	public Client(int port, String name) {
		this.name = name;
		Log.printLn("--starting Client--", getClass().getName(), 1);
		try {
			client = new Socket("localhost", port);
		} catch (UnknownHostException e) {
			Log.printErrorLn("unknown host!", getClass().getName(), 1);
		} catch (IOException e) {
			Log.printErrorLn("not able to start client", getClass().getName(), 1);
		}
		init();
	}

	public Client(String inetAdress, int port, String name) {
		this.name = name;
		Log.printLn("--starting Client--", getClass().getName(), 1);
		try {
			client = new Socket(inetAdress, port);
		} catch (UnknownHostException e) {
			Log.printErrorLn("unknown host!", getClass().getName(), 1);
		} catch (IOException e) {
			Log.printErrorLn("not able to start client", getClass().getName(), 1);
		}
		init();
	}

	public void init(){

		try {
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		new Thread(this).start();
		out.println("/name:" + name);
	}

	@Override
	public void run() {
		String line;
		while(running){
			try
			{
				line=in.readLine();
				if(line==null) break;
				Chat.printLn(line);
			} catch (IOException e) {}
		}
		Chat.printLn(Chat.system + "client closed");
	}

	public void send(String message){
		out.println(name + ": "+message);
	}
	
	public void sendRaw(String message){
		out.println(message);
	}

	public void close(){
		running = false;
	}

	public void setName(String name) {
		this.name = name;		
	}


}
