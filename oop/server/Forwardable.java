package oop.server;

import org.json.simple.JSONObject;
import server.ConnectedClient;
import server.ServerMain;

abstract class Forwardable {
    void forwardMessage(JSONObject json, String to, ServerMain serverMain)
    {
        ConnectedClient connectedClient = serverMain.getConnectedClientById(to);
        serverMain.sendToClients(json,connectedClient.getObjectOutputStream());
    }
}
