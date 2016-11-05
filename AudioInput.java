import java.awt.*;
import javax.sound.sampled.*;
import java.nio.*;

import org.apache.commons.math3.complex.*;
import org.apache.commons.math3.transform.*;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

class AudioInput{
	
	TargetDataLine microphone;

	final int audioFrames = 8192;	
	final float sampleRate = 8000.0f;
	final int bitsPerRecord = 16; 
	final int channels = 1; 
	final boolean bigEndian = true;
	final boolean signed = true;

	byte[] byteData;		// length = audioFrames*2;
	double[] doubleData;	// length = audioFrames only reals needed for apache lib.

	AudioFormat format;
	FastFourierTransformer transformer;

	public  AudioInput(){
		byteData = new byte[audioFrames*2];
		// doubleData = new double[audioFrames*2]; 	// real & imaginary
		doubleData = new double[audioFrames];		// only real for apache
		// transformer = new FastFourierTransformer(DftNormalization.STANDARD);
		System.out.println("Microphone initialising...");
		format = new AudioFormat(sampleRate, bitsPerRecord, channels, signed, bigEndian);
	 	DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		// **
		if(!AudioSystem.isLineSupported(info)){
			System.err.println("AudioSystem is not LineSupported(info)");
			System.exit(1);
		}
		try{
			microphone = (TargetDataLine)AudioSystem.getLine(info);
			microphone.open(format); 
			System.out.println("Microphone opened with format : "+format.toString());

			microphone.start();

		}catch(LineUnavailableException ex){
			System.out.println("Line Unavailable Exception");
			System.out.println("Microphone failed to start. "+ex.getMessage());
			System.exit(1);
		}
	}

	public int readPCM(){
		int numBytesRead = microphone.read(byteData, 0, byteData.length);
		if(numBytesRead != byteData.length){
			System.out.println("warning: Less bytes read than buffer");
			System.exit(1);
		}
		return numBytesRead;
	}

	public void byteToDouble(){
		ByteBuffer buf = ByteBuffer.wrap(byteData);
		buf.order(ByteOrder.BIG_ENDIAN);
		int i =0;
		while(buf.remaining()>2){
			short s = buf.getShort();
			doubleData[i] = (new Short(s)).doubleValue();
			++i;
		}
	}

	public void findFrequency(){
		double frequency;
		// Complex[] complx = transformer.transform(doubleData, TransformType.FORWARD);
		Complex[] complx = FFT.fft(doubleData);
		double real;
		double im; 
		double mag[] = new double[complx.length];
		for (int i=0; i<complx.length; i++) {
			// real = complx[i].getReal();
			real = complx[i].re();
			// im = complx[i].getImaginary();
			im = complx[i].im();
			mag[i] = Math.sqrt((real*real)+(im*im));
		}
		double peak = -1;
		int index = -1;
		for (int i=0; i<complx.length; i++) {
			if(peak<mag[i]){
				index = i;
				peak = mag[i];
			}
		}
		frequency = (sampleRate*index)/audioFrames;
		System.out.println("Index : "+index+"....Frequency : "+frequency+"\n");

	}
	
	public void printFrequency(){
		for (int i=0; i<audioFrames/4; i++) {
			System.out.println("bin "+i+", Frequency : "+(sampleRate*i)/audioFrames);
		}
	}

	public static void main(String[] args){
		Toolkit tk = Toolkit.getDefaultToolkit();
		tk.beep();
		
		AudioInput ai = new AudioInput();

		int turns = 10;
		while(turns-- >0){
			System.out.println(turns+1);
			ai.readPCM();
			ai.byteToDouble();
			
			// ai.findFrequency();
			// ai.printFrequency();
		}
	}

}