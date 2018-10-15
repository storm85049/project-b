package server;

import java.io.ObjectOutputStream;
import java.net.InetAddress;

public class ConnectedClient {
    private String name;
    private String id;
    private InetAddress ip;
    private String publicRSAKey;
    private String publicElGamalKey;
    private ObjectOutputStream objectOutputStream;

    /**
     *
     * @String name
     * @String id
     * @String publicRSAKey
     * @String publicElGamalKey
     */
    public ConnectedClient(String name, String id, InetAddress ip, String publicRSAKey, String publicElGamalKey,ObjectOutputStream o){
        this.name = name;
        this.id = id;
        this.ip = ip;
        this.publicRSAKey = publicRSAKey;
        this.publicElGamalKey = publicElGamalKey;
        this.objectOutputStream = o;
    }



    public InetAddress getIp() {
        return ip;
    }

    public void setPublicElGamalKey(String publicElGamalKey) {
        this.publicElGamalKey = publicElGamalKey;
    }

    public void setPublicRSAKey(String publicRSAKey) {
        this.publicRSAKey = publicRSAKey;
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
