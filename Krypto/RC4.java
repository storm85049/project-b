package Krypto;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
//////////////////////// IMPLEMENTATION ///////////////////////////////
/*ISymmetricEncryption rc4A = new RC4("ßTestHalloWorld Hello Welt Simon Hoyos Cadavid");
        ISymmetricEncryption rc4B = new RC4();

        // A --------> Sends Key to B
        rc4B.setSelectedKeys(rc4A.getKeyMap());
        // A --------> Sends Encrypted message to B
        String encryptedMA = rc4A.encrypt("Hello world, It´s a me, MARIO!");
        System.out.println("Encrypted message A ----> B " + encryptedMA);
        // B --------> Decrypts Message
        System.out.println("Decrypted message B: " + rc4B.decrypt(encryptedMA));

        // Answer from B with new Random key
        rc4B.generateRandomKey();

        // B --------> Sends Key to B
        rc4A.setSelectedKeys(rc4B.getKeyMap());
        // A --------> Sends Encrypted message to B
        String encryptedMB = rc4B.encrypt("I´m answering with a RANDOM KEY // 154786==!`())()()()??");
        System.out.println("Encrypted message B ----> A " + encryptedMB);
        // A --------> Decrypts Message
        System.out.println("Decrypted message B: " + rc4A.decrypt(encryptedMB));
        */
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// CORRECT RANDOM KEY GENERATION!!!
public class RC4 implements ISymmetricEncryption{
    private static final short BYTE_AMOUNT = 8;
    private static final short SIZE = 256;

    private short[] s = new short[SIZE];
    private short[] k = new short[SIZE];
    private Map<String, String> keyMap = new HashMap<>();

    //Constructor
    public RC4(String key){
        keyMap.put("key", key);
        keyMap.put("is_key_random", "false");
        setSelectedKeys(keyMap);
    }

    // Empty Constructor for later addition of keys/ Random generation of key
    public RC4(){}

    @Override
    public String encrypt(String m) {
        char[] encryptedCharArray = new char[m.length()];
        for(int i = 0; i < m.length(); i ++){
            short encryptionTempKey = pseudoRandomNumberFromSeed(s);
            int intCharacter = m.toCharArray()[i];
            intCharacter = (intCharacter ^ encryptionTempKey);
            encryptedCharArray[i] = (char) (intCharacter);
        }
        return String.valueOf(encryptedCharArray);
    }

    @Override
    public String decrypt(String c) {
        return encrypt(c);
    }

    @Override
    public void setSelectedKeys(Map<String, String> keyMap) {
        for(short i = 0; i < s.length; i++){s[i] = i;} // Generate sequential s
        if(keyMap.get("is_key_random").equalsIgnoreCase("false")){
            k = calculateKArray(keyMap.get("key"));
        }
        else{
        keyMap.put("is_key_random", "false");}
        this.keyMap = keyMap;
        // Scramble
        scrambleSeed(s);
    }

    @Override
    public void setAlphabetMode(String mode) {}

    @Override
    public Map<String, String> getKeyMap() {
        return keyMap;
    }

    @Override
    public String getMode() {
        return "ASCII";
    }

    @Override
    public void generateRandomKey() {
        keyMap.put("is_key_random", "true");
        Random rnd = new Random();
        for(short element : k){
            element = (short) rnd.nextInt(2);
        }
        for(short i = 0; i < s.length; i++){s[i] = i;} // Generate sequencial s
        // Scramble
        scrambleSeed(s);
    }

    private short[] calculateKArray(String key){
        // Transforms a String into a short binary array.
        short[] k = new short[SIZE];
        char[] tempByteArray = key.toCharArray();
        short counter = 0;
        outerLoop:
        for(short i = 0; i < k.length; i+=BYTE_AMOUNT){
            if (counter == tempByteArray.length) {
                counter = 0;
            }
            middleLoop:
            for(short x = 0; x < tempByteArray.length; x++){
                short temp = (short)tempByteArray[x];
                innerLoop:
                for(short j = 0; j < BYTE_AMOUNT; j++){
                    if(j+i+(x*BYTE_AMOUNT) >= SIZE){
                        break outerLoop;
                    }
                    k[i+j+(x*BYTE_AMOUNT)] = (short)(((temp & (1<<(BYTE_AMOUNT - 1 -j))) != 0) ? 1 : 0);
                }
            }
            counter++;
        }
        return k;
    }

    private short[] scrambleSeed(short[] s){
        // Scrambles Seed Array (s) with the Key array (k) 256 times
        int j = 0;
        for(int i = 0; i < SIZE; i++) {
            j = (j + s[i] + k[i]) % SIZE;
            // Swap
            short temp = s[i];
            s[i] = s[j];
            s[j] = temp;
        }
        return s;
    }

    private short pseudoRandomNumberFromSeed(short[] s){
        // Generates a PRNM for the Encryption with RC4
        int i = 0, j = 0;
        i = (i + 1) % SIZE;
        j = (j + s[i]) % SIZE;
        // Swap
        short temp = s[i];
        s[i] = s[j];
        s[j] = temp;
        int t = (s[i] + s[j]) % SIZE;
        // Returns value for encryption
        return s[t];
    }
}
