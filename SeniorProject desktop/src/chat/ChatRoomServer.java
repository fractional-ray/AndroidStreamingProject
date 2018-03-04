package chat;
/**
Evan Wang
This is the main class that runs the actual server. It loops continually and accepts connections, passing them to the connection class. 
 */


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatRoomServer
{
	
	public static final int PORT = 1337;
	private static final Executor exec = Executors.newCachedThreadPool();
	private static ServerSocket serverSocket = null;
	private static PrintWriter logWriter;
	private static HashMap<String,Socket> userMap = new HashMap<String,Socket>();
	private static ArrayList<Socket> userSocketList = new ArrayList<Socket>();
	private static ArrayList<String> userNameList = new ArrayList<String>();
	
	public static void main(String[] args) throws IOException
	{

		try
		{
			// establish the socket
			serverSocket = new ServerSocket(PORT);

			System.out.println("Listening for connections:");
			
			while (true)
			{	
				/**
				 * now listen for connections and service the connection in a
				 * separate thread.
				 */
				Socket newSocket = serverSocket.accept();
				System.out.println("Got connection");
				
				Runnable task = new Connection(newSocket,userMap);
				System.out.println(task);
				exec.execute(task);
			}
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			if (serverSocket != null)
				serverSocket.close();
			if(logWriter != null)
				logWriter.close();
		}

	}
}
