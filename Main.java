import Krypto.*;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        IAsymmetricEncryption elGamalA = new ElGamal();
        IAsymmetricEncryption elGamalB = new ElGamal();

        String encryptStringA = "Simon Hoyos Cadavid";
        System.out.println("OG Text: " +  encryptStringA);
        String encryptStringB = "020252368945";

        encryptStringA = StringAndIntTransformation.asciiToInt(encryptStringA);

        String encryptMessageA = elGamalA.encrypt(encryptStringA, elGamalB.getPublicKeyMap());
        String encryptMessageB = elGamalB.encrypt(encryptStringB, elGamalA.getPublicKeyMap());

        System.out.println("OG: " + encryptStringA);
        System.out.println("Encryption A ---> B");
        System.out.println(encryptMessageA);
        System.out.println("Decryption B");
        elGamalB.addExternalKeys(elGamalA.getPublicKeyMap().get("public_key_j"));
        String decryptedMessageB = elGamalB.decrypt(encryptMessageA);
        System.out.println(decryptedMessageB);
        System.out.println(StringAndIntTransformation.remakeStringFromAscii(decryptedMessageB));

        System.out.println("OG: " + encryptStringB);
        System.out.println("Encryption B ---> A");
        System.out.println(encryptMessageB);
        elGamalA.addExternalKeys(elGamalB.getPublicKeyMap().get("public_key_j"));
        System.out.println("Decryption A");
        System.out.println(elGamalA.decrypt(encryptMessageB) );

    }
}
