package oop.server;

import org.json.simple.JSONObject;
import server.ConnectedClient;
import server.ServerMain;
import util.Actions;
import util.JSONUtil;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerLoginAction implements ServerActionResolver {

    ObjectOutputStream out = null;

    public ServerLoginAction(ObjectOutputStream out){
        this.out = out;
    }

    @Override
    public void resolve(JSONObject jsonObject, ServerMain serverMain) {
        JSONObject jsonIN =  jsonObject;
        JSONObject jsonOut = null;

        String name = (String)jsonIN.get("name");
        String id = (String)jsonIN.get("id");
        InetAddress ip = (InetAddress)jsonIN.get("ip");

        if(isNameAvailable(name,serverMain)){
            String rsa = null;
            String elgamal = null;
            ConnectedClient client = new ConnectedClient(name,id,ip,rsa,elgamal,out);
            jsonOut= JSONUtil.getLoginResponseJSON(name,id,rsa,elgamal, Actions.ACTION_LOGIN_GRANTED,serverMain.getConnectedClients());
            serverMain.addClient(client);
            ArrayList<ConnectedClient> receiver = new ArrayList<>();
            receiver.add(client);
            serverMain.sendToClients(jsonOut,receiver);
            notifyChatStatus(serverMain);
        }else{
            jsonOut= JSONUtil.getLoginResponseJSON(name,id,null,null,Actions.ACTION_LOGIN_FAILED,null);
            serverMain.sendToClients(jsonOut,out);
        }
    }


    private synchronized boolean isNameAvailable(String name,ServerMain serverMain) {
        for(ConnectedClient c : serverMain.getConnectedClients()){
            if(c.getName().equalsIgnoreCase(name)){
                return false;
            }
        }return true;
    }
    private static void notifyChatStatus(ServerMain serverMain){

        HashMap<String,String> updatedUsers = JSONUtil.getConnectedClientsHashMap(serverMain.getConnectedClients());
        JSONObject jsonObject = JSONUtil.getUpdatedChatViewJSON(updatedUsers);
        serverMain.sendToClients(jsonObject,serverMain.getConnectedClients());
    }

}
