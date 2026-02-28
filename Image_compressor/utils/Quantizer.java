package utils;

public class Quantizer {
    // Standard JPEG Luminance Quantization Matrix
    public static final int[][] LUMINANCE_MATRIX = {
        {16, 11, 10, 16, 24, 40, 51, 61}, {12, 12, 14, 19, 26, 58, 60, 55},
        {14, 13, 16, 24, 40, 57, 69, 56}, {14, 17, 22, 29, 51, 87, 80, 62},
        {18, 22, 37, 56, 68, 109, 103, 77}, {24, 35, 55, 64, 81, 104, 113, 92},
        {49, 64, 78, 87, 103, 121, 120, 101}, {72, 92, 95, 98, 112, 100, 103, 99}
    };

    // Standard JPEG Chrominance Quantization Matrix
    public static final int[][] CHROMA_MATRIX = {
        {17, 18, 24, 47, 99, 99, 99, 99}, {18, 21, 26, 66, 99, 99, 99, 99},
        {24, 26, 56, 99, 99, 99, 99, 99}, {47, 66, 99, 99, 99, 99, 99, 99},
        {99, 99, 99, 99, 99, 99, 99, 99}, {99, 99, 99, 99, 99, 99, 99, 99},
        {99, 99, 99, 99, 99, 99, 99, 99}, {99, 99, 99, 99, 99, 99, 99, 99}
    };

    /**
     * Quantizes the DCT coefficients.
     * Smaller scale = Higher Quality (Less Haze).
     */
    public static int[][] quantize(Block block, boolean isLuminance, double scale) {
        int[][] matrix = isLuminance ? LUMINANCE_MATRIX : CHROMA_MATRIX;
        int[][] result = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Apply the quality scale to the matrix element
                double step = Math.max(1.0, matrix[i][j] * scale);
                // Divide and round to the nearest integer
                result[i][j] = (int) Math.round(block.data[i][j] / step);
            }
        }
        return result;
    }

    /**
     * Reverses the quantization (Dequantization).
     */
    public static Block dequantize(int[][] quantized, boolean isLuminance, double scale) {
        int[][] matrix = isLuminance ? LUMINANCE_MATRIX : CHROMA_MATRIX;
        Block result = new Block();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                double step = Math.max(1.0, matrix[i][j] * scale);
                // Multiply back the step
                result.data[i][j] = quantized[i][j] * step;
            }
        }
        return result;
    }
}