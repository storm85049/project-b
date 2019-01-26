package oop.server;

import org.json.simple.JSONObject;
import server.ConnectedClient;
import server.ServerMain;
import util.JSONUtil;

import java.util.HashMap;

public class ServerLogoutAction implements ServerActionResolver {
    @Override
    public void resolve(JSONObject jsonObject, ServerMain serverMain) {
        String id = (String)jsonObject.get("id");
        if(id != null){
            for(ConnectedClient client : serverMain.getConnectedClients()){
                if(client.getId().equals(id)){
                    serverMain.deleteClient(client);
                    notifyChatStatus(serverMain);
                }
            }
        }
    }


    private static void notifyChatStatus(ServerMain serverMain){
        JSONObject updatedUsers = JSONUtil.getConnectedClientsJSON(serverMain.getConnectedClients());
        serverMain.sendToClients(updatedUsers,serverMain.getConnectedClients());
    }
}
