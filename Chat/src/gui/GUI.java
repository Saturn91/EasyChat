package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class GUI extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private static TextArea textOutput;
	
	private JTextField input;
	private volatile boolean newInput;
	
	private JButton send;
	
	private Font font = new Font("Courier New", Font.BOLD, 18);
	
	private static int xComponent = 10;
	private static int componentMaxWidth = 720;
	
	private static int yTextOutput = 10;
	
	private static int sendHeight = 50;
	
	
	
	public GUI(){
		super("Easy-Chat by Saturn91");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(750, 460);
		//setResizable(false);
		setLocation(150, 150);		
		setLayout(null);	
		setBackground(Color.DARK_GRAY);
		
		textOutput = new TextArea();
		textOutput.setEditable(false);
		textOutput.setFont(font);
		textOutput.setForeground(Color.GREEN);
		textOutput.setBackground(Color.DARK_GRAY);
		textOutput.setFocusable(false);
		JScrollPane jsp = new JScrollPane(textOutput, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setBounds(xComponent, yTextOutput, componentMaxWidth, (int) (getBounds().getHeight()-110));
		add(jsp);
		
		input = new JTextField("name");
		input.setBounds(10, 360, 720, sendHeight);
		
		input.setFont(font);
		input.setForeground(Color.GREEN);
		input.setBackground(Color.DARK_GRAY);
		
		Action action = new AbstractAction()
		{
			private static final long serialVersionUID = 2L;

			@Override
		    public void actionPerformed(ActionEvent e)
		    {
		        newInput = true;
		    }
		};
		
		input.addActionListener(action);
		input.requestFocus();
		JScrollPane jsp2 = new JScrollPane(input, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp2.setBounds(10, (int) (getBounds().getHeight()-110+10), 620, 50);
		add(jsp2);
		
		send = new JButton("Send");
		send.setBounds(630, (int) (getBounds().getHeight()-110+10), 100, 50);
		send.setBackground(Color.DARK_GRAY);
		send.setForeground(Color.lightGray);
		send.setFont(font);
		
		send.addActionListener(action);
		
		add(send);
		
		addComponentListener(new ComponentAdapter() 
		{  
		        public void componentResized(ComponentEvent evt) {		        	
		        	jsp.setBounds(xComponent, yTextOutput, (int) getBounds().getWidth()-30, (int) (getBounds().getHeight()-110));
		        	jsp2.setBounds(10, (int) (getBounds().getHeight()-110+10), (int) getBounds().getWidth()-130, 50);
		        	send.setBounds((int) getBounds().getWidth()-130+10, (int) (getBounds().getHeight()-110+10), 100, 50);
		        }
		});
		
		setVisible(true);		
	}
	
	public void print(String output){
		textOutput.append(output);
	}
	
	public void printLn(String output){
		textOutput.append(output + "\n");
	}
	
	public String getInput(){
		while(true){
			if(newInput){
				break;
			}
		}
		newInput = false;
		String text = input.getText();
		printLn(text);
		input.setText("");
		return text;
	}
	
	public void setName(String name){
		setTitle("Easy-Chat by Saturn91 - " + name);
	}

}
