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
}
