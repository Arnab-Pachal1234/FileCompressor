package utils;

public class IDCTMath {

    private static double getC(int index) {
        return (index == 0) ? 1.0 / Math.sqrt(2.0) : 1.0;
    }

    public static Block applyInverseDCT(Block freqBlock) {
        Block outputBlock = new Block();

        // x and y loop through the spatial pixels we are reconstructing
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                
                double sum = 0.0;

                // u and v loop through the frequencies
                for (int u = 0; u < 8; u++) {
                    for (int v = 0; v < 8; v++) {
                        
                        double cosX = Math.cos(((2.0 * x + 1.0) * u * Math.PI) / 16.0);
                        double cosY = Math.cos(((2.0 * y + 1.0) * v * Math.PI) / 16.0);
                        
                        sum += getC(u) * getC(v) * freqBlock.data[u][v] * cosX * cosY;
                    }
                }
                
                outputBlock.data[x][y] = 0.25 * sum;
            }
        }
        return outputBlock;
    }
}