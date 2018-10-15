package oop.client;

import controller.MainViewController;
import org.json.simple.JSONObject;

public interface ActionResolver {
    void resolve(JSONObject jsonObject, MainViewController mainViewController);
}
