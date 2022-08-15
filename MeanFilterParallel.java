import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import javax.imageio.ImageIO;

public class MeanFilterParallel extends RecursiveAction{

 private int imgWidth;
 private int imgHeight;
 private int startingY;
 public  int lastY;
 private BufferedImage inputImg;
 private int winSize;
 public static BufferedImage output;
 
 
 public MeanFilterParallel(BufferedImage img, int rows, int columns, int kernelSize,int startY, int endY){
   inputImg = img;
   imgHeight = rows;
   imgWidth = columns;
   startingY = startY;
   lastY = endY;
   winSize = kernelSize;
  
 }

 protected void computeDirectly(){
  
    int p,a,r,g,b;
    int sumA,sumR,sumG,sumB;
    int avgA = 0;
    int avgR = 0;
    int avgG = 0;
    int avgB = 0;
    int avgPixel;
    
       
      
    for(int y = startingY ; y < lastY; y++){
      for (int x = 0; x < imgWidth ; x++){
      	int surroundPixels = (winSize - 1) / 2;
        sumA = 0;
        sumR = 0;
        sumG = 0;
        sumB = 0;
        int winSizeUsed = 0;    
        for(int column = -surroundPixels; column <= surroundPixels; column++){
        
           for(int row = -surroundPixels; row <= surroundPixels; row++){
            
            if((row+y) < 0 || (row+y) >= lastY || (column+x) <0 || (column+x) >= imgWidth){
             continue;
            }
            
            else{ 
             p = inputImg.getRGB(column + x,row + y);
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
        avgR = sumR/(winSizeUsed);
        avgG = sumG/(winSizeUsed);
        avgB = sumB/(winSizeUsed);
        avgPixel = (avgA<<24) | (avgR<<16) | (avgG<<8) | avgB;
          
        }
      else{
        avgPixel = inputImg.getRGB(x,y); 
      }
         
        output.setRGB(x,y,avgPixel); 
          
      }

    }   
  
 }
 
 protected static int pixelThreshold = 100000;

    @Override
    protected void compute() {
        
        if ((lastY-startingY)*imgWidth < pixelThreshold) {
            
            computeDirectly();
            return;
        }
        
        
        int split = ((lastY + startingY)/2);
        
	
	MeanFilterParallel upper = new MeanFilterParallel(inputImg, imgHeight, imgWidth, winSize,startingY, split );
	MeanFilterParallel lower = new MeanFilterParallel(inputImg, imgHeight, imgWidth, winSize, split, lastY);
	upper.fork();
	lower.compute();
	upper.join();	
	
        
    }
    
   public static void main(String[] args) {
    BufferedImage src = null;
    BufferedImage dst = null;
    File f = null;
    

    //read image
    try{
      f = new File("/home/maqhobosheane/Downloads/Image.jpg");
      src = ImageIO.read(f);
      output = ImageIO.read(f);
      System.out.println("Image successfully read.");
    }catch(IOException e){
      System.out.println(e);
    }
    
     int w = src.getWidth();
     int h = src.getHeight();
     int winSize = Integer.valueOf(args[2]);
     MeanFilterParallel meanFilter = new MeanFilterParallel(src, h, w,winSize, 0, h ); 
     ForkJoinPool pool = new ForkJoinPool(); 
     pool.invoke(meanFilter);
     
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

