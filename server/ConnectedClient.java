package server;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.Map;

public class ConnectedClient {
    private String name;
    private String id;
    private InetAddress ip;
    private Map<String,String> rsaPublicKeyMap;
    private Map<String,String> elGamalPublicKeyMap;
    private ObjectOutputStream objectOutputStream;

    /**
     *
     * @String name
     * @String id
     * @String rsaPublicKeyMap
     * @String elGamalPublicKeyMap
     */
    public ConnectedClient(String name, String id, InetAddress ip, Map<String,String> rsaPublicKeyMap, Map<String,String> elGamalPublicKeyMap,ObjectOutputStream o){
        this.name = name;
        this.id = id;
        this.ip = ip;
        this.rsaPublicKeyMap = rsaPublicKeyMap;
        this.elGamalPublicKeyMap = elGamalPublicKeyMap;
        this.objectOutputStream = o;
    }

    public Map<String, String> getPublicElGamalKeyMap() {
        return elGamalPublicKeyMap;
    }

    public Map<String, String> getPublicRSAKeyMap() {
        return rsaPublicKeyMap;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setElGamalPublicKeyMap(Map<String,String> elGamalPublicKeyMap) {
        this.elGamalPublicKeyMap = elGamalPublicKeyMap;
    }

    public void setRsaPublicKeyMap(Map<String,String> rsaPublicKeyMap) {
        this.rsaPublicKeyMap = rsaPublicKeyMap;
    }

    public String getId(){
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
        this.objectOutputStream = objectOutputStream;
    }
}
