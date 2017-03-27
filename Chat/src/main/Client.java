package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable{
	private Socket client;
	
	public final static String empty = "@@";
	
	private String messageRecived = empty;
	
	private DataOutputStream out;

	private boolean running = true;
	
	public Client(int port) {
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
	
	public Client(String inetAdress, int port) {
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
		new Thread(this).start();
		try {
			out = new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(running){			
			
		}
		Log.printLn("Client-Thread got closed", getClass().getName(), 3);
	}
	
	public String getMessage(){
		String msg = messageRecived;
		messageRecived = empty;
		return msg;
	}
	
	public void send(String message){
		try {
			out.writeBytes(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		running = false;
	}
	
	
}
