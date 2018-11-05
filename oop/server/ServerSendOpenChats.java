package oop.server;

import org.json.simple.JSONObject;
import server.ConnectedClient;
import server.ServerMain;
import util.JSONUtil;

import java.util.HashMap;

public class ServerSendOpenChats implements ServerActionResolver {
    @Override
    public void resolve(JSONObject jsonObject, ServerMain serverMain) {

        HashMap<String,String> updatedUsers = JSONUtil.getConnectedClientsHashMap(serverMain.getConnectedClients());
        JSONObject jsonOut = JSONUtil.getUpdatedChatViewJSON(updatedUsers);
        serverMain.sendToClients(jsonOut,serverMain.getConnectedClients());


    }


}
