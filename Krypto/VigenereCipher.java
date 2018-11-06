package Krypto;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
/*
/////////////////////////////////// IMPLEMENTATION /////////////    ///////////////////////////////////
        ISymmetricEncryption vA = new VigenereCipher("thekey","ABC"); // MODE: ABC OR ASCII
        ISymmetricEncryption vB = new VigenereCipher();

        // A selects keys and mode, and Sends data to B. B sets it´s data.
        Map<String, String> symmKey = vA.getKeyMap(); // -------> B
        String mode = vA.getMode();                   // -------> B

        // B gets the mode and the keys
        vB.setSelectedKeys(symmKey);
        vB.setAlphabetMode(mode);
        // B sends encrypted Message
        String messageB = "hello A, HOW ARE YOU.... THIS IS not sparta, but lower case tho! --$$ NOT";
        System.out.println("OG Message: " + messageB);
        // System.out.println("OG Message Number: " + messageB);
        messageB = vB.encrypt(messageB); // -----> A
        System.out.println("Encrypted message: " + messageB);
        // A Decrypts message
        System.out.println("Decrypted Message: " + vA.decrypt(messageB));
        */
/////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class VigenereCipher implements ISymmetricEncryption {
    private BigInteger p;
    private String key;
    private String mode;
    private Map<String, String> keyMap = new HashMap<>();
    public VigenereCipher(String key, String mode){ // Mode should be: ASCII or ABC
        setAlphabetMode(mode);
        this.key = key;
        keyMap.put("key", key);
        this.mode = mode;
    }
    public VigenereCipher(){} // Constructor for Empty Initialization

    public VigenereCipher(String mode){ // Constructor for generation of a Random key
        this.mode = mode;
        setAlphabetMode(mode);
        generateRandomKey();
        System.out.println("");
    }


    @Override
    public String encrypt(String m) {
        String result = "";
        int blockLength = key.length();
        int actualPosition = 0;
        for (char character : m.toCharArray()) {
            int modifier = 0;
            int keyModifier = 0;
            if(mode.equalsIgnoreCase("ABC")) {
                    modifier = checkModifiers(character);
                    keyModifier = checkModifiers(key.toCharArray()[actualPosition]);
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
                String tempString = String.valueOf((int)key.toCharArray()[actualPosition]- keyModifier);
                charBigInt = (charBigInt.add(new BigInteger(tempString)).mod(p));
                charInt = charBigInt.intValue();
                char encryptedChar = (char) (charInt + modifier);
                charIntString = "" + encryptedChar;
                result += charIntString;
                actualPosition++;
                if (actualPosition >= blockLength){
                    actualPosition = 0;
                }
            }
        }
        return result;
    }

    @Override
    public String decrypt(String c) {
        String result = "";
        int blockLength = key.length();
        int actualPosition = 0;
        for (char character : c.toCharArray()) {
            int modifier = 0;
            int keyModifier = 0;
            if(mode.equalsIgnoreCase("ABC")) {
                modifier = checkModifiers(character);
                keyModifier = checkModifiers(key.toCharArray()[actualPosition]);
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
                String tempString = String.valueOf((int)key.toCharArray()[actualPosition]-keyModifier);
                charBigInt = (charBigInt.subtract(new BigInteger(tempString)).mod(p));
                charInt = charBigInt.intValue();
                char encryptedChar = (char) (charInt + modifier);
                charIntString = "" + encryptedChar;
                result += charIntString;
                actualPosition++;
                if (actualPosition >= blockLength){
                    actualPosition = 0;
                }
            }
        }
        return result;
    }

    @Override
    public void setSelectedKeys(Map<String, String> keyMap) {
        key = keyMap.get("key");
        this.keyMap.put("key", key);
    }

    @Override
    public void setAlphabetMode(String mode) {
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
        // Random word from a chart? Random ASCII values with a random length?
    }

    private int checkModifiers(char character){
            if ((int) character >= 97 && (int) character <= 122) {
                return 97;
            } else {
                return 65;
            }
    }
}
