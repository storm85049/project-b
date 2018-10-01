package util;

import com.sun.deploy.util.StringUtils;
import org.json.simple.JSONObject;
import server.ConnectedClient;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;



public class JSONUtil {


    public static JSONObject getLoginResponseJSON(String name, String id,
                                                  String publicRSAKey, String publicELGamalKey,
                                                  String status, ArrayList<ConnectedClient>connectedClients){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action",Actions.ACTION_LOGIN_RESPONSE);
        jsonObject.put("status",status);
        jsonObject.put("name",name);
        jsonObject.put("id",id);
        jsonObject.put("publicRSAKey",publicRSAKey);
        jsonObject.put("publicELGamalKey",publicELGamalKey);

        HashMap<String,String> activeUsers = JSONUtil.getConnectedClientsHashMap(connectedClients);
        jsonObject.put("activeUsers",activeUsers);
        return jsonObject;
    }

    public static JSONObject getUpdatedChatViewJSON(HashMap<String,String> activeUsers){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("activeUsers",activeUsers);
        jsonObject.put("action",Actions.ACTION_UPDATE_CHAT_VIEW);

        return jsonObject;
    }

    public static HashMap<String,String> getConnectedClientsHashMap(ArrayList<ConnectedClient> connectedClients){
        HashMap<String,String> activeUsers = new HashMap<>();
        for(ConnectedClient c : connectedClients) {
            activeUsers.put(c.getName(), c.getId());
        }
        return activeUsers;
    }






    public static JSONObject getLoginRequestJSON(String name){
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
        jsonObject.put("action",Actions.ACTION_LOGIN_REQUEST);
        jsonObject.put("name",name);
        jsonObject.put("id",hexString.toString());
        jsonObject.put("ip",ip);
        return jsonObject;
    }


    public static JSONObject shutDownConnectionJSON(String id){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        jsonObject.put("action",Actions.ACTION_CLOSE_APPLICATION);

        return jsonObject;
    }



}
