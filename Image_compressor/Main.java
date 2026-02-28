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
        String inputPath = "images.jpg"; 
        
     
        double qualityScale = 0.2; 
        
        BufferedImage inputImg = ImageIO.read(new File(inputPath));
        int width = inputImg.getWidth(), height = inputImg.getHeight();

        System.out.println("Encoding with Quality Scale: " + qualityScale);
        List<RunLengthEncoder.RLEPair> allRleData = new ArrayList<>();

        for (int blockY = 0; blockY < height; blockY += 8) {
            for (int blockX = 0; blockX < width; blockX += 8) {
                Block[] channels = {new Block(), new Block(), new Block()}; 
                
                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {
                        int pX = Math.min(blockX + x, width - 1), pY = Math.min(blockY + y, height - 1);
                        int rgb = inputImg.getRGB(pX, pY);
                        channels[0].data[y][x] = ColorSpace.getLumiance(rgb);
                        channels[1].data[y][x] = ColorSpace.getCb(rgb);
                        channels[2].data[y][x] = ColorSpace.getCr(rgb);
                    }
                }

                for (int i = 0; i < 3; i++) {
                    Block dct = DCTMath.applyForwardDCT(channels[i]);
                
                    int[][] quantized = Quantizer.quantize(dct, i == 0, qualityScale); 
                    allRleData.addAll(RunLengthEncoder.encode(ZigZagScanner.flatten(quantized)));
                }
            }
        }

        HuffmanEncoder.EncodedResult huff = HuffmanEncoder.encode(allRleData);
        BitWriter.saveCompressedStream(huff.binaryString, "color_compressed.bin");

        System.out.println("Decoding...");
        List<RunLengthEncoder.RLEPair> recovered = BitReader.readCompressedStream("color_compressed.bin", huff.huffmanTable);
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int pairIdx = 0;

        for (int blockY = 0; blockY < height; blockY += 8) {
            for (int blockX = 0; blockX < width; blockX += 8) {
                Block[] rec = new Block[3];
                for (int i = 0; i < 3; i++) {
                    List<RunLengthEncoder.RLEPair> blockPairs = new ArrayList<>();
                    int count = 0;
                    while (pairIdx < recovered.size()) {
                        RunLengthEncoder.RLEPair p = recovered.get(pairIdx++);
                        blockPairs.add(p);
                        if (p.run == 0 && p.value == 0) break;
                        count += p.run + 1;
                        if (count >= 64) break;
                    }
                  
                    rec[i] = IDCTMath.applyInverseDCT(Quantizer.dequantize(ZigZagScanner.unflatten(RunLengthEncoder.decode(blockPairs)), i == 0, qualityScale));
                }

                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {
                        int pX = blockX + x, pY = blockY + y;
                        if (pX < width && pY < height) {
                            outputImage.setRGB(pX, pY, ColorSpace.toRGB(rec[0].data[y][x], rec[1].data[y][x], rec[2].data[y][x]));
                        }
                    }
                }
            }
        }

        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Color Codec - Quality: " + qualityScale);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(new JLabel(new ImageIcon(outputImage)));
            f.pack(); f.setLocationRelativeTo(null); f.setVisible(true);
        });
    }
}