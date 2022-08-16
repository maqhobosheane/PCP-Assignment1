import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import javax.imageio.ImageIO;

/**
 * 	This class implements parallelisation on a mean filter used for image noise reduction.
 * 	@author Maqhobosheane Mohlerepe 
 * 	@version 13 August 2022 
 */ 

public class MeanFilterParallel extends RecursiveAction{

 private int imgWidth;
 private int imgHeight;
 private int startingY;
 public  int lastY;
 private BufferedImage inputImg;
 private int winSize;
 public static BufferedImage output;
 
 /**
	 * Creates a parallel object from an image and a filter.
	 * @param img the image that is to be filtered.
         * @param rows the height of the image to be processed.
     	 * @param columns the width of the image to be processed.
     	 * @param kernelSize  the width of the filter window.
     	 * @param startY the y-coordinate of the first pixel to be processed in the image.
     	 * @param endY the y-coordinate of the last pixel that is going to be processed in the image.
     	 *Since all the parallel objects will be dealing with the same image but different parts of it, 
     	 *there is a range for which portion of the image each thread will process and this is what the @param startY and @param endY are used for.
         */
 
 
 public MeanFilterParallel(BufferedImage img, int rows, int columns, int kernelSize,int startY, int endY){
   inputImg = img;
   imgHeight = rows;
   imgWidth = columns;
   startingY = startY;
   lastY = endY;
   winSize = kernelSize;
  
 }
 
 //=================================================================================================================
	
	/**
	 * Implements the sequential mean filter operations.
         * Sets the pixels of output image to the filtered pixels computed by the median filter operations.
         */

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
 
 //=================================================================================================================
 
 protected static int pixelThreshold = 100000;// The sequential cutoff
 
     /**
	 * Executes when a thread starts.
	 * Threads implement mean filter on different parts of the image. The range the thread works on is determined
	 * by the number of times the height of the image is divided up. Only portions of the image with a total number of pixels
	 * that are less than the sequential threshold.
         */

    @Override
    protected void compute() {
        
        if ((lastY-startingY)*imgWidth < pixelThreshold) {
            
            computeDirectly();
            return;
        }
        
        
        int split = ((lastY + startingY)/2);
        
	
	MeanFilterParallel upper = new MeanFilterParallel(inputImg, imgHeight, imgWidth, winSize,startingY, split );//first division(upper half of image)
	MeanFilterParallel lower = new MeanFilterParallel(inputImg, imgHeight, imgWidth, winSize, split, lastY);//second division(lower half of image)
	upper.fork();
	lower.compute();
	upper.join();	
	
        
    }
    
     //========================================================================================================================================================
    
         /*
         * Writes the filtered array to a file.
         * @param args commandline arguments: 
         *             &lt;data file name&gt; &lt;filter size (odd number)&gt; &lt;output file name&gt;
         */
    
   public static void main(String[] args) {
    BufferedImage src = null;
    BufferedImage dst = null;
    File f = null;
    

    //read image
    try{
      f = new File(args[0]);
      src = ImageIO.read(f);
      output = ImageIO.read(f);
      
    }catch(IOException e){
      System.out.println("Input image not found.");
      System.exit(0);
    }
    
     //Ensures that the filter size(given by third main argument) is valid: an odd integer >= 3.
        if(Integer.valueOf(args[2]) < 3 || (Integer.valueOf(args[2]))%2 != 1){
    	System.out.println("Invalid window width.The window width should be an odd number greater than or equal to 3.");
    	System.exit(0);
    }
    
     int w = src.getWidth();
     int h = src.getHeight();
      System.out.println("Image size is " + h + "x" + w);
     int winSize = Integer.valueOf(args[2]);
     System.out.println("Filter size: " + winSize);
     System.out.println("Threshold: " + pixelThreshold);
     MeanFilterParallel meanFilter = new MeanFilterParallel(src, h, w,winSize, 0, h ); //the task that needs to be done by divide-and-conquer is to filter the image given by @param src
     ForkJoinPool pool = new ForkJoinPool(); //Create pool of worker threads
     long startTime = System.currentTimeMillis();  
     pool.invoke(meanFilter);//Start the running
     long endTime = System.currentTimeMillis();
     System.out.println("Image serial median filter took " + (endTime - startTime) + 
                " milliseconds."); 
     
     
     //write image
    try{
      f = new File(args[1]);
      ImageIO.write(output, "jpg", f);
      
    }catch(IOException e){
      System.out.println("Image output directory not found.");
      System.exit(0);
    }
    
    
   }
 
 	

}	

