import Krypto.ElGamal;
import Krypto.IAsymmetricEncryption;
import Krypto.ISymmetricEncryption;
import Krypto.RSA;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        int i = 0;
        System.out.println("Hallo Welt");

       IAsymmetricEncryption try1 = new ElGamal();
        String tryString = "1231234";
        Map<String, String> keyPair = try1.calculateKeyPair();
        String tryStringEncrypt = try1.encrypt(tryString, keyPair);

       /* System.out.println(tryString);
        System.out.println(tryStringEncrypt);
        System.out.println(try1.decrypt(tryStringEncrypt, keyPair));*/
    }
}
