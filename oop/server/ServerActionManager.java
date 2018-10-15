package oop.server;

import org.json.simple.JSONObject;
import server.ServerMain;

public class ServerActionManager implements ServerActionResolver{
    public ServerActionResolver actionResolver;

    public void setServerActionResolver(ServerActionResolver actionResolver) {
        this.actionResolver = actionResolver;
    }

    @Override
    public void resolve(JSONObject jsonObject, ServerMain serverMain) {
        this.actionResolver.resolve(jsonObject,serverMain);
    }
}
