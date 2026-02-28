import utils.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String imagePath = "C:\Users\Arnab\Desktop\Projects\compressor\Image_compressor\sample_640×426.jpeg"; // PUT YOUR IMAGE PATH HERE
        BufferedImage inputImage = ImageIO.read(new File(imagePath));
        
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        System.out.println("Compressing and Decoding Image...");

        for (int blockY = 0; blockY < height; blockY += 8) {
            for (int blockX = 0; blockX < width; blockX += 8) {
           
                Block spatialBlock = new Block();
                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {
                        int pX = Math.min(blockX + x, width - 1);
                        int pY = Math.min(blockY + y, height - 1);
                        spatialBlock.data[y][x] = ColorSpace.getLuminance(inputImage.getRGB(pX, pY));
                    }
                }

                Block dctBlock = DCTMath.applyForwardDCT(spatialBlock);
                int[][] quantizedBlock = Quantizer.quantize(dctBlock);
                int[] flatArray = ZigZagScanner.flatten(quantizedBlock);
                List<RunLengthEncoder.RLEPair> rleData = RunLengthEncoder.encode(flatArray);

             
                int[] decodedFlat = RunLengthEncoder.decode(rleData);
                int[][] unflattenedBlock = ZigZagScanner.unflatten(decodedFlat);
                Block dequantizedBlock = Quantizer.dequantize(unflattenedBlock);
                Block reconstructedPixels = IDCTMath.applyInverseDCT(dequantizedBlock);

                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {
                        int pX = blockX + x;
                        int pY = blockY + y;
                        
                        if (pX < width && pY < height) {
                          
                            int luminance = (int) Math.round(reconstructedPixels.data[y][x] + 128.0);
                            
                            luminance = Math.max(0, Math.min(255, luminance));
                           
                            int rgb = (255 << 24) | (luminance << 16) | (luminance << 8) | luminance;
                            outputImage.setRGB(pX, pY, rgb);
                        }
                    }
                }
            }
        }

        File outputFile = new File("output_compressed.png");
        ImageIO.write(outputImage, "png", outputFile);
        System.out.println("Done! Check your folder for 'output_compressed.png'.");
    }
}