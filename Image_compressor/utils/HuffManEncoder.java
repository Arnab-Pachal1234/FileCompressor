package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanEncoder {

    private static class Node implements Comparable<Node> {
        String symbol; 
        int frequency;
        Node left;
        Node right;

    
        public Node(String symbol, int frequency) {
            this.symbol = symbol;
            this.frequency = frequency;
        }

        // Internal node constructor
        public Node(Node left, Node right) {
            this.symbol = null;
            this.frequency = left.frequency + right.frequency;
            this.left = left;
            this.right = right;
        }

        // PriorityQueue needs to know how to sort the nodes (lowest frequency first)
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.frequency, other.frequency);
        }
    }

    // Main method to encode our list of RLE pairs into a binary string
    public static EncodedResult encode(List<RunLengthEncoder.RLEPair> rleData) {
        // 1. Calculate the frequency of each RLE pair
        Map<String, Integer> frequencies = new HashMap<>();
        for (RunLengthEncoder.RLEPair pair : rleData) {
            String symbol = pair.toString();
            frequencies.put(symbol, frequencies.getOrDefault(symbol, 0) + 1);
        }

        // 2. Build the Huffman Tree using a Priority Queue
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }

        // Edge case: if there's only one unique symbol
        if (pq.size() == 1) {
            pq.add(new Node("DUMMY", 1)); // Add a dummy to create a valid tree
        }

        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            pq.add(new Node(left, right)); // Merge two smallest nodes
        }

        Node root = pq.poll();

        // 3. Generate the binary lookup table from the tree
        Map<String, String> huffmanTable = new HashMap<>();
        buildLookupTable(root, "", huffmanTable);

        // 4. Translate the original RLE data into the final binary string
        StringBuilder binaryOutput = new StringBuilder();
        for (RunLengthEncoder.RLEPair pair : rleData) {
            binaryOutput.append(huffmanTable.get(pair.toString()));
        }

        return new EncodedResult(binaryOutput.toString(), huffmanTable);
    }

    // Recursive helper to traverse the tree and assign 0s and 1s
    private static void buildLookupTable(Node node, String code, Map<String, String> table) {
        if (node == null) return;

        // If it's a leaf node, save the symbol and its binary code
        if (node.left == null && node.right == null) {
            table.put(node.symbol, code);
        }

        buildLookupTable(node.left, code + "0", table);
        buildLookupTable(node.right, code + "1", table);
    }

    // A simple wrapper to return both the binary string and the dictionary needed to decode it
    public static class EncodedResult {
        public final String binaryString;
        public final Map<String, String> huffmanTable;

        public EncodedResult(String binaryString, Map<String, String> huffmanTable) {
            this.binaryString = binaryString;
            this.huffmanTable = huffmanTable;
        }
    }
}