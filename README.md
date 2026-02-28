# 📦 File & Image Compressor in Java

### Huffman (Text - Lossless) + JPEG-Based (Image - Lossy)

A complete compression system built from scratch in **Java**,
implementing both:

-   📝 **Lossless Text Compression using Huffman Encoding**
-   🖼️ **Lossy Image Compression inspired by JPEG algorithm**

This project demonstrates deep understanding of: - Data Structures -
Frequency-domain transformations - Entropy encoding - Bit-level file
handling - Compression theory

------------------------------------------------------------------------

# 🚀 Features

## 📝 Text Compression (Huffman Encoding)

-   ⚡ Fast compression & decompression
-   🔒 100% Lossless
-   🧠 Automatic frequency analysis
-   🌳 Custom Huffman Tree implementation
-   📦 Binary encoding with Bit-level writing
-   📊 Compression ratio calculation

------------------------------------------------------------------------

## 🖼️ Image Compression (JPEG-Based Implementation)

-   🎨 RGB → YCbCr conversion
-   📦 8×8 Block processing
-   🔄 DCT (Discrete Cosine Transform)
-   📉 Quantization
-   🧭 Zig-Zag scanning
-   🔢 Run-Length Encoding (Zero compression)
-   🧠 Huffman Encoding
-   🔁 Full decompression pipeline (IDCT supported)

------------------------------------------------------------------------

# 📁 Project Structure

COMPRESSION_PROJECT/ │ ├── HuffmanEncoding/ │ ├── Encoder.java │ ├──
Decoder.java │ ├── example.txt │ └── decompressed.txt │ ├──
Image_compressor/ │ ├── utils/ │ │ ├── BitReader.java │ │ ├──
BitWriter.java │ │ ├── Block.java │ │ ├── ColorSpace.java │ │ ├──
DCTMath.java │ │ ├── IDCTMath.java │ │ ├── Quantizer.java │ │ ├──
ZigZagScanner.java │ │ ├── RunLengthEncoder.java │ │ ├──
HuffmanEncoder.java │ │ └── JpegEncoder.java │ │ │ ├── Main.java │ └──
.gitignore │ └── README.md

------------------------------------------------------------------------

# 🧠 Text Compression -- How Huffman Encoding Works

1️⃣ Count frequency of each character\
2️⃣ Build Min-Heap (Priority Queue)\
3️⃣ Construct Huffman Tree\
4️⃣ Generate prefix-free binary codes\
5️⃣ Encode file into compressed binary\
6️⃣ Decode using stored tree

More frequent characters get shorter codes → Reduced average bit length.

------------------------------------------------------------------------

# 🖼️ Image Compression -- JPEG Pipeline Implemented

### 1️⃣ Color Space Conversion

Convert RGB → YCbCr\
(Y = Luminance, Cb/Cr = Chrominance)

------------------------------------------------------------------------

### 2️⃣ 8×8 Block Splitting

Image divided into fixed-size blocks.

------------------------------------------------------------------------

### 3️⃣ Discrete Cosine Transform (DCT)

Transforms spatial pixels into frequency coefficients.

------------------------------------------------------------------------

### 4️⃣ Quantization

Reduces high-frequency components (lossy stage).

------------------------------------------------------------------------

### 5️⃣ Zig-Zag Scanning

Reorders 8×8 matrix into 1D array (low frequency first).

------------------------------------------------------------------------

### 6️⃣ Run-Length Encoding (RLE)

Compress consecutive zeros: (ZeroCount, Value)

------------------------------------------------------------------------

### 7️⃣ Huffman Encoding

Final entropy compression stage.

------------------------------------------------------------------------

### 8️⃣ Decompression Pipeline

-   Huffman Decode
-   RLE Decode
-   Dequantization
-   IDCT
-   YCbCr → RGB conversion

------------------------------------------------------------------------

# ▶️ How to Run

## 📝 Text Compression

javac Encoder.java Decoder.java\
java Encoder example.txt\
java Decoder compressed.bin

------------------------------------------------------------------------

## 🖼️ Image Compression

javac Main.java utils/\*.java\
java Main input.jpg

------------------------------------------------------------------------

# 🛠️ Technologies Used

-   Java
-   Priority Queue (Min Heap)
-   Binary Tree
-   Matrix Transformations
-   Discrete Cosine Transform
-   Run-Length Encoding
-   Huffman Coding
-   Bitwise File Handling

------------------------------------------------------------------------

# 🎯 Key Concepts Demonstrated

-   Lossless vs Lossy Compression
-   Frequency-domain transformation
-   Entropy encoding
-   Image signal processing
-   Bit-level I/O
-   Algorithm optimization

------------------------------------------------------------------------

# 👨‍💻 Author

Arnab Pachal\
📧 ap.23cs8031@nitdgp.ac.in\
🌐 https://github.com/Arnab-pachal

------------------------------------------------------------------------

⭐ If you found this project useful, consider giving it a star!
