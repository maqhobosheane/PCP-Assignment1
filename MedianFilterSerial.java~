import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.*;

public class MedianFilterSerial{
    
    public static void main(String [] args){
    
        BufferedImage input = null;
        BufferedImage output = null;
        File f = null;
        
        //read image
    try{
      f = new File("/home/maqhobosheane/Downloads/Image.jpg");
      input = ImageIO.read(f);
      output = ImageIO.read(f);
      System.out.println("Image successfully read.");
    }catch(IOException e){
      System.out.println(e);
    }


    int width = input.getWidth();
    int p,a,r,g,b;
    int medianA,medianR,medianG,medianB,medianPixel;
    int height = input.getHeight();
    int winSize = Integer.valueOf(args[2]);
    
    int[] outputA = new int[winSize*winSize];
    int[] outputR = new int[winSize*winSize];
    int[] outputG = new int[winSize*winSize];
    int[] outputB = new int[winSize*winSize];
    
      for(int x = 0; x < width; x++){
        for (int y = 0; y < height ; y++){
  
        int count = 0;    
        for(int column = x - (winSize/2); column <= x + (winSize/2); column++){
              
           for(int row = y - (winSize/2); row <= y + (winSize/2); row++){
            
            if(row < 0 || row >= height || column <0 || column >= width){
             continue;
            }
            
            else{ 
             p = input.getRGB(column,row);
             a = (p>>24) & 0xff;
             r = (p>>16) & 0xff;
             g = (p>>8) & 0xff;   
             b = p & 0xff;   
             
             outputA[count] = a;
             outputR[count] = r;
             outputG[count] = g;
             outputB[count] = b;
             count++;
           }
        
          }
          
          
        }
        
        Arrays.sort(outputA);
        Arrays.sort(outputR);
        Arrays.sort(outputG);
        Arrays.sort(outputB);
        
        medianA = outputA[outputA.length/2];
        medianR = outputR[outputR.length/2];
        medianG = outputG[outputG.length/2];
        medianB = outputB[outputB.length/2];
        medianPixel = (medianA<<24) | (medianR<<16) | (medianG<<8) | medianB;

        output.setRGB(x,y,medianPixel);
        
        
    }

    }
    //write image
    try{
      f = new File("/home/maqhobosheane/Downloads/Output.jpg");
      ImageIO.write(output, "jpg", f);
      System.out.println("Image successfully written to.");
    }catch(IOException e){
      System.out.println(e);
    }
    
    }
}