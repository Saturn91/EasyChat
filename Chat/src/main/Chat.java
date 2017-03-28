package main;

import java.util.Scanner;

public class Chat {

	public static final String system= "SYSTEM: ";
	public static final String me = "me: ";

	private Server server;
	private String internetAddress;

	private Client client;

	private static int port = -1;

	private Scanner in;

	public Chat() {
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
					case "/stop": running = false; break;
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
		System.exit(1);
	}

	private void initChat(){
		print(system + "Enter your name: ");
		String name = null;
		while(name==null){
			name = getInput();
		}
		print(system + "Do you want to create a new room? [y]/[n] ");

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
				print(system + "please enter the internet-adress of a chat room: ");
				internetAddress = getInput();
				enterPort();
				client = new Client(internetAddress, port, name);
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
			print(system + "please enter a free port: ");
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
		in = new Scanner(System.in);
		return in.nextLine();
	}

	public static void printLn(String text){
		System.out.println("["+Log.getDate()+"] " + text);
	}

	public static void print(String text){
		System.out.print("["+Log.getDate()+"] " + text);
	}
	
	public static int getPort(){
		return port;
	}
}
