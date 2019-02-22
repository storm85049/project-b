package util;

import client.ClientData;
import client.RemoteClient;
import controller.BubbleController;
import controller.IController;
import controller.MainViewController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.json.simple.JSONObject;

import java.util.*;

public class ChatViewUtil {



    public static Node find(String selector)
    {
        String searchfor = selector;
        if(!searchfor.startsWith("#")){
            searchfor = "#" + searchfor;
        }
        return MainViewController.getInstance().getPane().lookup(searchfor);
    }




    private static void setPublicKeysOfRemoteClient(String id,Map<String,String> publicRSAKeyMap,Map<String,String> publicElGamalKeyMap){

        ClientData.getInstance().getRemoteClientById(id).setPublicElGamalKeyMap(publicElGamalKeyMap);
        ClientData.getInstance().getRemoteClientById(id).setPublicRSAKeyMap(publicRSAKeyMap);


    }


    public static VBox updateChatView(JSONObject users, VBox list, IController controller) {
        VBox tmp = list;
        tmp.getChildren().clear();

        HashMap<String,String> dataSet = new HashMap<>();

        Iterator<String> temp = users.keySet().iterator();
        while (temp.hasNext()) {
            String id = temp.next();

            if(  users.get(id) instanceof JSONObject ){
                JSONObject json = (JSONObject) users.get(id);

                //todo:.eigentlich nur den einen neuen hinzufügen, nicht die ganze liste  neu bzw wenn, dann nicht die publickeys neu setten

                if(ClientData.getInstance().getId().equalsIgnoreCase(id)) continue;

                String name = (String) json.get("name");
                dataSet.put(name,id);
                HashMap<String, String> publicRSAKeyMap = (HashMap<String, String>) json.get("publicRSAKeyMap");
                HashMap<String, String> publicElGamalKeyMap = (HashMap<String, String>) json.get("publicElGamalKeyMap");
                RemoteClient remoteClient = new RemoteClient(id,name);
                ClientData.getInstance().addToAvailableChats(remoteClient);
                setPublicKeysOfRemoteClient(id,publicRSAKeyMap,publicElGamalKeyMap);

            }

        }

        boolean lastOneStillOnline = false;

        for (Map.Entry<String, String> entry : dataSet.entrySet()) {

            String name = entry.getKey();
            String id = entry.getValue();
            if (id.equals(ClientData.getInstance().getId())) continue;

            HBox buttonBox  = (HBox) MainViewController.loadComponent(controller.getClass(),MainViewController.CHATBUBBLE);
            buttonBox.setId(id);
            Text nameText  = (Text) buttonBox.getChildren().get(BubbleController.NAME_INDEX);
            nameText.setText(name);
            Text messageCount = (Text)buttonBox.getChildren().get(BubbleController.COUNT_INDEX);
            messageCount.setManaged(false);
            messageCount.setVisible(false);


            String possibleOpenChat = ClientData.getInstance().getIdFromOpenChat();
            if(possibleOpenChat != null && possibleOpenChat.equals(id)){
                buttonBox.getStyleClass().addAll("active");
                lastOneStillOnline = true;
            }
            tmp.getChildren().add(buttonBox);
        }
        if(!lastOneStillOnline && ClientData.getInstance().getIdFromOpenChat() != null ){
            HashMap<String, RemoteClient> falseList = ClientData.getInstance().getAvailableChats();

            for (String id: falseList.keySet()) {

                if(!dataSet.containsValue(id)){

                    //the active chat went offline
                    String idFromOpenChat = ClientData.getInstance().getIdFromOpenChat();

                    String name = ClientData.getInstance().getRemoteClientById(idFromOpenChat).getName();
                    ClientData.getInstance().setIdFromOpenChat(null);
                    ClientData.getInstance().removeRemoteClientById(id);

                    Text topName = (Text) ChatViewUtil.find("topName");
                    topName.setText("active Chat");
                    VBox chatBox = (VBox)ChatViewUtil.find("chatBox");
                    chatBox.getChildren().clear();

                    Text welcomeText = new Text(name + " is offline... chat with someone else!");

                    welcomeText.setId("welcomeText");
                    welcomeText.setFont(Font.font("Agency FB",20));

                    chatBox.setAlignment(Pos.CENTER);
                    chatBox.getChildren().addAll(welcomeText);



                    break;
                }
            }

        }


        if(tmp.getChildren().size() <= 0){
            Text t = new Text("no one is online");
            t.setManaged(true);
            t.setFont(new Font("Agency FB",20));
            t.setFill(Color.GRAY);
            tmp.setMargin(t,new Insets(100,0,0,0));
            tmp.getChildren().add(t);
        }
        return tmp;
    }




}
