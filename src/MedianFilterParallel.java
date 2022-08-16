import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import javax.imageio.ImageIO;
import java.util.Arrays;

/**
 * 	This class implements parallelisation on a median filter used for image noise reduction.
 * 	@author Maqhobosheane Mohlerepe 
 * 	@version 12 August 2022 
 */ 

public class MedianFilterParallel extends RecursiveAction{

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
 	public MedianFilterParallel(BufferedImage img, int rows, int columns, int kernelSize,int startY, int endY){
   		inputImg = img;
   		imgHeight = rows;
   		imgWidth = columns;
   		startingY = startY;
   		lastY = endY;
   		winSize = kernelSize;
 }
	//=================================================================================================================
	
	/**
	 * Implements the sequential median filter operations.
         * Sets the pixels of output image to the filtered pixels computed by the median filter operations.
         */
	
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
 
 //=================================================================================================================
 
 private static int pixelThreshold = 100000; // The sequential cutoff 
 
	 /**
	 * Executes when a thread starts.
	 * Threads implement median filter on different parts of the image. The range the thread works on is determined
	 * by the number of times the height of the image is divided up. Only portions of the image with a total number of pixels
	 * that are less than the sequential threshold.
         */

    @Override
    protected void compute() {
    
    	
        
        if ((lastY-startingY)*imgWidth < pixelThreshold) {
            
            computeDirectly();
            return;
        }
        
        
        int split = (startingY+lastY)/2;
	
	MedianFilterParallel upper = new MedianFilterParallel(inputImg, imgHeight, imgWidth, winSize,startingY, split);//first division(upper half of image)
	MedianFilterParallel lower = new MedianFilterParallel(inputImg, imgHeight, imgWidth, winSize, split, lastY);//second division(lower half of image)
	upper.fork();//assign upper half to new threads
	lower.compute();//execute lower half in this thread
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
     int winSize = Integer.valueOf(args[2]);
     MedianFilterParallel meanFilter = new MedianFilterParallel(src, h, w,winSize, 0, h ); //the task that needs to be done by divide-and-conquer is to filter the image given by @param src
     ForkJoinPool pool = new ForkJoinPool(); //Create pool of worker threads
     long startTime = System.currentTimeMillis(); 
     pool.invoke(meanFilter);//Start the running
     long endTime = System.currentTimeMillis();
     int cores = Runtime.getRuntime().availableProcessors();//Get number of cores in use
     System.out.println(winSize+","+(h*w)+","+(endTime - startTime)+","+pixelThreshold+","+cores); 
     
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
