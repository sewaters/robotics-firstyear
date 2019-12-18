import java.io.*;
import java.net.*;

import javax.swing.JFrame;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class CompBlueTooth
{
	private static String ip = "10.0.1.1";
	static ObjectInputStream oIn;

	public static void main(String[] args) throws IOException
	{
		
		if(args.length > 0)ip = args[0];
		Socket sock = new Socket(ip, 1123);
		InputStream in = sock.getInputStream();
		oIn = new ObjectInputStream(in);
		System.out.println("Connected");
		drawGridScreen();
	}
	
	public static void drawGridScreen() 
	{
		GridSquare[][] mazeGrid = null;
		JFrame window = new JFrame();
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setBounds(30, 30, 1000, 1000);
	    
		while (true) 
		{
			try
			{
				mazeGrid = (GridSquare[][]) oIn.readObject();
			}
			catch (IOException | ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			
			GridDisplay.getMaze(mazeGrid);
			
			
		    window.getContentPane().add(new GridDisplay());
		    window.setVisible(true);
		}
	}
	

}
