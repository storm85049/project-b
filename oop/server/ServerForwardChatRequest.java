package oop.server;

import org.json.simple.JSONObject;
import server.ServerMain;

public class ServerForwardChatRequest  extends Forwardable implements ServerActionResolver {



    @Override
    public void resolve(JSONObject jsonObject, ServerMain serverMain) {
        String to = (String) jsonObject.get("to");
        this.forwardMessage(jsonObject,to, serverMain);

    }
}
