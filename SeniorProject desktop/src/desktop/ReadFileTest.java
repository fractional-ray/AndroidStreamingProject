/**
Evan
*/

package desktop;

import java.io.FileInputStream;
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
			int rc = s.read(by);
			
			int i = 0;
			while(rc>0)
			{	
				i++;
				System.out.println(rc+" "+i);
				rc = s.read(by);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
