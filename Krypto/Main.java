package Krypto;

import java.sql.SQLOutput;
import java.util.Map;

public class Main {
    public static void main(String[] args){

        String [][] key = {
                {"5","24", "1"},
                {"13","3", "10"},
                {"20","17", "24"},
        };
        try {
            ISymmetricEncryption hillA = new HillCipher(key, "ASCII");
            System.out.println(hillA.encrypt("Hello this is a test"));
            System.out.println(hillA.decrypt(hillA.encrypt("Hello this is a test")));
        }
        catch(Exception e){
            System.out.println(e);
        }

    }
}
