package oop.server;

import org.json.simple.JSONObject;
import server.ConnectedClient;
import server.ServerMain;
import util.Actions;
import util.JSONUtil;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Map;

public class ServerLoginNameAction implements ServerActionResolver {

    ObjectOutputStream out = null;

    public ServerLoginNameAction(ObjectOutputStream out){
        this.out = out;
    }

    @Override
    public void resolve(JSONObject jsonObject, ServerMain serverMain) {
        JSONObject jsonIN =  jsonObject;
        JSONObject jsonOut = null;

        String name = (String)jsonIN.get("name");

        if(isNameAvailable(name,serverMain)){
            jsonOut = JSONUtil.getLoginResponseNameJSON(Actions.ACTION_LOGIN_GRANTED);
            serverMain.sendToClients(jsonOut,out);
        }else{
            jsonOut = JSONUtil.getLoginResponseNameJSON(Actions.ACTION_LOGIN_FAILED);
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


}
