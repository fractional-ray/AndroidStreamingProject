/**
Evan
*/

package desktopGit;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


import java.io.*;

public class ConvertTest
{

	final static int CHANNELS_STEREO = 2;

	public static void main(String[] args)
	{
		Path path = Paths.get("C:\\Users\\Evan\\Documents\\School\\Westminster\\Senior Project\\m\\tests2");

		try
		{

			File dir = path.toFile();
			File[] list;
			if (dir.isDirectory())
			{
				list = dir.listFiles();

				for (File fi : list)
				{

					if (!fi.isDirectory())
					{
						Path p = Paths.get(fi.getAbsolutePath());
						System.out.println("converting: " + p.getFileName());

						byte[] b = Files.readAllBytes(p);

						/**
						 * 1: Encoding 2: Sample rate in hz (same as frame rate
						 * for pcm). The number of "sounds played" per second 3:
						 * Sample size: How many bits are used to store each
						 * sample. Typically 8 or 16. 4: Number of channels
						 * (mono or stero 5: Frame size. The size of the set of
						 * samples for all channels at any given time. It is the
						 * sample size times the number of channels. (in bytes(
						 * 6: Frame rate: same as sample rate for pcm.
						 */
						byte[] n = getAudioDataBytes(b, new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, (float) 44100.0, 16, 2, 4, (float) (44100), false));

						

						FileOutputStream fos = new FileOutputStream("C:\\Users\\Evan\\Documents\\School\\Westminster\\Senior Project\\m\\tests2\\converted\\" + p.getFileName() + "2.wav");
						DataOutputStream output = new DataOutputStream(fos);
						
					
						    // WAVE header
						    // see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
						    writeString(output, "RIFF"); // chunk id
						    writeInt(output, 36 + n.length); // chunk size
						    writeString(output, "WAVE"); // format
						    writeString(output, "fmt "); // subchunk 1 id
						    writeInt(output, 16); // subchunk 1 size
						    writeShort(output, (short) 1); // audio format (1 = PCM)
						    writeShort(output, (short) 2); // number of channels
						    writeInt(output, 44100); // sample rate
						    writeInt(output, 44100*2*16/8); // byte rate
						    writeShort(output, (short) (2*16/8)); // block align
						    writeShort(output, (short) 16); // bits per sample
						    writeString(output, "data"); // subchunk 2 id
						    writeInt(output, n.length); // subchunk 2 size
						    // Audio data (conversion big endian -> little endian)
						    short[] shorts = new short[n.length / 2];
						    ByteBuffer.wrap(n).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
						    ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
						    for (short s : shorts) {
						        bytes.putShort(s);
						    }

						    output.write(n);
				
					}
				
				}
			}
			else
			{
				System.exit(0);
			}

		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnsupportedAudioFileException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			System.out.println("done");
		}

	}
	
	
	
	private static void writeInt(final DataOutputStream output, final int value) throws IOException {
		output.write(value >> 0);
		output.write(value >> 8);
		output.write(value >> 16);
		output.write(value >> 24);
		}

		private static void writeShort(final DataOutputStream output, final short value) throws IOException {
		output.write(value >> 0);
		output.write(value >> 8);
		}

		private static void writeString(final DataOutputStream output, final String value) throws IOException {
		for (int i = 0; i < value.length(); i++) {
		    output.write(value.charAt(i));
		    }
		}

	public static byte[] getAudioDataBytes(byte[] sourceBytes, AudioFormat audioFormat) throws UnsupportedAudioFileException, IllegalArgumentException, Exception
	{
		if (sourceBytes == null || sourceBytes.length == 0 || audioFormat == null)
		{
			throw new IllegalArgumentException("Illegal Argument passed to this method");
		}

		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;
		AudioInputStream sourceAIS = null;
		AudioInputStream convert1AIS = null;
		AudioInputStream convert2AIS = null;

		try
		{

			bais = new ByteArrayInputStream(sourceBytes);
			sourceAIS = AudioSystem.getAudioInputStream(bais);
			AudioFormat sourceFormat = sourceAIS.getFormat();
			AudioFormat convertFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * 2,
					sourceFormat.getSampleRate(), false);
			convert1AIS = AudioSystem.getAudioInputStream(convertFormat, sourceAIS);// target
																					// format
																					// and
																					// source
																					// stream
			convert2AIS = AudioSystem.getAudioInputStream(audioFormat, convert1AIS);

			baos = new ByteArrayOutputStream();

			byte[] buffer = new byte[8192];
			while (true)
			{
				int readCount = convert2AIS.read(buffer, 0, buffer.length);
				if (readCount == -1)
				{
					break;
				}
				baos.write(buffer, 0, readCount);
			}

			return baos.toByteArray();

		}
		catch (UnsupportedAudioFileException uafe)
		{
			// uafe.printStackTrace();
			throw uafe;
		}
		catch (IOException ioe)
		{
			// ioe.printStackTrace();
			throw ioe;
		}
		catch (IllegalArgumentException iae)
		{
			// iae.printStackTrace();
			throw iae;
		}
		catch (Exception e)
		{
			// e.printStackTrace();
			throw e;
		}
		finally
		{
			if (baos != null)
			{
				try
				{
					baos.close();
				}
				catch (Exception e)
				{
				}
			}
			if (convert2AIS != null)
			{
				try
				{
					convert2AIS.close();
				}
				catch (Exception e)
				{
				}
			}
			if (convert1AIS != null)
			{
				try
				{
					convert1AIS.close();
				}
				catch (Exception e)
				{
				}
			}
			if (sourceAIS != null)
			{
				try
				{
					sourceAIS.close();
				}
				catch (Exception e)
				{
				}
			}
			if (bais != null)
			{
				try
				{
					bais.close();
				}
				catch (Exception e)
				{
				}
			}
		}
	}

	public void testPlay(String filename)
	{
		try
		{
			File file = new File(filename);
			AudioInputStream in = AudioSystem.getAudioInputStream(file);
			AudioInputStream din = null;
			AudioFormat baseFormat = in.getFormat();
			AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2,
					baseFormat.getSampleRate(), false);
			din = AudioSystem.getAudioInputStream(decodedFormat, in);
			// Play now.
			rawplay(decodedFormat, din);
			in.close();
		}
		catch (Exception e)
		{
			// Handle exception.
		}
	}

	private void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException
	{
		byte[] data = new byte[4096];
		SourceDataLine line = getLine(targetFormat);
		if (line != null)
		{
			// Start
			line.start();
			int nBytesRead = 0, nBytesWritten = 0;
			while (nBytesRead != -1)
			{
				nBytesRead = din.read(data, 0, data.length);
				if (nBytesRead != -1)
					nBytesWritten = line.write(data, 0, nBytesRead);
			}
			// Stop
			line.drain();
			line.stop();
			line.close();
			din.close();
		}
	}

	private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException
	{
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}

}
