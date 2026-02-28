package utils;
import java.util.*;
public  class JpegEncoder
{
    public List<Block> splitIntoLumianceBlocks(int[][] image) {
        int width = image[0].length;
        int height = image.length;
        
        List<Block> yblock = new ArrayList<>();

        for (int i = 0; i < height; i += 8) {
            for (int j = 0; j < width; j += 8) {
                Block block = new Block();
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        int pixelX = i + x;
                        int pixelY = j + y;
                        if (pixelX < height && pixelY < width) {
                            block.data[x][y] = colorSpace.getLumiance(image[pixelX][pixelY]);
                        } else {
                            block.data[x][y] = 0; // Padding for out-of-bounds
                        }
                    }
                }
                yblock.add(block);
            }
        }
    }
}