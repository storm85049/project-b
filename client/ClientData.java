package client;

import Krypto.ElGamal;
import Krypto.RSA;

import java.net.InetAddress;
import java.util.*;

public class ClientData{
    private static ClientData instance;
    private String name = null;
    private String id = null;
    private String ip = null;
    private InetAddress serverAdress = null;
    private String idFromOpenChat = null;

    private RSA rsa = null;
    private ElGamal elGamal = null;

    public void setRsa(RSA rsa){
        this.rsa = rsa;
    }
    public void setElGamal(ElGamal elGamal){
        this.elGamal = elGamal;
    }

    public ElGamal getElGamal() {
        return elGamal;
    }

    public RSA getRsa() {
        return rsa;
    }


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
}
