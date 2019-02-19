package client;

import Krypto.*;
import controller.EncryptionController;
import org.json.simple.JSONObject;
import util.Actions;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Map;

public class RemoteClient {


    private String asymEncryptionString = null;
    private String symEncryptionString = null;
    private ISymmetricEncryption symEncryption = null;

    private JSONObject requestedEncryptionData = null;
    private String encryptionStatus;


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
            //todo:das geht aber nicht auf der empängerseite !!!!!
            String key = (String) keys.get("key");
            this.setSymEncryption(new VigenereCipher(key, EncryptionController.DEFAULT_ALPHABET_MODE));
        }else if (symMode.equals(Actions.MODE_AFFINE)){
            String t = (String) keys.get("t");
            String k = (String) keys.get("k");
            this.setSymEncryption(new AffineCipher(k,t,EncryptionController.DEFAULT_ALPHABET_MODE));
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
            return requestedEncryptionData;
        }else if (symMode.equals(Actions.MODE_AFFINE)){
            JSONObject jsonNew = new JSONObject();
            jsonNew.put("t",keys.get("t"));
            jsonNew.put("k",keys.get("k")) ;
            keys.remove("t");
            keys.remove("k");
            keys.put("encryptedKey", this.getEncryptedStringForCurrentAsymMode(asymMode,jsonNew.toString()));
            requestedEncryptionData.put("encryptionParams",keys);
            return requestedEncryptionData;
        }

        return requestedEncryptionData;
    }



    private String getEncryptedStringForCurrentAsymMode(String mode, String s)
    {
        if(mode.equals(Actions.MODE_RSA)){
            return ClientData.getInstance().getRSA().encrypt(s,this.getPublicRSAKeyMap());
        }else if(mode.equals(Actions.MODE_ELGAMAL)){
            return ClientData.getInstance().getElGamal().encrypt(s,this.getPublicElGamalKeyMap());
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
