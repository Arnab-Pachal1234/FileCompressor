package utils;
import java.util.*;
public class Block {
    public final double[][] data = new double[8][8];

    public Block() {}

    public void print() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.printf("%6.1f ", data[i][j]);
            }
            System.out.println();
        }
    }
}