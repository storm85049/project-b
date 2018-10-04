package Krypto;

public interface ISymmetricEncryption {
    public String encrypt(String m, String key);
    public String decrypt(String c, String key);
}
