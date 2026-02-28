/*
In this file I will use Discrete Cosine Transform to store in 8*8 blocks
in the [0][0] cell the average brightness or intensity and in the rest of the cells
horizontal and vertical variations 
*/ 
package utils;

public class DCTMath {

    // Helper method for the C(u) and C(v) constants in the formula
    private static double getC(int index) {
        if (index == 0) {
            return 1.0 / Math.sqrt(2.0);
        }
        return 1.0;
    }

    // Applies 2D Forward DCT to a spatial 8x8 block to get a frequency 8x8 block
    public static Block applyForwardDCT(Block inputBlock) {
        Block outputBlock = new Block();

        // u and v loop through the new 8x8 frequency grid we are creating
        for (int u = 0; u < 8; u++) {
            for (int v = 0; v < 8; v++) {
                
                double sum = 0.0;

                // x and y loop through the original 8x8 spatial pixels
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        
                        double pixelValue = inputBlock.data[x][y];
                        
                        // The cosine formulas
                        double cosX = Math.cos(((2.0 * x + 1.0) * u * Math.PI) / 16.0);
                        double cosY = Math.cos(((2.0 * y + 1.0) * v * Math.PI) / 16.0);
                        
                        sum += pixelValue * cosX * cosY;
                    }
                }

                // Apply the scaling factors to finish the formula
                outputBlock.data[u][v] = 0.25 * getC(u) * getC(v) * sum;
            }
        }
        
        return outputBlock;
    }
}