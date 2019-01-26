package client;

import Krypto.IAsymmetricEncryption;
import Krypto.ISymmetricEncryption;
import org.json.simple.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Map;

public class RemoteClient {


    private IAsymmetricEncryption currentAsymEncryption;
    private ISymmetricEncryption currentSymEncryption;
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



    public IAsymmetricEncryption getCurrentAsymEncryption() {
        return currentAsymEncryption;
    }

    public ISymmetricEncryption getCurrentSymEncryption() {
        return currentSymEncryption;
    }

    public void setCurrentSymEncryption(ISymmetricEncryption currentSymEncryption) {
        this.currentSymEncryption = currentSymEncryption;
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

    public void setCurrentAsymEncryption(IAsymmetricEncryption currentAsymEncryption) {
        this.currentAsymEncryption = currentAsymEncryption;
    }


}
