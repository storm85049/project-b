package Krypto;

import java.math.BigInteger;
import java.util.*;

// WARNING! Check if it works with all the code, encrypted
// strings are not being completely shown in the console!
///////// IMPLEMENTATION ///////////////
/*  // Initialize DES Objects
        ISymmetricEncryption desA = new DES();
        ISymmetricEncryption desB = new DES();
        // Set the Key in the second DES
        desB.setSelectedKeys(desA.getKeyMap());

        // Encrypt + Decrypt
        String stringA = "Hello I´m Simon, and this is a VEEERY LONG string ,- with different SYMBOLS!!§$%&/";
        System.out.println("Encryption A ---> B: ");
        String encryptedString = desA.encrypt(stringA);
        System.out.println("Encrypted by A: " + encryptedString);
        System.out.println("Decrypted by B: " + desA.decrypt(encryptedString));*/

////////////////////////////////////////

public class DES implements ISymmetricEncryption{
    private final int DES_STANDARD_KEY_LENGTH = 64;
    private BigInteger key = new BigInteger("0");
    private Map<String, String> keyMap = new HashMap<>();
    private List<String> subKeys;
    private List<String> invertedSubKeys = new ArrayList<>();

    // Constructor
    public DES(){
        generateRandomKey();
        subKeys = calculateSubKeys();
        invertedSubKeys = invertSubKeys();
    }

    public DES(Map<String, String> keyMap){
        setSelectedKeys(keyMap);
    }


    @Override
    public String encrypt(String m) {
        // Separate the String in groups of 64 Bits
        String c = "";
        for(String block : separateIn64BitBlocks(m)){
            c += encrypt64BitBlock(block, subKeys);
        }
        return c;
    }

    // Encryption for each of the 64 bit blocks
    private String encrypt64BitBlock(String m, List<String> subKeys){
        m = StringAndIntTransformation.asciiToBinary(m);
        while(m.length() < 64){
            m = "0" + m;
        }
        // Perform initial permutation
        char[] mCharArray = m.toCharArray();
        String IP = "";
        for(int i : DESTables.INITIAL_PERMUTATION){
            IP += mCharArray[i-1];
        }

        String l = IP.substring(0, IP.length()/2);
        String r = IP.substring(IP.length()/2);
        for(int i = 0; i < 16; i++){
            String lHolder = l;
            l = r;
            r = new BigInteger(lHolder, 2).xor(new BigInteger(fFunction(r, i, subKeys), 2)).
                    toString(2);
            while(r.length() < 32){
                r = "0" + r;
            }
        }

        char[] combinedCharArray = (r + l).toCharArray();
        String IPInverse = "";
        for(int i : DESTables.INVERSE_INITIAL_PERMUTATION){
            IPInverse += combinedCharArray[i-1];
        }
        String toAsciiInteger = StringAndIntTransformation.binaryToAscii(IPInverse);
        return toAsciiInteger;
    }

    // F - function For the encryption
    private String fFunction(String r, int index, List<String> subKeys){
        // Expand 32 to 48 Bits
        while(r.length() < 32){
            r = "0" + r;
        }
        char[] rCharArray = r.toCharArray();
        String eBitSelection;
        StringBuilder builder = new StringBuilder();
        for(int i : DESTables.EXPANSION_FUNCTION){
            builder.append(rCharArray[i-1]);
        }
        eBitSelection = builder.toString();
        // XOR With corresponding subkey
        eBitSelection = new BigInteger(eBitSelection, 2).
                xor(new BigInteger(subKeys.get(index), 2)).toString(2);
        while(eBitSelection.length() < 48){
            eBitSelection = "0" + eBitSelection;
        }

        // Split the String in groups of 6 Bits
        List<String> blockList = new ArrayList<>();
        for(int i = 0; i < 48; i += 1){
            if(i % 6 == 0) {
                blockList.add(eBitSelection.substring(i, i + 6));
            }
        }
        // SBox Search
        String sBoxResult = "";
        int sBoxNumber = 0;
        for(String block : blockList){
            int i = Integer.parseInt(block.substring(0,1) + block.substring(block.length()), 2);
            int j = Integer.parseInt(block.substring(1 , block.length() - 1), 2);
            String tempInt = Integer.toBinaryString(DESTables.S_BOX_LIST.get(sBoxNumber)[i][j]);
            while(tempInt.length() < 4){
                tempInt = "0" + tempInt;
            }
            sBoxNumber++;
            sBoxResult += tempInt;
        }
        // Permutate the result
        String result = "";
        for(int i : DESTables.PERMUTATION_FUNCTION){
            result += sBoxResult.toCharArray()[i-1];
        }
        return result;
    }

