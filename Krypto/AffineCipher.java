package Krypto;

import javafx.scene.transform.Affine;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
/////////////////////// IMPLEMENTATION /////////////////////////////
        ISymmetricEncryption affA = new AffineCipher("ASCII");
        ISymmetricEncryption affB = new AffineCipher();

         // A selects keys and mode, and Sends data to B. B sets it´s data.
        Map<String, String> symmKey = affA.getKeyMap(); // -------> B
        String mode = affA.getMode();                   // -------> B

        // B gets the mode and the keys
        affB.changeSelectedKeys(symmKey);
        affB.selectAlphabetMode(mode);

        // B sends encrypted Message
        String messageB = "Hello A, how are you?";
        System.out.println("OG Message: " + messageB);
        //messageB = StringAndIntTransformation.asciiToInt(messageB);
        // System.out.println("OG Message Number: " + messageB);
        messageB = affB.encrypt(messageB); // -----> A
        System.out.println("Encrypted message: " + messageB);

        // A Decrypts message
        System.out.println("Decrypted Message: " + affA.decrypt(messageB));*/
/////////////////////////////////////////////////////////////////////////////////

public class AffineCipher implements ISymmetricEncryption {
    // k: Additive key
    // t: Multiplicative key
    // p: Modulo (Is dependant of the alphabet)
    private BigInteger k, t, p;
    private String mode;
    private Map<String, String> keyMap = new HashMap<>();

    public AffineCipher(String k, String t, String mode){ // Mode should be: ASCII or ABC
        this.k = new BigInteger(k);
        this.t = new BigInteger(t);
        keyMap.put("key_k", k);
        keyMap.put("key_t", t);
        this.mode = mode;
        selectAlphabetMode(mode);
    }
    public AffineCipher(){} // Constructor for Empty Initialization

    public AffineCipher(String mode){ // Constructor for generation of a Random key
        this.mode = mode;
        selectAlphabetMode(mode);
        generateRandomKey();
        System.out.println("");
    }

    // Affine encryption: c = m*t + k mod p
    @Override
    public String encrypt(String m) {
        String result = "";
        if(mode.equalsIgnoreCase("ASCII")) {
            for (char character : m.toCharArray()) {
                int charInt = (int) character;
                String charIntString = "" + charInt;
                BigInteger charBigInt = new BigInteger(charIntString);
                charBigInt = (charBigInt.multiply(t).add(k)).mod(p);
                charInt = charBigInt.intValue();
                char encryptedChar = (char) charInt;

                charIntString = "" + encryptedChar;
                result += charIntString;
            }
        }
        // Else if ABC
        return result;
    }
    // Decryption: m = (c-k) * (1/t) mod p
    @Override
    public String decrypt(String c) {
        String result = "";
        if(mode.equalsIgnoreCase("ASCII")) {
            for (char character : c.toCharArray()) {
                int charInt = (int) character;
                String charIntString = "" + charInt;
                BigInteger charBigInt = new BigInteger(charIntString);
                charBigInt = (charBigInt.subtract(k).multiply(t.modInverse(p)).mod(p));
                charInt = charBigInt.intValue();
                char encryptedChar = (char) charInt;

                charIntString = "" + encryptedChar;
                result += charIntString;
            }
        }
        return result;
    }

    // Changes the keys
    @Override
    public void changeSelectedKeys(Map<String, String> keyMap) {
        k = new BigInteger(keyMap.get("key_k"));
        t = new BigInteger(keyMap.get("key_t"));
    }

    // Changes selected Alphabet
    @Override
    public void selectAlphabetMode(String mode){
        if(mode.equalsIgnoreCase("ASCII")){ p = new BigInteger("256"); this.mode = mode;}
        else if(mode.equalsIgnoreCase(("ABC"))){ p = new BigInteger("26"); this.mode = mode;}
        else{System.out.println("Not an integrated Alphabet");}
    }

    @Override
    public Map<String, String> getKeyMap() {
        return keyMap;
    }

    @Override
    public String getMode() {
        return mode;
    }

    @Override
    public void generateRandomKey() {
        BigInteger k = new BigInteger(8, new Random());
        BigInteger t = new BigInteger(8, new Random());
        while(t == BigInteger.ZERO || t.gcd(p).compareTo(BigInteger.ONE) != 0){
            t = new BigInteger(8, new Random());
        }
        this.k = k;
        this.t = t;
        keyMap.put("key_k", k.toString());
        keyMap.put("key_t", t.toString());
    }
}
