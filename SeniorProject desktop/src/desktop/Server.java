/**
Evan
*/

package desktopGit;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Server
{
	final static int BUFFER_SIZE = 4096;
	public static void main(String[] args)
	{
		try
		{
			Path path = Paths.get("C:\\Users\\Evan\\Documents\\School\\Westminster\\Senior Project\\m\\tests2\\converted\\a.wav");
			byte[] b = Files.readAllBytes(path);
			
			int portNum = 5000;
			ServerSocket ss = new ServerSocket(portNum);
			
			while(true)
			{
				System.out.println("waiting for connection (v2):");
				Socket clientSocket = ss.accept();
				System.out.println("Got client");
				
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//				Thread.sleep(500);
				String line = in.readLine();
				
				DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
				int c=0;
				
//				System.out.println(line);
				
				if(line.equals("0"))
				{
					byte[] by = new byte[BUFFER_SIZE];
					FileInputStream s = new FileInputStream(path.toString());
						int rc = s.read(by,0,BUFFER_SIZE);
						
						
						
						while(rc>0)
						{
//							System.out.println(rc);
							out.write(by,0,rc);
							rc = s.read(by,0,BUFFER_SIZE);
							c++;
						}
						s.close();
						
//						out.write("\r\n".getBytes());
//					out.write("-1\r\n".getBytes());
						System.out.println(c);
				}
				else
				{
					System.out.println("test");
					out.write("other\r\n".getBytes());
				}
				
				clientSocket.close();
//				out.write((line+"\r\n").getBytes());
//				for(int i = 0; true ; i ++)
//				{
//					out.write(i+"");
//				}
//				while(clientSocket.isConnected())
//				{
//				out.write("-1");
//				}
				
			}
			
			
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
