package oop.client;

import controller.IController;
import controller.MainViewController;
import org.json.simple.JSONObject;

public class ActionManager implements ActionResolver{
    public ActionResolver actionResolver;

    public void setActionResolver(ActionResolver actionResolver) {
        this.actionResolver = actionResolver;
    }

    @Override
    public  void resolve(JSONObject jsonObject, IController controller) {
        this.actionResolver.resolve(jsonObject,controller);
    }
}
