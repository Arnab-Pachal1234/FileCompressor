import utils.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String imagePath = "sample_640×426.jpeg"; // PUT YOUR ACTUAL IMAGE PATH HERE
        BufferedImage inputImage = ImageIO.read(new File(imagePath));
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        System.out.println("=== PHASE 1: ENCODING ===");
        List<RunLengthEncoder.RLEPair> allRleData = new ArrayList<>();

        // Compress every block into RLE pairs
        for (int blockY = 0; blockY < height; blockY += 8) {
            for (int blockX = 0; blockX < width; blockX += 8) {
                Block spatialBlock = new Block();
                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {
                        int pX = Math.min(blockX + x, width - 1);
                        int pY = Math.min(blockY + y, height - 1);
                        spatialBlock.data[y][x] = ColorSpace.getLumiance(inputImage.getRGB(pX, pY));
                    }
                }
                Block dctBlock = DCTMath.applyForwardDCT(spatialBlock);
                int[][] quantizedBlock = Quantizer.quantize(dctBlock);
                int[] flatArray = ZigZagScanner.flatten(quantizedBlock);
                allRleData.addAll(RunLengthEncoder.encode(flatArray));
            }
        }

        // Huffman Encode and write to disk
        HuffmanEncoder.EncodedResult finalResult = HuffmanEncoder.encode(allRleData);
        String binFile = "compressed_data.bin";
        BitWriter.saveCompressedStream(finalResult.binaryString, binFile);
        
        System.out.println("\n=== PHASE 2: DECODING ===");
        System.out.println("Reading bits from " + binFile + "...");
        
        // 1. READ THE FILE BACK FROM DISK!
        List<RunLengthEncoder.RLEPair> recoveredData = BitReader.readCompressedStream(binFile, finalResult.huffmanTable);
        
        // 2. REBUILD THE IMAGE FROM THE RECOVERED DATA
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int pairIndex = 0;

        for (int blockY = 0; blockY < height; blockY += 8) {
            for (int blockX = 0; blockX < width; blockX += 8) {
                
                // Grab the RLE pairs for just this one 8x8 block
                List<RunLengthEncoder.RLEPair> blockPairs = new ArrayList<>();
                int coefficientCount = 0;
                
                while (pairIndex < recoveredData.size()) {
                    RunLengthEncoder.RLEPair pair = recoveredData.get(pairIndex++);
                    blockPairs.add(pair);
                    
                    if (pair.run == 0 && pair.value == 0) break; // EOB marker ends the block
                    
                    coefficientCount += pair.run + 1;
                    if (coefficientCount >= 64) break; // Block is completely full
                }

                // Reverse the math pipeline
                int[] decodedFlat = RunLengthEncoder.decode(blockPairs);
                int[][] unflattenedBlock = ZigZagScanner.unflatten(decodedFlat);
                Block dequantizedBlock = Quantizer.dequantize(unflattenedBlock);
                Block reconstructedPixels = IDCTMath.applyInverseDCT(dequantizedBlock);

                // Draw the pixels to the output image
                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {
                        int pX = blockX + x;
                        int pY = blockY + y;
                        if (pX < width && pY < height) {
                            int lumiance = (int) Math.round(reconstructedPixels.data[y][x] + 128.0);
                            lumiance = Math.max(0, Math.min(255, lumiance));
                            int rgb = (255 << 24) | (lumiance << 16) | (lumiance << 8) | lumiance;
                            outputImage.setRGB(pX, pY, rgb);
                        }
                    }
                }
            }
        }

        File outputFile = new File("final_reconstructed_image.png");
        ImageIO.write(outputImage, "jpeg", outputFile);
        System.out.println("Success! Image rebuilt from pure binary and saved as final_reconstructed_image.png");
    }
}