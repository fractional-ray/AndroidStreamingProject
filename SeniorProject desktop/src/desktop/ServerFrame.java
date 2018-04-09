/**
Evan
*/

package desktopGit;

import java.awt.Frame;

import javax.swing.JFrame;



public class ServerFrame extends JFrame
{
	/**
	 *Serial number 
	 */
	private static final long serialVersionUID = -2716404967062765967L;
	
	private ServerPanel currentPanel;
	ServerController parent;
	
	public ServerFrame(ServerController parent)
	{
		this.parent = parent;
		currentPanel = new ServerPanel(parent);
		
		setupFrame();
	}
																																																																																																																																																																																																																																																																																										
	public void start()
	{
		this.setVisible(true);
	
	}
	
	public void setupFrame()
	{
		this.setContentPane(currentPanel);
		this.setTitle("Android Streaming Project");
		this.setSize(800,280);
		this.setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public ServerPanel getPanel()
	{
		return currentPanel;
	}
	
}
