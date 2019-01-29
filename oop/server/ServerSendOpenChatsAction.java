package oop.server;

import org.json.simple.JSONObject;
import server.ServerMain;
import util.JSONUtil;

import java.util.HashMap;

public class ServerSendOpenChatsAction implements ServerActionResolver {
    @Override
    public void resolve(JSONObject jsonObject, ServerMain serverMain) {

        JSONObject json  = JSONUtil.getConnectedClientsJSON(serverMain.getConnectedClients());
        serverMain.sendToClients(json,serverMain.getConnectedClients());


    }


}
