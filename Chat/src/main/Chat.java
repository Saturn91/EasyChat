package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import gui.GUI;

public class Chat {

	public static final String system= "SYSTEM: ";
	public static final String me = "me: ";

	private Server server;
	private String internetAddress;

	private Client client;

	private static int port = -1;

	private Scanner in;
	
	private static GUI gui;
	
	private static StringBuilder log;

	public Chat() {
		
		gui = new GUI();
		
		log = new StringBuilder();
		
		printLn(system + "welcome to easychat");

		initChat();
		boolean running = true;
		while(running){
			String send = getInput();

			if(send != null){
				if(!send.startsWith("/")){
					client.send(send);
				}else{
					switch(send){
					case "/stop": running = false; print("\n"); break;
					default: 
						if(send.startsWith("/name:")){
							client.setName(send.substring(6));
							client.sendRaw(send);
						}else{
							if(send.startsWith("/help")){
								Chat.printLn(system + ": /help -> open help");
								Chat.printLn(system + ": /name:newName -> change Name to newName");
								Chat.printLn(system + ": /online -> see who's online");
								Chat.printLn(system + ": /ip -> see the ip");
								Chat.printLn(system + ": /whisper@name:msg -> whisper only to name");
								Chat.printLn(system + ": /stop -> end session and close programm");								
								if(server!=null){
									Chat.printLn(system + ": /kick:Name@reason -> kick \"Name\" for \"reason\"");
									Chat.printLn(system + ": /del -> kick unkown clients");
								}							
							}else{
								if(send.startsWith("/kick:")){
									if(server != null){
										client.sendRaw(send);
									}else{
										Chat.printLn(system + "no permission!");
									}
								}else{
									if(send.startsWith("/del")){
										if(server != null){
											client.sendRaw(send);
										}else{
											Chat.printLn(system + "no permission!");
										}
									}else{
										if(send.startsWith("/online")){
											if(server != null){
												server.printNamesAndIp();
											}else{
												client.sendRaw(send);
											}
										}else{
											client.sendRaw(send);
										}										
									}									
								}
							}
						}
					}					
				}				
			}	
		}
		
		if(server != null){
			server.close();
		}		
		client.close();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//print logfile
		writeLog();
		
		System.exit(1);
	}

	private void initChat(){
		printWithTimeStamp(system + "Enter your name: ");
		String name = null;
		while(name==null){
			name = getInput();
		}
		gui.setName(name);
		printWithTimeStamp(system + "Do you want to create a new room? [y]/[n] ");

		if(getInput().equals("y")){
			port = -1;

			while(server == null){
				enterPort();
				server = new Server(port);
				if(server == null){
					printLn(system + "port is already used!");
				}else{
					break;
				}
			}

			printLn(system + "opened a room at: " + server.getIp() + ":" + port);
			client = new Client(port, name);
		}else{
			while(client == null){
				printWithTimeStamp(system + "please enter the internet-adress of a chat room: ");
				internetAddress = getInput();
				enterPort();
				try{
					client = new Client(internetAddress, port, name);
				}catch(Exception e){
					System.err.println(e.toString());
					printLn(system + "not able to connect to: " + internetAddress + ":" + port);
				}
				
				if(client == null){
					printLn(system + "not able to connect to: " + internetAddress + ":" + port);
				}
			}

			printLn(system + "connected succesful to Server: " + internetAddress + ":" +port);
		}

		printLn(system + "enter: /help to see the available commands!");
	}

	private void enterPort(){
		int testPort = -1; 
		port = -1;
		while(port == -1){
			testPort = -1;
			printWithTimeStamp(system + "please enter a free port: ");
			try {
				testPort = Integer.parseInt(getInput());
				if(testPort <= 0 || testPort > 65536){
					printLn(system + "port out of range!");
				}else{
					port = testPort;
				}
			} catch (Exception e) {
				printLn(system + "your input is not a valid number!");
			}						
		}
	}

	public String getInput(){
		String input = gui.getInput();
		System.out.println(input);
		log.append(input);
		return input;
	}

	public static void printLn(String text){
		System.out.println("["+Log.getDate()+"] " + text);
		gui.printLn("["+Log.getDate()+"] " + text);
		log.append("["+Log.getDate()+"] " + text + "\n");
	}

	public static void printWithTimeStamp(String text){
		System.out.print("["+Log.getDate()+"] " + text);
		gui.print("["+Log.getDate()+"] " + text);
		log.append("["+Log.getDate()+"] " + text);
	}
	
	public static void print(String text){
		System.out.print(text);
		gui.print(text);
		log.append(text);
	}
	
	public static int getPort(){
		return port;
	}
	
	public static void writeLog(){
		String dateStamp = Log.getDate().replaceAll(":", "_");
		printLn("printing log...");
		try{
		    PrintWriter writer = new PrintWriter("EC_Log_" + dateStamp + ".txt", "UTF-8");
		    String[] logText = log.toString().split("\n");
		    for(String s: logText){
		    	writer.println(s);
		    }
		    
		    writer.close();
		} catch (IOException e) {
		   // do something
		}
	}
}
