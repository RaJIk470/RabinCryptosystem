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
        System.out.println(n + " " + n.bitLength());
        byte[] defineBytes = RabinNumbersGenerator.getDefineBytes();
        int pieceSize = n.bitLength() / 8 - defineBytes.length;
        if (pieceSize < 1) throw new RuntimeException("Smol key");
        if (pieceSize <= 0) pieceSize = 1;
        BigInteger[] cipher = new BigInteger[(bytes.length + pieceSize - 1) / pieceSize];
        for (int i = 0; i < cipher.length; i++) {
            int len = (i + 1) * pieceSize > bytes.length ? bytes.length % pieceSize : pieceSize;
            byte[] bs = new byte[len];
            for (int j = 0; j < len; j++) {
                bs[j] = bytes[i * pieceSize + j];
            }
            cipher[i] = new BigInteger(1, RabinNumbersGenerator.addDefineBytes(bs));
            //cipher[i] = new BigInteger(1, bytes, i * pieceSize, len);
            System.out.println(Arrays.toString(cipher[i].toByteArray()));
            cipher[i] = cipher[i].multiply(cipher[i].add(b)).mod(n);
        }

        return cipher;
    }
}
