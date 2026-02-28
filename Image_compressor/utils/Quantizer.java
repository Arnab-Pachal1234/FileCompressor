package utils;

public class Quantizer {
    public static final int[][] LUMINANCE_MATRIX = {
        {16, 11, 10, 16, 24, 40, 51, 61}, {12, 12, 14, 19, 26, 58, 60, 55},
        {14, 13, 16, 24, 40, 57, 69, 56}, {14, 17, 22, 29, 51, 87, 80, 62},
        {18, 22, 37, 56, 68, 109, 103, 77}, {24, 35, 55, 64, 81, 104, 113, 92},
        {49, 64, 78, 87, 103, 121, 120, 101}, {72, 92, 95, 98, 112, 100, 103, 99}
    };

    public static final int[][] CHROMA_MATRIX = {
        {17, 18, 24, 47, 99, 99, 99, 99}, {18, 21, 26, 66, 99, 99, 99, 99},
        {24, 26, 56, 99, 99, 99, 99, 99}, {47, 66, 99, 99, 99, 99, 99, 99},
        {99, 99, 99, 99, 99, 99, 99, 99}, {99, 99, 99, 99, 99, 99, 99, 99},
        {99, 99, 99, 99, 99, 99, 99, 99}, {99, 99, 99, 99, 99, 99, 99, 99}
    };

    // New: Scale the matrix based on quality. 
    // scale 0.5 = High Quality, scale 2.0 = Low Quality
    public static int[][] quantize(Block block, boolean isLuminance, double scale) {
        int[][] matrix = isLuminance ? LUMINANCE_MATRIX : CHROMA_MATRIX;
        int[][] result = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                double val = Math.max(1, matrix[i][j] * scale); 
                result[i][j] = (int) Math.round(block.data[i][j] / val);
            }
        }
        return result;
    }

    public static Block dequantize(int[][] quantized, boolean isLuminance, double scale) {
        int[][] matrix = isLuminance ? LUMINANCE_MATRIX : CHROMA_MATRIX;
        Block result = new Block();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                double val = Math.max(1, matrix[i][j] * scale);
                result.data[i][j] = quantized[i][j] * val;
            }
        }
        return result;
    }
}