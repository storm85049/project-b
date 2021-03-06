package client;

import Krypto.DES;
import Krypto.ElGamal;
import Krypto.RSA;

import controller.logger.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Orientation;
import org.json.simple.JSONObject;
import util.Actions;

import java.net.InetAddress;
import java.util.*;

public class ClientData{
    private static ClientData instance;
    private String name = null;
    private String id = null;
    private String ip = null;
    private InetAddress serverAdress = null;
    private SimpleStringProperty  idFromOpenChat = new SimpleStringProperty();
    private String idFromLastRequest = null;
    private Map<String, String> internalChosenDESKeyMap = new HashMap<>();



    private String asymmetricEncryptionFromOpenChat = null;
    private String symmetricEncryptionFromOpenChat = null;
    private JSONObject symmetricEncryptionParameters = null;
    private JSONObject encryptionData = null;
    private RSA RSA = null;
    private ElGamal elGamal = null;

    private Orientation paneOrientation = Orientation.HORIZONTAL;



    /**
     * first String represents id of remoteclient -> makes it easy to query for remoteclient
     */
    private HashMap<String, RemoteClient> availableChats = new HashMap<>();


    public static ClientData getInstance() {
        if(ClientData.instance == null){
            ClientData.instance = new ClientData();
        }
        return ClientData.instance;
    }



    public void removeRemoteClientById(String id){
        if(availableChats.containsKey(id)){
            Logger.getInstance().log(Actions.LOG_OFFLINE, getRemoteClientById(id).getName());
            availableChats.remove(id);
        }
    }

    public HashMap<String, RemoteClient> getAvailableChats() {
        return availableChats;
    }

    public void setAvailableChats(HashMap<String, RemoteClient> availableChats) {
        this.availableChats = availableChats;
    }

    public InetAddress getServerAdress() {
        return serverAdress;
    }
/*
    public String getIdFromOpenChat() {
        return idFromOpenChat;
    }*/



    public RemoteClient getRemoteClientById(String id)
    {
        if(this.availableChats.containsKey(id)){
            return this.availableChats.get(id);
        }
        return null;
    }

    public void addToAvailableChats(RemoteClient remoteClient)
    {
        if(!this.availableChats.containsKey(remoteClient.getId())){
            remoteClient.setOnlineSince(new Date());
            Logger.getInstance().log(Actions.LOG_ONLINE,remoteClient.getName());
            this.availableChats.put(remoteClient.getId(), remoteClient);
        }

    }


    public String getIdFromLastRequest() {
        return idFromLastRequest;
    }

    public void setIdFromLastRequest(String idFromLastRequest) {
        this.idFromLastRequest = idFromLastRequest;
    }

    public RemoteClient getRemoteClientFromOpenChat()
    {
        String id = this.getIdFromOpenChat();
        if(id.isEmpty())
            return null;
        return this.getRemoteClientById(id);

    }

    public String getIdFromOpenChat() {
        return idFromOpenChat.get();
    }

    public SimpleStringProperty getIdFromOpenChatProperty() {
        return idFromOpenChat;
    }

    public void setIdFromOpenChat(String idFromOpenChat) {
        this.idFromOpenChat.set(idFromOpenChat);
    }


    /*public void setIdFromOpenChat(String idFromOpenChat) {
        this.idFromOpenChat = idFromOpenChat;
    }*/

    public void setServerAdress(InetAddress serverAdress) {
        this.serverAdress = serverAdress;
    }

    public String getName() {
        return name;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JSONObject getEncryptionData() {
        return encryptionData;
    }

    public void setEncryptionData(JSONObject encryptionData) {
        this.encryptionData = encryptionData;
    }

    public RSA getRSA(){
        return RSA;
    }

    public void setRSA(RSA RSA){
        this.RSA = RSA;
    }

    public ElGamal getElGamal(){
        return elGamal;
    }

    public void setElGamal(ElGamal elGamal){
        this.elGamal = elGamal;
    }

    public String getSymmetricEncryptionFromOpenChat() {
        return symmetricEncryptionFromOpenChat;
    }

    public void setSymmetricEncryptionFromOpenChat(String symmetricEncryptionFromOpenChat) {
        this.symmetricEncryptionFromOpenChat = symmetricEncryptionFromOpenChat;
    }

    public String getAsymmetricEncryptionFromOpenChat() {
        return asymmetricEncryptionFromOpenChat;
    }

    public void setAsymmetricEncryptionFromOpenChat(String asymmetricEncryptionFromOpenChat) {
        this.asymmetricEncryptionFromOpenChat = asymmetricEncryptionFromOpenChat;
    }

    public JSONObject getSymmetricEncryptionParameters() {
        return symmetricEncryptionParameters;
    }

    public void setSymmetricEncryptionParameters(JSONObject symmetricEncryptionParameters) {
        this.symmetricEncryptionParameters = symmetricEncryptionParameters;
    }


    public Map<String, String> getInternalChosenDESKeyMap() {
        return internalChosenDESKeyMap;
    }

    public void setInternalChosenDESKeyMap(Map<String, String> internalChosenDESKeyMap) {
        this.internalChosenDESKeyMap = internalChosenDESKeyMap;
    }

    public Orientation getPaneOrientation() {
        return paneOrientation;
    }

    public void setPaneOrientation(Orientation paneOrientation) {
        this.paneOrientation = paneOrientation;
    }
}
