/**
Evan
*/

package desktopGit;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Server
{
	final static int BUFFER_SIZE = 4096;

	final static AudioFormat AUDIO_FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, (float) 44100.0, 16, 2, 4, (float) (44100), false);

	public static void main(String[] args)
	{
		try
		{
			Path path = Paths.get("C:\\Users\\Evan\\Documents\\School\\Westminster\\Senior Project\\m\\tests2\\unconverted\\sky.mp3");
			byte[] b = Files.readAllBytes(path);

			int portNum = 5000;
			ServerSocket ss = new ServerSocket(portNum);

			while (true)
			{
				System.out.println("waiting for connection (v2):");
				Socket clientSocket = ss.accept();
				
				System.out.println(ss.getInetAddress().getHostAddress());
				
				System.out.println("Got client");

				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				String line = in.readLine();

				DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
				

				if (line.equals("0"))
				{
					System.out.println("Finished streaming "+streamFile(out,clientSocket,path.toString())+" segments");
				}
				else
				{
					System.out.println("test");
					out.write("other\r\n".getBytes());
				}

				clientSocket.close();

			}

		}
		catch (

		Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static int streamFile(DataOutputStream out, Socket s, String filePath) throws IOException, UnsupportedAudioFileException
	{
		
		int c = 0;
		
		Path path = Paths.get(filePath);
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
		AudioInputStream sourceAIS = null;
		AudioInputStream convert1AIS = null;
		AudioInputStream convert2AIS = null;

		bais = new ByteArrayInputStream(Files.readAllBytes(path));

		sourceAIS = AudioSystem.getAudioInputStream(bais);
		AudioFormat sourceFormat = sourceAIS.getFormat();
		AudioFormat convertFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * 2,
				sourceFormat.getSampleRate(), false);
		convert1AIS = AudioSystem.getAudioInputStream(convertFormat, sourceAIS);// target
																				// format
																				// and
																				// source
																				// stream
		convert2AIS = AudioSystem.getAudioInputStream(AUDIO_FORMAT, convert1AIS);

		byte[] by = new byte[BUFFER_SIZE];
		int rc = 0;

		while (true)
		{
			rc = convert2AIS.read(by, 0, BUFFER_SIZE);
			if (rc < 0)
			{
				break;
			}
			out.write(by, 0, rc);
			c++;
		}

		

		return c;
	}
	
	
	
}
