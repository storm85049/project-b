package Krypto;

import java.sql.SQLOutput;
import java.util.Map;

public class Main {
    public static void main(String[] args){

        ISymmetricEncryption desA = new DES();
        ISymmetricEncryption desB = new DES();
        // Set the Key in the second DES
       desB.setSelectedKeys(desA.getKeyMap());
        desA.setSelectedKeys(desA.getKeyMap());


        //todo:drchgehen

        // Encrypt + Decrypt
        String stringA = "Hello I´m Simon, and this is a VEEERY LONG string ,- with different SYMBOLS!!§$%&/";
        System.out.println("Encryption A ---> B: ");
        String encryptedString = desA.encrypt(stringA);
        System.out.println("Encrypted by A: " + encryptedString);
        System.out.println("Decrypted by B: " + desB.decrypt(encryptedString));
        String encrypted2 = desB.encrypt("teststring");
        System.out.println(desA.decrypt(encrypted2));
        System.out.println("Decrypted by A: " + desA.decrypt(encryptedString));

    }
}
