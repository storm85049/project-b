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
import java.util.Map;

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
        Map<String,String> rsaPublicKeyMap = (Map<String,String>) jsonIN.get("publicRSAKeyMap");
        Map<String,String> elGamalPublicKeyMap = (Map<String,String>) jsonIN.get("publicElGamalKeyMap");

       ConnectedClient client = new ConnectedClient(name,id,ip,rsaPublicKeyMap,elGamalPublicKeyMap,out);
       jsonOut = JSONUtil.getLoginResponseJSON(name,id,serverMain.getConnectedClients());
       serverMain.addClient(client);
       ArrayList<ConnectedClient> receiver = new ArrayList<>();
       receiver.add(client);
       serverMain.sendToClients(jsonOut,receiver);
    }


}
