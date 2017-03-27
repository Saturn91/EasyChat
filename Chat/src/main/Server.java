package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Server {
	private ServerSocket ssocket;
	private HashMap<String, ClientDto> clients;
	private int onlineCounter = 0;

	private boolean running = true;

	public Server(int port){
		try {
			ssocket = new ServerSocket(port);
		} catch (Exception e) {
			Log.printLn("not able to start Sever", getClass().getName(), 1);
		}
		clients = new HashMap<>();
		Log.printLn("--Server started--", getClass().getName(), 3);

		new Thread(new Runnable() {

			@Override
			public void run() {
				do{
					ClientDto client = null;
					// a "blocking" call which waits until a connection is requested
					try {
						client = new ClientDto(ssocket.accept());
						clients.put(client.getAddres(), client);
						onlineCounter ++;
						System.out.println();
						Chat.printLn(Chat.system, "Client joined [" + onlineCounter + "] people are online");
					} catch (IOException e) {
						Chat.printLn(Chat.system, "room closed");
					}
				}while(running);	
				Log.printLn("ServerThread got closed", getClass().getName(), 3);
			}
		}).start();

	}

	public void sendtoAll(String msg){

	}

	public boolean isValid(){
		return ssocket != null;
	}

	public String getIp(){
		try {
			ssocket.getInetAddress();
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "not found!";
	}	

	private class ClientDto implements Runnable{
		private Socket socket;
		private String name;
		private String addres;
		private boolean running = true;

		public ClientDto(Socket socket) {
			super();
			this.socket = socket;
			this.addres = socket.getInetAddress().getHostAddress();

			new Thread(this).start();
		}

		public String getAddres(){
			return addres;
		}

		@Override
		public void run() {
			// open up IO streams
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			PrintWriter out = null;
			try {
				out = new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// waits for data and reads it in until connection dies
			// readLine() blocks until the server receives a new line from client
			String s;
			try {
				while ((s = in.readLine()) != null) {
					out.println(s);
					if(!running){
						break;
					}
				}										
			} catch (IOException e) {
			}

			// close IO streams, then socket
			Chat.printLn(name, "left room!");

			out.close();
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.printLn("ClientSocket-Thread " + name + " got closed!", getClass().getName(), 3);
		}

		public void close(){
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			running = false;
		}
	}

	public void close(){
		running = false;
		for(ClientDto c: clients.values()){
			c.close();
		}

		try {
			ssocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
