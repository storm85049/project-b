package Krypto;

import java.util.Map;

public interface ISymmetricEncryption {
    public String encrypt(String m);
    public String decrypt(String c);
    public void setSelectedKeys(Map<String, String> keyMap);
    public void setAlphabetMode(String mode);
    public Map<String, String> getKeyMap();
    public String getMode();
    public void generateRandomKey();

    /**
     * this method makes it easier for the frontend to retreive the currently
     * set keys in a READABLE form without having to check which mode it actually is.
     */
    public String getModeSpecificKey();

}
