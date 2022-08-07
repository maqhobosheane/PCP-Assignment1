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
      f = new File("/home/maqhobosheane/Downloads/Image.jpeg");
      input = ImageIO.read(f);
      System.out.println("Image successfully read.");
    }catch(IOException e){
      System.out.println(e);
    }
    
    int width = input.getWidth();
    int height = input.getHeight();
    int sum = 0;
    int average;
    int winSize = Integer.valueOf(args[2]);
    ArrayList<Integer> pixels = new ArrayList<Integer>();
    
    for(int count = 0; count < width; count++){
      for (int counter = 0; counter < height ; counter++){
        for(int winCount = 0; winCount < winSize;winCount++){
           for(int winCounter = 0; winCounter < winSize;winCounter++){       
             sum = sum + input.getRGB(count+winCount,counter+winCounter);
          }
          
        }
         
        average = sum/(winSize*winSize);
        input.setRGB(count,counter,average);  
      }

    }
    
     //write image
    try{
      f = new File("/home/maqhobosheane/Downloads/Output.jpeg");
      ImageIO.write(input, "jpg", f);
      System.out.println("Image successfully written to.");
    }catch(IOException e){
      System.out.println(e);
    }
    
    }
}