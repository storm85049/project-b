package Krypto;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
/////////////////////// IMPLEMENTATION /////////////////////////////
        ISymmetricEncryption affA = new AffineCipher("10", "7","ABC"); // MODE: ABC OR ASCII
        ISymmetricEncryption affB = new AffineCipher();

        // A selects keys and mode, and Sends data to B. B sets it´s data.
        Map<String, String> symmKey = affA.getKeyMap(); // -------> B
        String mode = affA.getMode();                   // -------> B

        // B gets the mode and the keys
        affB.setSelectedKeys(symmKey);
        affB.setAlphabetMode(mode);
        // B sends encrypted Message
        String messageB = "hello A, HOW ARE YOU.... THIS IS not sparta, but lower case tho! --$$ NOT";
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
        setAlphabetMode(mode);
        this.k = new BigInteger(k);
        this.t = new BigInteger(t);
        if(mode.equalsIgnoreCase("ABC")){
            this.k = this.k.mod(p);
            this.t = this.t.mod(p);
        }
        keyMap.put("key_k", k);
        keyMap.put("key_t", t);
        this.mode = mode;
    }
    public AffineCipher(){} // Constructor for Empty Initialization

    public AffineCipher(String mode){ // Constructor for generation of a Random key
        this.mode = mode;
        setAlphabetMode(mode);
        generateRandomKey();
        System.out.println("");
    }

    // Affine encryption: c = m*t + k mod p
    @Override
    public String encrypt(String m) {
        String result = "";
            for (char character : m.toCharArray()) {
                int modifier = 0;
                if(mode.equalsIgnoreCase("ABC")) {
                    if ((int) character >= 97 && (int) character <= 122) {
                        modifier = 97;
                    } else {
                        modifier = 65;
                    }
                }
                if(modifier != 0 &&
                        ((int)character < 65 ||
                        (int)character > 90 && (int)character < 97 ||
                        (int) character > 122)){
                    result += character;
                }
                else{
                    int charInt = (int) character - modifier;
                    String charIntString = "" + charInt;
                    BigInteger charBigInt = new BigInteger(charIntString);
                    charBigInt = (charBigInt.multiply(t).add(k)).mod(p);
                    charInt = charBigInt.intValue();
                    char encryptedChar = (char) (charInt + modifier);
                    charIntString = "" + encryptedChar;
                    result += charIntString;
                }
        }
        return result;
    }
    // Decryption: m = (c-k) * (1/t) mod p
    @Override
    public String decrypt(String c) {
        String result = "";
            for (char character : c.toCharArray()) {
                int modifier = 0;
                if(mode.equalsIgnoreCase("ABC")) {
                    if ((int) character >= 97 && (int) character <= 122) {
                        modifier = 97;
                    } else {
                        modifier = 65;
                    }
                }
                if(modifier != 0 &&
                        ((int)character < 65 ||
                                (int)character > 90 && (int)character < 97 ||
                                (int) character > 122)){
                    result += character;
                }
                else{
                        int charInt = (int) (character) - modifier;
                        String charIntString = "" + charInt;
                        BigInteger charBigInt = new BigInteger(charIntString);
                        charBigInt = (charBigInt.subtract(k).multiply(t.modInverse(p)).mod(p));
                        charInt = charBigInt.intValue();
                        char encryptedChar = (char) (charInt + modifier);
                        charIntString = "" + encryptedChar;
                        result += charIntString;
            }
        }
        return result;
    }

    // Changes the keys
    @Override
    public void setSelectedKeys(Map<String, String> keyMap) {
        k = new BigInteger(keyMap.get("key_k"));
        t = new BigInteger(keyMap.get("key_t"));
        this.keyMap.put("key_k", k.toString());
        this.keyMap.put("key_t", t.toString());
    }

    // Changes selected Alphabet
    @Override
    public void setAlphabetMode(String mode){
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
