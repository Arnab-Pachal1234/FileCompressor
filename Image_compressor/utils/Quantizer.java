package utils;

public class Quantizer {

    // The standard JPEG Luminance (Brightness) Quantization Matrix
    // Notice how the numbers get much larger towards the bottom-right
    private static final int[][] LUMINANCE_MATRIX = {
        { 16,  11,  10,  16,  24,  40,  51,  61 },
        { 12,  12,  14,  19,  26,  58,  60,  55 },
        { 14,  13,  16,  24,  40,  57,  69,  56 },
        { 14,  17,  22,  29,  51,  87,  80,  62 },
        { 18,  22,  37,  56,  68, 109, 103,  77 },
        { 24,  35,  55,  64,  81, 104, 113,  92 },
        { 49,  64,  78,  87, 103, 121, 120, 101 },
        { 72,  92,  95,  98, 112, 100, 103,  99 }
    };

    public static int[][] quantize(Block dctBlock) {
        int[][] quantized = new int[8][8];

        for (int u = 0; u < 8; u++) {
            for (int v = 0; v < 8; v++) {
                
                // Divide the DCT coefficient by the corresponding matrix value
                double value = dctBlock.data[u][v] / LUMINANCE_MATRIX[u][v];
                
                // Round to the nearest integer. This is where the data is "lost" but heavily compressed.
                quantized[u][v] = (int) Math.round(value);
            }
        }
        
        return quantized;
    }
}