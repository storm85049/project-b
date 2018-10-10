package Krypto;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
/*

        ////////////IMPLEMENTATION///////////////////
       IAsymmetricEncryption elGamalA = new ElGamal();
       IAsymmetricEncryption elGamalB = new ElGamal();

       String encryptStringA = "Simon Hoyos Cadavid";
       System.out.println("OG Text: " +  encryptStringA);
       String encryptStringB = "020252368945";

       encryptStringA = asciiToInt(encryptStringA);

       String encryptMessageA = elGamalA.encrypt(encryptStringA, elGamalB.getPublicKeyMap());
       String encryptMessageB = elGamalB.encrypt(encryptStringB, elGamalA.getPublicKeyMap());

       System.out.println("OG: " + encryptStringA);
       System.out.println("Encryption A ---> B");
       System.out.println(encryptMessageA);
       System.out.println("Decryption B");
       elGamalB.addExternalKeys(elGamalA.getPublicKeyMap().get("public_key_j"));
       String decryptedMessageB = elGamalB.decrypt(encryptMessageA);
       System.out.println(decryptedMessageB);
       System.out.println(remakeStringFromAscii(decryptedMessageB));

       System.out.println("OG: " + encryptStringB);
       System.out.println("Encryption B ---> A");
       System.out.println(encryptMessageB);
       elGamalA.addExternalKeys(elGamalB.getPublicKeyMap().get("public_key_j"));
       System.out.println("Decryption A");
       System.out.println(elGamalA.decrypt(encryptMessageB) );
       ///////////////////////////////////////////////////////////////

 */

public class ElGamal implements IAsymmetricEncryption {
    private static final BigInteger UNITY = new BigInteger("1");
    private static final BigInteger ZERO = new BigInteger("0");
    private static final BigInteger STANDARD_SIZE = new BigInteger("2048");
    private static final int DIVIDER = 4;
    private static final BigInteger TWO = new BigInteger("2");
    private int standardSize = 2048;
    private Map<String, String> publicKeyMap;
    private String privateKey, publicKey, modulo, generator, externalPublicKeyJ;

    // Constructor
    public ElGamal(){
        publicKeyMap = calculateKeyPair();
        privateKey = publicKeyMap.get("private_key");
        publicKey = publicKeyMap.get("public_key");
        modulo = publicKeyMap.get("modulo");
        publicKeyMap.remove("private_key"); // Key Pair has all the Data that´s Public
    }
    @Override
    public String encrypt(String m, Map<String, String> keys) {
        BigInteger moduloInt = new BigInteger(keys.get("modulo"));
        BigInteger iInt = new BigInteger(keys.get("public_key"));
        BigInteger gInt = new BigInteger(keys.get("generator"));
        BigInteger mTextInt = new BigInteger(m);

        BigInteger y = new BigInteger(moduloInt.bitCount() - 1, new Random());
        BigInteger j = gInt.modPow(y, moduloInt);
        publicKeyMap.put("public_key_j",j.toString());

        return mTextInt.multiply(iInt.modPow(y, moduloInt)).toString();
    }

    @Override
    public String decrypt(String c) {
        BigInteger jInt = new BigInteger(externalPublicKeyJ);
        BigInteger xInt = new BigInteger(privateKey);
        BigInteger moduloInt = new BigInteger(modulo);
        BigInteger cTextInt = new BigInteger(c);

        return (cTextInt.multiply(jInt.modPow(moduloInt.subtract(UNITY).subtract(xInt), moduloInt))).
                mod(moduloInt).toString();
    }

    private Map<String, String> calculateKeyPair() {
        Map<String, String> keyPair = new HashMap<>();

        // Generate modulo p and primitive root g
        // Generation of Prime q to calculate p. r is the generator for q
        BigInteger p = ZERO;
        BigInteger q = BigInteger.probablePrime(standardSize /DIVIDER, new Random());
        BigInteger h = ZERO;
        BigInteger g = ZERO;
        int counter = 0;
        long startTime = System.nanoTime();
        // Calculate p, if composite, repeat
        while(!p.isProbablePrime(100) || p.compareTo(ZERO) == 0){
            BigInteger r = new BigInteger(standardSize/DIVIDER, new Random());
            p = (q.multiply(r)).add(UNITY);

            counter++;
        }
        long endTime = System.nanoTime();
        long totalTime = (endTime - startTime) / (long) Math.pow(10, 9);
        // Generate g
        while(g.compareTo(ZERO) == 0 || g.compareTo(UNITY) == 0 || h.compareTo(p) == 1){
            h = new BigInteger(p.bitCount() - 1, new Random());
            g = h.modPow((p.subtract(UNITY)).divide(q), p);
        }
        // Pick x and calculate i
        BigInteger x = new BigInteger(p.bitCount() - 1, new Random());
        BigInteger i = g.modPow(x, p);

        // Put values in List to return
        keyPair.put("public_key",i.toString());
        keyPair.put("private_key", x.toString());
        keyPair.put("modulo", p.toString());
        keyPair.put("generator", g.toString());

        return keyPair;
    }

    @Override
    public String sign(String signature) {
        return null;
    }

    @Override
    public Map<String, String> getPublicKeyMap() {
        return publicKeyMap;
    }

    @Override
    public String getPublicKey() {
        return publicKey;
    }

    @Override
    public String getPrivateKey() {
        return privateKey;
    }

    @Override
    public String getModulo() {
        return modulo;
    }

    @Override
    public String getGenerator() {
        return generator;
    }

    @Override
    public void addExternalKeys(String keyJ) {
        externalPublicKeyJ = keyJ;
    }
}
