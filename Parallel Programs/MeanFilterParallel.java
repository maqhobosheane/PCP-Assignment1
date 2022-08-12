import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;


public class MeanFilterParallel extends RecursiveAction{

 private int imgWidth;
 private int imgHeight;
 private BufferedImage inputImg;
 private int winSize;
 private BufferedImage output;
 
 
 public void MeanParallelFilter(BufferedImage img,BufferedImage dst, int rows, int columns, int kernelSize){
   inputImg = img;
   output = dst;
   rows = imgHeight;
   columns = imgWidth;
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
       
      
     for(int x = 0; x < width; x++){
      for (int y = 0; y < height ; y++){
      
        sumA = 0;
        sumR = 0;
        sumG = 0;
        sumB = 0;
        int winSizeUsed = 0;    
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
        avgPixel = input.getRGB(x,y); 
      }
        output.setRGB(x,y,avgPixel); 
          
      }

    }   
  
 }
 
  @Override
    protected void compute() {
        if (imgWidth <= winSize && imgHeight <= winSize) {
            computeDirectly();
            return;
        }
 
       BufferedImage[] minis =  split(inputImg);
       MeanFilterParallel left = minis[0];
       MeanFilterParallel right = minis[1];
       left.fork();
       right.compute();
       left.join();
    }
 
 public BufferedImage[] split(BufferedImage img){
 
  BufferedImage images[] = new BufferedImage[2];
  int subImgWidth = img.getWidth();
  int subImgHeight = img.getHeight();
  int currentSub = 0;
  
  for (int r = 0; c < 1; i++)
        {
            for (int c = 0; r < 1; j++)
            {
                // Creating sub image
                images[currentSub] = new BufferedImage(subImgWidth, subImgHeight, img.getType());
                Graphics2D imgCreator = images[currentSub].createGraphics();

                // coordinates of source image
                int srcFirstX = subImgWidth * c;
                int srcFirstY = subImgHeight * r;

                // coordinates of sub-image
                int dstCornerX = subImgWidth * c + subImgWidth;
                int dstCornerY = subImgHeight * r + subImgHeight;
                
                imgCreator.drawImage(image, 0, 0, subImgWidth, subImgHeight, srcFirstX, srcFirstY, dstCornerX, dstCornerY, null);
                currentSub++;
            }
        }
        
   //writing sub-images into image files
        for (int i = 0; i < 16; i++)
        {
            File outputFile = new File("img" + i + ".jpg");
            ImageIO.write(imgs[i], "jpg", outputFile);
        }
        
        return images; 
  
 }
 
 public static void main(String[] args) {
    BufferedImage src = null;
    BufferedImage dst = null;
    File f = null;
    

    //read image
    try{
      f = new File("/home/maqhobosheane/Downloads/Image.jpg");
      src = ImageIO.read(f);
      dst = ImageIO.read(f);
      System.out.println("Image successfully read.");
    }catch(IOException e){
      System.out.println(e);
    }
    
     int w = src.getWidth();
     int h = src.getHeight();
     int winSize = Integer.valueOf(args[2]);
     MeanFilterParallel meanFilter = MeanFilterParalle(src,dst, h, w,winSize ); 
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