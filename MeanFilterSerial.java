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
    ArrayList<Integer> pixels = new ArrayList<Integer>();
    
    for(int count = 0; count < width; count++){
      for (int counter = 0; counter < height; counter++){
        int p = input.getRGB(count,counter);
        pixels.add(p); 
      }
    }
    
    }
}