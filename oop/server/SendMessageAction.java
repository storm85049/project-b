package oop.server;

import org.json.simple.JSONObject;
import server.ServerMain;

import java.util.ArrayList;

public class SendMessageAction implements ServerActionResolver{
    @Override
    public void resolve(JSONObject jsonObject, ServerMain serverMain) {

        String to = (String)jsonObject.get("toID");

        if(serverMain.hasClientById(to)){
            ArrayList<String> receiver = new ArrayList<>();
            receiver.add(to);
            serverMain.sendToClients(jsonObject,serverMain.getConnectedClientsByID(receiver));
        }

    }
}
