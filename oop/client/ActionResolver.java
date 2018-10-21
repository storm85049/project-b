package oop.client;

import controller.IController;
import controller.MainViewController;
import org.json.simple.JSONObject;

public interface ActionResolver {
     void resolve(JSONObject jsonObject, IController controller);
}
