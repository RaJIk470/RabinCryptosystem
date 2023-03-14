package org.example;

import java.math.BigInteger;

public interface Decoder {
    byte[] decode(BigInteger[] cipher);
}
