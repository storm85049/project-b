package client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class ClientData {
    public static ClientData instance;
    private String name = null;
    private String id = null;
    private String ip = null;

    public static ClientData getInstance() {
        if(ClientData.instance == null){
            ClientData.instance = new ClientData();
        }
        return ClientData.instance;

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
