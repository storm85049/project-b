package oop.client;

import controller.MainViewController;
import org.json.simple.JSONObject;

public class ActionManager implements ActionResolver{
    public ActionResolver actionResolver;

    public void setActionResolver(ActionResolver actionResolver) {
        this.actionResolver = actionResolver;
    }

    @Override
    public void resolve(JSONObject jsonObject, MainViewController mainViewController) {
        this.actionResolver.resolve(jsonObject,mainViewController);
    }
}
