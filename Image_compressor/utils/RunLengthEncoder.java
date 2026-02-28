package utils;

import java.util.ArrayList;
import java.util.List;

public class RunLengthEncoder {

    // A simple data structure to hold our (run of zeros, non-zero value) pairs
    public static class RLEPair {
        public int run;   // Number of preceding zeros (0 to 15)
        public int value; // The actual non-zero frequency value

        public RLEPair(int run, int value) {
            this.run = run;
            this.value = value;
        }

        @Override
        public String toString() {
            if (run == 0 && value == 0) return "EOB (End of Block)";
            if (run == 15 && value == 0) return "ZRL (16 Zeros)";
            return "(" + run + ", " + value + ")";
        }
    }

    public static List<RLEPair> encode(int[] flatBlock) {
        List<RLEPair> encodedData = new ArrayList<>();

        // 1. Extract the DC Coefficient (Index 0)
        // In a pro encoder, this is stored as the difference from the previous block's DC, 
        // but for our scratch build, storing it directly is perfectly fine.
        encodedData.add(new RLEPair(0, flatBlock[0]));

        // Find the position of the very last non-zero number so we know when to stop
        int lastNonZeroIndex = 0;
        for (int i = 63; i >= 1; i--) {
            if (flatBlock[i] != 0) {
                lastNonZeroIndex = i;
                break;
            }
        }

        // 2. Encode the AC Coefficients (Indices 1 through 63)
        int zeroRun = 0;
        for (int i = 1; i <= lastNonZeroIndex; i++) {
            if (flatBlock[i] == 0) {
                zeroRun++;
                
                // Rule 2: If we hit 16 zeros in a row, write a ZRL marker and reset
                if (zeroRun == 16) {
                    encodedData.add(new RLEPair(15, 0));
                    zeroRun = 0;
                }
            } else {
                // Rule 1: We found a non-zero! Write the run count and the value
                encodedData.add(new RLEPair(zeroRun, flatBlock[i]));
                zeroRun = 0; // Reset the zero counter
            }
        }

        // 3. Rule 3: If there is empty space left at the end of the 8x8 block, cap it with EOB
        if (lastNonZeroIndex < 63) {
            encodedData.add(new RLEPair(0, 0));
        }

        return encodedData;
    }
    public static int[] decode(List<RLEPair> rleData) {
        int[] flatBlock = new int[64];
        int index = 0;

        for (RLEPair pair : rleData) {
            if (pair.run == 0 && pair.value == 0) {
                break; // EOB: The rest of the array remains 0
            }
            // Skip forward by the number of zeros
            index += pair.run;
            // Place the non-zero value
            if (index < 64) {
                flatBlock[index] = pair.value;
                index++;
            }
        }
        return flatBlock;
    }
}