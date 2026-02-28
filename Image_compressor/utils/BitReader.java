package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BitReader {

    public static List<RunLengthEncoder.RLEPair> readCompressedStream(String filePath, Map<String, String> huffmanTable) {
        // 1. Reverse the dictionary so we can look up Symbols using Binary Strings
        Map<String, String> reverseDict = new HashMap<>();
        for (Map.Entry<String, String> entry : huffmanTable.entrySet()) {
            reverseDict.put(entry.getValue(), entry.getKey()); // e.g., "101" -> "EOB"
        }

        List<RunLengthEncoder.RLEPair> recoveredPairs = new ArrayList<>();
        StringBuilder currentBits = new StringBuilder();

        // 2. Read the file byte-by-byte
        try (FileInputStream fis = new FileInputStream(filePath)) {
            int currentByte;
            while ((currentByte = fis.read()) != -1) {
                
                // Extract all 8 bits from the byte, from left to right
                for (int i = 7; i >= 0; i--) {
                    int bit = (currentByte >> i) & 1;
                    currentBits.append(bit);

                    // 3. Check if our growing bit string matches a known code in the dictionary
                    if (reverseDict.containsKey(currentBits.toString())) {
                        String symbol = reverseDict.get(currentBits.toString());
                        recoveredPairs.add(parseSymbol(symbol));
                        currentBits.setLength(0); // Reset to capture the next code
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading binary file: " + e.getMessage());
        }
        
        return recoveredPairs;
    }

    // Helper method to convert your text symbols back into actual RLEPair objects
    private static RunLengthEncoder.RLEPair parseSymbol(String symbol) {
        if (symbol.contains("EOB")) return new RunLengthEncoder.RLEPair(0, 0);
        if (symbol.contains("ZRL")) return new RunLengthEncoder.RLEPair(15, 0);
        
        // Strip the parentheses and parse the numbers. e.g., "(2, -1)"
        String clean = symbol.replace("(", "").replace(")", "");
        String[] parts = clean.split(",");
        return new RunLengthEncoder.RLEPair(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()));
    }
}