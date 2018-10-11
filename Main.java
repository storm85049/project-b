import Krypto.*;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

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
        System.out.println("Decrypted Message: " + affA.decrypt(messageB));





    }
}
