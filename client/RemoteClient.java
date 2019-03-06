package client;

import Krypto.*;
import controller.EncryptionController;
import org.json.simple.JSONObject;
import util.Actions;
import util.JSONUtil;

import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class RemoteClient {


    private String asymEncryptionString = null;
    private String symEncryptionString = null;
    private ISymmetricEncryption symEncryption = null;
    private Map<String,String> desKeyMap = null;
    private JSONObject requestedEncryptionData = null;
    private Date onlineSince = null;
    private String asymKeyReadable = null;



    private String id;
    private String name ;
    private String ip  ;
    private InetAddress serverAdress ;
    private int unreadMessages = 0;
    private Map<String,String> publicRSAKeyMap;
    private Map<String,String> publicElGamalKeyMap;

    public void setPublicElGamalKeyMap(Map<String, String> publicElGamalKeyMap) {
        this.publicElGamalKeyMap = publicElGamalKeyMap;
    }

    public boolean isEncryptionSet()
    {
        return this.getRequestedEncryptionData() != null;
    }

    public void setPublicRSAKeyMap(Map<String, String> publicRSAKeyMap) {
        this.publicRSAKeyMap = publicRSAKeyMap;
    }

    public Map<String, String> getPublicRSAKeyMap() {
        return publicRSAKeyMap;
    }

    public Map<String, String> getPublicElGamalKeyMap() {
        return publicElGamalKeyMap;
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(int unreadMessages) {
        this.unreadMessages = unreadMessages;
    }


    public String getAsymKeyReadable() {
        return asymKeyReadable;
    }

    public void setAsymKeyReadable(String asymKeyReadable) {
        this.asymKeyReadable = asymKeyReadable;
    }

    /**
     * in dieser ArrayList werden die Nachrichten mit allen zugehörigen Infos als JsonObjekte gespeichert.
     * Drin stehen kann (für den LOG )zb. ->timestamp ->enc.message ->dec.message und was sonst noch so dazugehört
     *
     *
     */
    private ArrayList<JSONObject> chatHistory = new ArrayList<>();

    /**
     *
     * @param id
     * @param name
     */
    public RemoteClient(String id, String name)
    {
        this.id = id;
        this.name = name;

    }


   public String getOnlineSince() {

       String strDateFormat = "hh:mm:ss a";
       DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
       return  dateFormat.format(this.onlineSince);
    }

    public void setOnlineSince(Date onlineSince) {
        this.onlineSince = onlineSince;
    }

    public Map<String, String> getDesKeyMap() {
        return desKeyMap;
    }

    public void setDesKeyMap(Map<String, String> desKeyMap) {
        this.desKeyMap = desKeyMap;
    }

    public ArrayList<JSONObject> getChatHistory()
    {
        if(this.chatHistory.isEmpty())
            return null;
        return this.chatHistory;
    }

    public void addMessageToChatHistory(JSONObject messageData)
    {
        this.chatHistory.add(messageData);
    }

    public JSONObject getRequestedEncryptionData() {
        return requestedEncryptionData;
    }

    public void setRequestedEncryptionData(JSONObject requestedEncryptionData) {

        String asymMode = (String) requestedEncryptionData.get("asymMode");
        String symMode = (String) requestedEncryptionData.get("symMode");
        JSONObject keys =  (JSONObject) requestedEncryptionData.get("encryptionParams");

        this.setAsymEncryptionString(asymMode);
        this.setSymEncryptionString(symMode);

        if(symMode.equals(Actions.MODE_VIGENERE)){
            String key = (String) keys.get("key");
            this.setSymEncryption(new VigenereCipher(key, EncryptionController.DEFAULT_ALPHABET_MODE));
            this.setSymEncryptionString(Actions.MODE_VIGENERE);

        }else if (symMode.equals(Actions.MODE_AFFINE)){
            String t = (String) keys.get("t");
            String k = (String) keys.get("k");
            this.setSymEncryption(new AffineCipher(k,t,EncryptionController.DEFAULT_ALPHABET_MODE));
            this.setSymEncryptionString(Actions.MODE_AFFINE);
        }else if(symMode.equals(Actions.MODE_RC4)){
            String key = (String) keys.get("key");
            this.setSymEncryption(new RC4(key));
            this.setSymEncryptionString(Actions.MODE_RC4);
        }else if(symMode.equals(Actions.MODE_DES)){
            Map<String,String> keyMap =(Map<String,String>)keys.get("keymap");
            this.setSymEncryption(new DES(keyMap));
            this.setSymEncryptionString(Actions.MODE_DES);
        }else if (symMode.equals(Actions.MODE_HILL)){
            String[][] keyArray =(String[][])keys.get("key");
            try{
                this.setSymEncryption(new HillCipher(keyArray,"ASCII"));
            }catch(Exception e){};
            this.setSymEncryptionString(Actions.MODE_HILL);
        }

        this.requestedEncryptionData = requestedEncryptionData;

    }


    public JSONObject getEncryptedEncryptionData()
    {


        JSONObject requestedEncryptionData = (JSONObject) this.getRequestedEncryptionData().clone();
        JSONObject keys = (JSONObject) requestedEncryptionData.get("encryptionParams");
        String symMode = this.getSymEncryptionString();
        String asymMode = this.getAsymEncryptionString();

        if(symMode.equals(Actions.MODE_VIGENERE)){
            String key = (String) keys.get("key");
            keys.remove("key");
            keys.put("encryptedKey", this.getEncryptedStringForCurrentAsymMode(asymMode, key));
            requestedEncryptionData.put("encryptionParams", keys);
        }else if (symMode.equals(Actions.MODE_AFFINE)){
            JSONObject jsonNew = new JSONObject();
            jsonNew.put("t",keys.get("t"));
            jsonNew.put("k",keys.get("k")) ;
            keys.remove("t");
            keys.remove("k");
            keys.put("encryptedKey", this.getEncryptedStringForCurrentAsymMode(asymMode,jsonNew.toString()));
            requestedEncryptionData.put("encryptionParams",keys);
        }else if(symMode.equals(Actions.MODE_RC4)){
            String key = (String) keys.get("key");
            keys.remove("key");
            keys.put("encryptedKey", this.getEncryptedStringForCurrentAsymMode(asymMode, key));
            requestedEncryptionData.put("encryptionParams", keys);
        }else if(symMode.equals(Actions.MODE_DES)) {
            Map<String, String> keymap = (Map<String, String>) keys.get("keymap");
            keys.remove("key");
            JSONObject json = new JSONObject();
            json.put("keymap",keymap);
            keys.put("encryptedKey", this.getEncryptedStringForCurrentAsymMode(asymMode, json.toString()));
            requestedEncryptionData.put("encryptionParams", keys);
        }else if(symMode.equals(Actions.MODE_HILL)){
            String [][] keyArray = (String[][]) keys.get("key");
            keys.remove("key");
            keys.put("encryptedKey", this.getEncryptedStringForCurrentAsymMode(asymMode,JSONUtil.stringifyHillKeys(keyArray)));
            requestedEncryptionData.put("encryptionParams", keys);
        }




        if(asymMode.equals(Actions.MODE_ELGAMAL)){
            String keyJ = ClientData.getInstance().getElGamal().getPublicKeyMap().get("public_key_j");
            requestedEncryptionData.put("public_key_j",keyJ );
        }
        return requestedEncryptionData;
    }



    private String getEncryptedStringForCurrentAsymMode(String mode, String s)
    {
        if(mode.equals(Actions.MODE_RSA)){
            String encrypted = ClientData.getInstance().getRSA().encrypt(s,this.getPublicRSAKeyMap());
            this.setAsymKeyReadable(encrypted);
            return encrypted;
        }else if(mode.equals(Actions.MODE_ELGAMAL)){
            String encrypted = ClientData.getInstance().getElGamal().encrypt(s,this.getPublicElGamalKeyMap());
            this.setAsymKeyReadable(encrypted);
            return encrypted;
        }
        return null;

    }


    public ISymmetricEncryption getSymEncryption() {
        return symEncryption;
    }

    public void setSymEncryption(ISymmetricEncryption symEncryption) {
        this.symEncryption = symEncryption;
    }

    public String getAsymEncryptionString() {
        return asymEncryptionString;
    }

    public void setAsymEncryptionString(String asymEncryptionString) {
        this.asymEncryptionString = asymEncryptionString;
    }

    public String getSymEncryptionString() {
        return symEncryptionString;
    }

    public void setSymEncryptionString(String symEncryptionString) {
        this.symEncryptionString = symEncryptionString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public InetAddress getServerAdress() {
        return serverAdress;
    }

    public void setServerAdress(InetAddress serverAdress) {
        this.serverAdress = serverAdress;
    }

}
