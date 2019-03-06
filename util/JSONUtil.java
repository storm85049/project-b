package util;

import org.json.simple.JSONObject;
import server.ConnectedClient;

import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class JSONUtil {

    public static JSONObject getLoginResponseJSON(String name, String id, ArrayList<ConnectedClient>connectedClients){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action",Actions.ACTION_INIT_LOGIN_RESPONSE);
        jsonObject.put("name",name);
        jsonObject.put("id",id);

        /*
        if(!connectedClients.isEmpty()){
            HashMap<String,String> activeUsers = JSONUtil.getConnectedClientsJSON(connectedClients);
            jsonObject.put("activeUsers",activeUsers);
        }*/
        return jsonObject;
    }


    /**
     *
     * @param id
     * @param rsaKeyMap
     * @param elGamalKeyMap
     * @return
     */
    public static JSONObject getPublicKeyBroadcastJSON(String id,
                                                       Map<String, String> rsaKeyMap,
                                                       Map<String,String> elGamalKeyMap){
        JSONObject json = new JSONObject();
        json.put("action", Actions.ACTION_SEND_ASYMMETRIC_KEYS);
        json.put("id", id);
        json.put("rsa_keymap", rsaKeyMap);
        json.put("elgamal_keymap", elGamalKeyMap);

        return json;

    }

    /**
     *
     * @param t
     * @param k
     * @return
     */
    public static JSONObject affineKeyJson(String asymMode,String t, String k)
    {
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();
        json.put("asymMode",asymMode);
        json.put("symMode", Actions.MODE_AFFINE);
        data.put("t",t);
        data.put("k",k);
        json.put("encryptionParams", data);
        return  json;
    }

    /**
     *
     * @param key
     * @return
     */
    public static JSONObject vigenereKeyJson(String asymMode, String key)
    {
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();
        json.put("asymMode",asymMode);
        json.put("symMode",Actions.MODE_VIGENERE);
        data.put("key",key);
        json.put("encryptionParams", data);
        return  json;
    }


    public static JSONObject rc4KeyJson(String asymMode, String key)
    {
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();
        json.put("asymMode",asymMode);
        json.put("symMode",Actions.MODE_RC4);
        data.put("key",key);
        json.put("encryptionParams", data);
        return  json;
    }

    public static JSONObject hillKeyJson(String asymMode, String[][] key)
    {


        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();
        json.put("asymMode",asymMode);
        json.put("symMode",Actions.MODE_HILL);
        data.put("key",key);
        json.put("encryptionParams", data);
        return  json;
    }


    public static JSONObject desKEYJson(String asymMode, Map<String,String> key)
    {
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();
        json.put("asymMode",asymMode);
        json.put("symMode",Actions.MODE_DES);
        data.put("keymap",key);
        json.put("encryptionParams", data);
        return  json;
    }


    public static String stringifyHillKeys(String[][] keys)
    {
        JSONObject toParse = new JSONObject();

        int counter = 0;
        for (int i = 0; i < keys.length; i++){
            for(int j = 0; j < keys[i].length; j++){
                toParse.put("h"+counter,keys[i][j]);
                counter++;
            }
        }
        return toParse.toJSONString();
    }


    public static String[][] objectifyHillString(JSONObject json)
    {
        boolean small = false;
        if(json.size() == 4){
            small = true;
        }else if (json.size() == 9){
            small = false;
        }

        //super geiler dynamischer code !!! extrem geil !! (nicht)

        String[][] smallKeys = new String[2][2];
        String[][] largeKeys = new String[3][3];

        for(int i = 0; i < json.size();i++){
            String value = (String) json.get("h"+i);
            if (small){
                if(i == 0 ) smallKeys[0][0] = value;
                if(i == 1 ) smallKeys[0][1] = value;
                if(i == 2 ) smallKeys[1][0] = value;
                if(i == 3 ) smallKeys[1][1] = value;
            }else{
                if(i == 0 ) largeKeys[0][0] = value;
                if(i == 1 ) largeKeys[0][1] = value;
                if(i == 2 ) largeKeys[0][2] = value;
                if(i == 3 ) largeKeys[1][0] = value;
                if(i == 4 ) largeKeys[1][1] = value;
                if(i == 5 ) largeKeys[1][2] = value;
                if(i == 6 ) largeKeys[2][0] = value;
                if(i == 7 ) largeKeys[2][1] = value;
                if(i == 8 ) largeKeys[2][2] = value;
            }
        }

        return small ? smallKeys : largeKeys;

    }



    public static JSONObject getMessageSendingJSON(String message, String fromID, String toID, String symmode)
    {
        JSONObject json = new JSONObject();
        json.put("action", Actions.ACTION_SEND_MESSAGE);
        json.put("message" , message);
        json.put("fromID" , fromID);
        json.put("toID" , toID);
        json.put("symMode" ,symmode);

        Date date = new Date();
        String strDateFormat = "hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);
        json.put("timestamp", formattedDate);

        return json;

    }


    public static JSONObject getConnectedClientsJSON(ArrayList<ConnectedClient> connectedClients){


        JSONObject mainJSON = new JSONObject();
        mainJSON.put("action",Actions.ACTION_UPDATE_CHAT_VIEW);


        for(ConnectedClient c : connectedClients) {
            JSONObject jsonConnectedClient = new JSONObject();
            jsonConnectedClient.put("name", c.getName());
            jsonConnectedClient.put("publicRSAKeyMap", c.getPublicRSAKeyMap());
            jsonConnectedClient.put("publicElGamalKeyMap", c.getPublicElGamalKeyMap());

            mainJSON.put(c.getId(), jsonConnectedClient);

        }
        return mainJSON;
    }




    public static JSONObject getLoginRequestNameJSON(String name)
    {
        JSONObject json = new JSONObject();
        json.put("action", Actions.ACTION_SEND_LOGIN_REQUEST_NAME);
        json.put("name",name);

        return json;

    }

    public static JSONObject getLoginResponseNameJSON(String grantedOrNot)
    {
        JSONObject json = new JSONObject();
        json.put("action",grantedOrNot);

        return json;

    }




    public static JSONObject getLoginRequestJSON(String name, Map<String,String> rsaPublicKeyMap, Map<String,String> elGamalPublicKeyMap ){
        InetAddress ip = ConnectionUtil.getIP();
        MessageDigest instance = null;
        try {
            instance = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] messageDigest = instance.digest(String.valueOf(System.nanoTime()).getBytes());
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < messageDigest.length; i++) {
            String hex = Integer.toHexString(0xFF & messageDigest[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action",Actions.ACTION_INIT_LOGIN);
        jsonObject.put("name",name);
        jsonObject.put("id",hexString.toString());
        jsonObject.put("ip",ip);
        jsonObject.put("publicRSAKeyMap", rsaPublicKeyMap);
        jsonObject.put("publicElGamalKeyMap", elGamalPublicKeyMap);
        return jsonObject;
    }


    public static JSONObject initEncryptedChatRequest(String from, String to, JSONObject encryptedData){

        JSONObject json = new JSONObject();

        json.put("action", Actions.ACTION_INIT_ENCRYPTED_CHAT_REQUEST);
        json.put("from", from);
        json.put("to", to);
        json.put("encryptionParams", encryptedData);


        return json;
    }








    public static JSONObject shutDownConnectionJSON(String id){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        jsonObject.put("action",Actions.ACTION_CLOSE_APPLICATION);

        return jsonObject;
    }




    public static JSONObject getEncryptionOptions(String asymmetricEncryptionMode, String symmetricEncryptionMode, JSONObject symmetricEncryptionParameters /*, String OperationMode */) {
        JSONObject encryptionObject = new JSONObject();
        encryptionObject.put("asymmetricEncryptionMode", asymmetricEncryptionMode);
        encryptionObject.put("symmetricEncryptionMode", symmetricEncryptionMode);
        encryptionObject.put("symmetricEncryptionParameters", symmetricEncryptionParameters);
        System.out.println(asymmetricEncryptionMode + ", " + symmetricEncryptionMode + ", " + symmetricEncryptionParameters);
        return encryptionObject;
    }

}
