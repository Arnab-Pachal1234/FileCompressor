import java.io.*;
import java.util.*;

public class Encoder {

    static class TreeNode {
        char ch;
        int freq;
        TreeNode left, right;

        TreeNode(char ch, int freq) { this.ch = ch; this.freq = freq; }
        TreeNode(int freq, TreeNode left, TreeNode right) {
            this.ch = '-'; this.freq = freq; this.left = left; this.right = right;
        }
        boolean isLeaf() { return left == null && right == null; }
    }

    static TreeNode buildTree(Map<Character, Integer> freqMap) {
        PriorityQueue<TreeNode> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.freq));
        for (Map.Entry<Character, Integer> e : freqMap.entrySet()) pq.add(new TreeNode(e.getKey(), e.getValue()));
        while (pq.size() > 1) {
            TreeNode left = pq.poll(), right = pq.poll();
            pq.add(new TreeNode(left.freq + right.freq, left, right));
        }
        return pq.peek();
    }

    static void generateCodes(TreeNode root, String code, Map<Character, String> map) {
        if (root == null) return;
        if (root.isLeaf()) map.put(root.ch, code.length() > 0 ? code : "0");
        generateCodes(root.left, code + "0", map);
        generateCodes(root.right, code + "1", map);
    }

    static byte[] bitStringToBytes(String bitString) {
        int len = (bitString.length() + 7) / 8;
        byte[] data = new byte[len];
        int byteIndex = 0, bitIndex = 0;
        //print bit string to char array and iterate
        System.out.println("Printing the bit string as char array :-");
        for (char c : bitString.toCharArray()) {
            System.out.print(c+" ");
        }
        for (char bit : bitString.toCharArray()) {
            data[byteIndex] <<= 1;
            if (bit == '1') data[byteIndex] |= 1;
            bitIndex++;
            if (bitIndex == 8) { bitIndex = 0; byteIndex++; }
        }
        if (bitIndex > 0) data[byteIndex] <<= (8 - bitIndex);
        System.out.println("Printing the bit string as byte array :-");
        for(byte b : data) System.out.print(Integer.toBinaryString(b) + " ");
        return data;
    }

    static String encodeText(String text, Map<Character, String> codeMap) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) sb.append(codeMap.get(c));
        return sb.toString();
    }

    static void writeCompressedFile(String filename, Map<Character, String> codeMap, String bitString) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(filename))) {
            dos.writeInt(codeMap.size());
            for (Map.Entry<Character, String> e : codeMap.entrySet()) {
                dos.writeChar(e.getKey());
                dos.writeInt(e.getValue().length());
                dos.writeUTF(e.getValue());
            }
            dos.writeInt(bitString.length());
            dos.write(bitStringToBytes(bitString));
        }
    }

    public static void main(String[] args) throws IOException {
        String inputFile = "example.txt";
        String compressedFile = "compressed.bin";

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
        }
        String text = sb.toString();

        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : text.toCharArray()) freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);

        TreeNode root = buildTree(freqMap);
        Map<Character, String> codeMap = new HashMap<>();
        generateCodes(root, "", codeMap);

        String bitString = encodeText(text, codeMap);
        writeCompressedFile(compressedFile, codeMap, bitString);
        //print the code map
        for(Map.Entry<Character, String> e : codeMap.entrySet()) System.out.println(e.getKey() + " : " + e.getValue());
        System.out.println("✅ Compression done! Saved as " + compressedFile);
    }
}

