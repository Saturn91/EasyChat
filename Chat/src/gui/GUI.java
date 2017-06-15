package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Scrollbar;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class GUI extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private static TextArea textOutput;
	
	private StringBuilder outputText;
	
	private JTextField input;
	private volatile boolean newInput;
	
	private JButton send;
	
	private Font font = new Font("Courier New", Font.BOLD, 18);
	
	public GUI(){
		super("Easy-Chat by Saturn91");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(750, 900);
		setResizable(false);
		setLocation(150, 150);		
		setLayout(null);	
		setBackground(Color.DARK_GRAY);
		
		outputText = new StringBuilder();
		
		textOutput = new TextArea();
		textOutput.setEditable(false);
		textOutput.setBounds(10, 10, 720, 800);
		textOutput.setFont(font);
		textOutput.setForeground(Color.GREEN);
		textOutput.setBackground(Color.DARK_GRAY);
		textOutput.setFocusable(false);
		JScrollPane jsp = new JScrollPane(textOutput, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setBounds(10, 10, 720, 800);
		add(jsp);
		
		input = new JTextField("name");
		input.setBounds(10, 810, 720, 50);
		
		input.setFont(font);
		input.setForeground(Color.GREEN);
		input.setBackground(Color.DARK_GRAY);
		
		Action action = new AbstractAction()
		{
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {
		        newInput = true;
		    }
		};
		
		input.addActionListener(action);
		input.requestFocus();
		JScrollPane jsp2 = new JScrollPane(input, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp2.setBounds(10, 810, 620, 50);
		add(jsp2);
		
		send = new JButton("Send");
		send.setBounds(630, 810, 100, 50);
		send.setBackground(Color.DARK_GRAY);
		send.setForeground(Color.lightGray);
		send.setFont(font);
		
		send.addActionListener(action);
		
		add(send);
		
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
