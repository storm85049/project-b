package oop.server;

import org.json.simple.JSONObject;
import server.ServerMain;

public interface ServerActionResolver {
   void resolve(JSONObject jsonObject, ServerMain serverMain);
}
