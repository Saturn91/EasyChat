package main;

import java.util.Scanner;

public class Chat {
	
	public static final String system= "SYSTEM";
	
	private Server server;
	private String internetAddress;
	
	private Client client;
	
	private int port = -1;

	private Scanner in;
	
	public Chat() {
		printLn(system, "welcome to easychat");
		
		initChat();
		boolean running = true;
		while(running){
			print("me", "");
			String send = getInput();
			
			if(send != null){
				if(!send.startsWith("/")){
					client.send(send);
				}else{
					switch(send){
					case "/stop": running = false; break;
					default: printLn(system, "unknown command!");
					}
					
				}				
			}	
		}
		
		server.close();
		client.close();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
	}
	
	private void initChat(){
		print(system, "Do want to create a room? [y]/[n] ");
		
		if(getInput().equals("y")){
			port = -1;
			
			while(server == null){
				enterPort();
				server = new Server(port);
				if(server == null){
					printLn(system, "port is already used!");
				}else{
					break;
				}
			}
			
			printLn(system, "opened a room at: " + server.getIp() + ":" + port);
			client = new Client(port);
		}else{
			while(client == null){
				print(system, "please enter the internet-adress of a chat room: ");
				internetAddress = getInput();
				enterPort();
				client = new Client(internetAddress, port);
				if(client == null){
					printLn(system, "not able to connect to: " + internetAddress + ":" + port);
				}
			}
			
			printLn(system, "connected succesful to Server: " + internetAddress + ":" +port);
		}	
	}
	
	private void enterPort(){
		int testPort = -1; 
		port = -1;
		while(port == -1){
			testPort = -1;
			print(system, "please enter a free port: ");
			try {
				testPort = Integer.parseInt(getInput());
				if(testPort <= 0 || testPort > 65536){
					printLn(system, "port out of range!");
				}else{
					port = testPort;
				}
			} catch (Exception e) {
				printLn(system, "your input is not a valid number!");
			}						
		}
	}
	
	public String getInput(){
		in = new Scanner(System.in);
		return in.nextLine();
	}
	
	public static void printLn(String name, String text){
		System.out.println("["+Log.getDate()+"] " + name + ": " + text);
	}
	
	public static void print(String name, String text){
		System.out.print("["+Log.getDate()+"] " + name + ": " + text);
	}
}
