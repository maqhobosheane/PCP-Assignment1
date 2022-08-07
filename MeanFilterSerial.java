import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MeanFilterSerial{

    public static void main(String args[])throws IOException{
    BufferedImage input = null;
    File f = null;

    //read image
    try{
      f = new File("/home/maqhobosheane/Downloads/Image.jpeg");
      input = ImageIO.read(f);
    }catch(IOException e){
      System.out.println(e);
    }

    
    
}