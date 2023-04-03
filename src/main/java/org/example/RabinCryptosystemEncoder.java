package org.example;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.PrimitiveIterator;

public class RabinCryptosystemEncoder implements Encoder {
    private BigInteger n;
    private BigInteger b;

    public RabinCryptosystemEncoder(BigInteger n, BigInteger b) {
        this.n = n;
        this.b = b;
    }

    @Override
    public BigInteger[] encode(byte[] bytes) {
        int pieceSize = n.bitLength() / 8 - 1;
        if (pieceSize <= 0) pieceSize = 1;
        BigInteger[] cipher = new BigInteger[(bytes.length + pieceSize - 1) / pieceSize];
        byte[][] byteArrays = new byte[(bytes.length + pieceSize - 1) / pieceSize][];
        int lenSum = 0;
        for (int i = 0; i < cipher.length; i++) {
            int len = (i + 1) * pieceSize > bytes.length ? bytes.length % pieceSize : pieceSize;
            byteArrays[i] = new byte[len + 1];
            byteArrays[i][0] = 1;
            System.arraycopy(bytes, i * pieceSize, byteArrays[i], 1, len);
            cipher[i] = new BigInteger(1, byteArrays[i], 0, len + 1);
            //cipher[i] = new BigInteger(bytes, i * pieceSize, len);
            byte[] bts = cipher[i].toByteArray();
            lenSum += bts.length;
            System.out.println(Arrays.toString(cipher[i].toByteArray()));
            cipher[i] = cipher[i].multiply(cipher[i].add(b)).mod(n);
        }
        System.out.println(lenSum);


        lenSum = 0;
        for (int i = 0; i < byteArrays.length; i++) {
            System.out.println(Arrays.toString(byteArrays[i]));
            lenSum += byteArrays[i].length;
        }

        System.out.println(lenSum);


        return cipher;
    }
}
