package server;

import oop.server.*;
import org.json.simple.JSONObject;
import util.Actions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerMain{

    public static final int PORT = 8888;
    public ArrayList<ConnectedClient> connectedClients = new ArrayList<>();
    private  ServerActionManager serverActionManager = new ServerActionManager();
    
    
    public ServerMain() throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server is up");
        while(true){
            Socket request = serverSocket.accept();
            handleRequestAndBuildThreads(request);
        }
    }


    public void handleRequestAndBuildThreads(Socket request){
        new Thread(()->{
            try {
                ObjectOutputStream out = new ObjectOutputStream(request.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(request.getInputStream());
                while(true){
                    Object incomingRequest = in.readObject();
                    resolveRequest(incomingRequest,out);
                }
            } catch (IOException e) {
            } catch (ClassNotFoundException e) {
            }
        }).start();

    }

    public synchronized void resolveRequest(Object o, ObjectOutputStream out){
        JSONObject json = (JSONObject)o;
        String action = (String)json.get("action");
        switch (action){
            case (Actions.ACTION_INIT_LOGIN):
                serverActionManager.setServerActionResolver(new ServerLoginAction(out));break;
            case (Actions.ACTION_CLOSE_APPLICATION):
                serverActionManager.setServerActionResolver(new ServerLogoutAction());break;
            case(Actions.ACTION_REQUEST_CHAT_STATUS):
                serverActionManager.setServerActionResolver(new ServerSendOpenChatsAction());break;
            case(Actions.ACTION_SEND_MESSAGE):
                serverActionManager.setServerActionResolver(new SendMessageAction());break;
            case(Actions.ACTION_SEND_LOGIN_REQUEST_NAME):
                serverActionManager.setServerActionResolver(new ServerLoginNameAction(out));break;
            case(Actions.ACTION_INIT_ENCRYPTED_CHAT_REQUEST):
                serverActionManager.setServerActionResolver(new ServerForwardChatRequest());break;

        }
        serverActionManager.resolve(json,this);
    }

    public synchronized void sendToClients(JSONObject jsonObject,ArrayList<ConnectedClient> receivers){
        receivers.forEach(e->{
            try {
                e.getObjectOutputStream().writeObject(jsonObject);
                e.getObjectOutputStream().flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    public synchronized void sendToClients(JSONObject jsonObject, ObjectOutputStream out){
        try {
            out.writeObject(jsonObject);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized ArrayList<ConnectedClient> getConnectedClients() {
        return connectedClients;
    }



    public ConnectedClient getConnectedClientById(String id)
    {
        for (ConnectedClient connectedClient : this.connectedClients) {
            String possibleID = connectedClient.getId();
            if(id.equals(possibleID)){
                return connectedClient;
            }
        }return  null;
    }



    public synchronized ArrayList<ConnectedClient> getConnectedClientsByID(ArrayList<String> ids)
    {
        ArrayList<ConnectedClient> tmpList = new ArrayList<>();

        for (ConnectedClient connectedClient : this.connectedClients) {
            String currentKey = connectedClient.getId();
            if(ids.contains(currentKey)){
                tmpList.add(connectedClient);
            }
        }

        return tmpList;
    }

    public synchronized void addClient(ConnectedClient c)
    {
        connectedClients.add(c);
    }

    public synchronized boolean hasClientById(String id )
    {
        for (ConnectedClient connectedClient : this.connectedClients) {
            if(connectedClient.getId().equals(id)){
                return true;
            }
        }
        return false;
    }

    public synchronized void deleteClient(ConnectedClient c){
        try {
            c.getObjectOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connectedClients.remove(c);
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new ServerMain();
    }
}
