import java.util.*;
import java.awt.*;
import javax.sound.sampled.*;
import java.nio.*;


class Solution {
	
	public static void main(String[] args){
		 try{
		      Mixer.Info[] mixerInfo = 
		                      AudioSystem.getMixerInfo();
		      System.out.println("Available mixers:");
		      for(int cnt = 0; cnt < mixerInfo.length;
		                                          cnt++){
		      	System.out.println(mixerInfo[cnt].
		      	                              getName());
		      }//end for loop
		  }catch(Exception ex){
		  	System.out.println(ex.getMessage());
		  }
	
	}// main()

	
}