    @Override
    public String decrypt(String c) {
        // Separate the String in groups of 64 Bits
        String m = "";
        for(String block : separateIn64BitBlocks(c)){
            m += encrypt64BitBlock(block, invertedSubKeys);
        }
        return m;
    }

    private List<String> separateIn64BitBlocks(String text){
        List<String> separatedString = new ArrayList<>();
        while(text.length() % 8 != 0 || text.length() < 8){
            text += " ";
        }
        for(int i = 0; i < text.length(); i+=8){
            separatedString.add(text.substring(i, i+8));
        }
        return separatedString;
    }


    // Generates a 64 Bit Random key
    @Override
    public void generateRandomKey() {
        BigInteger key = new BigInteger("0");
        Random rand = new Random();
        for(int i = 0; i <= DES_STANDARD_KEY_LENGTH; i++){
            String randomString = String.valueOf(rand.nextInt(2));
            key = key.add(new BigInteger(randomString));
            key = key.shiftLeft(2);
        }
        this.key = key;
        String tempKeyWithZeroes = key.toString(2);
        while(tempKeyWithZeroes.length() < 64){
            tempKeyWithZeroes = "0" + tempKeyWithZeroes;
        }
        keyMap.put("key", tempKeyWithZeroes);
      //  System.out.println(Long.toBinaryString(key));
    }

    @Override
    public String getModeSpecificKey() {
        return this.key.toString();
    }

    // Calculates the 16 Subkeys for DES
    private List<String> calculateSubKeys(){
        String permutatedKey = "";
        char [] keyCharArray = keyMap.get("key").toCharArray();
        for(int i : DESTables.PERMUTATED_CHOICE_1){
            permutatedKey += keyCharArray[i];
        }

        List<String> cList = new ArrayList<>();
        List<String> dList = new ArrayList<>();
        List<String> cdList = new ArrayList<>();
        String c0 = permutatedKey.substring(0, 28); // Left
        String d0 = permutatedKey.substring(28);    // Right
        cList.add(c0);
        dList.add(d0);
        //cdList.add(c0 + d0);

        for(int n = 0; n < 16; n++){
            String cn = cList.get(n).substring(DESTables.NUMBER_OF_SHIFTS[n])
                    + cList.get(n).substring(0, DESTables.NUMBER_OF_SHIFTS[n]);
            cList.add(cn);

            String dn = dList.get(n).substring(DESTables.NUMBER_OF_SHIFTS[n])
                    + dList.get(n).substring(0, DESTables.NUMBER_OF_SHIFTS[n]);
            dList.add(dn);

            String permutatedSubKey = "";
            char [] subKeyCharArray = (cn + dn).toCharArray();
            for(int i : DESTables.PERMUTATED_CHOICE_2){
                permutatedSubKey += subKeyCharArray[i-1];
            }

            cdList.add(permutatedSubKey);
        }

        return cdList;
    }
    private List<String> invertSubKeys(){
        List<String> invertedList = new ArrayList<>();
        for(int i = 15 ; i >= 0; i--){
            invertedList.add(subKeys.get(i));
        }
        return invertedList;
    }

    // Getters and Setters

    @Override
    public void setSelectedKeys(Map<String, String> keyMap) {
        this.keyMap = keyMap;
        this.key = new BigInteger(keyMap.get("key"), 2);
        subKeys = calculateSubKeys();
        invertedSubKeys = invertSubKeys();
    }

    @Override
    public void setAlphabetMode(String mode) {

    }

    @Override
    public Map<String, String> getKeyMap() {
        return keyMap;
    }

    @Override
    public String getMode() {
        return null;
    }

    public BigInteger getKey() {
        return key;
    }

}
