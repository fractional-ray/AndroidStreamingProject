/**
Evan
*/

package desktopGit;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ServerController
{
	ArrayList<String> songs;
	
	ServerFrame sf;
	Server server;
	
	public ServerController()
	{
		try
		{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();	
		}
		
		sf = new ServerFrame(this);
		sf.start();
		
		server = new Server(this);
		try
		{
			server.start();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getSongList()
	{
		return sf.getPanel().getSongList();
	}
	
	public void setSongList(ArrayList<String> a)
	{
		songs = a;
	}
	
	
	
	
	
}
