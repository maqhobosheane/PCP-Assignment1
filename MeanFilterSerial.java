import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.*;

public class MeanFilterSerial{

    public static void main(String args[])throws IOException{
    BufferedImage input = null;
    File f = null;

    //read image
    try{
      f = new File("/home/maqhobosheane/Downloads/Image.jpg");
      input = ImageIO.read(f);
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
    int sumA = 0;
    int sumR = 0;
    int sumG = 0;
    int sumB = 0;
    int avgA;
    int avgR;
    int avgG;
    int avgB;
    int avgPixel;
    int winSize = Integer.valueOf(args[2]);
    ArrayList<Integer> pixels = new ArrayList<Integer>();
    
    
    for(int count = 0; count < width; count++){
      for (int counter = 0; counter < height ; counter++){
        for(int winCount = 0; winCount < winSize;winCount++){
           for(int winCounter = 0; winCounter < winSize;winCounter++){
           if(count <= width - winSize && counter <= height - winSize ){ 
             p = input.getRGB(count + winCount,counter + winCounter);
             a = (p>>24) & 0xff;
             r = (p>>16) & 0xff;
             g = (p>>8) & 0xff;   
             b = p & 0xff;   
             sumA = sumA + a;
             sumR = sumR + r;
             sumG = sumG + g;
             sumB = sumB + b;
           }
           else if(count > width - winSize && counter > height - winSize){
             p = input.getRGB(count - winCount,counter - winCounter);
             a = (p>>24) & 0xff;
             r = (p>>16) & 0xff;
             g = (p>>8) & 0xff;   
             b = p & 0xff;   
             sumA = sumA + a;
             sumR = sumR + r;
             sumG = sumG + g;
             sumB = sumB + b;
           }
           else if(count > width - winSize && counter <= height - winSize){
           
             p = input.getRGB(count - winCount,counter + winCounter);
             a = (p>>24) & 0xff;
             r = (p>>16) & 0xff;
             g = (p>>8) & 0xff;   
             b = p & 0xff;   
             sumA = sumA + a;
             sumR = sumR + r;
             sumG = sumG + g;
             sumB = sumB + b;
             
           }
           else if(count <= width - winSize && counter > height - winSize){
             p = input.getRGB(count + winCount,counter - winCounter);
             a = (p>>24) & 0xff;
             r = (p>>16) & 0xff;
             g = (p>>8) & 0xff;   
             b = p & 0xff;   
             sumA = sumA + a;
             sumR = sumR + r;
             sumG = sumG + g;
             sumB = sumB + b;
           
           }
          }
          
        }
         
        avgA = sumA/(winSize*winSize);
        avgR = sumA/(winSize*winSize);
        avgG = sumA/(winSize*winSize);
        avgB = sumA/(winSize*winSize);
        avgPixel = (avgA<<24) | (avgR<<16) | (avgG<<8) | avgB;
        input.setRGB(count,counter,avgPixel);  
      }

    }
    

   
     //write image
    try{
      f = new File("/home/maqhobosheane/Downloads/Output.jpg");
      ImageIO.write(input, "jpg", f);
      System.out.println("Image successfully written to.");
    }catch(IOException e){
      System.out.println(e);
    }
    
    }
}