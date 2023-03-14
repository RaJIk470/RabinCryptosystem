package org.example;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

public class RabinCryptosystemDecoder implements Decoder {
    private BigInteger p;
    private BigInteger q;

    private BigInteger b;

    private BigInteger n;

    private final BigInteger ZERO = BigInteger.valueOf(0);
    private final BigInteger ONE = BigInteger.valueOf(1);
    private final BigInteger TWO = BigInteger.valueOf(2);
    private final BigInteger FOUR = BigInteger.valueOf(4);

    public RabinCryptosystemDecoder(BigInteger p, BigInteger q, BigInteger b) {
        this.p = p;
        this.q = q;
        n = p.multiply(q);
        this.b = b;
    }

    @Override
    public byte[] decode(BigInteger[] cipher) {
        BigInteger[] result = new BigInteger[cipher.length];
        for (int i = 0; i < cipher.length; i++) {
            BigInteger d = b.pow(2).add(cipher[i].multiply(FOUR)).mod(n);
            BigInteger mp = d.modPow(p.add(ONE).divide(FOUR), p);
            BigInteger mq = d.modPow(q.add(ONE).divide(FOUR), q);
            BigInteger[] gcdResult = gcd(p, q);
            BigInteger yp = gcdResult[1];
            BigInteger yq = gcdResult[2];

            BigInteger d1 = yp.multiply(p).multiply(mq).add(yq.multiply(q).multiply(mp)).mod(n);
            BigInteger d2 = n.subtract(d1);
            BigInteger d3 = yp.multiply(p).multiply(mq).subtract(yq.multiply(q).multiply(mp)).mod(n);
            BigInteger d4 = n.subtract(d3);
            BigInteger[] ds = new BigInteger[]{d1, d2, d3, d4};

            for (int j = 0; j < ds.length; j++) {
                if (ds[j].subtract(b).mod(TWO).equals(ZERO)) {
                    ds[j] = b.negate().add(ds[j]).divide(TWO).mod(n);
                } else {
                    ds[j] = b.negate().add(ds[j]).add(n).divide(TWO).mod(n);
                }
            }

            result[i] = Arrays.stream(ds).min(BigInteger::compareTo).get();
        }

        System.out.println(Arrays.toString(result));
        byte[][] bytes = new byte[result.length][];
        int len = 0;
        for (int i = 0; i < result.length; i++) {
            bytes[i] = result[i].toByteArray();
            len += bytes[i].length;
        }
        byte[] resultBytes = new byte[len];
        int current = 0;
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0; j < bytes[i].length; j++) {
                resultBytes[current++] = bytes[i][j];
            }
        }
        return resultBytes;
    }

    public static BigInteger[] gcd(BigInteger a, BigInteger b) {
        BigInteger s = BigInteger.ZERO;
        BigInteger old_s = BigInteger.ONE;
        BigInteger t = BigInteger.ONE;
        BigInteger old_t = BigInteger.ZERO;
        BigInteger r = b;
        BigInteger old_r = a;
        while (!r.equals(BigInteger.ZERO)) {
            BigInteger q = old_r.divide(r);
            BigInteger tr = r;
            r = old_r.subtract(q.multiply(r));
            old_r = tr;

            BigInteger ts = s;
            s = old_s.subtract(q.multiply(s));
            old_s = ts;

            BigInteger tt = t;
            t = old_t.subtract(q.multiply(t));
            old_t = tt;
        }
        return new BigInteger[]{old_r, old_s, old_t};
    }
}
