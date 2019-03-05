package util;

public class ModeMapper {



    public static String map(String mode)
    {

        switch (mode){
            case(Actions.MODE_DES):
                return "Data Encryption Standard (DES)";
            case(Actions.MODE_RC4):
                return "Arcfour RC4";
            case(Actions.MODE_AFFINE):
                return "Affine Chiffre";
            case(Actions.MODE_VIGENERE):
                return "Vigenere Chiffre";
            case(Actions.MODE_HILL):
                return "Hill Chiffre";
            case(Actions.MODE_RSA):
                return "RSA (Rivest, Shamir, Adleman)";
            case(Actions.MODE_ELGAMAL):
                return "Elgamal (Taher Elgamal) ";
            default:
                return "";
        }

    }



}
