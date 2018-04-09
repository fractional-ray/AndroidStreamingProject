/**
Evan
*/

package desktopGit;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SpringLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JSplitPane;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class ServerPanel extends JPanel
{
	final String PROJ_LIBRARY = "C:\\users\\evan\\music\\seniorproject";
	SpringLayout springLayout;
	JLabel nameLabel ;
	JLabel currentDirectoryLabel;
	JButton updateDirectoryButton;
	JLabel libraryLabel;
	JTextArea ipArea = new JTextArea();
	JScrollPane ipScrollPane = new JScrollPane(ipArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	JTextArea libraryArea = new JTextArea();
	JScrollPane libraryScrollPane = new JScrollPane(libraryArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);;

	JFileChooser fc = new JFileChooser();
	
	ServerController parent;
	
	ArrayList<File> songs;
	
	public ServerPanel(ServerController parent) {
		
		this.parent = parent;
		
		libraryArea.setEditable(false);
		
		
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		
		springLayout = new SpringLayout();
		springLayout.putConstraint(SpringLayout.WEST, libraryScrollPane, 6, SpringLayout.EAST, ipScrollPane);
		springLayout.putConstraint(SpringLayout.NORTH, ipScrollPane, 118, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.SOUTH, libraryScrollPane, 0, SpringLayout.SOUTH, ipScrollPane);
		springLayout.putConstraint(SpringLayout.EAST, libraryScrollPane, -17, SpringLayout.EAST, this);
		
	
		nameLabel = new JLabel("Android Streaming Project");
		springLayout.putConstraint(SpringLayout.WEST, ipScrollPane, 0, SpringLayout.WEST, nameLabel);
		
		 currentDirectoryLabel = new JLabel("Current Directory:");
		 
		 springLayout.putConstraint(SpringLayout.NORTH, libraryScrollPane, 0, SpringLayout.NORTH, currentDirectoryLabel);
		 updateDirectoryButton = new JButton("Browse");
		 springLayout.putConstraint(SpringLayout.WEST, updateDirectoryButton, 0, SpringLayout.WEST, nameLabel);
		 springLayout.putConstraint(SpringLayout.SOUTH, updateDirectoryButton, -6, SpringLayout.NORTH, ipScrollPane);
		 
		
		 libraryLabel = new JLabel("Library:");
		 
		 springLayout.putConstraint(SpringLayout.NORTH, libraryLabel, 3, SpringLayout.NORTH, nameLabel);
		 springLayout.putConstraint(SpringLayout.WEST, libraryLabel, 0, SpringLayout.WEST, libraryScrollPane);
		 
		
		setLayout(springLayout);
		
		setupConstraints();
		setupListeners();
		initializeLibrary();
		
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7890445573544038926L;
	private JTextField currentDirectoryField;
	private JLabel songNumberLabel;
	
	private void setupConstraints()
	{
		springLayout.putConstraint(SpringLayout.NORTH, nameLabel, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, nameLabel, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, nameLabel, 31, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, nameLabel, 154, SpringLayout.WEST, this);
		
		springLayout.putConstraint(SpringLayout.NORTH, currentDirectoryLabel, 6, SpringLayout.SOUTH, nameLabel);
		springLayout.putConstraint(SpringLayout.WEST, currentDirectoryLabel, 0, SpringLayout.WEST, nameLabel);
		springLayout.putConstraint(SpringLayout.SOUTH, currentDirectoryLabel, 58, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, currentDirectoryLabel, 107, SpringLayout.WEST, this);
		
		currentDirectoryField = new JTextField();
		currentDirectoryField.setEditable(false);
	
		springLayout.putConstraint(SpringLayout.NORTH, updateDirectoryButton, 4, SpringLayout.SOUTH, currentDirectoryField);
		springLayout.putConstraint(SpringLayout.EAST, updateDirectoryButton, 0, SpringLayout.EAST, currentDirectoryField);
		currentDirectoryField.setText("C:\\users\\evan\\music\\seniorProject");
		springLayout.putConstraint(SpringLayout.EAST, ipArea, 0, SpringLayout.EAST, currentDirectoryField);
		springLayout.putConstraint(SpringLayout.NORTH, currentDirectoryField, 6, SpringLayout.SOUTH, currentDirectoryLabel);
		springLayout.putConstraint(SpringLayout.WEST, currentDirectoryField, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, currentDirectoryField, 85, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, currentDirectoryField, 205, SpringLayout.WEST, this);

		
		currentDirectoryField.setColumns(10);
				
		ipArea.setLineWrap(false);
		ipArea.setEditable(false);
		
		updateIpArea();
		
				
		
		ipScrollPane.setPreferredSize(new Dimension(250,112));
		
		add(nameLabel);
		add(currentDirectoryLabel);
		add(currentDirectoryField);
		add(updateDirectoryButton);
		add(libraryLabel);
		add(libraryScrollPane);
		add(ipScrollPane);
		
		songNumberLabel = new JLabel("Number of Songs");
		springLayout.putConstraint(SpringLayout.NORTH, songNumberLabel, 0, SpringLayout.NORTH, nameLabel);
		springLayout.putConstraint(SpringLayout.EAST, songNumberLabel, 0, SpringLayout.EAST, libraryScrollPane);
		add(songNumberLabel);

	}
	
	private void setupListeners()
	{
		 updateDirectoryButton.addActionListener(new ActionListener() {
			 	public void actionPerformed(ActionEvent arg0) {
			 		System.out.println("Test");
			 		updateLibraryArea();
			 	}
			 });
	}

	private ArrayList<String> getIps()
	{
		ArrayList<String> i = new ArrayList<String>();
		String ip; 
		try {
	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	        while (interfaces.hasMoreElements()) {
	            NetworkInterface iface = interfaces.nextElement();
	            // filters out 127.0.0.1 and inactive interfaces
	            if (iface.isLoopback() || !iface.isUp())
	                continue;

	            Enumeration<InetAddress> addresses = iface.getInetAddresses();
	            while(addresses.hasMoreElements()) {
	            	
	                InetAddress addr = addresses.nextElement();
	                
	                if(addr instanceof Inet4Address)
	                {
	                ip = addr.getHostAddress();
	                System.out.println(iface.getDisplayName() + " " + ip);
	                i.add(iface.getDisplayName() + "\n\t" + ip+"\n");
	                
	                }
	            }
	        }
	    } catch (SocketException e) {
	        throw new RuntimeException(e);
	    }
		return i;
	}
	
	private void updateIpArea()
	{
		ArrayList<String> s = getIps();
		for(String a : s)
		{
			ipArea.append(a+"\n");
		}
		
	}
	
	private void updateLibraryArea()
	{
		File dir = new File(chooseNewDirectory());
		updateLibraryArea(dir);
		currentDirectoryField.setText(dir.getAbsolutePath());
	}
	
	private void updateLibraryArea(File dir)
	{
		ArrayList<File> a;
		if(dir.isDirectory())
		{
			a = new ArrayList<File>();
			loadFile(dir,a);
			updateLibraryAreaWithList(a);
			songNumberLabel.setText(a.size()+" songs");
			songs = a;
		}
		else
		{
			System.err.println("Not a directory");
		}
		
	}
	
	private void loadFile(File f,ArrayList<File> a)
	{
		if(f.isDirectory())
		{
			File[] fi = f.listFiles();
			for(File fil: fi)
			{
				if(!fil.isDirectory() && getExtension(fil).equals("mp3"))
				{
					a.add(fil);
				}
				else
				{
					loadFile(fil,a);
				}
			}
		}
	}
	
	private String getExtension(File f)
	{
		return f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf(".")+1);
	}
	
	private String chooseNewDirectory()
	{
		fc.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator")+ "Music"));
	
		int returnVal = fc.showOpenDialog(this);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            //This is where a real application would open the file.
            System.out.println(file.getAbsolutePath());
            return file.getAbsolutePath();
            
        } else {
            System.out.println("Cancelled");
            return null;
        }
	}
	
	
	private void updateLibraryAreaWithList(ArrayList<File> a)
	{
		libraryArea.setText("");
		for(File f : a)
		{
			System.out.println(f.getAbsolutePath());
			libraryArea.append(f.getAbsolutePath()+"\n");
		}
	}
	
	private void initializeLibrary()
	{
		try {
			File f = new File(PROJ_LIBRARY);
			if(!f.exists())
			{
				f = new File(System.getProperty("user.home") + System.getProperty("file.separator")+ "Music");
				if(!f.exists())
				{
					f = new File(System.getProperty("user.home"));
				}
			}
			
			updateLibraryArea(f);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getSongList()
	{
		ArrayList<String> a = new ArrayList<String>();
		
		for(File f: songs)
		{
			a.add(f.getAbsolutePath());
		}
		
		return a;
	}
	
	    
}
