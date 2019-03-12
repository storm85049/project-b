package Krypto;

import java.sql.SQLOutput;
import java.util.Map;

public class Main {
    public static void main(String[] args){
        ISymmetricEncryption rc4B = new RC4(), rc4A = new RC4("10100100");

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

    }
}
