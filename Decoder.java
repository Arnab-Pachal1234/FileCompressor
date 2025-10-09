import java.io.*;
import java.util.*;

public class Decoder {

    static class TreeNode {
        char ch;
        TreeNode left, right;
        TreeNode() { this.ch = '-'; }
        boolean isLeaf() { return left == null && right == null; }
    }

    static String bytesToBitString(byte[] data, int totalBits) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            int b = data[i] & 0xFF;
            String bits = String.format("%8s", Integer.toBinaryString(b)).replace(' ','0');
            if (i == data.length - 1 && totalBits % 8 != 0) bits = bits.substring(0, totalBits % 8);
            sb.append(bits);
        }
        System.out.println("The bit string is :-"+sb.toString());
        return sb.toString();
    }

    static String decodeBitString(String bitString, TreeNode root) {
        StringBuilder sb = new StringBuilder();
        TreeNode current = root;
        if(current.isLeaf()){return new StringBuilder(current.ch+"").toString();}
        for (char bit : bitString.toCharArray()) {
            current = (bit == '0') ? current.left : current.right;
            if (current.isLeaf()) { sb.append(current.ch); current = root; }
        }
        return sb.toString();
    }

    static String readCompressedFile(String filename, Map<Character, String> codeMap, TreeNode[] treeRef) throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(filename))) {
            int numChars = dis.readInt();
            for (int i = 0; i < numChars; i++) {
                char c = dis.readChar();
                int length = dis.readInt();
                String code = dis.readUTF();
                codeMap.put(c, code);
            }

            TreeNode root = new TreeNode();
            for (Map.Entry<Character, String> e : codeMap.entrySet()) {
                TreeNode current = root;
                String code = e.getValue();
                for (char bit : code.toCharArray()) {
                    if (bit == '0') { if (current.left == null) current.left = new TreeNode(); current = current.left; }
                    else { if (current.right == null) current.right = new TreeNode(); current = current.right; }
                }
                current.ch = e.getKey();
            }
            treeRef[0] = root;

            int totalBits = dis.readInt();
            byte[] compressedBytes = dis.readAllBytes();
            System.out.println("The compressed bytes are :-");
            for(byte b : compressedBytes) System.out.print(Integer.toBinaryString(b) + " " );
            return bytesToBitString(compressedBytes, totalBits);
        }
    }

    public static void main(String[] args) throws IOException {
        String compressedFile = "compressed.bin";

        Map<Character, String> codeMap = new HashMap<>();
        TreeNode[] treeRef = new TreeNode[1];
        String bitString = readCompressedFile(compressedFile, codeMap, treeRef);

        String decodedText = decodeBitString(bitString, treeRef[0]);
        System.out.println("Decoded text: " + decodedText);
       
        String outputFile = "decompressed.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            bw.write(decodedText);
        }
    }
}
