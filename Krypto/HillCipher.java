package Krypto;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class HillCipher implements ISymmetricEncryption {
    Map<String, String> keyMap = new HashMap<>();
    String[][] key;
    String mode;
    public HillCipher(){}
    public HillCipher(String[][] key, String mode) throws Exception{
        if(matrixDeterminant(transformToIntMatrix(key)) == 0 ||
                BigInteger.valueOf(matrixDeterminant(transformToIntMatrix(key))).gcd(
                        BigInteger.valueOf(mode.equalsIgnoreCase("ASCII") ?
                                256 : 26)).compareTo(new BigInteger("1")) != 0
        ){
            throw new Exception("Matrix is has no Inverse in the chosen alphabetical mode");
        }
        this.key = key;
        this.mode = mode;
        keyMap.put("key", transformMatrixToStringTable(key));
    }

    private String transformMatrixToStringTable(String[][] matrix){
        // Basically summarize the Matrix in a String resembling a .csv File (Comma separated values)
        // In order to follow the Interface's implementation. NOT changing the interface because
        // value of the map would be Map<String, Object>, which is not ideal declaration of types.
        String result = "";
        for(String[] row: matrix){
            for(int i = 0; i < row.length; i++){
                result += row[i] + ",";
            }
            result += "\n";
        }
        return result;
    }
    private String[][] transformStringTableToMatrix(String table){
        short rows = 0, columns = 0;
        for(char ch : table.toCharArray()){
            if(new Regex("/,+/").equals(ch)){
                columns ++;
            }
            if(new Regex("\n").equals(ch)){
                rows ++;
            }
        }
        String[][] result = new String[rows][columns];
        String[] temp = (table.split("\n"));
        for(int i = 0; i < temp.length; i++){
            for(int j = 0; j < temp[i].split("/,+/").length; j++){
                result[i][j] = temp[i].split("/,+/")[j];
            }
        }
        return result;
    }

    @Override
    public String encrypt(String m) {
        int length = key[0].length;
        String result = "";
        while(m.length() % length != 0){
            if(mode == "ASCII"){m += " ";}
            else{m += "X";}
        }
        String[][] mVectors = new String[m.length() / length][length];
        int counterRows = 0, counterColumns = 0;
        for(char character : m.toCharArray()){
            if(counterColumns >= length){
                counterRows++;
                counterColumns = 0;
            }
            mVectors[counterRows][counterColumns] = String.valueOf(character);
            counterColumns ++;
        }
        for(String[] vector: mVectors){
            result += quadraticMatrixTimesVectorString(transformToIntMatrix(key), vector);
        }
        return result;
    }

    private int[][] transformToIntMatrix(String[][] matrix){
        int[][] intMatrix = new int[matrix.length][matrix[0].length];
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++){
                intMatrix[i][j] = Integer.parseInt(matrix[i][j]);
            }
        }
        return intMatrix;
    }

    private String quadraticMatrixTimesVectorString(int[][] intMatrix, String[] vector){
        int[] intVector = new int[vector.length];
        String result = "";
        int modifier = 0, outerModifier = 0;
        int mod = mode.equalsIgnoreCase("ASCII") ? 256 : 26;
        for(int i = 0; i < vector.length ; i++){
            for(char character : vector[i].toCharArray()){
                intVector[i] = (int) character;
            }
        }
        for(int i = 0; i < intMatrix.length; i++){
            int temp = 0;
            if(mode.equalsIgnoreCase("ABC")) {
                if (intVector[i] >= 97 && intVector[i] <= 122) {
                    outerModifier = 97;
                } else {
                    outerModifier = 65;
                }
            }
            for(int j = 0; j < intMatrix[i].length; j++){
                if(mode.equalsIgnoreCase("ABC")) {
                    if (intVector[j] >= 97 && intVector[j] <= 122) {
                        modifier = 97;
                    } else {
                        modifier = 65;
                    }
                }
                if(mode.equalsIgnoreCase("ABC")) {
                    if (intVector[j] >= 97 && intVector[j] <= 122) {
                        modifier = 97;
                    } else {
                        modifier = 65;
                    }
                }
                if(modifier != 0 &&
                        (intVector[j] < 65 ||
                                intVector[j] > 90 && intVector[j] < 97 ||
                                 intVector[j] > 122)){
                    intVector[j] = 88;
                }
                temp += intMatrix[i][j] * (intVector[j] - modifier);

            }
            result += (char) ((temp % mod) + outerModifier);

        }
        return result;
    }

    // Determinant method inspired by:
    // https://gist.github.com/Cellane/398372/23a3e321daa52d4c6b68795aae093bf773ce2940

    private int matrixDeterminant (int[][] matrix) {
        int temporary[][];
        int result = 0;

        if (matrix.length == 1) {
            result = matrix[0][0];
            return (result);
        }

        if (matrix.length == 2) {
            result = ((matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]));
            return (result);
        }

        for (int i = 0; i < matrix[0].length; i++) {
            temporary = new int[matrix.length - 1][matrix[0].length - 1];

            for (int j = 1; j < matrix.length; j++) {
                for (int k = 0; k < matrix[0].length; k++) {
                    if (k < i) {
                        temporary[j - 1][k] = matrix[j][k];
                    } else if (k > i) {
                        temporary[j - 1][k - 1] = matrix[j][k];
                    }
                }
            }

            result += matrix[0][i] * Math.pow (-1, i) * matrixDeterminant (temporary);
        }
        return (result);
    }

    @Override
    public String decrypt(String c) {
        int length = key[0].length;
        String result = "";
        while(c.length() % length != 0){
            if(mode == "ASCII"){c += " ";}
            else{c += "X";}
        }
        String[][] mVectors = new String[c.length() / length][length];
        int counterRows = 0, counterColumns = 0;
        for(char character : c.toCharArray()){
            if(counterColumns >= length){
                counterRows++;
                counterColumns = 0;
            }
            mVectors[counterRows][counterColumns] = String.valueOf(character);
            counterColumns ++;
        }
        for(String[] vector: mVectors){
            try {
                result += quadraticMatrixTimesVectorString(
                        invertMatrix(transformToIntMatrix(key)), vector);
            }
            catch (Exception e){
                System.out.println(e);
                break;
            }
        }
        return result;
    }

    private int[][] invertMatrix(int[][] matrix) throws Exception{
        BigInteger[][] invertedMatrix = new BigInteger[matrix.length][matrix[0].length];
        int [][] invertedMatrixInt = new int[matrix.length][matrix[0].length];
        BigInteger mod = mode.equalsIgnoreCase("ASCII") ? new BigInteger("256") :
                new BigInteger("26");
        if(matrix.length == 2){
            String temp = String.valueOf((matrix[1][1]*matrix[0][0]) - (matrix[0][1] * matrix[1][0]));
            BigInteger determinant = new BigInteger(temp);

            invertedMatrix[0][0] = new BigInteger(String.valueOf(matrix[1][1]));
            invertedMatrix[0][1] = new BigInteger(String.valueOf(matrix[0][1])).negate();
            invertedMatrix[1][0] = new BigInteger(String.valueOf(matrix[1][0])).negate();
            invertedMatrix[1][1] = new BigInteger(String.valueOf(matrix[0][0]));
            for(int i = 0; i < invertedMatrix.length; i++){
                for(int j = 0; j <invertedMatrix[i].length; j++){
                    invertedMatrix[i][j] = invertedMatrix[i][j].
                            multiply(determinant.modInverse(mod)).mod(mod);
                    invertedMatrixInt[i][j] = Integer.parseInt(invertedMatrix[i][j].toString());
                }
            }
            return invertedMatrixInt;
        }
        else if(matrix.length == 3){
            /*
            * {[a, b, c],
            *  [d, e, f],
            *  [g, h, i]}
            * */
            String temp = String.valueOf((matrix[0][0] * ((matrix[1][1] * matrix[2][2]) - (matrix[1][2] * matrix[2][1]))) -
                    (matrix[0][1] * ((matrix[1][0] * matrix[2][2]) - (matrix[1][2] * matrix[2][0]))) +
                    (matrix[0][2]) * ((matrix[1][0] * matrix[2][1]) - (matrix[1][1] * matrix[2][0])));
            BigInteger factor = new BigInteger(temp);
            // First Row
            invertedMatrix[0][0] = new BigInteger(String.valueOf(
                    (matrix[1][1] * matrix[2][2]) -
                            (matrix[1][2] * matrix[2][1]))); // ei - fh

            invertedMatrix[0][1] = new BigInteger(String.valueOf(
                    (matrix[0][2] * matrix[2][1]) -
                            (matrix[0][1] * matrix[2][2]))); // ch - bi

            invertedMatrix[0][2] = new BigInteger(String.valueOf(
                    (matrix[0][1] * matrix[1][2]) -
                            (matrix[0][2] * matrix[1][1]))); // bf - ce

            // Second row
            invertedMatrix[1][0] = new BigInteger(String.valueOf(
                    (matrix[1][2] * matrix[2][0]) -
                            (matrix[1][0] * matrix[2][2]))); // fg - di

            invertedMatrix[1][1] = new BigInteger(String.valueOf(
                    (matrix[0][0] * matrix[2][2]) -
                            (matrix[0][2] * matrix[2][0]))); // ai - cg

            invertedMatrix[1][2] = new BigInteger(String.valueOf(
                    (matrix[0][2] * matrix[1][0]) -
                            (matrix[0][0] * matrix[1][2]))); // cd - af

            // Third row
            invertedMatrix[2][0] = new BigInteger(String.valueOf(
                    (matrix[1][0] * matrix[2][1]) -
                            (matrix[1][1] * matrix[2][0]))); // dh - eg

            invertedMatrix[2][1] = new BigInteger(String.valueOf(
                    (matrix[0][1] * matrix[2][0]) -
                            (matrix[0][0] * matrix[2][1]))); // bg - ah

            invertedMatrix[2][2] = new BigInteger(String.valueOf(
                    (matrix[0][0] * matrix[1][1]) -
                            (matrix[0][1] * matrix[1][0]))); // ae - bd
            for(int i = 0; i < invertedMatrix.length; i++){
                for(int j = 0; j <invertedMatrix[i].length; j++){
                    invertedMatrix[i][j] = invertedMatrix[i][j].
                            multiply(factor.modInverse(mod)).mod(mod);
                    invertedMatrixInt[i][j] = Integer.parseInt(invertedMatrix[i][j].toString());
                }
            }
            return invertedMatrixInt;
        }
        else{
            throw new Exception("Matrix inversion for this matrix not supported");
        }
    }

    @Override
    public void setSelectedKeys(Map<String, String> keyMap){
        keyMap.put("key", keyMap.get("key"));
        this.key = transformStringTableToMatrix(keyMap.get("key"));
    }

    @Override
    public void setAlphabetMode(String mode) {
        this.mode = mode;
    }

    @Override
    public Map<String, String> getKeyMap() {
        return keyMap;
    }

    @Override
    public String getMode() {
        return mode;
    }

    @Override
    public void generateRandomKey() {
    }
}
