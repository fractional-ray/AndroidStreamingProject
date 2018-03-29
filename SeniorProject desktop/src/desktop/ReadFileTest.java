/**
Evan
*/

package desktopGit;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReadFileTest
{
	static final int BUFFER_SIZE = 1024;
	public static void main(String[] args)
	{
		Path path = Paths.get("C:\\Users\\Evan\\Music\\Music\\Metal\\current\\Chthonic\\Takasago Army\\02 Legacy Of The Seediq.m4a");
		byte[] by = new byte[BUFFER_SIZE];
		
		try {
		FileInputStream s = new FileInputStream(path.toString());
		FileOutputStream o = new FileOutputStream("C:\\users\\evan\\documents\\miscellaneous\\test.mp3");
			int rc = s.read(by);
			
			int i = 0;
			while(rc>0)
			{	
				i++;
//				System.out.println(rc+" "+i);
				o.write(by,0,rc);
				rc = s.read(by,0,BUFFER_SIZE);
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
