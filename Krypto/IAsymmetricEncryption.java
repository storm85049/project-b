package Krypto;

import java.util.Map;

public interface IAsymmetricEncryption {
    public String encrypt(String m, Map<String, String> keys);
    public String decrypt(String c, Map<String, String> keys);
    public Map<String, String> calculateKeyPair();
    public String sign(String signature, Map<String, String> keys);
}
