package Krypto;

import java.util.Map;

public interface ISymmetricEncryption {
    String encrypt(String m);
    String decrypt(String c);
    void changeSelectedKeys(Map<String, String> keyMap);
    void selectAlphabetMode(String mode);
    Map<String, String> getKeyMap();
    String getMode();
    void generateRandomKey();
}
