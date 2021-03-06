package Krypto;

/////////////// FIRST IMPLEMENTATION/ PROTOTYPE/////////////
// Converts a String of ASCII characters into a String of int and viceversa (every 3 Digits representing an
// ASCII Character)
// For implementation: Look at ---> CLASS ElGamal
public class StringAndIntTransformation {
    public static String intToAscii(String text){
        while(text.length() % 3 != 0){
            text = "0" + text;
        }
        char[] charArrayEncryptStringA = text.toCharArray();
        text = "";
        for(int i = 0; i <= charArrayEncryptStringA.length-3; i+=3){
            int temp = Integer.parseInt(String.valueOf(charArrayEncryptStringA[i])) * 100 +
                    Integer.parseInt(String.valueOf(charArrayEncryptStringA[i+1])) * 10 +
                    Integer.parseInt(String.valueOf(charArrayEncryptStringA[i+2]));
            text += (char) temp;
        }

        return text;
    }
    public static String binaryToAscii(String text){
        while(text.length() % 8 != 0){
            text = "0" + text;
        }
        char[] charArrayEncryptStringA = text.toCharArray();
        text = "";
        for(int i = 0; i <= charArrayEncryptStringA.length-8; i+=8){
            int temp = 0;
            int multiplier = 128;
            for(int j = 0; j < 8 ; j++) {
                temp += Integer.parseInt(String.valueOf(charArrayEncryptStringA[i+j])) * (multiplier);
                multiplier /= 2;
            }
            text += (char) temp;
        }

        return text;
    }

    public static String asciiToInt(String text) {
        char[] charArrayEncryptStringA = text.toCharArray();
        text = "";
        for (char character : charArrayEncryptStringA) {
                if (character < 100 && character >= 10) {
                    text += "0" + (int) character;
                } else if (character < 10) {
                    text += "00" + (int) character;
                } else if (character == 0) {
                    text += "000";
                } else {
                    text += (int) character;
                }
        }
        return text;
    }

    public static String asciiToBinary(String text) {
        char[] charArrayEncryptStringA = text.toCharArray();
        text = "";
        for (char character : charArrayEncryptStringA) {
            if (character < 0b10000000 && character >= 0b1000000) {
                text += "0" + Integer.toBinaryString((int) character);
            } else if (character < 0b1000000 && character >= 0b100000) {
                text += "00" + Integer.toBinaryString((int) character);
            } else if (character < 0b100000 && character >= 0b10000) {
                text += "000" + Integer.toBinaryString((int) character);
            } else if (character < 0b10000 && character >= 0b1000) {
                text += "0000" + Integer.toBinaryString((int) character);
            } else if (character < 0b1000 && character >= 0b100) {
                text += "00000" + Integer.toBinaryString((int) character);
            } else if (character < 0b100 && character >= 0b10) {
                text += "000000" + Integer.toBinaryString((int) character);
            } else if (character < 0b10 && character != 0b0) {
                text += "0000000" + Integer.toBinaryString((int) character);
            } else if (character == 0b0) {
                text += "00000000";
            } else {
                text += Integer.toBinaryString((int) character);
            }
        }
        return text;
    }
}
