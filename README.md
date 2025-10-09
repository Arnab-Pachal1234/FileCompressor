# 📦 File Compressor using Huffman Encoding

A lightweight, fast, and lossless **file compressor** built using the **Huffman Encoding algorithm**. This tool compresses plain text files by encoding frequent characters with shorter binary codes, reducing storage size without losing any data.

---

## 🚀 Features

- ⚡ **Fast compression & decompression**
- 🔒 **Lossless**: 100% data integrity
- 📊 Compression statistics (size before/after, ratio)
- 🧠 Huffman Tree built from input file frequencies
- 💻 Simple command-line interface
- 🧪 Works with any plain text file (`.txt`)

---

## 🧠 How Huffman Encoding Works

1. Count character frequencies in the file
2. Build a binary tree (Huffman Tree) using a min-heap
3. Assign shorter binary codes to more frequent characters
4. Encode the file using these codes
5. Store encoded data + Huffman Tree (for decoding)

---

## 📁 File Structure

HUFFMANENCODING/
├── Encoder.java # Compresses the input text using Huffman Encoding
├── Decoder.java # Decompresses the encoded file back to original
├── example.txt # Input file to be compressed
├── decompressed.txt # Output file after decompression
└── .gitignore # Git ignored files (e.g., .class, .bin)

✅ Features

Lossless text file compression using Huffman Coding

Efficient frequency table generation and tree building

Encodes characters using optimal bit-lengths

Decompresses accurately to match original text

Easy to run from the command line

👨‍💻 Author

Arnab Pachal
📧 ap.23cs8031@nitdgp.ac.in

🌐 GitHub - Arnab-pachal
📝 License
