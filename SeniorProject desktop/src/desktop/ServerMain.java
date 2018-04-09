/**
Evan
*/

package desktopGit;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ServerMain
{

	public static void main(String[] args)
	{
		try
		{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();	
		}
		
		ServerFrame sf = new ServerFrame();
		sf.start();
	}

}
