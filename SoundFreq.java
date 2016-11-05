import java.awt.*;
import javax.sound.sampled.*;
import java.nio.*;

class SoundFreq{
	public static void main(String[] args){
		Toolkit tk = Toolkit.getDefaultToolkit();
		tk.beep();

		// record an audio file.
		TargetDataLine line;

		final int audioFrames = 8192;
		final float sampleRate = 8000.0f;
		final int bitsPerRecord = 16; 
		final int channels = 1; 
		final boolean bigEndian = true;
		final boolean signed = true;

		byte[] byteData;
		double[] doubleData;

		AudioFormat format = new AudioFormat(sampleRate, bitsPerRecord, channels, signed, bigEndian);
		 DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		if(!AudioSystem.isLineSupported(info)){
			System.err.println("AudioSystem is not LineSupported(info)");
		}
		try{
			line = (TargetDataLine)AudioSystem.getLine(info);
			line.open(format);
		}catch(LineUnavailableException ex){
			System.out.println("Line Unavailable Exception");
		}

		//analyse the audio file to get frequency

		//
	}

}