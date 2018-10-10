package Krypto;

/////////////// FIRST IMPLEMENTATION/ PROTOTYPE/////////////
// Converts a String of ASCII characters into a String of int and viceversa (every 3 Digits representing an
// ASCII Character)
public class StringAndIntTransformation {
    public static String remakeStringFromAscii(String text){
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
    public static String asciiToInt(String text) {
        char[] charArrayEncryptStringA = text.toCharArray();
        text = "";
        for (char character : charArrayEncryptStringA) {
            if (character < 100 && character > 10) {
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
}
