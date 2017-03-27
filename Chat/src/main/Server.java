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
						Chat.printLn("yet unknown Client: " + client.getAddres() + " has joined");
					} catch (IOException e) {
						Chat.printLn(Chat.system + "room closed");
					}
				}while(running);	
				Log.printLn("ServerThread got closed", getClass().getName(), 3);
			}
		}).start();

	}

	public void sendtoAll(String msg){
		for(ClientDto c: clients.values()){
			c.send(msg);
		}
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
		private String name = null;
		private String addres;
		private boolean running = true;

		private BufferedReader in;
		private PrintWriter out;

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
			in = null;
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			out = null;
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
					if(!s.startsWith("/")){
						sendtoAll(s);
					}else{
						if(s.startsWith("/name:")){
							if(name != null){
								sendtoAll(Chat.system + name + " has changed to " + s.substring(6));
							}else{
								Chat.printLn(getAddres() + " is valid!");
								sendtoAll(Chat.system + "Client: " + s.substring(6) + " joined [" + onlineCounter + "] people are online");
							}
							name = s.substring(6);
						}else{
							if(s.startsWith("/kick:")){
								kick(s.substring(6));

							}else{
								if(s.startsWith("/del")){
									delUnknownClients();
								}else{
									Chat.printLn(Chat.system + "unknown command: <" + s + ">");
								}								
							}
						}

					}

					if(!running){
						break;
					}
				}										
			} catch (IOException e) {
			}

			// close IO streams, then socket
			Chat.printLn(Chat.system + ":" + name + " - left room!");

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
			onlineCounter--;
			
			Log.printLn("ClientSocket-Thread " + name + " got closed!", getClass().getName(), 3);
			Chat.printLn(Chat.system + "[" + onlineCounter + "] people are online");
		}

		private void delUnknownClients() {
			int counter = 0;
			for(ClientDto c: clients.values()){
				if(c.name == null){
					counter++;
					c.close();
				}
			}
			
			Chat.printLn(Chat.system + "deleted: " + counter + " unknown Clients");
		}

		public void send(String msg){
			out.println(msg);
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

	public void kick(String name){
		for(ClientDto c: clients.values()){
			if(c.name.equals(name)){
				Chat.printLn(Chat.system + "kicked " + name);
				c.close();
			}
		}
	}

}
