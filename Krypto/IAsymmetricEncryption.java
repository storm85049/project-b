package Krypto;
import java.util.Map;

public interface IAsymmetricEncryption {

    String encrypt(String m, Map<String, String> keys);
    String decrypt(String c);
//    Map<String, String> calculateKeyPair();
    String sign(String signature);
    // Getters
    Map<String, String> getPublicKeyMap();
    String getPublicKey();
    String getPrivateKey();
    String getModulo();
    String getGenerator();
    void addExternalKeys(String keyJ);
}
