package Krypto;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ElGamal implements IAsymmetricEncryption {
    private static final BigInteger UNITY = new BigInteger("1");
    private static final BigInteger ZERO = new BigInteger("0");
    private static final BigInteger STANDARD_SIZE = new BigInteger("2048");
    private static final BigInteger TWO = new BigInteger("2");
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
        // Generation of Prime q to calculate p. r is the generator for q

        // Prototyp 1: Too slow
        BigInteger p = ZERO;
        BigInteger q = ZERO;
        int counter = 0;
        // If p is composite, gen. a new q and calculate p
        while(!p.isProbablePrime(100) || p.equals(ZERO)){
            q = BigInteger.probablePrime(standardSize/2 - 1, new Random());
            p = (q.multiply(TWO)).add(UNITY);
           counter++;
        }
        // Generate g
        BigInteger g = ZERO;
       /* while(g.compareTo(STANDARD_SIZE) == -1 && g.compareTo(STANDARD_SIZE.divide(TWO)) == 1
                || g.equals(ZERO)
                || g.modPow(TWO, p).compareTo(UNITY) == 0
                || g.modPow(q, p).compareTo(UNITY) == 0){

        }*/
        return null;
    }

    @Override
    public String sign(String signature, Map<String, String> keys) {
        return null;
    }
}
