package utils;

public class ZigZagScanner {

    // A lookup table mapping the 0-63 zig-zag sequence to standard grid indices.
    // For example, the 3rd number in the zig-zag is at row 1, col 0 (which is index 8).
    private static final int[] ZIGZAG_ORDER = {
         0,  1,  8, 16,  9,  2,  3, 10,
        17, 24, 32, 25, 18, 11,  4,  5,
        12, 19, 26, 33, 40, 48, 41, 34,
        27, 20, 13,  6,  7, 14, 21, 28,
        35, 42, 49, 56, 57, 50, 43, 36,
        29, 22, 15, 23, 30, 37, 44, 51,
        58, 59, 52, 45, 38, 31, 39, 46,
        53, 60, 61, 54, 47, 55, 62, 63
    };

    public static int[] flatten(int[][] quantizedBlock) {
        int[] flatArray = new int[64];
        
        for (int i = 0; i < 64; i++) {
            // Get the target 1D index from our lookup table
            int index = ZIGZAG_ORDER[i];
            
            // Convert that 1D index back into 2D row and column coordinates
            int row = index / 8;
            int col = index % 8;
            
            // Place the 2D matrix value into our new flat 1D array
            flatArray[i] = quantizedBlock[row][col];
        }
        
        return flatArray;
    }
}