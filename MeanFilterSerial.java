import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.*;

public class MeanFilterSerial{

    public static void main(String args[])throws IOException{
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
    int height = input.getHeight();
    int p;
    int a;
    int r;
    int g;
    int b;
    int sumA,sumR,sumG,sumB;
    int avgA;
    int avgR;
    int avgG;
    int avgB;
    int avgPixel;
    int winSize = Integer.valueOf(args[2]);
    ArrayList<Integer> pixels = new ArrayList<Integer>();
    
    
    for(int x = 0; x < width; x++){
      for (int y = 0; y < height ; y++){
      
        sumA = 0;
        sumR = 0;
        sumG = 0;
        sumB = 0;
        int winSizeUsed = 0;    
        for(int column = x - winSize/2; column <= x + winSize; column++){
        
           for(int row = y - winSize/2; row <= x + winSize; row++){
            
            if(row < 0 || row >= height || column <0 || column >= width){
             continue;
            }
            
            else{ 
             p = input.getRGB(column,row);
             a = (p>>24) & 0xff;
             r = (p>>16) & 0xff;
             g = (p>>8) & 0xff;   
             b = p & 0xff;   
             sumA = sumA + a;
             sumR = sumR + r;
             sumG = sumG + g;
             sumB = sumB + b;
             winSizeUsed++;
           }
        
          }
          
          
        }
      
      if (winSizeUsed > 0){   
        avgA = sumA/(winSizeUsed);
        avgR = sumA/(winSizeUsed);
        avgG = sumA/(winSizeUsed);
        avgB = sumA/(winSizeUsed);
        avgPixel = (avgA<<24) | (avgR<<16) | (avgG<<8) | avgB;
        }
      else{
        avgPixel = input.getRGB(x,y); 
      }
        output.setRGB(x,y,avgPixel);  
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