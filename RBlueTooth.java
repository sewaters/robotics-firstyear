import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Deque;

public class RBlueTooth
{
	public static final int port = 1123;
	private static ObjectOutputStream oOut;
	private static ObjectOutputStream Pout;

	public static void main(String[] args)
	{
		

	}
	
	public void createConnection()
	{
		ServerSocket server;
		
		try
		{
			server = new ServerSocket(port);
			System.out.println("Awaiting client..");
			Socket client = server.accept();
			System.out.println("CONNECTED");
			OutputStream out = client.getOutputStream();
			oOut = new ObjectOutputStream(out);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void sendObject(GridSquare[][] mazeGrid) 
	{
		
		try
		{

			oOut.writeObject(mazeGrid);
			oOut.flush();
			oOut.reset();
		}
		catch (IOException e)
		{

			e.printStackTrace();
		}
		
		
	}
	

}
