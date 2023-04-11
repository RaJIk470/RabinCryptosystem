package org.example;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RabinNumbersGenerator {
    private SecureRandom secureRandom;
    public RabinNumbersGenerator() {
        secureRandom = new SecureRandom();
    }

    public BigInteger getPrimeModulo(int bitLength, int module, int remainder) {
        if (remainder >= module) throw new IllegalArgumentException("Remainder is bigger than module");
        BigInteger prime;
        do {
            prime = BigInteger.probablePrime(bitLength, secureRandom);
        } while(!prime.mod(BigInteger.valueOf(module)).equals(BigInteger.valueOf(remainder)));

        return prime;
    }

    public BigInteger getRabinPrime(int bitLength) {
        return getPrimeModulo(bitLength, 4, 3);
    }

    public int getRabinB() {
        return Math.abs(secureRandom.nextInt()) % 10533;
    }

    public BigInteger[] generateKey(int bitLength) {
        return new BigInteger[] {getRabinPrime(bitLength), getRabinPrime(bitLength), BigInteger.valueOf(getRabinB())};
    }

    public static byte[] getDefineBytes() {
        byte b = (byte)127;
        //return new byte[] {b, (byte)(b - 1), (byte)(b - 2), (byte)(b - 3), (byte)(b - 4)};
        //return new byte[] {b, (byte)(b - 1)};
        return new byte[] {b};
    }

    public static boolean defineBytesMatch(byte[] bytes) {
        byte[] defineBytes = getDefineBytes();
        for (int i = 0; i < defineBytes.length; i++) {
            if (bytes[i] != defineBytes[i])
                return false;
        }

        return true;
    }

    public static byte[] addDefineBytes(byte[] src) {
        byte[] defineBytes = getDefineBytes();
        byte[] result = new byte[src.length + defineBytes.length];
        for (int i = 0; i < defineBytes.length; i++) {
            result[i] = defineBytes[i];
        }
        for (int i = defineBytes.length; i < src.length + defineBytes.length; i++) {
            result[i] = src[i - defineBytes.length];
        }

        return result;
    }

    public static byte[] removeDefineBytes(byte[] src) {
        int length = getDefineBytes().length;
        byte[] result = new byte[src.length - length];
        for (int i = 0; i < result.length; i++) {
            result[i] = src[i + length];
        }

        return result;
    }
}
