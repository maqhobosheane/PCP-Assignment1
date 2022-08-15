import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import javax.imageio.ImageIO;
import java.util.Arrays;

public class MedianFilterParallel extends RecursiveAction{

 private int imgWidth;
 private int imgHeight;
 private int startingY;
 public  int lastY;
 private BufferedImage inputImg;
 private int winSize;
 public static BufferedImage output;
 
 
 public MedianFilterParallel(BufferedImage img, int rows, int columns, int kernelSize,int startY, int endY){
   inputImg = img;
   imgHeight = rows;
   imgWidth = columns;
   startingY = startY;
   lastY = endY;
   winSize = kernelSize;
 }

 protected void computeDirectly(){
  
    int p,a,r,g,b;
    int medianA,medianR,medianG,medianB,medianPixel;
    
    int[] outputA = new int[winSize*winSize];
    int[] outputR = new int[winSize*winSize];
    int[] outputG = new int[winSize*winSize];
    int[] outputB = new int[winSize*winSize];
       
      
     for(int y = startingY; y < lastY; y++){
      for (int x = 0 ; x < imgWidth ; x++){
      
      
        int surroundPixels = (winSize- 1) / 2;
        int halfWindowWidth = (winSize-1)/2;
        
        int count = 0;    
        for(int row = -surroundPixels; row <= surroundPixels; row++){
        
           for(int column = -surroundPixels; column <= surroundPixels; column++){
            
            if(row+y < 0 || row+y >= lastY || column+x <0 || column+x >= imgWidth){
             continue;
            }
            
            else{ 
             p = inputImg.getRGB(x+column,y+row);
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
  
 }
 
 protected static int pixelThreshold = 100000;

    @Override
    protected void compute() {
        
        if ((lastY-startingY)*imgWidth < pixelThreshold) {
            
            computeDirectly();
            return;
        }
        
        
        int split = (startingY+lastY)/2;
	
	MedianFilterParallel upper = new MedianFilterParallel(inputImg, imgHeight, imgWidth, winSize,startingY, split );
	MedianFilterParallel lower = new MedianFilterParallel(inputImg, imgHeight, imgWidth, winSize, split, lastY);
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
     MedianFilterParallel meanFilter = new MedianFilterParallel(src, h, w,winSize, 0, h ); 
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
