package Krypto;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ElGamal implements IAsymmetricEncryption {
    private static final BigInteger UNITY = new BigInteger("1");
    //private static final BigInteger TWO = new BigInteger("2");
    private int standardSize = 2048;

    @Override
    public String encrypt(String m, Map<String, String> keys) {
        return null;
    }

    @Override
    public String decrypt(String c, Map<String, String> keys) {
        return null;
    }

    @Override
    public Map<String, String> calculateKeyPair() {
        Map<String, String> keyPair = new HashMap<>();
        // Generate modulo p and primitive root g
        BigInteger p = BigInteger.probablePrime(standardSize - 1, new Random());
        return null;
    }

    @Override
    public String sign(String signature, Map<String, String> keys) {
        return null;
    }
}
