import utils.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        // --- CONFIGURATION ---
        String inputPath = "sample_640×426.jpeg"; 
        double qualityScale = 0.1;      // Lower is clearer (0.1 = High Quality)
        boolean enableColor = false;    // SET TO FALSE FOR BLACK & WHITE MODE
        
        BufferedImage img = ImageIO.read(new File(inputPath));
        int width = img.getWidth(), height = img.getHeight();
        List<RunLengthEncoder.RLEPair> allRleData = new ArrayList<>();

        System.out.println("Processing " + (enableColor ? "Color" : "B&W") + " image...");

        // 1. ENCODING PHASE
        for (int blockY = 0; blockY < height; blockY += 8) {
            for (int blockX = 0; blockX < width; blockX += 8) {
                int numChannels = enableColor ? 3 : 1;
                Block[] channels = new Block[numChannels];
                for(int i=0; i<numChannels; i++) channels[i] = new Block();

                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {
                        int pX = Math.min(blockX + x, width - 1), pY = Math.min(blockY + y, height - 1);
                        int rgb = img.getRGB(pX, pY);
                        channels[0].data[y][x] = ColorSpace.getLumiance(rgb);
                        if (enableColor) {
                            channels[1].data[y][x] = ColorSpace.getCb(rgb);
                            channels[2].data[y][x] = ColorSpace.getCr(rgb);
                        }
                    }
                }

                for (int i = 0; i < numChannels; i++) {
                    Block dct = DCTMath.applyForwardDCT(channels[i]);
                    int[][] quantized = Quantizer.quantize(dct, i == 0, qualityScale);
                    allRleData.addAll(RunLengthEncoder.encode(ZigZagScanner.flatten(quantized)));
                }
            }
        }

        HuffmanEncoder.EncodedResult huff = HuffmanEncoder.encode(allRleData);
        BitWriter.saveCompressedStream(huff.binaryString, "compressed.bin");
         //print size  diff between original and compressed file
        File originalFile = new File(inputPath);
        File compressedFile = new File("compressed.bin");
        long originalSize = originalFile.length();
        long compressedSize = compressedFile.length();
        System.out.println("Original file size: " + originalSize + " bytes");
        System.out.println("Compressed file size: " + compressedSize + " bytes");

        // 2. DECODING PHASE
        List<RunLengthEncoder.RLEPair> recovered = BitReader.readCompressedStream("compressed.bin", huff.huffmanTable);
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int pairIdx = 0;

        for (int blockY = 0; blockY < height; blockY += 8) {
            for (int blockX = 0; blockX < width; blockX += 8) {
                int numChannels = enableColor ? 3 : 1;
                Block[] rec = new Block[3]; 

                for (int i = 0; i < numChannels; i++) {
                    List<RunLengthEncoder.RLEPair> blockPairs = new ArrayList<>();
                    while (pairIdx < recovered.size()) {
                        RunLengthEncoder.RLEPair p = recovered.get(pairIdx++);
                        blockPairs.add(p);
                        if (p.run == 0 && p.value == 0) break;
                    }
                    rec[i] = IDCTMath.applyInverseDCT(Quantizer.dequantize(ZigZagScanner.unflatten(RunLengthEncoder.decode(blockPairs)), i == 0, qualityScale));
                }

                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {
                        int pX = blockX + x, pY = blockY + y;
                        if (pX < width && pY < height) {
                            if (enableColor) {
                                outputImage.setRGB(pX, pY, ColorSpace.toRGB(rec[0].data[y][x], rec[1].data[y][x], rec[2].data[y][x]));
                            } else {
                                // Grayscale shortcut: set R, G, and B to the same Luminance value
                                int lum = (int)Math.round(rec[0].data[y][x] + 128.0);
                                lum = Math.max(0, Math.min(255, lum));
                                outputImage.setRGB(pX, pY, (255<<24)|(lum<<16)|(lum<<8)|lum);
                            }
                        }
                    }
                }
            }
        }

        // 3. GUI PREVIEW
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Codec Preview - " + (enableColor ? "Color" : "B&W"));
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(new JLabel(new ImageIcon(outputImage)));
            f.pack(); f.setLocationRelativeTo(null); f.setVisible(true);
        });
    }
}