package Krypto;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
        IAsymmetricEncryption try1 = new RSA();
        String tryString = "1231234";
        Map<String, String> keyPair = try1.calculateKeyPair();
        String tryStringEncrypt = try1.encrypt(tryString, keyPair);

        System.out.println(tryString);
        System.out.println(tryStringEncrypt);
        System.out.println(try1.decrypt(tryStringEncrypt, keyPair));

 */
public class RSA implements IAsymmetricEncryption {
    private static final BigInteger UNITY = new BigInteger("1");
    //private static final BigInteger TWO = new BigInteger("2");
    private int standardSize = 2048;
    // Encryption: Uses external Public key
    @Override
    public String encrypt(String m, Map<String, String> keys){
        BigInteger mInt = new BigInteger(m);
        BigInteger keyInt = new BigInteger(keys.get("public_key"));
        BigInteger modInt = new BigInteger(keys.get("modulo"));
        return mInt.modPow(keyInt, modInt).toString();
    }

    // Decryption: Uses private key
    @Override
    public String decrypt(String c, Map<String, String> keys) {
        BigInteger cInt = new BigInteger(c);
        BigInteger keyInt = new BigInteger(keys.get("private_key"));
        BigInteger modInt = new BigInteger(keys.get("modulo"));
        return cInt.modPow(keyInt, modInt).toString();
    }

    // Calculates both Private and Public Key
    @Override
    public Map<String, String> calculateKeyPair() {
        Map<String, String> keyPair = new HashMap<String, String>();
        // Generate p and q
        BigInteger q = BigInteger.probablePrime(standardSize/2 - 1, new Random()); // Off by one
        BigInteger p = BigInteger.probablePrime(standardSize/2 - 1, new Random());
        // Check if the numbers are equal: Generate new p until they are not.
        while(p.compareTo(q) == 0){
            p = BigInteger.probablePrime(standardSize/2 - 1, new Random());
        }
        // Generate n: Multiply q and p. And phiN: (p-1)(q-1)
        BigInteger n = p.multiply(q);
        BigInteger phiN = (p.subtract(UNITY)).multiply(q.subtract(UNITY));
        // Generate e and d (Public and private keys)
        BigInteger e = new BigInteger(standardSize - 1, new Random());
        while (e.compareTo(phiN) == 1 || e.compareTo(phiN) == 0|| e.gcd(phiN).compareTo(UNITY) != 0
        ){
            // Checks that e is bigger than 2^2047, being smaller than 2^2048
            // and being smaller than phiN
            // Also checks if e and phiN are coprime
            e = new BigInteger(standardSize - 1, new Random());
        }
        BigInteger d = e.modInverse(phiN);
        keyPair.put("public_key", e.toString());
        keyPair.put("private_key", d.toString());
        keyPair.put("modulo", n.toString());
        return keyPair;
    }

    // Digital signature of the computer. Calculated with the private key
    @Override
    public String sign(String signature, Map<String, String> keys) {
            BigInteger signatureInt = new BigInteger(signature);
            BigInteger keyInt = new BigInteger(keys.get("private_key"));
            BigInteger modInt = new BigInteger(keys.get("modulo"));
            return signatureInt.modPow(keyInt, modInt).toString();
        }
    }
