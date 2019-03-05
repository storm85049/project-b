package Krypto;

import java.util.Map;

public interface ISymmetricEncryption {
    public String encrypt(String m);
    public String decrypt(String c);
    public void setSelectedKeys(Map<String, String> keyMap); // TODO: Can I make it an optional parameter with different type?
    public void setAlphabetMode(String mode);
    public Map<String, String> getKeyMap();
    public String getMode();
    public void generateRandomKey();
}
