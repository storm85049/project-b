package client;

import Krypto.ElGamal;
import Krypto.RSA;

import Krypto.ElGamal;
import Krypto.RSA;
import org.json.simple.JSONObject;

import java.net.InetAddress;
import java.util.*;

public class ClientData{
    private static ClientData instance;
    private String name = null;
    private String id = null;
    private String ip = null;
    private InetAddress serverAdress = null;
    private String idFromOpenChat = null;
    private String asymmetricEncryptionFromOpenChat = null;
    private String symmetricEncryptionFromOpenChat = null;
    private JSONObject symmetricEncryptionParameters = null;
    private JSONObject encryptionData = null;
    private RSA RSA = null;
    private ElGamal elGamal = null;


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

    public InetAddress getServerAdress() {
        return serverAdress;
    }

    public String getIdFromOpenChat() {
        return idFromOpenChat;
    }



    public RemoteClient getAvailableChatById(String id)
    {
        if(this.availableChats.containsKey(id)){
            return this.availableChats.get(id);
        }
        return null;
    }

    public void addToAvailableChats(RemoteClient remoteClient)
    {
        if(!this.availableChats.containsKey(remoteClient.getId()))
            this.availableChats.put(remoteClient.getId(), remoteClient);

    }


    public void setIdFromOpenChat(String idFromOpenChat) {
        this.idFromOpenChat = idFromOpenChat;
    }

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
}
