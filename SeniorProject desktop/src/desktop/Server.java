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
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Server
{

	final int PORT_NUM = 5555;
	Path path = Paths.get("C:\\Users\\Evan\\Documents\\School\\Westminster\\Senior Project\\m\\tests2\\unconverted\\sky.mp3");

	final static int BUFFER_SIZE = 4096;

	final static AudioFormat AUDIO_FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, (float) 44100.0, 16, 2, 4, (float) (44100), false);

	private static final Executor exec = Executors.newCachedThreadPool();
	private static ServerSocket ss = null;

	ServerController parent;

	public Server(ServerController parent)
	{
		this.parent = parent;
	}

	public void start() throws IOException
	{
		try
		{

			
			ss = new ServerSocket(PORT_NUM);

			while (true)
			{
				System.out.println("waiting for connection (v2):");
				Socket clientSocket = ss.accept();

				// System.out.println(ss.getInetAddress().getHostAddress());

				System.out.println("Got client");

				ConnectionHandler ch = new ConnectionHandler(clientSocket);
				exec.execute(ch);

			}

		}
		catch (

		Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if (ss != null)
				ss.close();
		}
	}

	private class ConnectionHandler implements Runnable
	{

		Socket clientSocket;

		ConnectionHandler(Socket s)
		{
			clientSocket = s;
		}

		@Override
		public void run()
		{
			BufferedReader in=null;
			DataOutputStream out=null;
			try
			{
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				String line = in.readLine();
				System.out.println(line);

				out = new DataOutputStream(clientSocket.getOutputStream());

				if (line.charAt(0) == ClientCodes.PLAY)
				{
					String parsed = parseSongPlayRequest(line);
					if (parsed != null)
					{
						System.out.println("Parsed: "+parsed);
						System.out.println("Finished streaming " + streamFile(out, clientSocket, parsed) + " segments");
					}
					else
					{
						//////////////////Make this send something back to client notifying.
						out.write("1<>Invalid File\r\n".getBytes());
						System.err.println("ERROR WITH SONG REQUEST");
					}
				}
				else if (line.charAt(0) == ClientCodes.STOP)
				{

				}
				else if (line.charAt(0) == ClientCodes.GET_LIST)
				{
					out.write(constructSongList().getBytes());
				}
				else if (line.charAt(0) == ClientCodes.DISCONNECT)
				{

				}
				else
				{
					System.out.println("test");
					out.write("other\r\n".getBytes());
				}

				clientSocket.close();	
				in.close();
				out.close();
				System.out.println("Connection closed");

			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				
			}

		}

	}

	private static int streamFile(DataOutputStream out, Socket s, String filePath) throws IOException, UnsupportedAudioFileException
	{

		out.write("0\r\n".getBytes());
		System.out.println(filePath);
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
		bais.close();
		sourceAIS.close();
		convert1AIS.close();
		convert2AIS.close();

		return c;
	}

	/**
	 * Constructs the list of songs to send to the app. Format is <> + filename1
	 * + <> + filename2 + <> + ... + <> 0
	 * 
	 * @return
	 */
	private String constructSongList()
	{
		ArrayList<String> a = parent.getSongList();
		String construct = "0<>";

		for (String aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaah : a)
		{
			construct += aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaah + "<>";
		}

		return construct + "0";

	}

	private String parseSongPlayRequest(String songPlay)
	{
		String[] s = songPlay.split("<>");
		System.out.println(s.length);
		for(String ss:s)
		{
			System.out.println(ss);
		}
		if (s.length == 3 && s[0].equals(ClientCodes.PLAY + "") && s[2].equals(ClientCodes.PLAY + ""))
		{
			System.out.println("Test");
			return s[1];
		}
		else
		{
			return null;
		}
	}

	private class ClientCodes
	{
		static final char PLAY = '0';
		static final char STOP = '1';
		static final char GET_LIST = '2';
		static final char DISCONNECT = '3';

	}
}
