/**
Evan
*/

package desktop;

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
	final static int BUFFER_SIZE = 1024;
	public static void main(String[] args)
	{
		try
		{
			Path path = Paths.get("C:\\Users\\Evan\\Music\\Music\\Metal\\current\\Chthonic\\Takasago Army\\02 Legacy Of The Seediq.m4a");
			byte[] b = Files.readAllBytes(path);
			
			int portNum = 5000;
			ServerSocket ss = new ServerSocket(portNum);
			
			while(true)
			{
				System.out.println("waiting for connection:");
				Socket clientSocket = ss.accept();
				System.out.println("Got client");
				
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//				Thread.sleep(500);
				String line = in.readLine();
				
				DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
				
				
				System.out.println(line);
				
				if(line.equals("0"))
				{
					byte[] by = new byte[BUFFER_SIZE];
					FileInputStream s = new FileInputStream(path.toString());
						int rc = s.read(by);
						
						while(rc>0)
						{
							System.out.println(rc);
							out.write(by,0,rc);
							rc = s.read(by);
						}
						s.close();
						
						out.write("\r\n".getBytes());
					out.write("-1\r\n".getBytes());
				}
				else
				{
					System.out.println("test");
					out.write("other\r\n".getBytes());
				}
				
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
