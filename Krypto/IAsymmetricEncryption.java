package Krypto;
import java.util.Map;

public interface IAsymmetricEncryption {

    public String encrypt(String m, Map<String, String> keys);
    public String decrypt(String c);
    //    public Map<String, String> calculateKeyPair();
    public String sign(String signature);
    // Getters
    public Map<String, String> getPublicKeyMap();
    public String getPublicKey();
    public String getPrivateKey();
    public String getModulo();
    public String getGenerator();
    public void addExternalKeys(String keyJ);
}

