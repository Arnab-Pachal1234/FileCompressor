package utils;

import java.io.FileOutputStream;
import java.io.IOException;

public class BitWriter {

    // Converts a string of '1's and '0's into actual bits and saves to disk
    public static void saveCompressedStream(String binaryString, String outputFilePath) {
        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            int currentByte = 0;
            int bitCount = 0;

            for (int i = 0; i < binaryString.length(); i++) {
                char bit = binaryString.charAt(i);
                
                // Shift the current byte left by 1, and add the new bit (0 or 1)
                currentByte = (currentByte << 1) | (bit == '1' ? 1 : 0);
                bitCount++;

                // Once we have packed 8 bits, write the byte to the file
                if (bitCount == 8) {
                    fos.write(currentByte);
                    currentByte = 0;
                    bitCount = 0;
                }
            }

            // If there are leftover bits that didn't perfectly divide by 8, pad with 0s and write
            if (bitCount > 0) {
                currentByte = currentByte << (8 - bitCount);
                fos.write(currentByte);
            }
            
            System.out.println("Successfully saved compressed binary data to: " + outputFilePath);
            
        } catch (IOException e) {
            System.out.println("Error writing binary file: " + e.getMessage());
        }
    }
}