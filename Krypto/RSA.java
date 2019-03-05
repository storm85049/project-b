package Krypto;
import util.Actions;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
       ///////////////// IMPLEMENTATION ////////////////////////////
        IAsymmetricEncryption rsaA = new RSA();
        IAsymmetricEncryption rsaB = new RSA();

        String encryptStringA = "Hello World!";
        String encryptStringB = "What´s up? Hi, how are you? Simon Hoyos Cadavid ||||||";

        String encryptMessageA = rsaA.encrypt(encryptStringA, rsaB.getPublicKeyMap());
        String encryptMessageB = rsaB.encrypt(encryptStringB, rsaA.getPublicKeyMap());

        System.out.println("Encryption A ---> B");
        System.out.println(encryptMessageA);
        System.out.println("Decryption B");
        System.out.println(rsaB.decrypt(encryptMessageA) );

        System.out.println("Encryption B ---> A");
        System.out.println(encryptMessageB);
        System.out.println("Decryption A");
        System.out.println(rsaA.decrypt(encryptMessageB) );
        ////////////////////////////////////////////////////////////
 */

public class RSA implements IAsymmetricEncryption {
    private static final BigInteger UNITY = new BigInteger("1");
    //private static final BigInteger TWO = new BigInteger("2");
    private int standardSize = 2048;
    private Map<String, String> publicKeyMap;
    private String privateKey, publicKey, modulo;

    // Constructor
    public RSA (){
        publicKeyMap = calculateKeyPair();
        privateKey = publicKeyMap.get("private_key");
        publicKey = publicKeyMap.get("public_key");
        modulo = publicKeyMap.get("modulo");
        publicKeyMap.remove("private_key"); // Key Pair has all the Data that´s Public

    }

    // Encryption: Uses external Public key
    @Override
    public String encrypt(String m, Map<String, String> keys){
    //    Map<String, String> returnValues = new HashMap<>();
        m = StringAndIntTransformation.asciiToInt(m);
        BigInteger mInt = new BigInteger(m);
        BigInteger keyInt = new BigInteger(keys.get("public_key"));
        BigInteger modInt = new BigInteger(keys.get("modulo"));
     //   returnValues.put("encrypted_text",mInt.modPow(keyInt, modInt).toString());
        return StringAndIntTransformation.intToAscii(mInt.modPow(keyInt, modInt).toString());
    }

    // Decryption: Uses private key
    @Override
    public String decrypt(String c) {
        c = StringAndIntTransformation.asciiToInt(c);
        BigInteger cInt = new BigInteger(c);
        BigInteger keyInt = new BigInteger(privateKey);
        BigInteger modInt = new BigInteger(modulo);
        return StringAndIntTransformation.intToAscii(cInt.modPow(keyInt, modInt).toString());
    }

    // Calculates both Private and Public Key
    private Map<String, String> calculateKeyPair() {
        Map<String, String> keyPair = new HashMap<String, String>();
        // Generate p and q
        BigInteger q = BigInteger.probablePrime(standardSize/2 - 1, new Random()); // Off by one
        BigInteger p = BigInteger.probablePrime(standardSize/2 - 1, new Random());
        // Check if the numbers are equal: Generate new p until they are not.
        while(p.compareTo(q) == 0){
            p = BigInteger.probablePrime(standardSize/2 - 1, new Random());
        }
        // Generate n: Multiply q and p. And phiN: (p-1)(q-1)
        BigInteger n = p.multiply(q);
        BigInteger phiN = (p.subtract(UNITY)).multiply(q.subtract(UNITY));
        // Generate e and d (Public and private keys)
        BigInteger e = new BigInteger(standardSize - 1, new Random());
        while (e.compareTo(phiN) == 1 || e.compareTo(phiN) == 0|| e.gcd(phiN).compareTo(UNITY) != 0
        ){
            // Checks that e is bigger than 2^2047, being smaller than 2^2048
            // and being smaller than phiN
            // Also checks if e and phiN are coprime
            e = new BigInteger(standardSize - 1, new Random());
        }
        BigInteger d = e.modInverse(phiN);
        keyPair.put("public_key", e.toString());
        keyPair.put("private_key", d.toString());
        keyPair.put("modulo", n.toString());
        return keyPair;
    }

    // Digital signature of the computer. Calculated with the private key
    @Override
    public String sign(String signature) {
            BigInteger signatureInt = new BigInteger(signature);
            BigInteger keyInt = new BigInteger(privateKey);
            BigInteger modInt = new BigInteger(modulo);
            return signatureInt.modPow(keyInt, modInt).toString();
        }


     // Getter Methods

    public Map<String, String> getPublicKeyMap() {
        // Returns the PUBLIC MAP
        return publicKeyMap;
    }


    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getModulo() {
        return modulo;
    }

    @Override
    public String getGenerator() {
        return null;
    }

    @Override
    public void addExternalKeys(String keyJ) {}

    @Override
    public String toString() {
        return Actions.MODE_RSA;
    }
}
