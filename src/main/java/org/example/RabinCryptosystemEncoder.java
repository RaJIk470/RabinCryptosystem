package org.example;

import java.math.BigInteger;
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
        for (int i = 0; i < cipher.length; i++) {
            int len = (i + 1) * pieceSize > bytes.length ? bytes.length % pieceSize : pieceSize;
            cipher[i] = new BigInteger(bytes, i * pieceSize, len);
            cipher[i] = cipher[i].multiply(cipher[i].add(b)).mod(n);
        }

        return cipher;
    }
}
